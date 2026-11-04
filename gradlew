#!/bin/sh
# Gradle start up script for POSIX systems
exec java -version >/dev/null 2>&1 || { echo "Java not found. Please install JDK 17+"; exit 1; }
exec ./gradle/wrapper/gradle-wrapper.jar "$@" 2>/dev/null || { echo "Run with gradle assembleDebug or open in Android Studio"; }
