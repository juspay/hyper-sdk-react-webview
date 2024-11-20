# Hyper SDK React WebView

Custom webview for a hyper-sdk webview integration for react-native apps.

## Installation

```sh
npm install hyper-sdk-react-webview

```

 with `yarn`

```sh
yarn add hyper-sdk-react-webview

```

 **NOTE** please make sure that you have also installed `react-native-webview` as a dependency. Even though it's declared as a peer dependency, react-native doesn't pickup it's native-build files unless it's specified as a direct dependency of the app.

## Usage

 Add our package repository to your app's `build.gradle`
```groovy
// ..
allprojects {
    repositories {
    // ..
        maven { url "https://maven.juspay.in/jp-build-packages/hyper-sdk/" }
    }
}
```

 Add the required intent handling in `MainActivity.kt`

```kotlin
    // ...
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == HyperWebViewManager.UPI_REQUEST_CODE) {
            HyperWebViewManager.onActivityResult(requestCode, resultCode, data)
        }
    }
```

 And then you can just start using it in react.

```js
import HyperWebView from 'hyper-sdk-react-webview';

// ...

const MyWebComponent = () => {
  return <HyperWebView source={{ uri: 'https://reactnative.dev/' }} style={{ flex: 1 }} iframeIntegration={ false } />;
}
```

## How it works

With this package we have basically created a very thin wrapper around the `WebView` component provided by [react-native-webview](https://www.npmjs.com/package/react-native-webview). For `Android`, one can extend their view manager implementation & override the functionality as needed. After which the new class will have to be made available as a react-native component.

For a more detailed walk-through you can visit their docs: [Custom-Android.md](https://github.com/react-native-webview/react-native-webview/blob/v13.10.2/docs/Custom-Android.md)

## License

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)
