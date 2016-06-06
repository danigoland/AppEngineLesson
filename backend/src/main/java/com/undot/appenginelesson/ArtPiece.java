package com.undot.appenginelesson;

import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;

/**
 * Created by 0503337710 on 06/06/2016.
 */

@Entity
public class ArtPiece{
    @Parent
    Key<Artist> artist;

    @Id
    String name;

    String imageUrl;

    public ArtPiece() {
    }

    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
    public Key<Artist> getArtist() {
        return artist;
    }

    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
    public void setArtist(Key<Artist> artist) {
        this.artist = artist;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
