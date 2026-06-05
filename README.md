# Stryker — 포크 (커뮤니티 부활판)

Stryker는 루팅된 안드로이드 기기에서 사용하는 모바일 침투 테스트 도구입니다.
Alpine Linux chroot 환경에서 각종 오픈소스 보안 도구를 실행하여 WiFi, 네트워크, 서비스 취약점을 분석합니다.

> **커뮤니티 포크입니다.** 원작자 @zalexdev의 [stryker-project/app](https://github.com/stryker-project/app)이 개발 종료(EOL)됨에 따라,
> 서버 의존성을 전부 제거하고 프로젝트를 현대화하여 독립 실행 가능하게 재구성했습니다.

---

## 요구사항

| 항목 | 사양 |
|------|------|
| 운영체제 | 안드로이드 8.0 이상 |
| 루팅 | Magisk 23.0 이상 |
| CPU | 64비트 권장 (32비트 일부 제한) |
| 저장소 | chroot 이미지용 약 500MB 여유 공간 |

---

## 기능 개요

| 모듈 | 설명 |
|------|------|
| **WiFi 감사** | WPS PixieDust, PIN/PSK 무차별 대입, 핸드셰이크 캡처, 디오스 공격 |
| **로컬 네트워크** | EternalBlue/BlueKeep/SMBGhost 스캔, ARP 스푸핑, SMB/SNMP 열거, 서비스 무차별 대입 |
| **Nmap 스캐너** | 포트 스캔, OS/서비스 버전 감지, 고속 스캔 옵션 |
| **라우터 스캐너** | 멀티스레드 IP 대역 스캔, 109종 라우터 DB, CSV 내보내기 |
| **익스플로잇 허브** | 커스텀 스크립트 등록/실행 마법사, 성공 패턴 정규식 매칭 |
| **SearchSploit** | Exploit-DB 오프라인 데이터베이스 검색 |
| **Geomac** | MAC 주소 기반 위치 추적 (OpenStreetMap) |
| **핸드셰이크 저장소** | WPA 핸드셰이크 캡처/관리/무차별 대입 (aircrack-ng) |
| **코어 관리자** | chroot 내 패키지 관리 (apk, pip3) |
| **모듈 저장소** | 확장 플러그인 시스템 |
| **Metasploit** | msfconsole 대화형 콘솔 (개발 중) |
| **3WiFi** | 온라인 WiFi 비밀번호 데이터베이스 연동 |

상세 기능 설명은 [docs/FEATURES.md](docs/FEATURES.md)를 참고하세요.

---

## 원본 대비 변경사항

### 서버 의존성 제거

| 대상 | 변경 내용 |
|------|-----------|
| Dashboard/CheckUpdates/CheckMsg | GitHub 업데이트/메시지 확인 코드 제거 |
| Core.getModules() | 원격 modules.list를 로컬 `assets/modules_list.json`으로 대체 |
| Slide3 (초기 설치) | GitHub chroot 다운로드를 로컬 파일(`stryker.tar.gz`) 방식으로 변경 |
| Account/About | 후원 및 외부 링크 숨김 처리 |

### 현대화

| 항목 | 변경 전 | 변경 후 |
|------|---------|---------|
| AGP | 7.1.2 | 8.2.2 |
| Gradle | 7.2 | 8.4 |
| compileSdk | 31 | 34 |
| targetSdk | 28 | 34 |
| Java | 1.8 | 17 |
| Kotlin | 없음 | 1.9.22 |
| JCenter | 활성 | 제거 |
| AsyncTask | 49개 클래스 | StrykerTask (ExecutorService) |
| Deprecated API | 38건 방치 | 전건 수정 |

### 버그 수정 및 견고화

- 4곳의 CPU 100% busy-wait를 CountDownLatch로 교체
- MsfConsole 무한 스레드 누수 및 프로세스 미종료 수정
- ScanTarget Timer 리소스 누수 수정
- 44개 셸 명령 실행 파일에 60초 타임아웃 추가
- 7개 파일에 쉘 인젝션 방어 (sanitizeShellArg)
- 6개 파일의 `su`를 `su -mm`으로 통일
- chroot 바이너리 권한 777 → 755 축소
- 위도/경도 필드 뒤바뀜, 오타, NPE 등 15건 이상의 기능 버그 수정

전체 변경 내역은 [CHANGELOG.md](CHANGELOG.md)를 참고하세요.

---

## 빌드 방법

### 필요 환경

- JDK 17
- Android SDK (platforms;android-34, build-tools;34.0.0)

### 빌드 명령

```powershell
# 환경 변수 설정
$env:ANDROID_SDK_ROOT = "path/to/android-sdk"
$env:GRADLE_USER_HOME = "path/to/.gradle_home"

# 디버그 APK 빌드
./gradlew assembleDebug
```

`env.ps1` 파일을 프로젝트 루트의 SDK 경로에 맞게 수정한 후 사용할 수 있습니다.

### 출력

```
app/build/outputs/apk/debug/app-debug.apk
```

---

## 설치 방법

1. 빌드된 APK를 기기에 설치
2. Alpine Linux chroot 이미지(`stryker.tar.gz`)를 `/storage/emulated/0/Download/`에 배치
3. 앱 최초 실행 시 저장소 권한 허용
4. 루트 권한 요청 승인
5. 설정 마법사에서 chroot 설치 진행

---

## 프로젝트 구조

```
app/src/main/java/com/zalexdev/stryker/
├── utils/              공통 유틸리티 (Core, StrykerTask, 셸 명령 등)
├── wifi/               WiFi 공격 모듈
├── local_network/      로컬 네트워크 스캔 및 공격
├── exploit_hub/        커스텀 익스플로잇 관리
├── router_scan/        멀티스레드 라우터 스캐너
├── nmap/               Nmap 스캐너
├── metasploit/         Metasploit 콘솔
├── searchsploit/       Exploit-DB 검색
├── geomac/             MAC 위치 추적
├── handshakes/         핸드셰이크 관리
├── three_wifi/         3WiFi 연동
├── coremanger/         chroot 패키지 관리
├── modules/            모듈 플러그인 시스템
├── appintro/           초기 설정 마법사
└── custom/             데이터 모델 클래스
```

---

## 라이선스

원작자 @zalexdev의 [LICENSE](LICENSE)를 따릅니다.

---

## 면책 조항

이 도구는 보안 연구 및 허가된 침투 테스트 목적으로만 사용해야 합니다.
유지보수자는 이 도구의 오용으로 인한 어떠한 결과에도 책임을 지지 않습니다.
