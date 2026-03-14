# hermes-android

Android device control toolset for [hermes-agent](https://github.com/your-org/hermes-agent).

## Components

- **Python package** (`tools/android_tool.py`) — 13 tools that register into hermes-agent's tool registry
- **Android bridge app** (`hermes-android-bridge/`) — Kotlin app that runs on the phone, exposes HTTP API via AccessibilityService

## Quick Start

### 1. Install the bridge app
Build and install `hermes-android-bridge/` on your Android device via Android Studio.

### 2. Grant permissions
- Enable Accessibility Service: Settings > Accessibility > Hermes Bridge
- Grant overlay permission when prompted

### 3. Connect
```bash
# USB (recommended)
adb forward tcp:8765 tcp:8765
export ANDROID_BRIDGE_URL=http://localhost:8765

# Or WiFi
export ANDROID_BRIDGE_URL=http://<phone-ip>:8765
```

### 4. Install Python package
```bash
pip install -e .
```

### 5. Verify
```bash
curl http://localhost:8765/ping
```

## Tools

| Tool | Description |
|------|-------------|
| `android_ping` | Check bridge connectivity |
| `android_read_screen` | Get accessibility tree |
| `android_tap` | Tap by coordinates or node ID |
| `android_tap_text` | Tap by visible text |
| `android_type` | Type into focused field |
| `android_swipe` | Swipe gesture |
| `android_open_app` | Launch app by package |
| `android_press_key` | Press hardware/software key |
| `android_screenshot` | Capture screenshot |
| `android_scroll` | Scroll screen/element |
| `android_wait` | Wait for element to appear |
| `android_get_apps` | List installed apps |
| `android_current_app` | Get foreground app |

## Development

```bash
pip install -e ".[dev]"
python -m pytest tests/
```
