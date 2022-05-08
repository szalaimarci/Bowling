package com.example.bowling;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;

public class TimeItemAdapter extends RecyclerView.Adapter<TimeItemAdapter.ViewHolder> implements Filterable {
    private static final String LOG_TAG = TimeItemAdapter.class.getName();
    private ArrayList<TimeItem> mtimeItemsData;
    private ArrayList<TimeItem> mtimeItemsDataAll;
    private Context mContext;
    private int lastPosition = -1;

    TimeItemAdapter(Context context, ArrayList<TimeItem> itemsData) {
        this.mtimeItemsData = itemsData;
        this.mtimeItemsDataAll = itemsData;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_time, parent, false));
    }

    @Override
    public void onBindViewHolder(TimeItemAdapter.ViewHolder holder, int position) {
        TimeItem currentItem = mtimeItemsData.get(position);

        holder.bindTo(currentItem);

        if (holder.getAdapterPosition() > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_row);
            holder.itemView.startAnimation(animation);
            lastPosition = holder.getAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return mtimeItemsData.size();
    }

    @Override
    public Filter getFilter() {
        return timeFilter;
    }

    public Filter timeFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<TimeItem> filteredList = new ArrayList<>();
            FilterResults results = new FilterResults();

            if (charSequence == null || charSequence.length() == 0) {
                results.count = mtimeItemsDataAll.size();
                results.values = mtimeItemsDataAll;
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (TimeItem item : mtimeItemsDataAll) {
                    if (item.getTime().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
                results.count = filteredList.size();
                results.values = filteredList;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mtimeItemsData = (ArrayList) filterResults.values;
            notifyDataSetChanged();
        }
    };

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTimeText;
        private TextView mAlleyText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mTimeText = itemView.findViewById(R.id.timeTitle);
            mAlleyText = itemView.findViewById(R.id.alleyTitle);

            itemView.findViewById(R.id.book).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(LOG_TAG, "booking clicked");
                    Animation animationButton = AnimationUtils.loadAnimation(mContext, R.anim.anim_button);
                    view.startAnimation(animationButton);
                    ((TimeListActivity)mContext).updateAlertIcon();
                }
            });

        }

        public void bindTo(TimeItem currentItem) {
            mTimeText.setText(currentItem.getTime());
            mAlleyText.setText(currentItem.getAlley());
        }
    }

};


