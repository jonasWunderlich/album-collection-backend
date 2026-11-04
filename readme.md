# Album Collection Quarkus Backend

Dies ist ein Backend das Endpunkte bereitstellt um Inhalte aus einer Musiksammlung zurückzugeben.
Die vollständigen Daten werden aus einer JSON Datei beim Start der Applikation in den Memory geladen. Damit wird vollständig auf eine externe Datenbank verzichtet. Die JSON ist etwa 55 MB groß und enthält ca. 68'000 Einträge. Auch das Frontend wird statisch mit im Quarkus Backend ausgeliefert.

Diese Dateien werden in einer externen Build-Pipeline verarbeitet und an folgende Orte kopiert:

- Die Daten liegen unter src/main/resources/***library.json***

- Das Frontend liegt unter src/main/resources/META-INF/***index.html, style.css, main.js ...***

Die Bilder welche für das Frontend noch benötigt werden werden in der schon genannten Pipeline direkt mit dem Server synchronisisert.

## Endpunkte

Es gibt folgende Endpunkte um diese Einträge auszulesen

### GET albums

Gibt eine gefilterte Liste von Metadaten zu Alben zurück.

Der Rückgabewert hat folgende struktur.

    {
    "content": [
        {
        "albumArtist": [
            "string"
        ],
        "artist": "string",
        "title": "string",
        "releaseYear": 0,
        "releaseDate": "string",
        "addedDate": "string",
        "publisher": [
            "string"
        ],
        "genre": [
            "string"
        ],
        "style": [
            "string"
        ],
        "country": "string",
        "city": "string",
        "rating": 0,
        "reissue": true,
        "fan": true,
        "favorite": true,
        "owned": true,
        "tino": true,
        "wire": true,
        "wishlist": true,
        "hidden": true,
        "dateFirstRelease": "string",
        "urlAlbum": "string",
        "urlArtist": "string",
        "urlCover": "string",
        "urlVideo": "string",
        "urlWikipedia": "string"
        }
    ],
    "page": 0,
    "size": 0,
    "totalElements": 0,
    "totalPages": 0
    }

### GET yearsAndDecades

Gibt alle vorhandenen releaseYears und decades als Listen zurück

Der Rückgabewert hat folgende Struktur.

{
  "releaseYears": [
    "string"
  ],
  "decades": [
    "string"
  ]
}

## Swagger UI

Mehr Informationen zu Filtermöglichkeiten sind in der Swagger UI zu finden.

    http://localhost:8080/swagger-ui/


## Run

### Build

Der Build enthält auch bereits einen Angular Build für die Auslieferung des Frontends.
Damit bauen wir ein natives Executable für Linux, macOS und Windows ohne zusätzliche Systemabhängigkeiten. Wir brauchen kein Docker, keine Datenbank und keinen Webserver.

    ./mvnw clean package -Dnative -Dquarkus.analytics.disabled=true

### Start

    ./mvnw quarkus:dev -Dquarkus.analytics.disabled=true

### Stop

    ./mvnw quarkus:stop -Dquarkus.analytics.disabled=true

### Logs

    tail -f target/quarkus-app/quarkus.log
