# opencaching

A simple, cross-platform Geocaching app built with Compose Multiplatform.

Supports OpenCaching sites through [OKAPI].

### Android setup

To display the map, a Google Maps API key is needed. It can be obtained through
Google Cloud Console. Once you have the key, paste it into
`composeApp/src/androidMain/res/values/google_maps_api_key.xml`:

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="google_maps_key" translatable="false">API_KEY_HERE</string>
</resources>
```

[OKAPI]: https://www.opencaching.pl/okapi/introduction.html
