#!/bin/sh

BUILD_DIR=bin
rm -rf "$BUILD_DIR"
mkdir -p "$BUILD_DIR"

SRC="$(find ./src -type f -name '*.java' | tr '\n' ' ')"
javac -d "$BUILD_DIR" $SRC
cp -r icons images layouts "$BUILD_DIR"
