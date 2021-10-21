package com.example.mvvmretrofiddemo.model;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.mvvmretrofiddemo.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import service.MovieService;
import service.RetrofitInstance;
import android.content.Context;



public class AppRepository {

    private MutableLiveData<List<Result>> mutableLiveData = new MutableLiveData<>();
    private ArrayList<Result> results;
    Application application;

    public AppRepository(Application application) {

        this.application = application;

    }

    public MutableLiveData<List<Result>> getMutableLiveData() {

        MovieService movieService = RetrofitInstance.getService();
        Call<MovieApiResponse> call = movieService.getResults(application.getApplicationContext().getString(R.string.api_key));

        call.enqueue(new Callback<MovieApiResponse>() {
            @Override
            public void onResponse(Call<MovieApiResponse> call,
                                   Response<MovieApiResponse> response) {


                MovieApiResponse movieApiResponse =
                        response.body();

                if (movieApiResponse != null && movieApiResponse.getResults() != null) {

                    results = (ArrayList<Result>) movieApiResponse.getResults();
                    mutableLiveData.setValue(results);


                }


            }

            @Override
            public void onFailure(Call<MovieApiResponse> call, Throwable t) {
                Log.e("onFAILURE", t.toString());
            }
        });

        return mutableLiveData;

    }





}
