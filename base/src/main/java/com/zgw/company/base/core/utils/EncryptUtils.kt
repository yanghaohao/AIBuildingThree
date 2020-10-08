package com.zgw.company.base.utils

object EncryptUtils {

    init {
        System.loadLibrary("codec")
    }

    external fun nativeCheck(): String

    external fun encryptPayPassword(oldString: String): String

    external fun encryptPayPasswordWithSalt(oldString: String, type: Int): String
}
