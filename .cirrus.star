load(
    "./cirrus/common.star",
    "secrets",
    "setup_1password_cli",
    "setup_credentials",
    "setup_fastlane",
)
load("cirrus", "fs", "yaml")
load(
    "github.com/cirrus-modules/helpers",
    "cache",
    "container",
    "macos_instance",
    "script",
    "task",
)

def main():
    return [
        task(
            name = "prepare",
            instance = container(
                image = "ghcr.io/cirruslabs/android-sdk:34",
            ),
            instructions = [
                script(
                    "presetup",
                    "touch local.properties",
                ),
                script(
                    "lint",
                    "./gradlew :composeApp:lint",
                ),
            ],
        ),
        task(
            name = "Deploy iOS app",
            env = {"CIRRUS_CLONE_TAGS": "true"} | secrets(),
            instance = macos_instance(
                image = "ghcr.io/cirruslabs/macos-sonoma-xcode@sha256:07bbebb2931113e187a49284f98d3834ffbe8584e9c90ab789d914b0f2df4a40",
            ),
            only_if = "$CIRRUS_TAG =~ 'v.*' || $CIRRUS_BRANCH == 'master'",
            instructions = [
                cache("cocoapods", "~/.cocoapods"),
                setup_1password_cli(),
                setup_credentials(),
                setup_fastlane(),
                script(
                    "fastlane_ios_distribute",
                    "chmod +x ./gradlew",  # workaround for "permission denied" when running gradlew
                    "cd iosApp",
                    """\
if [ "$CIRRUS_TAG" == "v*" ]; then
    op run -- bundle exec fastlane ios prod
else
    op run -- bundle exec fastlane ios tst
fi
""",
                ),
            ],
        ),
    ]
