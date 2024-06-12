package `in`.juspay.hyper.webview.react

import android.content.Intent
import android.util.Log
import android.webkit.WebView
import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.uimanager.ThemedReactContext
import com.reactnativecommunity.webview.RNCWebViewManager
import com.reactnativecommunity.webview.RNCWebViewWrapper
import `in`.juspay.hyper.webview.upi.HyperWebViewServices
import java.util.ArrayList

@ReactModule(name = HyperWebViewManager.REACT_CLASS)
class HyperWebViewManager : RNCWebViewManager() {
    private lateinit var hyperWebViewService: HyperWebViewServices
    override fun getName(): String = REACT_CLASS

    override fun createViewInstance(ctx: ThemedReactContext): RNCWebViewWrapper {
        val webViewWrapper = super.createViewInstance(ctx)
        attachBridge(ctx, webViewWrapper.webView)

        return webViewWrapper
    }

    private fun attachBridge(ctx: ThemedReactContext, webView: WebView) {
        try {
            hyperWebViewService =
                HyperWebViewServices(ctx.currentActivity!!, webView)
            hyperWebViewService.attach()
            HYPER_WEB_VIEW_SERVICES.add(hyperWebViewService)
        } catch (e: Exception) {
            Log.e(REACT_CLASS, "Failed to attach bridge due to $e. " +
                "Possibly activity is not present in provided context. " +
                "Stack trace: ${e.stackTrace}")
        }
    }

    override fun onDropViewInstance(view: RNCWebViewWrapper) {
        super.onDropViewInstance(view)
        HYPER_WEB_VIEW_SERVICES.remove(hyperWebViewService)
    }

    companion object {
        private val HYPER_WEB_VIEW_SERVICES = ArrayList<HyperWebViewServices>()
        // Re-exporting so that users can refer w/o adding HyperWebViewServices as a dependency.
        const val UPI_REQUEST_CODE = HyperWebViewServices.UPI_REQUEST_CODE
        const val REACT_CLASS = "HyperWebViewManager"

        fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            if (requestCode == UPI_REQUEST_CODE) {
                for (service in HYPER_WEB_VIEW_SERVICES) {
                    service.onActivityResult(requestCode, resultCode, data)
                }
            }
        }
    }
}
