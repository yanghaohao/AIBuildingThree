package com.zgw.company.base.dialog

import androidx.fragment.app.FragmentActivity
import com.zgw.company.base.R

class ProgressDialogFragment : BaseDialogFragment() {

    var listener: (() -> Unit?)? = null

    companion object {
        fun create(context: FragmentActivity): BaseDialogFragment {
            return DialogBuilder(context, ProgressDialogFragment::class.java)
                .setCancelableOnTouchOutside(false)
                .showAllowingStateLoss()
        }
    }

    override fun build(initialBuilder: Builder): Builder {
        val view = initialBuilder.layoutInflater.inflate(R.layout.cus_progress, null, false)
        return initialBuilder.setView(view)
    }

    fun setOnCancelListener(listener: () -> Unit): ProgressDialogFragment {
        this.listener = listener
        return this
    }

    override fun onDestroyView() {
        listener?.let { it() }
        super.onDestroyView()
    }
}
