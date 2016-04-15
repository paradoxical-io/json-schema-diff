#!/usr/bin/env bash

set -x

if [ "$TRAVIS_BRANCH" == "master" ]; then
  mvn clean deploy -DskipTests -s settings.xml
fi