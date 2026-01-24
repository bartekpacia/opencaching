# Repository Guidelines

## Project Structure & Module Organization
- `composeApp/` is the shared Compose Multiplatform app (Android, iOS, desktop).
- `androidApp/` and `iosApp/` host platform shells and packaging.
- `okapi-client/` is the OKAPI client library; `okapi-cli/` is the CLI wrapper.
- Shared Kotlin sources live under `*/src/commonMain/`; tests live under
  `*/src/commonTest/` (examples: `composeApp/src/commonTest/`,
  `okapi-client/src/commonTest/`).
- Tooling and config: `config/detekt/` (static analysis), `fastlane/` (release),
  `docs/` (project notes).

## Build, Test, and Development Commands
- `./gradlew build` builds all modules.
- `./gradlew test` runs common JVM tests across modules.
- `./gradlew :composeApp:desktopRun` launches the desktop app.
- `./gradlew :androidApp:assembleDebug` builds an Android debug APK.
- `./gradlew :okapi-cli:linkDebugExecutableMacosArm64` builds the native CLI
  (see `okapi-cli/README.md` for other targets).
- `./gradlew :okapi-cli:installJvmDist` builds the JVM CLI distribution.

## Coding Style & Naming Conventions
- Kotlin/Compose with 4-space indentation; keep imports sorted by IDE defaults.
- Classes use `UpperCamelCase`, functions/vars use `lowerCamelCase`,
  constants use `UPPER_SNAKE_CASE`, packages are lowercase.
- Static analysis uses Detekt with auto-correct enabled; config lives in
  `config/detekt/detekt.yml`.

## Testing Guidelines
- Tests use `kotlin.test` and Compose UI test helpers.
- Name tests as `*Test.kt`, colocated in `src/commonTest`.
- Run all tests with `./gradlew test` or a module-specific task like
  `./gradlew :okapi-client:test`.

## Commit & Pull Request Guidelines
- Commits are short, imperative, and often scoped (`chore: update .gitignore`,
  `bump deps`, `fix ...`). Follow that style.
- PRs should include a clear description, linked issues if applicable, and
  screenshots for UI changes. Ensure CI passes before requesting review.

## Security & Configuration Tips
- Run `./setup_credentials` to bootstrap secrets and `.env`.
- For commands that need secrets, prefix with `op run --` as in `README.md`.
- Never commit local keys like `google_maps_api_key.xml`, `.env`,
  `local.properties`, or signing files.
