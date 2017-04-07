package com.toll.tollapp;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Rohit33 on 30-03-2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {
    private List<News> newsList;
    private static final String TAG=NewsAdapter.class.getSimpleName();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, date, content;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.trans_title);
            content = (TextView) view.findViewById(R.id.trans_content);
            date = (TextView) view.findViewById(R.id.trans_date);
        }
    }

    public NewsAdapter(List<News> moviesList) {
        this.newsList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Log.d(TAG+"bdh",String.valueOf(position));
        News news= newsList.get(position);
        holder.title.setText(news.getTitle());
        holder.content.setText(news.getContent());
        holder.date.setText(news.getDate());
    }

    @Override
    public int getItemCount() {
        Log.d(TAG,String.valueOf(newsList.size()));
        return newsList.size();
    }

}
