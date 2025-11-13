import type { WebViewProps } from 'react-native-webview';

/**
 * Props for HyperWebView component
 */
export interface HyperWebViewProps extends WebViewProps {
  /**
   * Enable iframe integration for UPI payments
   * @default false
   */
  iframeIntegration?: boolean;
}

/**
 * Native module interface for HyperWebView services
 */
export interface HyperWebViewNativeModule {
  /**
   * Initialize HyperWebView services for a specific WebView instance
   * @param reactTag The React Native tag of the WebView
   * @param iframeIntegration Whether to enable iframe integration
   */
  initializeHyperWebView(
    reactTag: number,
    iframeIntegration: boolean
  ): Promise<void>;

  /**
   * Attach HyperWebView services to the WebView
   * @param reactTag The React Native tag of the WebView
   */
  attachHyperWebView(reactTag: number): Promise<void>;

  /**
   * Clean up HyperWebView services for a specific WebView instance
   * @param reactTag The React Native tag of the WebView
   */
  cleanupHyperWebView(reactTag: number): void;
}
