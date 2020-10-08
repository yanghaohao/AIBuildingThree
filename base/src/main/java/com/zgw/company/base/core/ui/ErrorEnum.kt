package com.zgw.company.base.core.ui

enum class ErrorEnum(val value:String) {
    QR200("成功"),
    QR404("失败"),
    QRERROR("内部异常"),
    QR500("没有权限"),
    QR501("未登录"),
    QRKICKOUT("用户被踢下线"),
    QR1001("用户名密码不匹配"),
    QR1002("无此账号"),
    QR1003("用户锁定一分钟"),
    QR_T1001("令牌token过期"),
    QR_T1002("令牌token无效"),
    QR_T1003("令牌token格式错误"),
    QR_T1004("令牌toekn参数异常"),
    QR_T1006("token服务异常"),
    QR_ACCOUNT("账号主体异常"),
    QR_unkown("未知异常")
}