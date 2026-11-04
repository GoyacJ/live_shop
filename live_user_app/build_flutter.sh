#!/bin/bash

# 读取 pubspec.yaml 中的版本号
version=$(grep 'version:' pubspec.yaml | awk '{print $2}' | cut -d '+' -f1)

flutter clean
flutter pub get

echo "📦 开始构建 Flutter APK v$version（多 ABI）..."
flutter build apk --release --no-tree-shake-icons

echo "✅ 构建完成"