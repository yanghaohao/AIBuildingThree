package com.zgw.company.base.core

import androidx.multidex.MultiDexApplication
import kotlin.properties.Delegates

open class BaseApplication : MultiDexApplication(){

    override fun onCreate() {
        super.onCreate()

        instance = this

    }

    companion object{
        var instance : BaseApplication by Delegates.notNull()
            private set
    }
}