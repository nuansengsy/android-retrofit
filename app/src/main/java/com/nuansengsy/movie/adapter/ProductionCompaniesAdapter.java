package com.nuansengsy.movie.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nuansengsy.movie.R;
import com.nuansengsy.movie.model.movie.ProductionCompanyItem;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.nuansengsy.movie.Config.IMAGE_URL_BASE_PATH;

public class ProductionCompaniesAdapter extends RecyclerView.Adapter<ProductionCompaniesAdapter.PcViewHolder> {

    private List<ProductionCompanyItem> pc;
    private int rowLayout;
    private Context context;

    public ProductionCompaniesAdapter(List<ProductionCompanyItem> pc, int rowLayout, Context context) {
        this.pc = pc;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    public static class PcViewHolder extends RecyclerView.ViewHolder {

        TextView pc_name;
        TextView pc_origin_country;
        TextView pc_id;
        ImageView logo_path;

        public PcViewHolder(View v) {
            super(v);
            logo_path = (ImageView) v.findViewById(R.id.logo_path);
            pc_name = (TextView) v.findViewById(R.id.pc_name);
            pc_id = (TextView) v.findViewById(R.id.pc_id);
            pc_origin_country = (TextView) v.findViewById(R.id.pc_origin_country);
        }
    }

    @Override
    public ProductionCompaniesAdapter.PcViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new PcViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PcViewHolder holder, final int position) {
        holder.pc_origin_country.setText(this.pc.get(position).getOrigin_country());
        holder.pc_name.setText(this.pc.get(position).getName());
        holder.pc_id.setText(this.pc.get(position).getId().toString());

        final ImageView logo_path = holder.logo_path;

        Picasso.with(context)
                .load(IMAGE_URL_BASE_PATH + this.pc.get(position).getLogo_path())
                .placeholder(R.drawable.star)
                .into(logo_path, new Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap bitmap = ((BitmapDrawable) logo_path.getDrawable()).getBitmap();
                        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {

                            }
                        });
                    }

                    @Override
                    public void onError() {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return pc.size();
    }
}
