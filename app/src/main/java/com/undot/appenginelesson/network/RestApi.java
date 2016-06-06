package com.undot.appenginelesson.network;

import com.undot.appenginelesson.artPieceApi.model.ArtPiece;
import com.undot.appenginelesson.artistApi.model.Artist;
import com.undot.appenginelesson.models.BaseResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by 0503337710 on 06/06/2016.
 */
public interface RestApi {

    @GET("artPieceApi/v1/artPiece")
    Call<BaseResponse<ArtPiece>> getAllArtPieces();

    @GET("artPieceApi/v1/byArtist")
    Call<BaseResponse<ArtPiece>> getArtPiecesByArtist(@Query("name") String artistName);

    @GET("artistApi/v1/artist")
    Call<BaseResponse<Artist>> getAllArtist();
}
