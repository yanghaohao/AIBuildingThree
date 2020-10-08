package com.zgw.company.base.core.vo

import androidx.fragment.app.FragmentActivity
import com.zgw.company.base.core.config.IActivityManager
import com.zgw.company.base.dialog.ProgressDialogFragment
import com.zgw.company.base.core.ext.toast

class Resource<T>(private var status: Status, var data: T? = null, var message: String? = "") {

    fun work(
        onLoading: () -> Boolean = { false },
        onError: (message: String?) -> Unit = { toast(message) },
        onSuccess: () -> Unit
    ) {
        when (status) {
            Status.LOADING -> if (onLoading()) {
                dialog =
                    ProgressDialogFragment.create(IActivityManager.lastActivity() as FragmentActivity) as ProgressDialogFragment
                dialog?.setOnCancelListener { cancelListener?.let { it() } }
            }
            Status.ERROR -> {
                onError(message)
                dialog?.dismissAllowingStateLoss()
            }
            Status.SUCCESS -> {
                onSuccess()
                dialog?.dismissAllowingStateLoss()
            }
        }
    }

    var dialog: ProgressDialogFragment? = null

    fun success(data: T? = null): Resource<T> {
        this.status = Status.SUCCESS
        this.data = data
        return this
    }

    fun error(data: T? = null, message: String): Resource<T>? {
        this.data = data
        this.message = message
        this.status = Status.ERROR
        return this
    }

    fun loading(): Resource<T>? {
        this.status = Status.LOADING
        return this
    }

    override fun toString(): String =
        "com.zgw.company.base.core.vo.Resource(status=$status, data=$data, message=$message)"

    override fun hashCode(): Int {
        var result = status.hashCode()
        result = 27 * result + (data?.hashCode() ?: 0)
        result = 27 * result + (message?.hashCode() ?: 0)
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Resource<*>

        if (status != other.status) return false
        if (data != other.data) return false
        if (message != other.message) return false

        return true
    }

    var cancelListener: (() -> Unit?)? = null
    fun setOnCancelListener(cancelListener: () -> Unit) {
        this.cancelListener = cancelListener
    }
}