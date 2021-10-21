package viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.example.mvvmretrofiddemo.model.AppRepository;
import com.example.mvvmretrofiddemo.model.MovieDataSource;
import com.example.mvvmretrofiddemo.model.MovieDataSourceFactory;
import com.example.mvvmretrofiddemo.model.Result;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import service.MovieService;
import service.RetrofitInstance;

public class MovieViewModel extends AndroidViewModel {

    private AppRepository appRepository;
    private LiveData<List<Result>> liveData = null;
    private LiveData<MovieDataSource> movieDataSourceLiveData;
    private Executor executor;
    private LiveData<PagedList<Result>> pagedListLiveData;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        appRepository = new AppRepository(application);

        MovieService movieService = RetrofitInstance.getService();
        MovieDataSourceFactory movieDataSourceFactory = new MovieDataSourceFactory(application, movieService);
        movieDataSourceLiveData = movieDataSourceFactory.getMutableLiveData();

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setInitialLoadSizeHint(10)
                .setPageSize(20)
                .setPrefetchDistance(3)
                .build();

        executor = Executors.newCachedThreadPool();

        pagedListLiveData = new LivePagedListBuilder<Long, Result>(movieDataSourceFactory, config)
                .setFetchExecutor(executor)
                .build();
    }

    public LiveData<List<Result>> getAllMovieData() {

        if(liveData == null) {

            liveData = appRepository.getMutableLiveData();

        }

        return liveData;

    }

    public LiveData<PagedList<Result>> getPagedListLiveData() {
        return pagedListLiveData;
    }
}
