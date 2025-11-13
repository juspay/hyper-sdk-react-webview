//
//  HyperWebViewModule.swift
//
//  Copyright © Juspay Technologies. All rights reserved.
//

import Foundation
import React
import WebKit
import HyperWebView

@objc(HyperWebViewModule)
class HyperWebViewModule: NSObject {

    @objc var bridge: RCTBridge!

    var hyperWebViewServices: [Int: HyperWebViewServices] = [:]
    var webViewInstances: [Int: WKWebView] = [:]

    @objc
    static func requiresMainQueueSetup() -> Bool {
        return true
    }

    private func findWebView(fromReactTag reactTag: NSNumber) -> WKWebView? {
        guard let bridge = self.bridge else { return nil }

        // Find the view from React tag
        if let view = bridge.uiManager.view(forReactTag: reactTag) {
            return findWKWebView(in: view)
        }

        return nil
    }

    private func findWKWebView(in view: UIView) -> WKWebView? {
        if let webView = view as? WKWebView {
            return webView
        }

        for subview in view.subviews {
            if let webView = findWKWebView(in: subview) {
                return webView
            }
        }

        return nil
    }

    @objc
    func initializeHyperWebView(_ reactTag: NSNumber,
                               iframeIntegration: Bool,
                               resolve: @escaping RCTPromiseResolveBlock,
                               reject: @escaping RCTPromiseRejectBlock) {

        DispatchQueue.main.async { [weak self] in
            guard let self = self else {
                reject("ERROR", "Module deallocated", nil)
                return
            }

            guard let webView = self.findWebView(fromReactTag: reactTag) else {
                reject("ERROR", "WebView not found for reactTag: \(reactTag)", nil)
                return
            }

            let tagValue = reactTag.intValue

            // Store the WebView reference and settings
            self.webViewInstances[tagValue] = webView

            // Create HyperWebViewServices
            let hyperWebViewServices = HyperWebViewServices(webView: webView, isIframeIntegration: iframeIntegration)
            self.hyperWebViewServices[tagValue] = hyperWebViewServices

            // This handles special cases like about:srcdoc that JavaScript onNavigationStateChange doesn't catch
            self.setupNavigationDelegate(for: webView, reactTag: tagValue)

            resolve(nil)
        }
    }

    @objc
    func attachHyperWebView(_ reactTag: NSNumber,
                           resolve: @escaping RCTPromiseResolveBlock,
                           reject: @escaping RCTPromiseRejectBlock) {

        DispatchQueue.main.async { [weak self] in
            guard let self = self else {
                reject("ERROR", "Module deallocated", nil)
                return
            }

            let tagValue = reactTag.intValue

            guard let hyperWebViewServices = self.hyperWebViewServices[tagValue] else {
                reject("ERROR", "HyperWebView not initialized for reactTag: \(reactTag)", nil)
                return
            }

            hyperWebViewServices.attach()
            resolve(nil)
        }
    }

    private func setupNavigationDelegate(for webView: WKWebView, reactTag: Int) {
        // Create a custom navigation delegate that allows about:srcdoc while preserving RN WebView functionality
        let customDelegate = HyperWebViewNavigationDelegate(reactTag: reactTag, originalDelegate: webView.navigationDelegate)
        webView.navigationDelegate = customDelegate

        // Store the custom delegate to prevent deallocation
        objc_setAssociatedObject(webView,
                               "HyperWebViewNavigationDelegate",
                               customDelegate,
                               .OBJC_ASSOCIATION_RETAIN_NONATOMIC)
    }


    // Clean up when WebView is removed
    @objc
    func cleanupHyperWebView(_ reactTag: NSNumber) {
        let tagValue = reactTag.intValue

        // Clean up custom navigation delegate if it exists
        if let webView = webViewInstances[tagValue] {
            if let customDelegate = objc_getAssociatedObject(webView, "HyperWebViewNavigationDelegate") as? HyperWebViewNavigationDelegate {
                // Restore original delegate if it was preserved
                webView.navigationDelegate = customDelegate.originalDelegate
            }
        }

        hyperWebViewServices.removeValue(forKey: tagValue)
        webViewInstances.removeValue(forKey: tagValue)
    }
}

// Custom navigation delegate that allows about:srcdoc while preserving RN WebView functionality
private class HyperWebViewNavigationDelegate: NSObject, WKNavigationDelegate {
    private let reactTag: Int
    weak var originalDelegate: WKNavigationDelegate?

    init(reactTag: Int, originalDelegate: WKNavigationDelegate?) {
        self.reactTag = reactTag
        self.originalDelegate = originalDelegate
        super.init()
    }

    // Forward other navigation delegate methods to original delegate
    func webView(_ webView: WKWebView, didFinish navigation: WKNavigation!) {
        print("HyperWebView: Navigation finished for tag \(reactTag)")
        // Forward to original delegate
        originalDelegate?.webView?(webView, didFinish: navigation)
    }

    func webView(_ webView: WKWebView, didStartProvisionalNavigation navigation: WKNavigation!) {
        // Forward to original delegate
        originalDelegate?.webView?(webView, didStartProvisionalNavigation: navigation)
    }

    func webView(_ webView: WKWebView, didFail navigation: WKNavigation!, withError error: Error) {
        // Forward to original delegate
        originalDelegate?.webView?(webView, didFail: navigation, withError: error)
    }

    func webView(_ webView: WKWebView, didFailProvisionalNavigation navigation: WKNavigation!, withError error: Error) {
        print("HyperWebView: Navigation failed with error: \(error)")
        // Forward to original delegate
        originalDelegate?.webView?(webView, didFailProvisionalNavigation: navigation, withError: error)
    }

    func webView(_ webView: WKWebView, didCommit navigation: WKNavigation!) {
        print("HyperWebView: Native navigation delegate triggered for tag \(reactTag)")
        originalDelegate?.webView?(webView, didCommit: navigation)
    }

}

// Make the module available to React Native
extension HyperWebViewModule: RCTBridgeModule {
    static func moduleName() -> String! {
        return "HyperWebViewModule"
    }
}

