package com.zgw.company.base.core.config

import android.os.Process
import androidx.appcompat.app.AppCompatActivity
import java.lang.ref.WeakReference
import java.util.*
import kotlin.system.exitProcess

class IActivityManager private constructor(){

    private var mCurrentActivityWeakRef : WeakReference<AppCompatActivity>? = null

    fun currentActivity() : AppCompatActivity?{
        var currentActivity : AppCompatActivity? = null
        if (null != mCurrentActivityWeakRef){
            currentActivity = mCurrentActivityWeakRef!!.get()
        }

        return currentActivity
    }

    fun registerActivity(activity: AppCompatActivity){
        mCurrentActivityWeakRef = WeakReference(activity)
        activities.add(activity)
    }

    fun unregisterActivity(activity: AppCompatActivity?){
        if (null != mCurrentActivityWeakRef){
            mCurrentActivityWeakRef!!.clear()
        }

        if (null != activity){
            val position = activities.indexOf(activity)
            if (position > 0){
                activities.removeAt(position)
            }
            activity.finish()
        }
    }

    fun exitApp(){
        synchronized(activities){
            for (activity in activities){
                activity.finish()
            }
        }
        activities.clear()

        Process.killProcess(Process.myPid())
        exitProcess(0)
    }


    companion object{
        private val activities = LinkedList<AppCompatActivity>()

        var instance = IActivityManager()

        fun killBeforeActivities(){
            val size = activities.size -1
            for (i in 0..size){
                activities[i].finish()
            }
        }

        fun lastActivity() : AppCompatActivity? {
            return activities.last
        }
    }
}