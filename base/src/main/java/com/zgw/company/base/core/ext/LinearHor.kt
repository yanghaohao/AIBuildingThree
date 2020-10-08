package com.zgw.company.base.core.ext

import android.annotation.SuppressLint
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.safframework.log.L
import com.zgw.company.base.R

fun RecyclerView.linearHor() {
    this.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
}

fun RecyclerView.linear() {
    this.layoutManager = LinearLayoutManager(this.context)
}

fun RecyclerView.grid(spanCount: Int) {
    this.layoutManager = GridLayoutManager(this.context, spanCount)
}

fun RecyclerView.itemDecoration() {
    val dividerItemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
    dividerItemDecoration.setDrawable(ContextCompat.getDrawable(context!!, R.drawable.shape_list_divider_gray_05dp)!!)
    addItemDecoration(dividerItemDecoration)
}

@SuppressLint("ClickableViewAccessibility")
fun View.event(click: ((View) -> Unit)? = null, doubleTap: (() -> Unit)? = null,
               longPress: (() -> Unit)? = null) {
    this.isLongClickable = true
    val gestureDetector = GestureDetector(this.context, object : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
            L.d("com.zgw.company.base.core.ext.event", "onSingleTapConfirmed ")
            click?.apply { this(this@event) }
            return false
        }

        override fun onDoubleTap(e: MotionEvent?): Boolean {
            L.d("com.zgw.company.base.core.ext.event", "onDoubleTap ")
            doubleTap?.apply { this() }
            return false
        }

        override fun onLongPress(e: MotionEvent?) {
            super.onLongPress(e)
            L.d("com.zgw.company.base.core.ext.event", "onLongPres ")
            longPress?.apply { this() }
        }
    })

    this.setOnTouchListener { _, event ->
        gestureDetector.onTouchEvent(event)
    }
}