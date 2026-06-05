# Stryker — 포크 (커뮤니티 부활판)

Stryker는 루팅된 안드로이드 기기에서 사용하는 모바일 침투 테스트 도구입니다. Alpine Linux chroot 환경 내에서 각종 침투 테스트 도구를 실행합니다.

> **이 프로젝트는 커뮤니티 포크입니다.** 원작자 @zalexdev의 [stryker-project/app](https://github.com/stryker-project/app)이 개발 종료(EOL)됨에 따라, 서버 의존성을 제거하고 프로젝트 전체를 현대화했습니다.

## 요구사항

- 안드로이드 8.0+
- 루팅 (Magisk 23.0+)
- 64비트 CPU (권장)

## 기능

- **WiFi 감사** — WPS PixieDust, PIN 무차별 대입, 핸드셰이크 캡처, 디오스 공격, PSK 무차별 대입
- **로컬 네트워크** — EternalBlue, BlueKeep, SMBGhost 스캐닝, ARP 스푸핑
- **Nmap 스캐너** — OS/서비스 감지 포함 포트 스캐닝
- **라우터 스캐너** — 멀티스레드 IP 대역 스캐너, CSV 내보내기 지원
- **익스플로잇 허브** — 침투 테스트 스크립트 실행을 위한 커스텀 익스플로잇 마법사
- **SearchSploit** — Exploit-DB 검색 연동
- **Geomac** — OpenStreetMap 기반 MAC 주소 위치 추적
- **핸드셰이크 저장소** — WPA 핸드셰이크 캡처 및 무차별 대입
- **코어 관리자** — chroot 내 패키지 관리자 (apt/pip3)
- **모듈 저장소** — 확장 가능한 플러그인 시스템

## 원본 대비 변경사항

### 1단계 — 서버 의존성 제거
- GitHub 업데이트/메시지 확인 코드 제거 (Dashboard, CheckUpdates, CheckMsg)
- 원격 모듈 목록을 로컬 `assets/modules_list.json`으로 대체
- chroot 다운로드를 로컬 파일 방식(`stryker.tar.gz`)으로 변경
- Account, About 화면의 후원/홍보 링크 숨김 처리

### 2단계 — 현대화
- **AGP** 7.1.2 → 8.2.2, **Gradle** 7.2 → 8.4
- **compileSdk** 34, **targetSdk** 34, **Java** 17
- **JCenter** 저장소 제거
- **Kotlin** 1.9.22 플러그인 추가
- **AsyncTask** → `StrykerTask` (ExecutorService 기반, 49개 클래스)
- Deprecated API 38건 수정 (`getFragmentManager`, `Html.fromHtml`, `PreferenceManager`, `onBackPressed`, `NetworkInfo` 등)
- 의존성 라이브러리 최신 안정화 버전으로 업데이트

## 빌드 방법

### 필요 환경
- JDK 17
- Android SDK (platforms;android-34, build-tools;34.0.0)

### 빌드
```bash
# 환경 변수 설정 (PowerShell)
$env:ANDROID_SDK_ROOT = "path/to/android-sdk"
$env:GRADLE_USER_HOME = "path/to/.gradle_home"

# 빌드 실행
./gradlew assembleDebug
```

`env.ps1` 샘플 환경 스크립트가 포함되어 있습니다.

### APK 출력 경로
```
app/build/outputs/apk/debug/app-debug.apk
```

## 설치 방법

1. APK 설치
2. Alpine Linux chroot tar 파일(`stryker.tar.gz`)을 `/storage/emulated/0/Download/`에 위치
3. 첫 실행 시 저장소 권한 및 루트 권한 허용
4. 설정 마법사 완료

## 라이선스

[LICENSE](LICENSE) — 원작자 @zalexdev.

## 면책 조항

이 도구는 보안 연구 및 허가된 침투 테스트 목적으로만 사용해야 합니다. 유지보수자는 오용에 대해 어떠한 책임도 지지 않습니다.
