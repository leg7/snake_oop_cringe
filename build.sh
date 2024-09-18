#!/bin/sh

BUILD_DIR=build
mkdir -p "$BUILD_DIR"

SRC="$(find ./src -type f -name '*.java' | tr '\n' ' ')"
javac -d "$BUILD_DIR" $SRC
