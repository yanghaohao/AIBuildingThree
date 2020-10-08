package com.zgw.company.base.core.http.entity.request

import java.io.Serializable

/**
 * @Description
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 2018/9/10 下午1:41
 * @Version
 */
class LoginParams : Serializable {
    var phone_number: String? = ""
    var validation_code: String? = ""
//    var password: String? = ""
//        set(value) {
//            field = com.zgw.company.base.utils.EncryptUtils.encryptPayPassword(value?:"")
//        }
//    var oauth_expires: String? = ""
//    var oauth_token: String? = ""
//    var oauth_user_id: String? = ""
//    var invite_code: String? = ""
//    var device_id: String = DeviceInfoUtils.getDeviceId(App.instance)
//    var os_type: String = DeviceInfoUtils.os
    private var login_type: Int? = 0
    fun setType(enum: Int) {
        login_type = enum
    }
}