# okapi-client

Kotlin Multiplatform library for interacting with
the [Opencaching API, also known as OKAPI](https://opencaching.pl/okapi/introduction.html).

See [USAGE.md](./USAGE.md) for examples of interacting with OKAPI using `curl`.

## About OKAPI

### Authentication Levels

OKAPI uses a four-tier authentication hierarchy.
Each method has a minimum authentication requirement.

| Level | Name                           | Description                                                       |
|-------|--------------------------------|-------------------------------------------------------------------|
| 0     | Anonymous                      | No authentication required                                        |
| 1     | Consumer Key                   | Requires `consumer_key` parameter                                 |
| 2     | OAuth Consumer Signature       | Requires OAuth 1.0a signature with Consumer Secret                |
| 3     | OAuth Consumer+Token Signature | Requires full OAuth signature with Consumer Secret + Token Secret |

### Level 2 Endpoints

Only one endpoint requires Level 2:

| Endpoint                       | Purpose             |
|--------------------------------|---------------------|
| `services/oauth/request_token` | Initiate OAuth flow |

### Level 3 Endpoints

These endpoints require Level 3 authentication (full OAuth with access token):

| Endpoint                              | Purpose                                 |
|---------------------------------------|-----------------------------------------|
| `services/oauth/access_token`         | Exchange request token for access token |
| `services/logs/submit`                | Submit a new log entry                  |
| `services/logs/edit`                  | Edit an existing log entry              |
| `services/logs/delete`                | Delete a log entry                      |
| `services/logs/capabilities`          | Get logging capabilities for a cache    |
| `services/logs/images/add`            | Add image to a log                      |
| `services/logs/images/edit`           | Edit a log image                        |
| `services/logs/images/delete`         | Delete a log image                      |
| `services/caches/edit`                | Edit a geocache (owner only)            |
| `services/caches/mark`                | Mark cache as watched/ignored           |
| `services/caches/save_personal_notes` | Save personal notes for a cache         |
| `services/caches/map/tile`            | Get map tiles                           |

### Level 1 Endpoints with Level 3 Parameters

Some endpoints work at Level 1,
but have specific parameters or fields that require Level 3.

#### `services/caches/geocache`

Fields requiring Level 3:

- `is_watched` - whether user is watching the cache
- `is_ignored` - whether user has ignored the cache
- `my_rating` - user's rating of the cache
- `my_notes` - user's personal notes
- `alt_wpts` (user-coords type) - user-submitted corrected coordinates

#### `services/users/user`

Level 3 enables:

- `user_uuid` parameter becomes optional (omit to get current authenticated user)
- `home_location` field returns actual value (otherwise null)
- `is_admin` field returns actual boolean (otherwise null)

#### `services/caches/search/all` (and `nearest`, `bbox`, `save`)

Parameters requiring Level 3:

- `found_status` - filter by found/not found status
- `recommended_only` - filter to caches user recommended
- `watched_only` - filter to watched caches
- `ignored_status` - filter by ignored status
- `exclude_ignored` - exclude ignored caches (deprecated)
- `exclude_my_own` - exclude user's own caches

#### `services/caches/formatters/gpx`

Level 3 required for:

- `my_notes` - any value other than `none`
- `latest_logs` with value `user` - include only the authenticated user's logs
