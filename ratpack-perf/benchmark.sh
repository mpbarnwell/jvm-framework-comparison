#!/bin/sh

ghz --insecure \
  --proto ./src/test/proto/helloworld.proto \
  --call helloworld.Greeter.SayHello \
  -d '{"name":"Bumble"}' \
  -z 5m \
  0.0.0.0:50051