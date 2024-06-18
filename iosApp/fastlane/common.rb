def create_export_options_plist(bundle_id:, provisioning_profile:, method:)
    <<~PLIST
      <?xml version="1.0" encoding="UTF-8"?>
      <!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
      <plist version="1.0">
      <dict>
          <key>method</key>
          <string>#{method}</string>
          <key>provisioningProfiles</key>
          <dict>
            <key>#{bundle_id}</key>
            <string>#{provisioning_profile}</string>
          </dict>
          <key>signingStyle</key>
          <string>manual</string>
          </dict>
      </plist>
    PLIST
  end
