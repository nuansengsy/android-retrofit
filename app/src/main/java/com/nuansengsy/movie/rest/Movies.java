package com.nuansengsy.movie.rest;

import com.nuansengsy.movie.model.movie.MovieResponse;
import com.nuansengsy.movie.model.movies.MoviesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Movies {

    //TOP RATED MOVIES
    @GET("movie/top_rated")
    Call<MoviesResponse> topRated(@Query("api_key") String apiKey,
                                  @Query("language") String language,
                                  @Query("page") int page);

    //MOVIE DETAIL
    @GET("/3/movie/{id}")
    Call<MovieResponse> movieDetails(@Path("id") int movieID,
                                     @Query("api_key") String apiKey,
                                     @Query("language") String language);
}
