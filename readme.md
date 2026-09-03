# Album Collection Quarkus Backend

Dies ist ein Backend das endpunkte bereitgestellt um inhalte aus einer musiksammlung zurückzugeben.

Es gibt folgende Endpunkte

## GET albums

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

## GET yearsAndDecades

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


## Swagger

Mehr informationen zu filtermöglichkeiten sind in der Swagger umgebung zu finden

http://localhost:8080/swagger-ui/