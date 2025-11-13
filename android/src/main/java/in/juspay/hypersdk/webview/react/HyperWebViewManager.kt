package `in`.juspay.hypersdk.webview.react

import android.content.Intent
import `in`.juspay.hyper.webview.upi.HyperWebViewServices

class HyperWebViewManager {

    companion object {
        const val UPI_REQUEST_CODE = HyperWebViewServices.UPI_REQUEST_CODE

        @JvmStatic
        fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            if (requestCode == UPI_REQUEST_CODE) {
                HyperWebViewModule.onActivityResult(requestCode, resultCode, data)
            }
        }
    }
}
