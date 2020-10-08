package com.zgw.company.base.core.config

object Config {

    const val DB_NAME = "zgw_company_app"
    const val FAVORITE_TABLE_NAME = "favorite_table"
    const val NULL_TABLE_NAME = "null_table"

    const val ROUTER_LOGIN_ACTIVITY = "/login/activity"
    const val ROUTER_MAIN_ACTIVITY = "/main/activity"
    const val LOGIN_INTERCEPTOR = "com.zgw.company.base.core.http.interceptor.RouterLoginInterceptor"
    const val ROUTER_WEBVIEW_ACTIVITY = "/webview/activity"


    const val SELECTED_LANGUAGE = "selected_language"
    const val LAST_LANGUAGE = "lastLanguage"
    const val LANGUAGE_CHANGED = "language_changed"

    const val DIALOG_CANCEL_REQUEST_CODE = 1000

    val SHARE_REQUEST_CODE_FACEBOOK = 1001
    val SHARE_REQUEST_CODE_MESSENGER = 1002
    val SHARE_REQUEST_CODE_COPY = 1003

    const val DIALOG_BIG_SCALE = 329.0 / 375
}