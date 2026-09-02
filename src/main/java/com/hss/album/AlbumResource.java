package com.hss.album;

import com.hss.common.PagedResponse;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

/**
 * REST resource for album queries.
 */
@Path("/albums")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Albums", description = "Operations on the album collection")
public class AlbumResource {

    @Inject
    AlbumService albumService;

    @GET
    @Operation(
            summary = "Get albums",
            description = "Returns a paginated, filterable and sortable list of albums. " +
                    "Albums with hidden=true are always excluded."
    )
    public PagedResponse<Album> getAlbums(

            // ---- Pagination ----
            @Parameter(description = "Page index (0-based)")
            @QueryParam("page") @DefaultValue("0") int page,

            @Parameter(description = "Page size")
            @QueryParam("size") @DefaultValue("20") int size,

            // ---- Sorting ----
            @Parameter(description = "Sort field: artist | albumArtist | title | releaseYear | releaseDate | addedDate | country | city | rating")
            @QueryParam("sortBy") String sortBy,

            @Parameter(description = "Sort direction: asc | desc (default: asc)")
            @QueryParam("sortDir") @DefaultValue("asc") String sortDir,

            // ---- String filters ----
            @Parameter(description = "Filter by artist name (contains, case-insensitive)")
            @QueryParam("artist") String artist,

            @Parameter(description = "Filter by album artist (contains in array, case-insensitive)")
            @QueryParam("albumArtist") String albumArtist,

            @Parameter(description = "Filter by album title (contains, case-insensitive)")
            @QueryParam("title") String title,

            @Parameter(description = "Filter by country (contains, case-insensitive)")
            @QueryParam("country") String country,

            @Parameter(description = "Filter by city (contains, case-insensitive)")
            @QueryParam("city") String city,

            @Parameter(description = "Filter by genre (contains in array, case-insensitive)")
            @QueryParam("genre") String genre,

            @Parameter(description = "Filter by style (contains in array, case-insensitive)")
            @QueryParam("style") String style,

            @Parameter(description = "Filter by publisher (contains in array, case-insensitive)")
            @QueryParam("publisher") String publisher,

            @Parameter(description = "Filter by video URL (contains, case-insensitive)")
            @QueryParam("videoUrl") String videoUrl,

            // ---- Boolean filters ----
            @Parameter(description = "Filter by reissue flag")
            @QueryParam("reissue") Boolean reissue,

            @Parameter(description = "Filter by fan flag")
            @QueryParam("fan") Boolean fan,

            @Parameter(description = "Filter by favorite flag")
            @QueryParam("favorite") Boolean favorite,

            @Parameter(description = "Filter by owned flag")
            @QueryParam("owned") Boolean owned,

            @Parameter(description = "Filter by tino flag")
            @QueryParam("tino") Boolean tino,

            @Parameter(description = "Filter by wire flag")
            @QueryParam("wire") Boolean wire,

            @Parameter(description = "Filter by wishlist flag")
            @QueryParam("wishlist") Boolean wishlist,

            // ---- Year filter ----
            @Parameter(description = "Filter by exact release year")
            @QueryParam("releaseYear") Integer releaseYear,

            // ---- Date range filters ----
            @Parameter(description = "Filter release date from (ISO yyyy-MM-dd, inclusive)")
            @QueryParam("releaseDateFrom") String releaseDateFrom,

            @Parameter(description = "Filter release date to (ISO yyyy-MM-dd, inclusive)")
            @QueryParam("releaseDateTo") String releaseDateTo,

            @Parameter(description = "Filter added date from (ISO yyyy-MM-dd, inclusive)")
            @QueryParam("addedDateFrom") String addedDateFrom,

            @Parameter(description = "Filter added date to (ISO yyyy-MM-dd, inclusive)")
            @QueryParam("addedDateTo") String addedDateTo,

            // ---- Rating filters ----
            @Parameter(description = "Filter by exact rating (takes precedence over ratingMin/Max)")
            @QueryParam("rating") Double rating,

            @Parameter(description = "Filter by minimum rating (inclusive)")
            @QueryParam("ratingMin") Double ratingMin,

            @Parameter(description = "Filter by maximum rating (inclusive)")
            @QueryParam("ratingMax") Double ratingMax
    ) {
        AlbumFilterParams params = new AlbumFilterParams();
        params.page = page;
        params.size = size;
        params.sortBy = sortBy;
        params.sortDir = sortDir;
        params.artist = artist;
        params.albumArtist = albumArtist;
        params.title = title;
        params.country = country;
        params.city = city;
        params.genre = genre;
        params.style = style;
        params.publisher = publisher;
        params.videoUrl = videoUrl;
        params.reissue = reissue;
        params.fan = fan;
        params.favorite = favorite;
        params.owned = owned;
        params.tino = tino;
        params.wire = wire;
        params.wishlist = wishlist;
        params.releaseYear = releaseYear;
        params.releaseDateFrom = releaseDateFrom;
        params.releaseDateTo = releaseDateTo;
        params.addedDateFrom = addedDateFrom;
        params.addedDateTo = addedDateTo;
        params.rating = rating;
        params.ratingMin = ratingMin;
        params.ratingMax = ratingMax;

        return albumService.getAlbums(params);
    }
}
