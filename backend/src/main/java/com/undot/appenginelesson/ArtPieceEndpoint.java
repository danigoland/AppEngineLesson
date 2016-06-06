package com.undot.appenginelesson;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.datastore.AdminDatastoreService;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Nullable;
import javax.inject.Named;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * WARNING: This generated code is intended as a sample or starting point for using a
 * Google Cloud Endpoints RESTful API with an Objectify entity. It provides no data access
 * restrictions and no data validation.
 * <p/>
 * DO NOT deploy this code unchanged as part of a real application to real users.
 */
@Api(
        name = "artPieceApi",
        version = "v1",
        resource = "artPiece",
        namespace = @ApiNamespace(
                ownerDomain = "appenginelesson.undot.com",
                ownerName = "appenginelesson.undot.com",
                packagePath = ""
        )
)
public class ArtPieceEndpoint {

    private static final Logger logger = Logger.getLogger(ArtPieceEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;

    static {
        // Typically you would register this inside an OfyServive wrapper. See: https://code.google.com/p/objectify-appengine/wiki/BestPractices
        ObjectifyService.register(ArtPiece.class);
    }

    /**
     * Returns the {@link ArtPiece} with the corresponding ID.
     *
     * @param name the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code ArtPiece} with the provided ID.
     */
    @ApiMethod(
            name = "get",
            path = "artPiece/{name}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public ArtPiece get(@Named("name") String name) throws NotFoundException {
        logger.info("Getting ArtPiece with ID: " + name);
        ArtPiece artPiece = ofy().load().type(ArtPiece.class).id(name).now();
        if (artPiece == null) {
            throw new NotFoundException("Could not find ArtPiece with ID: " + name);
        }
        return artPiece;
    }

    /**
     * Inserts a new {@code ArtPiece}.
     */
    @ApiMethod(
            name = "insert",
            path = "artPiece/insert",
            httpMethod = ApiMethod.HttpMethod.GET)
    public ArtPiece insert(@Named("artist_name")String artist,@Named("art_name")String art_name,@Named("imageUrl")String imageUrl) {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that artPiece.name has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        ArtPiece artPiece = new ArtPiece();
        artPiece.setName(art_name);
        artPiece.setImageUrl(imageUrl);
        Key<Artist> artistKey = Key.create(Artist.class,artist);
        artPiece.setArtist(artistKey);

        ofy().save().entity(artPiece).now();
        logger.info("Created ArtPiece.");

        return ofy().load().entity(artPiece).now();
    }

    /**
     * Updates an existing {@code ArtPiece}.
     *
     * @param name     the ID of the entity to be updated
     * @param artPiece the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code name} does not correspond to an existing
     *                           {@code ArtPiece}
     */
    @ApiMethod(
            name = "update",
            path = "artPiece/{name}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public ArtPiece update(@Named("name") String name, ArtPiece artPiece) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        checkExists(name);
        ofy().save().entity(artPiece).now();
        logger.info("Updated ArtPiece: " + artPiece);
        return ofy().load().entity(artPiece).now();
    }

    /**
     * Deletes the specified {@code ArtPiece}.
     *
     * @param name the ID of the entity to delete
     * @throws NotFoundException if the {@code name} does not correspond to an existing
     *                           {@code ArtPiece}
     */
    @ApiMethod(
            name = "remove",
            path = "artPiece/{name}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("name") String name) throws NotFoundException {
        checkExists(name);
        ofy().delete().type(ArtPiece.class).id(name).now();
        logger.info("Deleted ArtPiece with ID: " + name);
    }

    /**
     * List all entities.
     *
     * @param cursor used for pagination to determine which page to return
     * @param limit  the maximum number of entries to return
     * @return a response that encapsulates the result list and the next page token/cursor
     */
    @ApiMethod(
            name = "list",
            path = "artPiece",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<ArtPiece> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<ArtPiece> query = ofy().load().type(ArtPiece.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<ArtPiece> queryIterator = query.iterator();
        List<ArtPiece> artPieceList = new ArrayList<ArtPiece>(limit);
        while (queryIterator.hasNext()) {
            artPieceList.add(queryIterator.next());
        }
        return CollectionResponse.<ArtPiece>builder().setItems(artPieceList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }


    @ApiMethod(
            name = "byArtist",
            path = "byArtist",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<ArtPiece> byArtist(@Named("name") String artistName) {
        Key<Artist> key = Key.create(Artist.class,artistName);
        Query<ArtPiece> query = ofy().load().type(ArtPiece.class).ancestor(key);

        return CollectionResponse.<ArtPiece>builder().setItems(query.list()).build();
    }
    private void checkExists(String name) throws NotFoundException {
        try {
            ofy().load().type(ArtPiece.class).id(name).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find ArtPiece with ID: " + name);
        }
    }
}