package com.undot.appenginelesson.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.undot.appenginelesson.R;
import com.undot.appenginelesson.artistApi.model.Artist;

import java.util.List;

/**
 * Created by 0503337710 on 06/06/2016.
 */
public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.PersonViewHolder>
{

    private List<Artist> list;
    private Context _context;

    public ArtistAdapter(Context c, List<Artist> artistList)
    {
        _context = c;
        list = artistList;

    }



    @Override
    public void onBindViewHolder(final PersonViewHolder holder, final int position) {

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        Picasso.with(_context).load(list.get(position).getImageUrl()).fit().into(holder.photo);
        holder.text.setText(list.get(position).getName());

    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.artist_card, parent, false);
        PersonViewHolder dataObjectHolder = new PersonViewHolder(v);

        return dataObjectHolder;
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        ImageView photo;
        TextView text;

        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv_sm);
            photo = (ImageView)itemView.findViewById(R.id.artist_image);
            text = (TextView)itemView.findViewById(R.id.artist_name);
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

}