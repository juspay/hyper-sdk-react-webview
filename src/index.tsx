import { Platform, NativeModules } from 'react-native';
import React, { Component } from 'react';
import { WebView } from 'react-native-webview';
import type { HyperWebViewProps, HyperWebViewNativeModule } from './types';

const LINKING_ERROR =
  `The package 'hyper-sdk-react-webview' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const HyperWebViewModule =
  NativeModules.HyperWebViewModule as HyperWebViewNativeModule;

if (!HyperWebViewModule) {
  throw new Error(LINKING_ERROR);
}

// Export types for TypeScript users
export type { HyperWebViewProps } from './types';

/**
 * HyperWebView - A React Native WebView component with HyperSDKWebView integration
 *
 * This component wraps react-native-webview and adds HyperWebView services
 * for UPI Intent integration. It works with both Old and New Architecture.
 *
 * @example
 * ```tsx
 * import HyperWebView from 'hyper-sdk-react-webview';
 *
 * <HyperWebView
 *   source={{ uri: 'https://example.com' }}
 *   iframeIntegration={true}
 *   style={{ flex: 1 }}
 * />
 * ```
 */
export default class HyperWebView extends Component<HyperWebViewProps> {
  // private webViewRef = createRef<WebView>();
  private isInitialized = false;
  private reactTag: number | null = null;

  componentWillUnmount() {
    // Clean up HyperWebView services
    if (this.reactTag) {
      HyperWebViewModule.cleanupHyperWebView(this.reactTag);
    }
  }

  private async initializeHyperWebView(reactTag: number) {
    if (this.isInitialized) return;

    try {
      await HyperWebViewModule.initializeHyperWebView(
        reactTag,
        this.props.iframeIntegration || false
      );
      this.isInitialized = true;
      this.reactTag = reactTag;
      console.log('HyperWebView initialized successfully with tag:', reactTag);
    } catch (error) {
      console.warn('Failed to initialize HyperWebView:', error);
    }
  }

  private handleLoadStart = (event: any) => {
    // Try to get the native tag from the event target
    if (!this.isInitialized && event?.nativeEvent?.target) {
      const reactTag = event.nativeEvent.target;
      if (typeof reactTag === 'number') {
        console.log('HyperWebView: Initializing HyperWebView');
        this.initializeHyperWebView(reactTag);
      }
    }

    // Call original handler if provided
    if (this.props.onLoadStart) {
      this.props.onLoadStart(event);
    }
  };

  private handleNavigationStateChange = (navState: any) => {
    // Attach HyperWebView services when navigation state changes
    if (this.isInitialized && this.reactTag) {
      console.log('HyperWebView: attach on HyperWebView');
      HyperWebViewModule.attachHyperWebView(this.reactTag).catch((error) =>
        console.warn('Failed to attach HyperWebView:', error)
      );
    }

    // Call original handler if provided
    if (this.props.onNavigationStateChange) {
      this.props.onNavigationStateChange(navState);
    }
  };

  render() {
    // eslint-disable-next-line @typescript-eslint/no-unused-vars
    const { iframeIntegration, ...webViewProps } = this.props;

    return (
      <WebView
        {...webViewProps}
        onLoadStart={this.handleLoadStart}
        onNavigationStateChange={this.handleNavigationStateChange}
      />
    );
  }
}
