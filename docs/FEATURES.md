# 기능 상세

## WiFi 감사

### WiFi 스캐닝
주변 WiFi 네트워크를 검색하여 BSSID, SSID, 신호 세기, 채널, WPS 지원 여부, 제조사 모델 정보를 수집합니다. `iw dev` 명령어를 chroot 환경에서 실행하며, 결과를 RecyclerView로 표시합니다.

### WPS PixieDust 공격
PixieWps 도구를 사용해 오프라인 WPS PIN 복구를 수행합니다. `pixie.py -K -F` 옵션으로 대상 AP와 연관 설정 후 PixieDust 취약점을 공격합니다. 기본 타임아웃 45초.

### WPS PIN 무차별 대입
PixieWps 도구로 WPS PIN을 무차별 대입합니다. `pixie.py -B` 옵션으로 00000000부터 99999999까지 순차 시도하며, 진행률과 속도를 실시간 표시합니다.

### 커스텀 PIN 연결
사용자가 직접 입력한 WPS PIN으로 접속을 시도합니다. `pixie.py -p <pin>` 옵션을 사용합니다.

### PSK 무차별 대입
워드리스트 파일을 읽어 각 비밀번호로 WiFi 연결을 시도합니다. Android WifiManager API로 연결 후 `dumpsys netstats`로 트래픽 발생 여부를 확인해 성공을 판별합니다.

### 핸드셰이크 캡처
`airodump-ng`로 대상 AP의 WPA 핸드셰이크를 캡처합니다. 내장 WiFi(wlan0)와 외부 어댑터(wlan1)를 모두 지원하며, `aireplay-ng`로 디오스 공격을 병행해 강제 재연결을 유도합니다.

### 디오스 공격
`aireplay-ng -0` 옵션으로 대상 AP에 디오스 패킷을 전송해 연결된 클라이언트를 강제로 끊습니다.

---

## 로컬 네트워크

### 네트워크 스캐닝
`nmap -sP -n`으로 전체 서브넷 핑 스윕을 수행합니다. 발견된 각 호스트에 대해 상세 TCP 포트 스캔을 실행하고 OS 탐지를 시도합니다.

### 취약점 스캔
- **EternalBlue (MS17-010)**: `eternalscan.py`로 SMBv1 취약점 검사 (포트 445)
- **SMBGhost (CVE-2020-0796)**: `ghostscanner.py`로 SMBv3 압축 취약점 검사 (포트 445)
- **BlueKeep (CVE-2019-0708)**: `bluekeepscan.py`로 RDP 취약점 검사 (포트 3389)

### ARP 스푸핑
MegaCut 도구로 네트워크 차단 공격을 수행합니다. 4가지 모드 지원: 일시 차단, 영구 차단, 20초 차단, 복구.

### SMB 공유 열거
`smbclient -L <ip> -N` 명령으로 대상의 SMB 공유 폴더 목록을 조회합니다.

### 서비스 무차별 대입
hydra 도구로 SSH(22), FTP(21), Telnet(23) 서비스에 대한 무차별 대입 공격을 수행합니다. 워드리스트는 `/sdcard/Stryker/wordlist/`에서 선택합니다.

### SNMP 열거
`snmpwalk -v2c -c public <ip>` 명령으로 대상 장비의 SNMP 정보를 수집합니다.

---

## Nmap 스캐너

대상 IP 또는 호스트명에 대해 Nmap 포트 스캔을 실행합니다. 다음 옵션을 제공합니다:
- OS 감지 (`-O`)
- 서비스 버전 감지 (`-sV`)
- 고속 스캔 (`-F --top 100`)
- 호스트 온라인 간주 (`-Pn`)

결과는 터미널 스타일 콘솔에 실시간 출력됩니다.

---

## 라우터 스캐너

멀티스레드 방식으로 IP 대역과 포트 조합을 HTTP 핑으로 검사합니다. 다음과 같은 설정이 가능합니다:
- IP 범위 (CIDR 또는 대시 표기법)
- 대상 포트 목록
- 최대 동시 스레드 수
- 연결 타임아웃

109종의 라우터 모델명을 내장 데이터베이스로 보유하며, 발견된 라우터 정보(SSID, 인증 방식, WPS PIN, 비밀번호 등)를 CSV로 내보낼 수 있습니다.

---

## 익스플로잇 허브

커스텀 침투 테스트 스크립트를 등록하고 실행할 수 있는 마법사입니다.

### 익스플로잇 정의
- 제목, 스크립트 경로, 언어(Python/Bash 등)
- 인자 유형 (IP, MAC, Gateway, Mask, Port)
- 성공 패턴 (정규식)

### 익스플로잇 실행
등록된 익스플로잇을 chroot 환경에서 실행하고, 출력에서 성공 패턴을 검사하여 결과를 판별합니다.

---

## SearchSploit

Exploit-DB 오프라인 데이터베이스를 검색합니다. `searchsploit` 바이너리를 chroot에서 실행하며, JSON 형식 결과를 파싱해 RecyclerView로 표시합니다. 검색 결과는 제목, 날짜, 작성자, 유형, 플랫폼 정보를 포함합니다.

---

## Geomac

WiFi BSSID(MAC 주소)를 기반으로 Wigle.net 데이터베이스에서 위치 정보를 조회합니다. `geomac` 바이너리를 chroot에서 실행하며, 결과 좌표를 OpenStreetMap 지도 위에 마커로 표시합니다. 마커를 길게 누르면 좌표를 클립보드에 복사합니다.

---

## 핸드셰이크 저장소

캡처된 WPA 핸드셰이크 파일(`.cap`)을 관리합니다.

### 핸드셰이크 무차별 대입
`aircrack-ng`로 오프라인 무차별 대입을 수행합니다. 워드리스트 파일과 캡처 파일을 선택해 실행하며, 진행률과 예상 남은 시간을 표시합니다. 키 발견 시 알림을 통해 알려줍니다.

### 핸드셰이크 업로드
`curl`을 사용해 외부 크래킹 서비스에 핸드셰이크 파일을 업로드합니다.

---

## 코어 관리자

Alpine Linux chroot 환경 내 패키지 관리 기능을 제공합니다.

### APK 패키지
- `apk search`로 패키지 검색
- `apk add`로 패키지 설치

### Pip 패키지
- `pip install`로 Python 패키지 설치

---

## 모듈 저장소

확장 가능한 플러그인 시스템입니다. `assets/modules_list.json`에 등록된 모듈을 로컬에서 불러오며, 각 모듈은 chroot 내 `/modules/<이름>/` 디렉토리에 `install.sh` 또는 `delete.sh` 스크립트를 포함합니다.

---

## Metasploit 콘솔

Metasploit Framework의 `msfconsole`을 chroot 환경에서 실행하는 대화형 콘솔입니다. 명령어 입력과 출력을 실시간으로 표시합니다. (개발 중)

---

## 3WiFi 연동

3WiFi 온라인 데이터베이스에서 BSSID 기반 WiFi 비밀번호를 조회합니다. 로그인 후 API 키를 발급받아 사용합니다. (외부 서버 상태에 따라 이용 불가할 수 있음)

---

## 설정

- WiFi 스캔/디오스 공격 인터페이스 선택
- 테마 (라이트/다크/시스템)
- MAC 주소 숨기기
- 디버그 모드
- PixieDust 토글
- 스캔 결과 저장
- 자동 업데이트
- chroot 마운트 해제 및 코어 삭제
