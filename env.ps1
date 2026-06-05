$env:ANDROID_SDK_ROOT = "$PSScriptRoot\tools\android-sdk"
$env:GRADLE_USER_HOME = "$PSScriptRoot\.gradle_home"
$env:JAVA_HOME = "C:\Program Files\Microsoft\jdk-17.0.18.8-hotspot"

# Gradle wrapper
Set-Alias -Name gradlew -Value "$PSScriptRoot\gradlew.bat"

Write-Output "OK: ANDROID_SDK_ROOT=$env:ANDROID_SDK_ROOT"
Write-Output "OK: GRADLE_USER_HOME=$env:GRADLE_USER_HOME"
Write-Output "OK: JAVA_HOME=$env:JAVA_HOME"
Write-Output "Ready. Use: gradlew assembleDebug"
