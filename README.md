# Simple MyRoom

Personal sky rooms for **Minecraft 1.21.1 / Fabric**. Each player gets a protected building area in a shared sky dimension, with visits, trust/ban lists, custom entry points and optional chunk loading.

**Server only.** Players do not need this mod on their clients. Requires **Fabric Loader 0.19.3+, Fabric API and Java 21+**. Cobblemon, Mod Menu and configuration mods are not required.

## Installation

1. Download `simple-myroom-1.0.0.jar` from [Releases](https://github.com/taku7664/SimpleMyRoom/releases).
2. Stop the server and place the JAR in its `mods` directory.
3. Remove previous `simple-myroom-*.jar` and original `myroom-*.jar` copies. They must not run together.
4. Start the server. Configuration files are created in `config`.

This is the first standalone release, numbered **1.0.0**. It retains the previous module's `simple_myroom` mod ID, `myroom:rooms` dimension, `myroom_rooms` saved data and configuration filenames. Existing rooms keep their saved layout. Back up your world before migrating from another MyRoom installation.

## Commands

`/myroom` is an alias for `/room` by default. Player names are command arguments.

| Command | Purpose |
| --- | --- |
| `/room` | Room information and help |
| `/room enter [player]` | Create/enter your room, or visit another player's room |
| `/room exit` | Return to the location saved before entry |
| `/room public <true\|false>` | Allow or restrict visits |
| `/room trust <player>` / `/room untrust <player>` | Grant/revoke building access |
| `/room ban <player>` / `/room unban <player>` | Block/unblock visits |
| `/room kick <player>` | Remove a visitor from your room |
| `/room setspawn` / `/room setspawn reset` | Set/reset your room's entry point |
| `/room visitors` | List current visitors |
| `/room notify <on\|off>` | Toggle visitor notifications |
| `/room active <on\|off\|status>` | Toggle/check room chunk loading |
| `/room reload` | Reload configuration; requires administrator permission |

## Configuration

- `config/simple-myroom.json`: room layout, permissions, protection, visitor interactions, entry/return behavior, room preparation and chunk-loading limits.
- `config/simple-myroom-messages.json`: editable player-facing messages. Defaults are Korean. Complete English and Korean presets are in [`examples`](examples); copy your chosen preset to this filename and use `/room reload`.

Most settings apply on reload. Command aliases are registered at startup, and existing worlds retain saved layout values. Invalid JSON or out-of-range settings reject the reload and keep the last accepted configuration; on initial startup, built-in defaults remain active if loading fails. Missing settings/message keys are added automatically.

### Defaults and behavior

- Rooms are **100 × 100 blocks**, spaced 512 blocks apart at floor Y=64. New rooms are public. Trusted players can build and visit private rooms; unauthorized physical entry is rejected.
- Room creation is queued, with a default budget of **4,096 blocks per tick**, then the requester enters automatically.
- Owners and trusted players can build. Default protection covers unauthorized block/entity interaction, PvP, explosion damage, fire spread, pistons, farmland/turtle eggs and natural mob spawning. Empty-handed visitors can use the configured safe-block list, including chests and crafting tables. Administrators can bypass protection by default.
- Return points support external mod dimensions. Entry can save the ground below a flying/riding player, and exit searches for a safe nearby position before falling back to the overworld spawn.
- `returnBehavior.otherHubDimensions` integrates optional hub dimensions by ID. It does not load or require those mods. Existing installations may include `jbro_policy:plaza`; edit the list for your server.
- Chunk loading is **off for new rooms**. `/room active on` keeps the room's overlapping chunks at entity-ticking level, allowing crops and block entities to tick without visitors. The default room covers 49 chunks; limits default to 20 active rooms and 64 chunks per room. Choices are saved and restored on restart with mod-owned tickets, separate from vanilla `/forceload`. Features that explicitly require a nearby player still need one.

## 한국어 안내

플레이어별 마이룸을 제공하는 **서버 전용** 모드입니다. 클라이언트 설치는 필요 없으며, 필수 모드 의존성은 Fabric API뿐입니다.

서버를 종료한 뒤 `simple-myroom-1.0.0.jar`를 `mods`에 넣고 기존 Simple MyRoom/MyRoom JAR은 제거하세요. 독립 출시 버전은 1.0.0이며, 기존 방 데이터·차원 ID·설정 파일은 유지합니다.

`/room enter`로 방을 만들고 입장하며 `/room exit`로 돌아옵니다. `/room public`, `trust`, `ban`, `kick`으로 방문 권한을 관리합니다. `/room setspawn`은 입장 위치, `/room notify`는 방문 알림, `/room active`는 상시 활성화 설정입니다.

서버 설정은 `config/simple-myroom.json`, 안내 문구는 `config/simple-myroom-messages.json`을 편집합니다. `/room reload`로 대부분 다시 적용할 수 있지만 명령어 별칭은 재시작이 필요하고, 기존 방의 배치는 저장된 값을 우선합니다. 한국어가 기본이며 `examples`의 영문·한국어 문구 파일을 사용할 수 있습니다.

## Build

Use JDK 21. The Gradle wrapper builds the mod and runs its JUnit tests:

```powershell
.\gradlew.bat build
```

The installable JAR is `build/libs/simple-myroom-1.0.0.jar`. The `-sources.jar` is for development and should not be installed.

## License and credits

[CC0 1.0 Universal](LICENSE). Based on MyRoom 0.5.1 by **Park_JH**, originally distributed under CC0-1.0; the original attribution is preserved in [`NOTICE_simple_myroom`](src/main/resources/NOTICE_simple_myroom). Standalone implementation and maintenance by **jbro**. No Minecraft game assets are bundled.
