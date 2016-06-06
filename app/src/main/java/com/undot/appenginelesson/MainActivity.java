package com.undot.appenginelesson;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.undot.appenginelesson.artPieceApi.model.ArtPiece;
import com.undot.appenginelesson.artistApi.model.Artist;
import com.undot.appenginelesson.models.BaseResponse;
import com.undot.appenginelesson.network.NetworkManager;
import com.undot.appenginelesson.ui.ArtistAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    ArtistAdapter adapter;
    List<Artist> artistList = new ArrayList<>();
    SwipeRefreshLayout refreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final RecyclerView recyclerView = (RecyclerView)findViewById(R.id.artist_rv);
        refreshLayout = (SwipeRefreshLayout)findViewById(R.id.artist_refresh);
        adapter = new ArtistAdapter(getApplicationContext(),artistList);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        refreshLayout.setOnRefreshListener(this);
        NetworkManager.getInstance().getAllArtist().enqueue(new Callback<BaseResponse<Artist>>() {
            @Override
            public void onResponse(Call<BaseResponse<Artist>> call, Response<BaseResponse<Artist>> response) {
                artistList.addAll(response.body().getItems());
                adapter.notifyDataSetChanged();
                findViewById(R.id.artist_progressBar).setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<BaseResponse<Artist>> call, Throwable t) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    public void onRefresh() {
        NetworkManager.getInstance().getAllArtist().enqueue(new Callback<BaseResponse<Artist>>() {
            @Override
            public void onResponse(Call<BaseResponse<Artist>> call, Response<BaseResponse<Artist>> response) {
                artistList.clear();
                artistList.addAll(response.body().getItems());
                adapter.notifyDataSetChanged();
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<BaseResponse<Artist>> call, Throwable t) {

            }
        });
    }
}
