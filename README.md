# hydration-android

The Android app for [Mizu](../README.md) — a hydration tracker fed by a load-cell
weight sensor over BLE. It shows your daily hydration progress, a history of
readings, and streaks.

Built as a from-scratch tour of industry-grade Android architecture (the full,
phase-by-phase learning roadmap is in [`LEARN.md`](./LEARN.md)).

## Tech stack

- **Kotlin** + **Jetpack Compose** (Material 3)
- **MVVM** — `ViewModel` + `StateFlow`, UI via `collectAsStateWithLifecycle`
- **Hilt** — dependency injection across every layer
- **Room** — local persistence of weight readings
- **Coroutines / Flow** — reactive, end to end
- **Retrofit** — HTTP client (wired, dormant until hardware is connected)
- **Navigation Compose** — bottom-nav across Dashboard / History / Settings

## Architecture

```
UI (Composables)
   ↕ observes StateFlow
ViewModel (DashboardViewModel, HistoryViewModel)
   ↕ calls suspend funs / collects Flows
Repository (HydrationRepository)
   ↕                      ↕
Room DB            WeightSensorService
                   (Mock today · BLE/HTTP later)
```

Every layer is injected via Hilt; every async path is a coroutine; every reactive
stream is a `Flow`. The weight source sits behind a `WeightSensorService`
interface, so swapping mock data for the real BLE sensor is a one-binding change
in `di/SensorModule.kt` — nothing upstream changes.

### Package layout (`app/src/main/java/com/example/mizu`)

| Package | Responsibility |
|---------|----------------|
| `data/local` | Room database, DAO, entities |
| `data/remote` | Retrofit service + DTOs (ESP32 HTTP endpoint) |
| `data/sensor` | `WeightSensorService` interface + mock implementation |
| `data/repository` | `HydrationRepository` — single source of truth |
| `di` | Hilt modules (Database, Network, Sensor) |
| `domain` | Pure domain models |
| `features/{dashboard,history,settings}` | Screen UI + ViewModel + state |
| `navigation` | NavHost, bottom nav bar, route definitions |
| `ui/theme` | Material 3 theme |

## Build & run

Requires Android Studio (or a configured Android SDK; `local.properties` points
Gradle at your SDK and is intentionally git-ignored).

```bash
./gradlew assembleDebug      # build the debug APK
./gradlew installDebug       # build + install on a connected device/emulator
./gradlew test               # unit tests (JVM)
./gradlew connectedAndroidTest  # instrumented tests (device/emulator)
```

Or just open `hydration-android/` in Android Studio and Run.

## Testing

- **Unit:** `DashboardViewModelTest` — state/reactivity, using Turbine + MockK.
- **Instrumented:** `WeightReadingDaoTest` — Room DAO against an in-memory DB;
  `DashboardContentTest` — Compose UI assertions.

## Status

Roadmap phases 1–8 (Compose, Navigation, MVVM, Room, Hilt, Coroutines/Flow,
Retrofit scaffolding, Testing) are implemented. The app runs on **mock weight
data** today; the live BLE link to the firmware, plus background polling
(WorkManager), are the next phases — see [`LEARN.md`](./LEARN.md).
