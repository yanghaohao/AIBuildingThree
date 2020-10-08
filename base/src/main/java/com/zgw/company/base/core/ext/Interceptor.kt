package com.zgw.company.base.core.ext

import androidx.annotation.IdRes

abstract class Interceptor {

    /**
     * 是否满足检验器的要求
     * @return
     */
    abstract fun skip(): Boolean

    @IdRes
    abstract fun getResId():Int
}