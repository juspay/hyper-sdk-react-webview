//
//  HyperReactWebView.swift
//
//  Copyright © Juspay Technologies. All rights reserved.
//

import Foundation
import React
import WebKit
import HyperWebView

private class HyperReactWebView: RNCWebViewImpl {
    
    @objc public var iframeIntegration = false
    
    private var hyperWebViewServices: HyperWebViewServices?
    private weak var realNavigationDelegate: WKNavigationDelegate?
    
    override func didMoveToWindow() {
        super.didMoveToWindow()
        self.createHyperWebViewServices()
    }
    
    private func createHyperWebViewServices() {
        if (self.responds(to: NSSelectorFromString("webView"))) {
            if let wkWebView = self.perform(NSSelectorFromString("webView"))?.takeUnretainedValue() as? WKWebView {
                if #available(iOS 16.4, *) {
                    wkWebView.isInspectable = true
                }
                wkWebView.navigationDelegate = self
                self.hyperWebViewServices = HyperWebViewServices(webView: wkWebView, isIframeIntegration: iframeIntegration)
            }
        }
    }
}

extension HyperReactWebView: WKNavigationDelegate {
    func webView(_ webView: WKWebView, didCommit navigation: WKNavigation!) {
        self.hyperWebViewServices?.attach()
    }
}

@objc(HyperWebViewManageriOS)
public class HyperWebViewManager: RNCWebViewManager {
    
    private var hyperWebView: HyperReactWebView!

    // Ensure setup is done on the main queue
    override public static func requiresMainQueueSetup() -> Bool {
        return true
    }
    
    // This function is called to create and return the view
    public override func view() -> UIView! {
        self.hyperWebView = HyperReactWebView()
        return self.hyperWebView
    }
}
