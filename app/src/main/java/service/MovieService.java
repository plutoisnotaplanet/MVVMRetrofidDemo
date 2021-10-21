package service;

import com.example.mvvmretrofiddemo.model.MovieApiResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieService {

    @GET("movie/popular")
    Call<MovieApiResponse> getResults(@Query("api_key") String apiKey);//

    @GET("movie/popular")
    Call<MovieApiResponse> getResultsWithPaging(
            @Query("api_key") String apiKey, @Query("page") long page);//

}
