package com.zgw.company.base.webview

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.webkit.JavascriptInterface
import android.webkit.ValueCallback
import com.just.agentweb.AgentWeb
import io.reactivex.disposables.CompositeDisposable
import org.json.JSONObject

class AndroidInterface(private val agentWeb: AgentWeb,
                       private val context: Context,
                       private val fragment: JSAgentWebFragment,
                       private var model: WebviewViewModel) {

    private var compositeDisposable = CompositeDisposable()


    fun onDestroy() {
        compositeDisposable.clear()
    }

    private val deliver = Handler(Looper.getMainLooper())

    private fun callJs(js: String, callback: ValueCallback<String>) {
        agentWeb.jsAccessEntrace.callJs(js, callback)
    }

    private fun callJs(js: String) {
        agentWeb.jsAccessEntrace.quickCallJs(js)
    }


    @JavascriptInterface
    fun back() {
        (context as Activity).finish()
    }

    private fun throttleEnable(function: () -> Unit): Boolean {
        return if (SystemClock.uptimeMillis() - model.oldTime > model.holderTime) {
            model.oldTime = SystemClock.uptimeMillis()
            function()
            true
        } else {
            false
        }
    }

    @JavascriptInterface
    fun bt(data: String) {
        val func = JSONObject(data).get("call_back").toString()
        val token = model.injectedToken
        callJs("$func('$token')")
    }

    @JavascriptInterface
    fun bt(): String? {
        return model.injectedToken
    }
}