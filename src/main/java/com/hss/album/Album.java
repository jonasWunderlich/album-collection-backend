package com.hss.album;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * Represents an album entry as stored in albums.json.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
public class Album {

    public List<String> albumArtist;
    public String artist;
    public String title;
    public Integer releaseYear;
    public String releaseDate;
    public String addedDate;
    public List<String> publisher;
    public List<String> genre;
    public List<String> style;
    public String country;
    public String city;
    public Double rating;
    public Boolean reissue;
    public Boolean fan;
    public Boolean favorite;
    public Boolean owned;
    public Boolean tino;
    public Boolean wire;
    public Boolean wishlist;
    public Boolean hidden;
    public String videoUrl;
    public String wikiUrl;
    public String discogsUrl;
    public String bandcampUrl;
    public String rymUrl;
}
