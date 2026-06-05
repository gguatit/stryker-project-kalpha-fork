# 변경 기록

## [2.1.0] — 2026-06-05

### 1단계 — 서버 의존성 제거

- Dashboard, CheckUpdates, CheckMsg에서 GitHub 업데이트/메시지 확인 코드 제거
- 원격 모듈 목록을 로컬 `assets/modules_list.json`으로 대체
- Slide3에서 chroot 다운로드를 로컬 파일(`stryker.tar.gz`) 방식으로 변경
- Account, About 화면에서 후원/홍보 링크 숨김 처리

### 2단계 — 현대화

- **AGP** 7.1.2 → 8.2.2, **Gradle** 7.2 → 8.4
- **compileSdk** 34, **targetSdk** 34, **Java** 17
- JCenter 저장소 제거
- Kotlin 1.9.22 플러그인 추가
- AsyncTask 49개 → StrykerTask (ExecutorService 기반)로 전면 교체
- Deprecated API 38건 수정
- 모든 의존성 라이브러리 최신 안정화 버전으로 업데이트

### 3단계 — 버그 수정

- CustomPin: WPS PIN 명령어 인자 사이 공백 누락 수정
- ModulesFragment: core가 초기화되기 전에 호출되던 NPE 수정
- LocalAdapter: busy-wait 루프 4곳을 CountDownLatch로 교체
- ScanTarget: Timer 리소스 누수 수정
- MsfConsole: 무한 스레드 누수 및 프로세스 미종료 수정
- CheckInet: 오타 수정 ("recieved" → "received")
- DownloadFile: busy-wait에 sleep 추가 및 실패 처리 구현
- GetApiKeys: Logcat에 평문 자격증명 출력 제거
- GetWiFI: 위도/경도 필드 뒤바뀜 수정
- UploadHS: 이메일 쉘 인젝션 방어 (sanitizeShellArg)
- Logger.writeLine: 로그 타입 태그 누락 수정
- Core.getExploits: 중복 익스플로잇 생성 버그 수정
- bootroot_env: chmod 777 → 755 (루트 바이너리 권한 축소)
- GetPackage: 쿼리 문자열 sanitizeQuery 적용
- CheckSmbGhost: 스크립트 파일명 오타 수정

### 4단계 — 견고화

- BruteWps, PixieDust, CustomPin, RunModule, BasicExploitLaunch, GetSploit, BruteHandshake에 쉘 인자 sanitizeShellArg 적용
- LocalAdapter: 다이얼로그 닫힘 시 CountDownLatch 데드락 수정
- MsfConsole: stderr drain 스레드 추가
- 6개 파일의 `su`를 `su -mm`으로 통일
- 44개 파일의 `process.waitFor()`에 60초 타임아웃 추가
- Core: mountcore/unmountcore/remountcore가 실제 종료 코드 반환하도록 수정
- CheckRoot: stderr 출력이 루트 감지 결과를 덮어쓰던 버그 수정
- RunModule: chmod 777 → 755, 설치 성공 시에만 모듈 등록

### 기능 추가 — 로컬 네트워크 확장

- SMB 공유 열거 버튼 추가
- 서비스 무차별 대입 버튼 추가 (SSH/FTP/Telnet, hydra 기반)
- SNMP 열거 버튼 추가
- 한글/러시아어 현지화 적용

---

## [2.0] — 2022-03-28

- @zalexdev 원작 릴리즈
- 13개 기능 모듈: WiFi, 로컬 네트워크, 익스플로잇 허브, 라우터 스캐너, Nmap, Metasploit, SearchSploit, Geomac, 3WiFi, 핸드셰이크, 코어 관리자, 모듈 저장소, 설정/정보
