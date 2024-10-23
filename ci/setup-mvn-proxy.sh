#!/bin/bash
# ci/setup-mvn-proxy.sh
# This script configures Maven to use the proxy if needed

if [ ! -z "$http_proxy" ]; then
    # Extract proxy host and port from http_proxy environment variable
    PROXY_HOST=$(echo $http_proxy | sed 's|http://||' | cut -d: -f1)
    PROXY_PORT=$(echo $http_proxy | sed 's|http://||' | cut -d: -f2)
    
    # Create .m2 directory if it doesn't exist
    mkdir -p ~/.m2
    
    # Create settings.xml with proxy configuration
    cat > ~/.m2/settings.xml << EOF
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                             https://maven.apache.org/xsd/settings-1.0.0.xsd">
    <proxies>
        <proxy>
            <id>proxy</id>
            <active>true</active>
            <protocol>http</protocol>
            <host>${PROXY_HOST}</host>
            <port>${PROXY_PORT}</port>
        </proxy>
    </proxies>
</settings>
EOF
    
    echo "Maven proxy configured with host: ${PROXY_HOST} and port: ${PROXY_PORT}"
else
    echo "No proxy configuration needed"
fi
