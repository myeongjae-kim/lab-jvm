#!/bin/bash

mkdir -p build/graalvm

docker run --rm -v $(pwd):/working ghcr.io/graalvm/graalvm-community:21 \
    /bin/bash -c "
                    native-image \
                      --enable-url-protocols=http,https \
                      -H:+UnlockExperimentalVMOptions \
                      -H:ReflectionConfigurationFiles=/working/src/main/resources/reflect.json \
                      --report-unsupported-elements-at-runtime \
                      --no-server \
                      -jar /working/build/libs/graalvm-kotlin-aws-lambda-all.jar \
                      -o /working/build/graalvm/server"

if [ ! -f "build/graalvm/server" ]; then
    echo 'there was an error building graalvm image'
    exit 1
fi
