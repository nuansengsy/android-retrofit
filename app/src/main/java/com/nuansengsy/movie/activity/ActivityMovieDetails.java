package com.nuansengsy.movie.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nuansengsy.movie.Config;
import com.nuansengsy.movie.R;
import com.nuansengsy.movie.adapter.GenresAdapter;
import com.nuansengsy.movie.adapter.ProductionCompaniesAdapter;
import com.nuansengsy.movie.model.movie.MovieResponse;
import com.nuansengsy.movie.rest.API;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Response;

public class ActivityMovieDetails extends AppCompatActivity {

    private Context context;
    private TextView tv_title, tv_original_title;
    private WebView webView;
    private ImageView img_movie;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ProgressBar progressBar;
    private CoordinatorLayout coordinatorLayout;
    private RecyclerView genres_recycler_view;
    private RecyclerView production_companies_rv;
    private AppBarLayout appBarLayout;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        this.context = getApplicationContext();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_content);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        img_movie = (ImageView) findViewById(R.id.image);
        tv_title = (TextView) findViewById(R.id.title);
        tv_original_title = (TextView) findViewById(R.id.original_title);
        webView = (WebView) findViewById(R.id.desc);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        genres_recycler_view = (RecyclerView) findViewById(R.id.genres_recycler_view);
        production_companies_rv = (RecyclerView) findViewById(R.id.production_companies_rv);

        setSupportActionBar(toolbar);

        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        genres_recycler_view.setHasFixedSize(true);
        genres_recycler_view.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        production_companies_rv.setHasFixedSize(true);
        production_companies_rv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        Intent iGet = getIntent();
        int movieId = iGet.getIntExtra("movieId", 0);


        API.movies().movieDetails(movieId, Config.API_KEY, Locale.getDefault().getLanguage()).enqueue(new retrofit2.Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {

                final MovieResponse movie = response.body();

                title = movie.getTitle();
                if (actionBar != null) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setTitle(title);
                }

                appBarLayout.setExpanded(true);

                appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                    boolean isShow = false;
                    int scrollRange = -1;

                    @Override
                    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                        if (scrollRange == -1) {
                            scrollRange = appBarLayout.getTotalScrollRange();
                        }
                        if (scrollRange + verticalOffset == 0) {
                            collapsingToolbarLayout.setTitle(title);
                            isShow = true;
                        } else if (isShow) {
                            collapsingToolbarLayout.setTitle(title);
                            isShow = false;
                        }
                    }
                });
                collapsingToolbarLayout.setTitle(title);
                renderMovie(movie);
                progressBar.setVisibility(View.GONE);
                coordinatorLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable throwable) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Error loading!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void renderMovie(MovieResponse movie) {
        tv_title.setText(movie.getTitle());
        tv_original_title.setText(context.getString(R.string.tv_original_title, movie.getOriginalTitle(), movie.getReleaseDate().toString()));
        webView.setBackgroundColor(Color.parseColor("#ffffff"));
        webView.setFocusableInTouchMode(false);
        webView.setFocusable(false);
        webView.getSettings().setDefaultTextEncodingName("UTF-8");
        WebSettings ws = webView.getSettings();
        ws.setDefaultFontSize(15);
        String mimeType = "text/html; charset=UTF-8";
        String encoding = "utf-8";
        String text = "<html>"
                + "<head>"
                + "<style type=\"text/css\">body{color: #525252;}"
                + "</style></head>"
                + "<body><h1>Overview:</h1>"
                + movie.getOverview()
                + "</body>"
                + "</html>";

        webView.loadData(text, mimeType, encoding);

        Picasso.with(this)
                .load(Config.IMAGE_URL_BASE_PATH + movie.getBackdropPath())
                .into(img_movie, new Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap bitmap = ((BitmapDrawable) img_movie.getDrawable()).getBitmap();
                        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {

                            }
                        });
                    }

                    @Override
                    public void onError() {
                        Toast.makeText(getApplicationContext(), "Error loading picture!", Toast.LENGTH_SHORT).show();
                    }
                });

        genres_recycler_view.setAdapter(new GenresAdapter(movie.getGenres(), R.layout.list_item_genre));

        production_companies_rv.setAdapter(
                new ProductionCompaniesAdapter(
                        movie.getProductionCompanies(),
                        R.layout.list_item_pc,
                        context
                )
        );

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }
}
