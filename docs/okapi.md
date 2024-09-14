# OKAPI

To get curl commands, check out [curl2httpie](https://curl2httpie.online).

### Get cache IDs in a bounding box

```console
http GET opencaching.pl/okapi/services/apiref/method name=='services/caches/search/bbox'
```

```
N|E: 50.215760, 18.488641
S|W: 50.181251, 18.421759
```

S|W|N|E

```console
http GET opencaching.pl/okapi/services/caches/search/bbox \
  consumer_key==$OKAPI_CONSUMER_KEY \
  bbox=='50.181251|18.421759|50.215760|18.488641'
```

### Get cache data by its ID

Basic request: Drewniany most na rudzie (OP9655):

```console
http GET opencaching.pl/okapi/services/caches/geocache \
  consumer_key==$OKAPI_CONSUMER_KEY \
  cache_code==OP9655
```

Customized fields:

```console
http GET opencaching.pl/okapi/services/caches/geocache \
  consumer_key==$OKAPI_CONSUMER_KEY \
  cache_code==OP9655 \
  fields=="code|name|location|type|status|url"
```

### Get caches in a bounding box

```console
http GET opencaching.pl/okapi/services/caches/shortcuts/search_and_retrieve \
    consumer_key==$OKAPI_CONSUMER_KEY \
    search_method=="services/caches/search/bbox" \
    search_params=='{"bbox": "50.181251|18.421759|50.215760|18.488641"}' \
    retr_method=="services/caches/geocaches" \
    retr_params=='{"fields": "code|name|location|status|type|url|owner"}' \
    wrap==false
```

### Get cache's logs

```console
http GET opencaching.pl/okapi/services/logs/logs \
    consumer_key==$OKAPI_CONSUMER_KEY \
    cache_code==OP9655
```
