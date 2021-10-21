package com.example.mvvmretrofiddemo.view;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import viewmodel.MovieViewModel;
import com.example.mvvmretrofiddemo.R;
import com.example.mvvmretrofiddemo.databinding.ActivityMainBinding;
import com.example.mvvmretrofiddemo.model.Result;

import java.util.ArrayList;
import java.util.List;

import adapters.ResultAdapter;

public class MainActivity extends AppCompatActivity {

    private PagedList<Result> results;
    private RecyclerView recyclerView;
    private ResultAdapter adapter;
    private SwipeRefreshLayout refreshLayout;
    private MovieViewModel movieViewModel;
    private ActivityMainBinding activityMainBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        movieViewModel = new ViewModelProvider
                .AndroidViewModelFactory(getApplication())
                .create(MovieViewModel.class);

        getPopularMovies();

        refreshLayout = activityMainBinding.swipeRefresh;
        refreshLayout.setColorSchemeResources(R.color.design_default_color_primary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getPopularMovies();

            }
        });
    }

    public void getPopularMovies() {

//        movieViewModel.getAllMovieData().observe(this, new Observer<List<Result>>() {
//            @Override
//            public void onChanged(List<Result> resultList) {
//                results = (ArrayList<Result>) resultList;
//                fillRecyclerView();
//            }
//        });
        movieViewModel.getPagedListLiveData().observe(this, new Observer<PagedList<Result>>() {
            @Override
            public void onChanged(PagedList<Result> resultList) {
                results = resultList;
                fillRecyclerView();
            }
        });

    }


    public void fillRecyclerView() {

        recyclerView = activityMainBinding.recyclerView;
        adapter = new ResultAdapter(this);
        adapter.submitList(results);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

            recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        }
        else {

            recyclerView.setLayoutManager(new GridLayoutManager(this,4));

        }

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        refreshLayout.setRefreshing(false);


    }
}