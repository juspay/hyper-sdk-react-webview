# Hyper SDK React WebView

A custom webview for integrating hyper-sdk with React Native apps.

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

```js
import HyperWebView from 'hyper-sdk-react-webview';

const MyWebComponent = () => {
  return <HyperWebView source={{ uri: 'https://reactnative.dev/' }} style={{ flex: 1 }} iframeIntegration={false} />;
}
```

## How it works

This package provides a thin wrapper around the `WebView` component from [react-native-webview](https://www.npmjs.com/package/react-native-webview). For Android, you can extend the view manager implementation and override functionality as needed. The new class must then be made available as a React Native component.

For a detailed walk-through, refer to their documentation: [Custom-Android.md](https://github.com/react-native-webview/react-native-webview/blob/v13.10.2/docs/Custom-Android.md).

## License

MIT

---

Created with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)
