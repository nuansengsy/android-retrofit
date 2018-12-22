package com.nuansengsy.movie.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nuansengsy.movie.R;
import com.nuansengsy.movie.model.movie.GenreItem;

import java.util.List;

public class GenresAdapter extends RecyclerView.Adapter<GenresAdapter.GenreViewHolder> {

    private List<GenreItem> genres;
    private int rowLayout;

    public GenresAdapter(List<GenreItem> genres, int rowLayout) {
        this.genres = genres;
        this.rowLayout = rowLayout;
    }

    public static class GenreViewHolder extends RecyclerView.ViewHolder {
        TextView genreName;

        public GenreViewHolder(View v) {
            super(v);
            genreName = (TextView) v.findViewById(R.id.genreName);
        }
    }

    @Override
    public GenresAdapter.GenreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new GenreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GenreViewHolder holder, final int position) {
        holder.genreName.setText(this.genres.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return genres.size();
    }
}
