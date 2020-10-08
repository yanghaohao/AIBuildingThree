package com.zgw.company.base.core.api

import android.net.ParseException
import com.google.gson.JsonParseException
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.security.cert.CertPathValidatorException
import javax.net.ssl.SSLHandshakeException

class InnerException {

    //    ("成功"),
//    ("失败"),
//    ("内部异常"),
//    ("没有权限"),
//    ("未登录"),
//    ("用户被踢下线"),
//    QR1001("用户名密码不匹配"),
//    QR1002("无此账号"),
//    QR1003("用户锁定一分钟"),
//    ("令牌token过期"),
//    ("令牌token无效"),
//    QR_T1003("令牌token格式错误"),
//    ("令牌toekn参数异常"),
//    QR_T1006("token服务异常"),
//    QR_ACCOUNT("账号主体异常"),
//    QR_unkown("未知异常")
    companion object {
        const val REQUEST_FAIL = "QR404"    //失败
        const val INNER_EXCEPTION = "QRERROR"   //内部异常
        private const val NO_PERMISSION = "QR500"       //没有权限
        private const val NOT_LOGIN = "QR501"    //未登录
        private const val USER_IS_OUT_ONLINE = "QRKICKOUT" //用户被踢下线
        private const val TOKEN_IS_MISS = "QR-T1001" // 令牌token过期
        private const val TOKEN_FORMAT_NO_USEFUL = "QR-T1002" // 令牌token无效
        private const val TOKEN_FORMAT_FORMAT_ERROR = "QR-T1003" // 令牌token格式错误
        private const val TOKEN_VAR_EXCEPTION = "QR-T1004" // 令牌toekn参数异常
        private const val TOKEN_SERVICE_IS_EXCEPTION = "QR-T1006" // token服务异常
        private const val ACCENT_IS_EXCEPTION = "QR-ACCOUNT" // 账号主体异常
        private const val UN_KNOWN_EXCEPTION = "QR-unkown" // 未知异常

        private const val ACCESS_DENIED = 302
        const val UNAUTHORIZED = 401
        const val FORBIDDEN = 403
        private const val NOT_FOUND = 404
        private const val REQUEST_TIMEOUT = 408
        private const val INTERNAL_SERVER_ERROR = 500
        private const val BAD_GATEWAY = 502
        private const val SERVICE_UNAVAILABLE = 503
        private const val GATEWAY_TIMEOUT = 504

        fun handle(e:Throwable) : Throwable{
            return when(e){
                is HttpException -> when(e.code()){
                    UNAUTHORIZED -> Throwable("token过期", e)
                    FORBIDDEN -> Throwable("请先登录", e)
                    NOT_FOUND -> Throwable("HTTP NOT FOUND", e)
                    REQUEST_TIMEOUT -> Throwable("请求超时", e)
                    GATEWAY_TIMEOUT -> Throwable("网关超时")
                    INTERNAL_SERVER_ERROR -> Throwable("内部服务器错误", e)
                    BAD_GATEWAY -> Throwable("无效网关", e)
                    SERVICE_UNAVAILABLE -> Throwable("找不到服务器", e)
                    ACCESS_DENIED -> Throwable("拒绝访问", e)
                    else -> Throwable("网络错误", e)
                }
                is ServiceException -> when (e.code) {
                    REQUEST_FAIL, INNER_EXCEPTION, NO_PERMISSION, NOT_LOGIN, USER_IS_OUT_ONLINE, TOKEN_IS_MISS, TOKEN_FORMAT_NO_USEFUL, TOKEN_FORMAT_FORMAT_ERROR, TOKEN_VAR_EXCEPTION, TOKEN_SERVICE_IS_EXCEPTION, ACCENT_IS_EXCEPTION, UN_KNOWN_EXCEPTION -> Throwable(e.message, e)
                    else -> Throwable("服务器异常", e)
                }
                is JsonParseException, is JSONException, is ParseException -> Throwable("解析错误", e)
                is ConnectException -> Throwable("连接失败", e)
                is UnknownHostException -> Throwable("网络连接失败", e)
                is SSLHandshakeException -> Throwable("证书验证失败", e)
                is CertPathValidatorException -> Throwable("证书路径没找到", e)
                is ConnectTimeoutException, is SocketTimeoutException -> Throwable("连接超时", e)
                is ClassCastException -> Throwable("类型转换出错", e)
                is NullPointerException -> Throwable("数据有空", e)
                else -> Throwable(e.message, e)
            }
        }
    }
}