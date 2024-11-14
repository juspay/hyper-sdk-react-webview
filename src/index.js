import { Platform } from 'react-native';
import React, { Component } from 'react';
import { requireNativeComponent } from 'react-native';
import { WebView } from 'react-native-webview';
const LINKING_ERROR =
  `The package 'hyper-sdk-react-webview' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';
export default class HyperWebView extends Component {
  render() {
    return React.createElement(WebView, {
      ...this.props,
      nativeConfig: { component: HyperWebViewManager },
    });
  }
}

const HyperWebViewManager =
  Platform.OS === 'ios'
    ? requireNativeComponent('HyperWebViewManageriOS')
    : requireNativeComponent('HyperWebViewManager');
if (typeof HyperWebViewManager === 'undefined') {
  throw new Error(LINKING_ERROR);
}
