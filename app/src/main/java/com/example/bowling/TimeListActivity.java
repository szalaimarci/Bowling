package com.example.bowling;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class TimeListActivity extends AppCompatActivity {
    private static final String LOG_TAG = TimeListActivity.class.getName();
    private FirebaseUser user;
    private FirebaseAuth mAuth;

    private RecyclerView mRecylerView;
    private ArrayList<TimeItem> mTimeList;
    private TimeItemAdapter mAdapter;

    private NotificationHandler mNotificationHandler;

    private FrameLayout redCircle;
    private TextView contentTextView;
    private int cartItems = 0;

    private int gridNumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_list);
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            Log.d(LOG_TAG, "Authenticated user!");
        } else {
            Log.d(LOG_TAG, "Unauthenticated user!");
            finish();
        }

        mRecylerView = findViewById(R.id.recyclerView);
        mRecylerView.setLayoutManager(new GridLayoutManager(this, gridNumber));
        mTimeList = new ArrayList<>();

        mAdapter = new TimeItemAdapter(this, mTimeList);
        mRecylerView.setAdapter(mAdapter);

        initializeData();

        mNotificationHandler = new NotificationHandler(this);

    }

    private void initializeData() {
        String[] timeList = getResources().getStringArray(R.array.time_item_names);
        String[] alleyList = getResources().getStringArray(R.array.alley_item_names);

        mTimeList.clear();

        for (int i = 0; i < timeList.length; i++) {
            mTimeList.add(new TimeItem(timeList[i], alleyList[i]));
        }

        mAdapter.notifyDataSetChanged();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.time_list_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.d(LOG_TAG, s);
                mAdapter.getFilter().filter(s);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.log_out:
                Log.d(LOG_TAG, "log out clicked");
                FirebaseAuth.getInstance().signOut();
                finish();
                return true;
            case R.id.setting_button:
                return true;
            case R.id.booked:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final MenuItem alertMenuItem = menu.findItem(R.id.booked);
        FrameLayout rootView = (FrameLayout) alertMenuItem.getActionView();

        redCircle = (FrameLayout) rootView.findViewById(R.id.red_circle);
        contentTextView = (TextView) rootView.findViewById(R.id.count_textview);

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOptionsItemSelected(alertMenuItem);
                mNotificationHandler.send("Időpont foglalva!");
            }
        });

        return super.onPrepareOptionsMenu(menu);
    }

    public void updateAlertIcon() {
        cartItems = (cartItems + 1);
        Log.d(LOG_TAG, "cartitems" + cartItems);
        if (0 < cartItems) {
            contentTextView.setText(String.valueOf(cartItems));
        } else {
            contentTextView.setText("");
        }

        redCircle.setVisibility((cartItems > 0) ? View.VISIBLE : View.GONE);
    }
}