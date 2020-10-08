package com.zgw.company.base.core.api

import java.io.Serializable

open class HttpResponse<T> : Serializable{

    var code = "-1"
    var msg:String?=null
    var data:T? = null

    val isOkStatus : Boolean
        get() = code == "QR200"
}