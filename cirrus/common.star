load("github.com/cirrus-modules/helpers", "script")

def secrets():
    return {
        "OP_SERVICE_ACCOUNT_TOKEN": "ENCRYPTED[1c8ed911ba6af668314a9fc5729c56abaadeac2c9ef67af86840b1fe65e40b0e5fb49b249739d57c1d3841655ba7ab33]",
    }

def setup_credentials():
    return script(
        "setup_credentials",
        """\
if [ $(uname) = \"Linux\" ]; then
    apt-get update && apt-get install -y jq
fi
""",
        "chmod +x ./setup_credentials && ./setup_credentials",
        'while read -r line; do echo "$line" >> $CIRRUS_ENV; done < .env',
    )

def setup_1password_cli():
    return script(
        "setup_1password_cli",
        """\
if [ $(uname) = \"Linux\" ]; then
    apt-get update && apt-get install -y jq
    %s
elif [ $(uname) = \"Darwin\" ]; then
    %s
fi
""" % (_setup_1password_cli_debian, _setup_1password_cli_macos),
    )

_setup_1password_cli_debian = """\
# Add key for 1Password APT repository
    curl -sS https://downloads.1password.com/linux/keys/1password.asc | sudo gpg --dearmor --output /usr/share/keyrings/1password-archive-keyring.gpg
    # Add the 1Password apt repository
    echo "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/1password-archive-keyring.gpg] https://downloads.1password.com/linux/debian/$(dpkg --print-architecture) stable main" | sudo tee /etc/apt/sources.list.d/1password.list
    #Add the debsig-verify policy
    sudo mkdir -p /etc/debsig/policies/AC2D62742012EA22/
    curl -sS https://downloads.1password.com/linux/debian/debsig/1password.pol | sudo tee /etc/debsig/policies/AC2D62742012EA22/1password.pol
    sudo mkdir -p /usr/share/debsig/keyrings/AC2D62742012EA22
    curl -sS https://downloads.1password.com/linux/keys/1password.asc | sudo gpg --dearmor --output /usr/share/debsig/keyrings/AC2D62742012EA22/debsig.gpg
    # Install 1Password CLI
    apt-get update && apt-get install -y 1password-cli"""

_setup_1password_cli_macos = """\
brew install 1password-cli"""

def setup_fastlane():
    return script(
        "setup_fastlane",
        "gem install bundler",
        # "cd android && bundle install",
        # "cd ..",
        "cd iosApp && bundle install",
    )
