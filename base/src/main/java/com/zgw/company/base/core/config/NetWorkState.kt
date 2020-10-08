package com.zgw.company.base.core.config

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.lifecycle.LiveData
import com.zgw.company.base.core.utils.DeviceInfoUtils

class NetWorkState(val context: Context?) : LiveData<Boolean>() {

    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            value = DeviceInfoUtils.isNetworkAvailable()
        }
    }

    override fun onInactive() {
        super.onInactive()
        context?.unregisterReceiver(broadcastReceiver)
    }

    override fun onActive() {
        super.onActive()

        val intentFilter = IntentFilter()
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        context?.registerReceiver(broadcastReceiver, intentFilter)
    }
}