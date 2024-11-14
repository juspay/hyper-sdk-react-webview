//
//  HyperSdkReactWebView.h
//
//  Copyright © Juspay Technologies. All rights reserved.
//

#import <React/RCTViewManager.h>
#import "RNCWebViewManager.h"

@interface RCT_EXTERN_MODULE(HyperWebViewManageriOS, RNCWebViewManager)

RCT_EXPORT_VIEW_PROPERTY(iframeIntegration, BOOL)

@end
