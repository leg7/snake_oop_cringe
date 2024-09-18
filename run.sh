#!/bin/sh

cd build || ./build.sh
java "$1"
