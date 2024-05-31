package `in`.juspay.hyper.webview.react

import android.util.Log
import android.webkit.WebView
import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.uimanager.ThemedReactContext
import com.reactnativecommunity.webview.RNCWebViewManager
import com.reactnativecommunity.webview.RNCWebViewWrapper
import `in`.juspay.hyper.webview.upi.HyperWebViewServices

@ReactModule(name = HyperWebViewManager.REACT_CLASS)
class HyperWebViewManager : RNCWebViewManager() {
    override fun getName(): String = REACT_CLASS

    override fun createViewInstance(ctx: ThemedReactContext): RNCWebViewWrapper {
        val webViewWrapper = super.createViewInstance(ctx)
        attachBridge(ctx, webViewWrapper.webView)

        return webViewWrapper
    }

    companion object {
        fun attachBridge(ctx: ThemedReactContext, webView: WebView) {
            try {
                val hyperWebViewServices =
                    HyperWebViewServices(ctx.currentActivity!!, webView)
                hyperWebViewServices.attach()
            } catch (e: Exception) {
                Log.e(REACT_CLASS, "Failed to attach bridge due to $e. " +
                    "Possibly activity is not present in provided context. " +
                    "Stack trace: ${e.stackTrace}")
            }
        }

        const val REACT_CLASS = "HyperWebViewManager"
    }
}
