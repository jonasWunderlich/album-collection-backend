package com.hss.album;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class CoverSlugUtil {

    private static final Pattern NON_ALPHANUMERIC = Pattern.compile("[^a-z0-9]");
    private static final Pattern MULTIPLE_DASHES = Pattern.compile("-+");
    private static final Pattern TRIM_DASHES = Pattern.compile("^-|-$");

    public static String formatArtistAndAlbumSlug(Integer year, String artist, String album) {
        String cleanArtist = sanitize(artist);
        String cleanAlbum = sanitize(album);
        
        String yearStr = (year != null) ? String.valueOf(year) : "unknown";

        return "./thumbs/" + yearStr + "/" + cleanArtist + "--" + cleanAlbum + ".webp";
    }

    private static String sanitize(String input) {
        if (input == null || input.isBlank()) {
            return "";
        }

        String result = input.trim().toLowerCase();

        // 1. Umlaute ersetzen
        result = result.replace("ä", "ae")
                       .replace("ö", "oe")
                       .replace("ü", "ue")
                       .replace("ß", "ss");

        // 2. Sonderzeichen zu Bindestrichen
        result = NON_ALPHANUMERIC.matcher(result).replaceAll("-");

        // 3. Mehrfache Bindestriche reduzieren
        result = MULTIPLE_DASHES.matcher(result).replaceAll("-");

        // 4. Führende/nachfolgende Bindestriche trimmen
        result = TRIM_DASHES.matcher(result).replaceAll("");

        return result;
    }
}