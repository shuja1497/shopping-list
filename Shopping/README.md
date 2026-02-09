# Shopping List App

A grocery list application built with Kotlin and Jetpack Compose that allows users to manage their shopping items.

## Demo

https://github.com/shuja1497/shopping-list/raw/main/Shopping/shopping-list-demo.mp4

## Features

- **Add items** with a name and category (Milk, Vegetables, Fruits, Breads, Meats)
- **Mark items** as purchased/completed with visual strikethrough
- **Edit and delete** items
- **Filter** items by category
- **Sort** items alphabetically, by category, or by purchase status
- **Data persistence** between app launches using Room database
- **Empty state** when no items have been added
- **Error handling** with snackbar notifications

## Installation

### APK (quickest way)

1. Download [`app-debug.apk`](app-debug.apk) from this repository
2. Transfer it to your Android device
3. On your device, enable **Settings > Security > Install from Unknown Sources** (if not already enabled)
4. Open the APK file and tap **Install**
5. Requires Android 7.0 (API 24) or higher

### Build from Source

#### Prerequisites
- Android Studio Ladybug or later
- JDK 11+
- Android SDK with API 36 (compile) and API 24+ device/emulator

#### Steps
1. Clone the repository
   ```bash
   git clone https://github.com/shuja1497/shopping-list.git
   ```
2. Open the `Shopping` folder in Android Studio
3. Let Gradle sync complete
4. Connect an Android device or start an emulator (API 24+)
5. Click **Run** or press `Shift+F10`

#### Generate APK manually
```bash
./gradlew assembleDebug
```
The APK will be at `app/build/outputs/apk/debug/app-debug.apk`.

## Architecture

The app follows the **MVVM** pattern with clear separation of concerns:

```
app/src/main/java/com/droid/shopping/
├── data/
│   ├── local/           # Room entity, DAO, database, type converters
│   └── repository/      # Repository interface & implementation
├── presentation/
│   ├── components/      # Reusable Compose UI components
│   ├── ShoppingListScreen.kt      # Screen composables
│   ├── ShoppingListViewModel.kt   # State management & event handling
│   ├── ShoppingListUiState.kt     # UI state
│   └── ShoppingListUiEvent.kt     # UI events
├── di/                  # Hilt dependency injection modules
└── ui/theme/            # Theme, colors, typography
```

## Tech Stack

| Category | Library |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose, Material Design 3 |
| Architecture | MVVM, Clean Architecture |
| DI | Hilt |
| Database | Room |
| Async | Kotlin Coroutines & Flow |
| Testing | JUnit, MockK, Turbine, Truth |

## Testing

Run all unit tests:
```bash
./gradlew test
```