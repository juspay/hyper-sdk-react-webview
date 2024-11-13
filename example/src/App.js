import * as React from 'react';
import { StyleSheet, SafeAreaView } from 'react-native';
import HyperWebView from 'hyper-sdk-react-webview';

export default function App() {
  return React.createElement(
    SafeAreaView,
    { style: styles.container },
    React.createElement(HyperWebView, {
      source: { uri: 'https://picasso.juspay.in/devtools/web/index.html' },
      style: { flex: 1 },
    })
  );
}
const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
