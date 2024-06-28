# opencaching

[![CI status][ci_badge]][ci_link]

A simple, cross-platform Geocaching app built with Compose Multiplatform.

Supports OpenCaching sites through [OKAPI].

### Credentials setup

It's fully automated thanks to 1Password.

1. Run `./setup_credentials` script from the project root. It creates, among
   other stuff, an `.env` file with all the required secrets, encoded as
   [1Password secret references]

2. Export values from the `.env` file into the current shell environment:

    ```console
    while read -r line; do export "$line"; done < .env
    ```

3. To run a command that requires secrets in the form of environment variables,
   prefix it with [`op run`][oprun], for example:

   ```console
   op run -- bundle exec fastlane ios tst
   ```

### CI

This project uses [Cirrus CI] for CI and CD.

```
op run -- cirrus run \
  --output simple \
  --lazy-pull \
  --env CIRRUS_BRANCH=master \
  --env OP_SERVICE_ACCOUNT_TOKEN="$(op read "op://Personal/1Password/service accounts/3eyjcas2mkzd4yh4jmwgd7xzoa")" \
  'Deploy iOS app'
```

### Android setup

> [!NOTE]  
> This file is automatically created by the `setup_credentials` script.

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
[ci_badge]: https://api.cirrus-ci.com/github/bartekpacia/opencaching.svg
[ci_link]: https://cirrus-ci.com/github/bartekpacia/opencaching
[1Password secret references]: https://developer.1password.com/docs/cli/secret-references
[Cirrus CI]: https://cirrus-ci.org/
