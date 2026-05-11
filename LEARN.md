# Mizu — Kotlin Learning Roadmap

Hydration tracker app built on top of a load cell + Arduino/ESP32 weight sensor.
Goal: learn industry-grade Android architecture from scratch, coming from React Native.
Mock data first, real hardware integration at the end.

---

## React Native → Kotlin Mental Model

| React Native | Kotlin/Android |
|---|---|
| Functional component | `@Composable` function |
| `useState` | `remember { mutableStateOf() }` |
| `useEffect` | `LaunchedEffect` / `SideEffect` |
| Redux/Zustand store | `ViewModel` + `StateFlow` |
| `fetch` / `axios` | `Retrofit` + coroutines |
| `AsyncStorage` | `Room` database |
| Context API / Provider | Hilt (Dependency Injection) |
| React Navigation | Navigation Compose |
| `Promise` / `async/await` | Coroutines (`suspend fun`) |
| RxJS Observable | Kotlin `Flow` |

---

## Phase 1 — Kotlin Language Basics + Compose UI
**Difficulty: Easy**

### Concepts
- `val` vs `var` — immutable vs mutable. Kotlin enforces this far more than JS.
- `data class` — like a TypeScript interface but with built-in `copy()`, `equals()`, and destructuring.
- `sealed class` — discriminated unions. Used everywhere for UI state: `Loading | Success(data) | Error(msg)`.
- Null safety — `?`, `!!`, `?.`, `?:`. Biggest paradigm shift from JS. Nullability is part of the type system.
- Extension functions — add methods to existing classes without subclassing.
- `when` expressions — exhaustive `switch` on steroids.
- Composables — every `@Composable` is a React function component. `Column/Row/Box` = flexbox.

### Build
Home screen dashboard showing today's hydration level, current weight, and a water intake card.
All hardcoded UI, no real data yet.

---

## Phase 2 — Navigation + Screen Architecture
**Difficulty: Easy-Medium**

### Concepts
- Navigation Compose — `NavController`, `NavHost`, `composable()` routes. Same mental model as React Navigation.
- Bottom nav bar with multiple tabs.
- Passing arguments between screens (type-safe navigation).
- Android back stack vs React Navigation's stack model.

### Build
3 screens: Dashboard, History (list of readings), Settings. Wired up with a bottom nav bar.

---

## Phase 3 — MVVM + ViewModel + State
**Difficulty: Medium**

The core architecture pattern. Most important phase.

### Concepts
- `ViewModel` — survives screen rotation (config changes). Android destroys and recreates the UI on rotation; ViewModel persists through it.
- `StateFlow` — like a Redux store observable. Composables `collectAsState()` from it, same as `useSelector` or subscribing to Zustand.
- `UiState` sealed classes — model every screen as `sealed class: Loading | Success(data) | Error(message)`.
- `viewModelScope` — a coroutine scope tied to the ViewModel lifecycle. Launches coroutines safely.
- `rememberSaveable` vs `remember` — survives process death vs survives recomposition.

### Build
`DashboardViewModel` with a `UiState` holding mock weight/hydration data. Dashboard composable observes it.

---

## Phase 4 — Repository Pattern + Data Layer
**Difficulty: Medium**

### Concepts
- Repository pattern — ViewModel never talks to the database or network directly. Goes through a Repository (single source of truth).
- `Room` — SQLite ORM. Define `@Entity` (table), `@Dao` (queries), `@Database` (entry point). Think Prisma for Android.
- Separating data models / domain models / UI models — the DB row shouldn't be your UI object.
- `Flow` from Room — Room returns `Flow<List<T>>` and the UI reactively updates when the DB changes. No polling.

### Build
`WeightReading` entity stored in Room. `HydrationRepository` that the ViewModel queries. Mock data seeded into Room on first launch.

---

## Phase 5 — Dependency Injection with Hilt
**Difficulty: Medium-Hard**

What separates a personal project from industry-grade code.

