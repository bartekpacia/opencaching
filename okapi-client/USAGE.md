# OKAPI

This document describes basic usage of [OKAPI] with only `curl`.

### Get information on a given OKAPI service method

```sh
curl "opencaching.pl/okapi/services/apiref/method" \
    --data-urlencode "name=services/caches/search/bbox"
```

### Get cache IDs in a bounding box

The format:

```
N|E: 50.215760, 18.488641
S|W: 50.181251, 18.421759
```

The order is `S|W|N|E`.

Example:

```sh
curl "opencaching.pl/okapi/services/caches/search/bbox" \
    --data-urlencode "consumer_key=$OKAPI_CONSUMER_KEY" \
    --data-urlencode "bbox=50.181251|18.421759|50.215760|18.488641"
```

### Get cache data by its ID

Example request to get data of the cache
[Drewniany most na rudzie (OP9655)](https://opencaching.pl/viewcache.php?wp=OP9655):

```sh
curl "opencaching.pl/okapi/services/caches/geocache" \
    --data-urlencode "consumer_key=$OKAPI_CONSUMER_KEY" \
    --data-urlencode "cache_code=OP9655"
```

Customized fields:

```sh
curl "opencaching.pl/okapi/services/caches/geocache" \
  --data-urlencode "consumer_key=$OKAPI_CONSUMER_KEY" \
  --data-urlencode "cache_code=OP9655" \
  --data-urlencode "fields=code|name|location|type|status|url"
```

### Get caches in a bounding box

```sh
curl "opencaching.pl/okapi/services/caches/shortcuts/search_and_retrieve" \
    --data-urlencode "consumer_key=$OKAPI_CONSUMER_KEY" \
    --data-urlencode "search_method=services/caches/search/bbox" \
    --data-urlencode 'search_params={"bbox": "50.181251|18.421759|50.215760|18.488641"}' \
    --data-urlencode "retr_method=services/caches/geocaches" \
    --data-urlencode 'retr_params={"fields": "code|name|location|status|type|url|owner"}' \
    --data-urlencode "wrap=false"
```

### Get cache's logs

```sh
curl "opencaching.pl/okapi/services/logs/logs" \
    --data-urlencode "consumer_key=$OKAPI_CONSUMER_KEY" \
    --data-urlencode "cache_code=OP9655"
```
