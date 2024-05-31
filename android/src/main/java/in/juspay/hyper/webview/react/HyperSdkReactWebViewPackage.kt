package `in`.juspay.hyper.webview.react

import com.facebook.react.ReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.ViewManager

class HyperSdkReactWebViewPackage : ReactPackage {
    override fun createNativeModules(ignored: ReactApplicationContext): List<NativeModule> {
        return emptyList()
    }

    override fun createViewManagers(ignored: ReactApplicationContext): List<ViewManager<*, *>> {
        return listOf(HyperWebViewManager())
    }
}
