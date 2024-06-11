import { Platform, type HostComponent } from 'react-native';
import React, { Component } from 'react';
import { requireNativeComponent } from 'react-native';
import { WebView, type WebViewProps } from 'react-native-webview';
import type { NativeProps } from 'react-native-webview/lib/RNCWebViewNativeComponent';

const LINKING_ERROR =
  `The package 'hyper-sdk-react-webview' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

export default class HyperWebView extends Component<WebViewProps> {
  render() {
    return (
      <WebView
        {...this.props}
        nativeConfig={{ component: HyperWebViewManager }}
      />
    );
  }
}

const HyperWebViewManager: HostComponent<NativeProps> = requireNativeComponent(
  'HyperWebViewManager'
);

if (typeof HyperWebViewManager === 'undefined') {
  throw new Error(LINKING_ERROR);
}
