#!/bin/sh

set -x
set -e

THIS_DIR="$(cd "$(dirname "$0")"; pwd)"
RESULT_LIB_PATH="$THIS_DIR/../../os/darwin/libpty.dylib"
LIB_X86_64="$THIS_DIR/libpty-x86-64.dylib"
LIB_ARM64="$THIS_DIR/libpty-arm64.dylib"

C_FLAGS="-std=c11 -O2 -Wall -Wextra -Wpedantic -Wno-newline-eof"
CC=gcc

[ -f "$LIB_X86_64" ] && rm "$LIB_X86_64"
[ -f "$LIB_ARM64" ] && rm "$LIB_ARM64"

FILES="$THIS_DIR/../exec_pty.c $THIS_DIR/../openpty.c $THIS_DIR/../pfind.c"

$CC -arch x86_64 -mmacosx-version-min=10.9 -std=c11 $C_FLAGS -dynamiclib -o "$LIB_X86_64" $FILES
$CC -arch arm64 -mmacosx-version-min=10.9 -std=c11 $C_FLAGS -dynamiclib -o "$LIB_ARM64" $FILES


lipo -create "$LIB_X86_64" "$LIB_ARM64" -o "$RESULT_LIB_PATH"

[ -f "$LIB_X86_64" ] && rm "$LIB_X86_64"
[ -f "$LIB_ARM64" ] && rm "$LIB_ARM64"

echo "Done successfully"
