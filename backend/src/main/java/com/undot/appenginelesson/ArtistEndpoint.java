package com.undot.appenginelesson;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
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
        name = "artistApi",
        version = "v1",
        resource = "artist",
        namespace = @ApiNamespace(
                ownerDomain = "appenginelesson.undot.com",
                ownerName = "appenginelesson.undot.com",
                packagePath = ""
        )
)
public class ArtistEndpoint {

    private static final Logger logger = Logger.getLogger(ArtistEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;

    static {
        // Typically you would register this inside an OfyServive wrapper. See: https://code.google.com/p/objectify-appengine/wiki/BestPractices
        ObjectifyService.register(Artist.class);
    }

    /**
     * Returns the {@link Artist} with the corresponding ID.
     *
     * @param name the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code Artist} with the provided ID.
     */
    @ApiMethod(
            name = "get",
            path = "artist/{name}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public Artist get(@Named("name") String name) throws NotFoundException {
        logger.info("Getting Artist with ID: " + name);
        Artist artist = ofy().load().type(Artist.class).id(name).now();
        if (artist == null) {
            throw new NotFoundException("Could not find Artist with ID: " + name);
        }
        return artist;
    }

    /**
     * Inserts a new {@code Artist}.
     */
    @ApiMethod(
            name = "insert",
            path = "artist",
            httpMethod = ApiMethod.HttpMethod.POST)
    public Artist insert(Artist artist) {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that artist.name has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        ofy().save().entity(artist).now();
        logger.info("Created Artist with ID: " + artist.getName());

        return ofy().load().entity(artist).now();
    }

    /**
     * Updates an existing {@code Artist}.
     *
     * @param name   the ID of the entity to be updated
     * @param artist the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code name} does not correspond to an existing
     *                           {@code Artist}
     */
    @ApiMethod(
            name = "update",
            path = "artist/{name}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public Artist update(@Named("name") String name, Artist artist) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        checkExists(name);
        ofy().save().entity(artist).now();
        logger.info("Updated Artist: " + artist);
        return ofy().load().entity(artist).now();
    }

    /**
     * Deletes the specified {@code Artist}.
     *
     * @param name the ID of the entity to delete
     * @throws NotFoundException if the {@code name} does not correspond to an existing
     *                           {@code Artist}
     */
    @ApiMethod(
            name = "remove",
            path = "artist/{name}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("name") String name) throws NotFoundException {
        checkExists(name);
        ofy().delete().type(Artist.class).id(name).now();
        logger.info("Deleted Artist with ID: " + name);
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
            path = "artist",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<Artist> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<Artist> query = ofy().load().type(Artist.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<Artist> queryIterator = query.iterator();
        List<Artist> artistList = new ArrayList<Artist>(limit);
        while (queryIterator.hasNext()) {
            artistList.add(queryIterator.next());
        }
        return CollectionResponse.<Artist>builder().setItems(artistList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }

    private void checkExists(String name) throws NotFoundException {
        try {
            ofy().load().type(Artist.class).id(name).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find Artist with ID: " + name);
        }
    }
}