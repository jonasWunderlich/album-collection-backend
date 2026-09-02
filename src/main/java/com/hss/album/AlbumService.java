package com.hss.album;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hss.common.PagedResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Stream;

/**
 ** Loads library.json on startup and provides filtering, sorting and pagination.
 */
@ApplicationScoped
public class AlbumService {
    
    private final List<Album> albums;
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Inject
    public AlbumService(
            ObjectMapper objectMapper,
            @ConfigProperty(name = "library.json.path") Optional<String> externalPath) {

        try {
            List<Album> loaded;
            if (externalPath.isPresent() && !externalPath.get().isBlank()) {
                // Allow overriding via config property (useful for integration tests / different envs)
                loaded = objectMapper.readValue(new File(externalPath.get()), new TypeReference<>() {});
            } else {
                // Default: load from classpath resource
                try (InputStream is = getClass().getClassLoader().getResourceAsStream("library.json")) {
                    if (is == null) {
                        throw new IllegalStateException("library.json not found on classpath. " +
                                "Set 'library.json.path' in application.properties to point to the file.");
                    }
                    loaded = objectMapper.readValue(is, new TypeReference<>() {});
                }
            }
            this.albums = loaded;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load library.json", e);
        }
    }

    /**
     * Applies all filters, sorts, and paginates the album list.
     */
    public PagedResponse<Album> getAlbums(AlbumFilterParams p) {

        Stream<Album> stream = albums.stream()
                // --- Always hide hidden albums ---
                .filter(a -> !Boolean.TRUE.equals(a.hidden));

        // ---- Global Search Filter ----
        if (p.search != null)
            stream = stream.filter(a -> matchesSearch(a, p.search));
        
        // ---- String filters (contains, case-insensitive) ----
        if (p.artist != null)
            stream = stream.filter(a -> containsIgnoreCase(a.artist, p.artist));
        if (p.albumArtist != null)
            stream = stream.filter(a -> a.albumArtist != null && 
                a.albumArtist.stream().anyMatch(v -> v != null && v.equalsIgnoreCase(p.albumArtist.trim())));
        if (p.title != null)
            stream = stream.filter(a -> containsIgnoreCase(a.title, p.title));
        if (p.country != null)
            stream = stream.filter(a -> containsIgnoreCase(a.country, p.country));
        if (p.city != null)
            stream = stream.filter(a -> containsIgnoreCase(a.city, p.city));
        if (p.genre != null)
            stream = stream.filter(a -> a.genre != null &&
                    a.genre.stream().anyMatch(v -> containsIgnoreCase(v, p.genre)));
        if (p.style != null)
            stream = stream.filter(a -> a.style != null &&
                    a.style.stream().anyMatch(v -> containsIgnoreCase(v, p.style)));
        if (p.publisher != null)
            stream = stream.filter(a -> a.publisher != null &&
                    a.publisher.stream().anyMatch(v -> containsIgnoreCase(v, p.publisher)));

        // ---- Boolean filters ----
        if (p.reissue != null)
            stream = stream.filter(a -> p.reissue.equals(a.reissue));
        if (p.fan != null)
            stream = stream.filter(a -> p.fan.equals(a.fan));
        if (p.favorite != null)
            stream = stream.filter(a -> p.favorite.equals(a.favorite));
        if (p.owned != null)
            stream = stream.filter(a -> p.owned.equals(a.owned));
        if (p.tino != null)
            stream = stream.filter(a -> p.tino.equals(a.tino));
        if (p.wire != null)
            stream = stream.filter(a -> p.wire.equals(a.wire));
        if (p.wishlist != null)
            stream = stream.filter(a -> p.wishlist.equals(a.wishlist));
        if (p.hasVideo != null)
            stream = stream.filter(a -> a.video != null);

        // ---- Year filter ----
        if (p.releaseYear != null)
            stream = stream.filter(a -> p.releaseYear.equals(a.releaseYear));

        // ---- Date range filters ----
        if (p.releaseDateFrom != null) {
            LocalDate from = LocalDate.parse(p.releaseDateFrom);
            stream = stream.filter(a -> a.releaseDate != null &&
                    !LocalDate.parse(a.releaseDate).isBefore(from));
        }
        if (p.releaseDateTo != null) {
            LocalDate to = LocalDate.parse(p.releaseDateTo);
            stream = stream.filter(a -> a.releaseDate != null &&
                    !LocalDate.parse(a.releaseDate).isAfter(to));
        }
        if (p.addedDateFrom != null) {
            LocalDate from = LocalDate.parse(p.addedDateFrom);
            stream = stream.filter(a -> a.addedDate != null &&
                    !LocalDateTime.parse(a.addedDate, TIMESTAMP_FORMATTER).toLocalDate().isBefore(from));
        }
        if (p.addedDateTo != null) {
            LocalDate to = LocalDate.parse(p.addedDateTo);
            stream = stream.filter(a -> a.addedDate != null &&
                    !LocalDateTime.parse(a.addedDate, TIMESTAMP_FORMATTER).toLocalDate().isAfter(to));
        }

        // ---- Rating filters (exact value takes precedence over range) ----
        if (p.rating != null) {
            stream = stream.filter(a -> p.rating.equals(a.rating));
        } else {
            if (p.ratingMin != null)
                stream = stream.filter(a -> a.rating != null && a.rating >= p.ratingMin);
            if (p.ratingMax != null)
                stream = stream.filter(a -> a.rating != null && a.rating <= p.ratingMax);
        }

        // ---- Sorting ----
        if (p.sortBy != null) {
            Comparator<Album> comparator = buildComparator(p.sortBy);
            if (comparator != null) {
                if ("desc".equalsIgnoreCase(p.sortDir)) {
                    comparator = comparator.reversed();
                }
                stream = stream.sorted(comparator);
            }
        }

        // ---- Collect then paginate ----
        List<Album> filtered = stream.toList();
        long total = filtered.size();

        int fromIndex = Math.min(p.page * p.size, (int) total);
        int toIndex   = Math.min(fromIndex + p.size, (int) total);
        List<Album> page = filtered.subList(fromIndex, toIndex);

        System.out.println("Records page: " + p.page + " of the year: " + p.releaseYear + " search: " + p.search);

        return new PagedResponse<>(page, p.page, p.size, total);
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private static boolean containsIgnoreCase(String value, String query) {
        if (value == null) return false;
        return value.toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT));
    }

    private static Comparator<Album> buildComparator(String sortBy) {
        return switch (sortBy) {
            case "artist"       -> Comparator.comparing(a -> nullSafe(a.artist));
            case "albumArtist"  -> Comparator.comparing(a -> firstOf(a.albumArtist));
            case "title"        -> Comparator.comparing(a -> nullSafe(a.title));
            case "releaseYear"  -> Comparator.comparing(a -> nullSafeInt(a.releaseYear));
            case "releaseDate"  -> Comparator.comparing(a -> nullSafe(a.releaseDate));
            case "addedDate"    -> Comparator.comparing(a -> nullSafe(a.addedDate));
            case "country"      -> Comparator.comparing(a -> nullSafe(a.country));
            case "city"         -> Comparator.comparing(a -> nullSafe(a.city));
            case "rating"       -> Comparator.comparing(a -> nullSafeDouble(a.rating));
            default             -> null;
        };
    }

    private static String nullSafe(String s)     { return s != null ? s : ""; }
    private static int    nullSafeInt(Integer i)  { return i != null ? i : 0; }
    private static double nullSafeDouble(Double d){ return d != null ? d : 0.0; }
    private static String firstOf(List<String> l) { return (l != null && !l.isEmpty()) ? l.get(0) : ""; }

    private boolean matchesSearch(Album a, String search) {
        if (search == null || search.isBlank()) {
            return true;
        }

        // Verkettung aller zu durchsuchenden Feldern (Strings & Sammlungen)
        StringBuilder sb = new StringBuilder();

        if (a.artist != null) sb.append(a.artist).append(" ");
        if (a.title != null) sb.append(a.title).append(" ");
        if (a.country != null) sb.append(a.country).append(" ");
        if (a.city != null) sb.append(a.city).append(" ");

        if (a.albumArtist != null && !a.albumArtist.isEmpty()) {
            sb.append(String.join(" ", a.albumArtist)).append(" ");
        }
        if (a.genre != null && !a.genre.isEmpty()) {
            sb.append(String.join(" ", a.genre)).append(" ");
        }
        if (a.style != null && !a.style.isEmpty()) {
            sb.append(String.join(" ", a.style)).append(" ");
        }

        String content = sb.toString().toLowerCase();

        // Jedes eingegebene Wort muss an irgendeiner Stelle vorkommen (AND-Verknüpfung)
        String[] keywords = search.trim().toLowerCase().split("\\s+");
        for (String keyword : keywords) {
            if (!content.contains(keyword)) {
                return false;
            }
        }

        return true;
    }
}
