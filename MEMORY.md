# MEMORY

## [2026-10-26 21:41] 1.0.3 main 최종 빌드·개발 서버 설치

- 코드 병합 `65ac87a` 및 후속 ID 정규화 `363cdfb`까지 main·origin/main·원격 main 일치를 확인했다. 커밋된 `363cdfb`를 F 소스 사본에 `git archive`로 추출한 뒤 최종 assemble 성공. 최종 테스트는 앞선 `check assemble`의 44/44 통과이며 최종 assemble에서는 반복하지 않았다. `jar --validate` 통과.
- `C:/Users/박주형/Documents/GitHub/Cobblemon-Mods/develop-product/server/mods/simple-myroom-1.0.2.jar`를 `simple-myroom-1.0.3.jar`로 교체했다. 함께 설치된 jbro-policy는 코드 병합 `8d0862784`의 범용 제공자를 포함한다. 서버 전용이므로 개발 클라이언트에는 마이룸을 넣지 않았다. 설치 전 개발 서버 정지·25566 리스너 없음 확인. 기존 설정·방·플레이어 데이터는 바꾸지 않았다.
- 최종 빌드본과 개발 설치본 SHA-256이 `feae1cb237c91ef546c977f672ae4fadd9ea62b11604cdde6771574f182decc0`로 같다. JAR 클래스에 광장·PvP 차원 ID가 없는 것을 확인했다. 이전 JAR은 `F:/AI/build/hub-pvp-return-5c87/installed-before/server/`에 보관했다.
- 서버 기동·실게임·대전/관전·재접속 왕복은 미검증이다. VPS·운영·릴리스 제품에는 설치하지 않았다. 후속 main 변경이 이 배포 기록뿐이면 최종 빌드와 기능 코드는 같다.

## [2026-10-26 21:38] 외부 차원 ID 정규화 보강

- 제공자가 `overworld`처럼 기본 네임스페이스를 생략한 유효 ID를 반환해도 `minecraft:overworld`로 일치하도록 표준형으로 저장한다. 여러 제공자의 목록을 함께 받는 검사도 추가했다.
- `check assemble` 성공, JUnit 44/44 통과. 앞선 43개 검사에 ID 정규화·복수 제공자 검사를 더한 결과다. 개발 설치·서버 기동·실게임 검증은 아직 진행 전이다.

## [2026-10-26 21:34] 외부 모드의 범용 복귀 위치 보존 연동 (1.0.3)

- **사용자 결정:** 마이룸은 광장·PvP를 몰라야 하며, 서버 기능 간 연동 규칙은 Cobblemon-Mods의 `jbro-policy`가 소유한다. 개발 배포까지 요청했으며 VPS·운영 제품 배포는 대상이 아니다.
- **구현:** `returnBehavior.otherHubDimensions` 기본값에서 특정 외부 차원 ID를 제거했다. 기존 사용자 설정 목록은 유지한다. 범용 Fabric 진입점 `simple_myroom:other_hub_dimensions`의 `Supplier<Collection<String>>` 제공자가 차원 목록을 등록하며, 서버 시작 시 모든 모드 초기화 후 읽는다. 이 목록은 설정 파일에 쓰지 않고 기존 목록에 더해 판단하므로 `/room reload`로 사라지지 않는다. 외부 모드의 클래스·차원 ID·의존성을 마이룸에 추가하지 않았다.
- **범위:** 입장 시 기존 기록 보존, 차원 이동·접속 시 기록 정리, 리스폰 시 판단 모두 기존 `ReturnDimensionPolicy`를 통해 같은 외부 목록을 사용한다. 일반 외부 차원으로 돌아오면 기존대로 오래된 기록을 지운다. 이미 지워진 좌표를 복구하는 기능은 아니다. 사용자 메시지·NBT 형식은 변경하지 않았다.
- **검증:** F 드라이브 소스 사본에서 `check build` 성공, JUnit 43/43 통과. 임시 차원 왕복·기존 설정 병행·연동 없는 단독 동작·잘못된 제공자 거절을 확인했다. Gradle 홈은 `F:/AI/caches/gradle`, 캐시·빌드·임시 경로는 `F:/AI/build/hub-pvp-return-5c87/room/`이다.
- **배포 상태:** 이 기록 시점에는 개발 JAR 설치 전이다. `jbro-policy` 제공자와 함께 개발 서버에 설치해야 한다. 서버 기동·실게임 대전/관전·재접속 왕복은 확인하지 않았다. 실제 설치 결과는 후속 항목으로 기록한다.
