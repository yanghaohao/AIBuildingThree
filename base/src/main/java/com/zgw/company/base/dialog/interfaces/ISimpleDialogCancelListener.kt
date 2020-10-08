package com.zgw.company.base.dialog.interfaces

interface ISimpleDialogCancelListener : DialogListener{

    fun onCancelled(requestCode : Int)
}