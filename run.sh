#!/bin/sh

cd bin || ./build.sh
java "$@"
