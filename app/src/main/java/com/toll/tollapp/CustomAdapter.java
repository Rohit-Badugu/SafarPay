package com.toll.tollapp;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Rohit33 on 30-03-2017.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    private List<News> newsList;
    private static final String TAG=CarsAdapter.class.getSimpleName();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView headline,news_info,news_img;

        public MyViewHolder(View view) {
            super(view);

            headline=(TextView)view.findViewById(R.id.news_headline);
            news_info=(TextView)view.findViewById(R.id.news_content);
            news_img=(TextView)view.findViewById(R.id.news_image);
        }
    }

    public CustomAdapter(List<News> moviesList)
    {
        this.newsList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Log.d(TAG+"bdh",String.valueOf(position));
        News news= newsList.get(position);
        holder.news_img.setText(news.getTitle());
        holder.headline.setText(news.getContent());
        holder.news_info.setText(news.getDate());
    }

    @Override
    public int getItemCount() {
        Log.d(TAG,String.valueOf(newsList.size()));
        return newsList.size();
    }

}
