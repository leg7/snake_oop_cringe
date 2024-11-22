#!/bin/sh

cd bin || ./build.sh
java "$1"
