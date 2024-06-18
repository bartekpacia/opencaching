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
                    "./gradlew :composeApp:embedAndSignAppleFrameworkForXcode",
                    "fastlane_ios_distribute",
                    "cd iosApp",
                    # "chmod +x /Users/admin/Library/Developer/Xcode/DerivedData/iosApp-hbzxgesegqzspqcqjnzjaxabpiif/Build/Intermediates.noindex/ArchiveIntermediates/iosApp/IntermediateBuildFilesPath/iosApp.build/Release-iphoneos/iosApp.build/Script-F36B1CEB2AD83DDC00CB74D5.sh",
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
