//
//  HyperWebViewModule.m
//
//  Copyright © Juspay Technologies. All rights reserved.
//

#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(HyperWebViewModule, NSObject)

RCT_EXTERN_METHOD(initializeHyperWebView:(nonnull NSNumber *)reactTag
                  iframeIntegration:(BOOL)iframeIntegration
                  resolve:(RCTPromiseResolveBlock)resolve
                  reject:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(attachHyperWebView:(nonnull NSNumber *)reactTag
                  resolve:(RCTPromiseResolveBlock)resolve
                  reject:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(cleanupHyperWebView:(nonnull NSNumber *)reactTag)

@end
