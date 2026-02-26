#!/usr/bin/env bash
#
# Regenerate Play Store screenshots from Compose UI tests.
#
# Prerequisites:
#   - A running Android emulator or connected device
#   - ADB on PATH (or set ADB below)
#
# Usage:
#   ./store-listing/regenerate-screenshots.sh

set -euo pipefail

ADB="${ANDROID_HOME:-$HOME/Library/Android/sdk}/platform-tools/adb"
PROJECT_ROOT="$(cd "$(dirname "$0")/.." && pwd)"
SCREENSHOT_DIR="$PROJECT_ROOT/store-listing/screenshots"
PKG="com.droid.shopping.debug"
TEST_RUNNER="$PKG.test/androidx.test.runner.AndroidJUnitRunner"
TEST_CLASS="com.droid.shopping.ScreenshotCapture"
SCREENSHOTS=(01_main_list 02_add_item 03_filter_category 04_sort_alphabetical 05_empty_state)

echo "==> Building debug + test APKs..."
"$PROJECT_ROOT/gradlew" -p "$PROJECT_ROOT" assembleDebug assembleDebugAndroidTest -q

echo "==> Installing APKs..."
"$ADB" install -r "$PROJECT_ROOT/app/build/outputs/apk/debug/app-debug.apk"
"$ADB" install -r "$PROJECT_ROOT/app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk"

echo "==> Running screenshot tests..."
"$ADB" shell "am instrument -w -e class $TEST_CLASS $TEST_RUNNER"

echo "==> Pulling screenshots..."
mkdir -p "$SCREENSHOT_DIR"
for f in "${SCREENSHOTS[@]}"; do
    "$ADB" shell "run-as $PKG cat files/store-screenshots/${f}.png > /data/local/tmp/${f}.png"
    "$ADB" pull "/data/local/tmp/${f}.png" "$SCREENSHOT_DIR/${f}.png"
    "$ADB" shell "rm /data/local/tmp/${f}.png"
done

echo "==> Done. Screenshots saved to:"
ls -1 "$SCREENSHOT_DIR"
