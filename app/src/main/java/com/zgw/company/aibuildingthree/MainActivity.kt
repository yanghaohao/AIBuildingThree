package com.zgw.company.aibuildingthree

import android.content.Intent
import com.zgw.company.base.core.utils.PermissionRequest
import com.zgw.company.aibuildingthree.databinding.ActivityMainBinding
import com.zgw.company.base.core.ui.DataBindingBaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : DataBindingBaseActivity<ActivityMainBinding>() {


    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {
        init {
            System.loadLibrary("native-lib")
        }
    }

    override fun setLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initView(){

        "123".toInt()
        sample_text.text = stringFromJNI()

        sample_text.text = "aaa"

        val permissionRequest = PermissionRequest()
        permissionRequest.requestPermission(this,this,permissionRequest.camera,permissionRequest.write_permission,permissionRequest.fine_location)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        
    }
}
