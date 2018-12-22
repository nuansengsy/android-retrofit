package com.nuansengsy.movie.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nuansengsy.movie.R;
import com.nuansengsy.movie.model.movies.MovieItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.nuansengsy.movie.Config.IMAGE_URL_BASE_PATH;

public class MoviesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MovieItem> mData = new ArrayList<>();
    private Context context;

    public MoviesAdapter(Context context) {
        this.context = context;
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        LinearLayout moviesLayout;
        TextView movieTitle;
        TextView movieDescription;
        TextView rating;
        ImageView movieImage;

        public MovieViewHolder(View v) {
            super(v);
            moviesLayout = (LinearLayout) v.findViewById(R.id.movies_layout);
            movieImage = (ImageView) v.findViewById(R.id.movie_image);
            movieTitle = (TextView) v.findViewById(R.id.title);
            movieDescription = (TextView) v.findViewById(R.id.description);
            rating = (TextView) v.findViewById(R.id.rating);
        }
    }

    public static class ProgressHolder extends RecyclerView.ViewHolder {

        public ProgressHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        switch (viewType) {
            case R.layout.list_item_movie:
                return new MovieViewHolder(view);

            default:
                return new ProgressHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        if (viewHolder instanceof MovieViewHolder) {
            MovieViewHolder holder = (MovieViewHolder) viewHolder;
            String image_url = IMAGE_URL_BASE_PATH + mData.get(position).getPosterPath();
            Picasso.with(context)
                    .load(image_url)
                    .placeholder(android.R.drawable.sym_def_app_icon)
                    .error(android.R.drawable.sym_def_app_icon)
                    .into(holder.movieImage);
            holder.movieTitle.setText(mData.get(position).getTitle());
            holder.movieDescription.setText(mData.get(position).getOverview());
            holder.rating.setText(mData.get(position).getVoteAverage().toString());
        }
    }

    @Override
    public final int getItemViewType(int position) {
        return position < mData.size() ? R.layout.list_item_movie : R.layout.item_progress;
    }

    @Override
    public final int getItemCount() {
        if (mData.isEmpty()) {
            return 0;
        }
        return mData.size() + 1;
    }

    public MovieItem getItem(int position) {
        return position >= 0 && position < mData.size() ? mData.get(position) : null;
    }

    public void insertData(List<MovieItem> data) {
        int startPosition = mData.size();
        mData.addAll(data);
        notifyItemRangeInserted(startPosition, data.size());
    }
}