### Concepts
- What DI solves — instead of constructing dependencies inside a class, Hilt injects them. Declare what you need, Hilt wires it up.
- `@HiltAndroidApp` on the Application class.
- `@Module` + `@Provides` — tell Hilt how to construct things it can't infer on its own (e.g. a Retrofit client).
- `@Singleton` scope — one instance app-wide.
- `@HiltViewModel` — Hilt-aware ViewModels.
- Testability is the real reason — with DI you swap real repositories for fakes in tests with zero code changes.

### Build
Refactor repository and database setup to be injected via Hilt. Everything provided from a `DatabaseModule`.

---

## Phase 6 — Coroutines + Flow (Deep Dive)
**Difficulty: Hard**

You've used coroutines shallowly since Phase 3. Now go deep.

### Concepts
- `suspend fun` — like `async function` in JS, but coroutines are far more efficient than OS threads.
- `launch` vs `async/await` — fire-and-forget vs parallel work with a result.
- `Dispatchers.IO` vs `Dispatchers.Main` — IO for database/network (background), Main for UI updates.
- `Flow` operators — `map`, `filter`, `combine`, `flatMapLatest`. Like RxJS but saner.
- `StateFlow` vs `SharedFlow` — hot streams; single subscriber vs broadcast.
- Structured concurrency — coroutines have a parent-child hierarchy; cancelling a parent cancels all children.

### Build
Combine multiple flows — weight readings + goal settings — into a single computed hydration analytics flow using `combine()`.

---

## Phase 7 — Networking + API Layer
**Difficulty: Hard**

### Concepts
- `Retrofit` — standard Android HTTP client. Define an interface with annotations, Retrofit generates the implementation.
- `Kotlin Serialization` or `Gson` for JSON parsing.
- Providing Retrofit via a Hilt `NetworkModule`.
- Error handling — wrap responses in `Result<T>` or a custom `Either` type.
- ESP32 integration — the Arduino/ESP32 exposes an HTTP server or BLE endpoint. Retrofit hits that local endpoint to pull weight readings.

### Build
A `WeightApiService` Retrofit interface targeting the ESP32 HTTP endpoint. A mock implementation behind the same interface for now — swapping mock → real is one line in the Hilt module thanks to DI.

---

## Phase 8 — Testing
**Difficulty: Hard**

### Concepts
- Unit tests for ViewModels using `TestCoroutineDispatcher` + fake repositories.
- Repository tests with an in-memory Room database.
- Compose UI tests with `ComposeTestRule` — click buttons, assert text appears.
- Unit vs integration vs end-to-end tests.
- MockK — Kotlin-native mock library.

### Build
Test suite for `DashboardViewModel` and `HydrationRepository`.

---

## Phase 9 — WorkManager + Background Processing
**Difficulty: Hard**

### Concepts
- `WorkManager` — the only reliable way to do background work in Android (periodic weight polling from ESP32, hydration reminders).
- Constraints — only run on WiFi, only when charging, etc.
- Chained work — Worker A finishes, triggers Worker B.
- Notification channels and local push notifications.

### Build
Background worker that polls the ESP32 every 15 minutes for a weight reading and stores it in Room.

---

## Phase 10 — Bluetooth/BLE for ESP32
**Difficulty: Very Hard**

### Concepts
- Android BLE API — permissions, scanning, connecting, GATT characteristics.
- Parsing binary data from hardware.
- Runtime permission handling — Android's permission model is its own beast.

### Build
Direct BLE connection to the ESP32 instead of HTTP, for real deployment.

---

## Final Architecture

```
UI Layer (Composables)
    ↕ observes StateFlow
ViewModel Layer (DashboardViewModel, HistoryViewModel)
    ↕ calls suspend funs / collects Flows
Repository Layer (HydrationRepository)
    ↕                    ↕
Room DB            Retrofit (ESP32 API)
```

Every layer injected via Hilt. Every async operation is a coroutine. Every reactive stream is a Flow.
