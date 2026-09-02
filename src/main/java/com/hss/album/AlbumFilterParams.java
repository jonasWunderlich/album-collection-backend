package com.hss.album;

/**
 * All query parameters accepted by GET /albums.
 * Passed as a plain parameter object to keep the resource method signature clean.
 */
public class AlbumFilterParams {

    // ---- Pagination ----
    public int page = 0;
    public int size = 20;

    // ---- Sorting ----
    public String sortBy;
    public String sortDir = "asc";

    // ---- String filters ----
    public String artist;
    public String albumArtist;
    public String title;
    public String country;
    public String city;
    public String genre;
    public String style;
    public String publisher;
    public String videoUrl;
    public String search;  // Volltextsuche: artist, albumArtist, title, genre, style, city, country, publisher

    // ---- Boolean filters ----
    public Boolean reissue;
    public Boolean fan;
    public Boolean favorite;
    public Boolean owned;
    public Boolean tino;
    public Boolean wire;
    public Boolean wishlist;

    // ---- Year filter ----
    public Integer releaseYear;

    // ---- Date range filters (ISO-8601: yyyy-MM-dd) ----
    public String releaseDateFrom;
    public String releaseDateTo;
    public String addedDateFrom;
    public String addedDateTo;

    // ---- Rating filters ----
    public Double rating;
    public Double ratingMin;
    public Double ratingMax;
}
