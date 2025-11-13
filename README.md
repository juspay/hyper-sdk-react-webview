# Hyper SDK React WebView

A React Native WebView component with HyperSDK integration for UPI payments. Supports both React Native Old and New Architecture (Fabric).

## Installation

To install using npm:

```sh
npm install hyper-sdk-react-webview
```

To install using yarn:

```sh
yarn add hyper-sdk-react-webview
```

**NOTE:** Ensure that `react-native-webview` is also installed as a dependency. Although it is declared as a peer dependency, React Native requires it to be a direct dependency to include its native build files.

## React Native New Architecture Support

This library supports both React Native architectures:

- ✅ **Old Architecture** (Bridge) - React Native 0.60+
- ✅ **New Architecture** (Fabric) - React Native 0.68+ with New Architecture enabled

No code changes are required when migrating between architectures. See [MIGRATION.md](./MIGRATION.md) for detailed migration instructions.

## Usage

### Android

Add our package repository to your app's `build.gradle`:

```groovy
allprojects {
    repositories {
        maven { url "https://maven.juspay.in/jp-build-packages/hyper-sdk/" }
    }
}
```

Add the required intent handling in `MainActivity.kt`:

```kotlin
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == HyperWebViewManager.UPI_REQUEST_CODE) {
        HyperWebViewManager.onActivityResult(requestCode, resultCode, data)
    }
}
```

### iOS

Add URI schemes for required UPI apps in `Info.plist`:

```plist
<key>LSApplicationQueriesSchemes</key>
<array>
    <string>credpay</string>
    <string>phonepe</string>
    <string>paytmmp</string>
    <string>tez</string>
    <string>paytm</string>
    <string>bhim</string>
    <string>myairtel</string>
</array>
```

Run `pod install` inside the iOS folder of your app.

### React Native

You can start using the component in your React Native app:

```tsx
import HyperWebView from 'hyper-sdk-react-webview';

const MyWebComponent = () => {
  return (
    <HyperWebView
      source={{ uri: 'https://reactnative.dev/' }}
      style={{ flex: 1 }}
      iframeIntegration={false}
    />
  );
};
```

### Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `iframeIntegration` | `boolean` | `false` | Enable iframe integration for UPI payments |
| ...rest | `WebViewProps` | - | All standard react-native-webview props are supported |

## How it works

This package wraps the `WebView` component from [react-native-webview](https://www.npmjs.com/package/react-native-webview) and integrates HyperSDK services through a native module approach. This design ensures compatibility with both React Native architectures:

### Architecture Overview

1. **JavaScript Layer**: Uses `react-native-webview` directly for maximum compatibility
2. **Native Module**: `HyperWebViewModule` provides bridge between WebView and HyperSDK services
3. **Integration**: Automatically attaches HyperWebView services when WebView navigates

### Benefits

- ✅ **Full compatibility** with both Old and New Architecture
- ✅ **Minimal overhead** - uses official react-native-webview
- ✅ **Automatic cleanup** - properly manages native resources
- ✅ **TypeScript support** - full type definitions included

## Troubleshooting

### New Architecture Issues

If you encounter `"Unimplemented component"` errors:

1. Ensure you're using the latest version (v1.0.0+)
2. Rebuild your project after updating
3. See [MIGRATION.md](./MIGRATION.md) for detailed troubleshooting

### Performance

For optimal performance:
- Use `iframeIntegration={true}` only if you are opening juspay payment page in an iframe
- Implement proper error handling in navigation callbacks
- Clean up any manual references to avoid memory leaks

## License

MIT

---

Created with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)
