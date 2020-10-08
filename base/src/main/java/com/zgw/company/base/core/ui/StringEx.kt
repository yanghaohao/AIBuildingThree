package com.zgw.company.base.core.ui

fun String.getCode() : Int = this.replace("QR","").toInt()