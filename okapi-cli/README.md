# okapi-cli

A command-line interface for OKAPI.

## Usage

```console
okapi geocache --coords 50.19479,18.44652 --radius-km 3
```

```console
CACHE_ID=OP8JJ4 # Rudy Młynek
okapi logs get --geocache-id $CACHE_ID --limit 5
```

## Development

### Native executable

Build a native executable (here, Apple Silicon):

```console
./gradlew :okapi-cli:linkDebugExecutableMacosArm64 
```

```console
./okapi-cli/build/bin/macosArm64/debugExecutable/okapi.kexe
```

### JVM

Build a JVM jar and startup script:

```console
./gradlew :okapi-cli:installJvmDist
```

Run it:

```console
./okapi-cli/build/install/okapi-cli-jvm/bin/okapi-cli 
```
