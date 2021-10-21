package com.example.mvvmretrofiddemo.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.example.mvvmretrofiddemo.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import service.MovieService;
import service.RetrofitInstance;

public class MovieDataSource extends PageKeyedDataSource<Long, Result> {

    private MovieService movieService;
    private Application application;

    public MovieDataSource(MovieService movieService, Application application) {
        this.movieService = movieService;
        this.application = application;
    }

    @Override
    public void loadInitial(@NonNull  PageKeyedDataSource.LoadInitialParams<Long> params, @NonNull
            PageKeyedDataSource.LoadInitialCallback<Long, Result> callback) {

        movieService = RetrofitInstance.getService();
        Call<MovieApiResponse> call = movieService.getResultsWithPaging(
                application.getApplicationContext().getString(R.string.api_key), 1
        );

        call.enqueue(new Callback<MovieApiResponse>() {
            @Override
            public void onResponse(Call<MovieApiResponse> call, Response<MovieApiResponse> response) {

                MovieApiResponse movieApiResponse = response.body();
                ArrayList<Result> results = new ArrayList<>();

                if(movieApiResponse != null && movieApiResponse.getResults() != null) {

                    results = (ArrayList<Result>) movieApiResponse.getResults();
                    callback.onResult(results, null,(long)2);

                }



            }

            @Override
            public void onFailure(Call<MovieApiResponse> call, Throwable t) {

            }
        });

    }

    @Override
    public void loadBefore(@NonNull  PageKeyedDataSource.LoadParams<Long> params, @NonNull
            PageKeyedDataSource.LoadCallback<Long, Result> callback) {



    }

    @Override
    public void loadAfter(@NonNull  PageKeyedDataSource.LoadParams<Long> params, @NonNull
            final PageKeyedDataSource.LoadCallback<Long, Result> callback) {

        movieService = RetrofitInstance.getService();
        Call<MovieApiResponse> call = movieService.getResultsWithPaging(
                application.getApplicationContext().getString(R.string.api_key), params.key
        );

        call.enqueue(new Callback<MovieApiResponse>() {
            @Override
            public void onResponse(Call<MovieApiResponse> call, Response<MovieApiResponse> response) {

                MovieApiResponse movieApiResponse = response.body();
                ArrayList<Result> results = new ArrayList<>();

                if (movieApiResponse != null && movieApiResponse.getResults() != null ) {

                    results = (ArrayList<Result>) movieApiResponse.getResults();
                    callback.onResult(results, params.key + 1);

                }



            }

            @Override
            public void onFailure(Call<MovieApiResponse> call, Throwable t) {

            }
        });

    }
}
