package `in`.juspay.hypersdk.webview.react

import android.content.Intent
import android.util.Log
import android.webkit.WebView
import com.facebook.react.bridge.*
import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.uimanager.UIManagerModule
import `in`.juspay.hyper.webview.upi.HyperWebViewServices
import java.util.concurrent.ConcurrentHashMap
import com.facebook.react.uimanager.UIManagerHelper
import com.facebook.react.uimanager.common.UIManagerType

@ReactModule(name = HyperWebViewModule.MODULE_NAME)
class HyperWebViewModule(reactContext: ReactApplicationContext) :
    ReactContextBaseJavaModule(reactContext) {

    private val webViewInstances = ConcurrentHashMap<Int, WebView>()

    override fun getName(): String = MODULE_NAME

    @ReactMethod
    fun initializeHyperWebView(reactTag: Int, iframeIntegration: Boolean, promise: Promise) {
        try {

            val uiManager = if (BuildConfig.IS_NEW_ARCHITECTURE_ENABLED) {
                UIManagerHelper.getUIManager(reactApplicationContext, UIManagerType.FABRIC)
            } else {
                reactApplicationContext.getNativeModule(UIManagerModule::class.java)
            }

            try {
                val view = uiManager?.resolveView(reactTag)
                val webView = if (view != null) findWebView(view) else null

                if (webView != null) {
                    webViewInstances[reactTag] = webView

                    // Create HyperWebViewServices
                    val currentActivity = reactApplicationContext.currentActivity
                    if (currentActivity != null) {
                        val hyperServices = HyperWebViewServices(currentActivity, webView)
                        hyperWebViewServices[reactTag] = hyperServices

                        Log.d(MODULE_NAME, "HyperWebView initialized for reactTag: $reactTag")
                        promise.resolve(null)
                    } else {
                        promise.reject("ERROR", "Current activity is null")
                    }
                } else {
                    promise.reject("ERROR", "WebView not found for reactTag: $reactTag")
                }
            } catch (e: Exception) {
                Log.e(MODULE_NAME, "Error initializing HyperWebView: ${e.message}", e)
                promise.reject("ERROR", "Failed to initialize HyperWebView: ${e.message}")
            }
        } catch (e: Exception) {
            Log.e(MODULE_NAME, "Error in initializeHyperWebView: ${e.message}", e)
            promise.reject("ERROR", "Failed to initialize HyperWebView: ${e.message}")
        }
    }

    @ReactMethod
    fun attachHyperWebView(reactTag: Int, promise: Promise) {
        try {
            val hyperServices = hyperWebViewServices[reactTag]
            if (hyperServices != null) {
                reactApplicationContext.currentActivity?.runOnUiThread {
                    try {
                        hyperServices.attach()
                        Log.d(MODULE_NAME, "HyperWebView attached for reactTag: $reactTag")
                        promise.resolve(null)
                    } catch (e: Exception) {
                        Log.e(
                            MODULE_NAME,
                            "Error attaching HyperWebView on UI thread: ${e.message}",
                            e
                        )
                        promise.reject("ERROR", "Failed to attach HyperWebView: ${e.message}")
                    }
                } ?: promise.reject("ERROR", "Current activity is null")
            } else {
                promise.reject("ERROR", "HyperWebView not initialized for reactTag: $reactTag")
            }
        } catch (e: Exception) {
            Log.e(MODULE_NAME, "Error attaching HyperWebView: ${e.message}", e)
            promise.reject("ERROR", "Failed to attach HyperWebView: ${e.message}")
        }
    }

    @ReactMethod
    fun cleanupHyperWebView(reactTag: Int) {
        try {
            hyperWebViewServices.remove(reactTag)
            webViewInstances.remove(reactTag)
            Log.d(MODULE_NAME, "HyperWebView cleaned up for reactTag: $reactTag")
        } catch (e: Exception) {
            Log.e(MODULE_NAME, "Error cleaning up HyperWebView: ${e.message}", e)
        }
    }

    private fun findWebView(view: android.view.View): WebView? {
        if (view is WebView) {
            return view
        }

        if (view is android.view.ViewGroup) {
            for (i in 0 until view.childCount) {
                val webView = findWebView(view.getChildAt(i))
                if (webView != null) {
                    return webView
                }
            }
        }

        return null
    }

    companion object {
        const val MODULE_NAME = "HyperWebViewModule"
        private val hyperWebViewServices = ConcurrentHashMap<Int, HyperWebViewServices>()

        // Re-exporting so that users can refer w/o adding HyperWebViewServices as a dependency.
        const val UPI_REQUEST_CODE = HyperWebViewServices.UPI_REQUEST_CODE

        // Handle activity result for all active HyperWebView instances
        @JvmStatic
        fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            if (requestCode == UPI_REQUEST_CODE) {
                for (service in hyperWebViewServices.values) {
                    service.onActivityResult(requestCode, resultCode, data)
                }
            }
        }
    }
}
