package com.zgw.company.base.webview

import android.view.KeyEvent

interface FragmentKeyDown {
    fun onFragmentKeyDown(keyCode: Int, event: KeyEvent?): Boolean?
}