package com.zgw.company.base.core.http.interceptor

import android.annotation.SuppressLint
import com.zgw.company.base.R
import com.zgw.company.base.core.api.HttpResponse
import com.zgw.company.base.core.api.InnerException
import com.zgw.company.base.core.api.RetrofitManager
import com.zgw.company.base.core.config.Config
import com.zgw.company.base.core.ext.routerWithAnim
import com.zgw.company.base.core.http.User
import com.zgw.company.base.core.http.entity.request.LoginParams
import com.zgw.company.base.core.http.entity.response.LoginRegisterResponse
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class HttpLoginInterceptor : Interceptor {

    @SuppressLint("CheckResult")
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response? {
        val request = chain.request()
        val response = chain.proceed(request)
        return when(response.code()){
            InnerException.FORBIDDEN -> router(response)
            InnerException.UNAUTHORIZED -> refreshToken(response, chain, request)
            else -> response
        }
    }

    fun router(response: Response) : Response? {
        routerWithAnim(Config.ROUTER_LOGIN_ACTIVITY).anim(R.anim.activity_open, R.anim.fade_out)
        return response
    }

    private fun refreshToken(response: Response?,chain: Interceptor.Chain,requset:Request) :Response?{
        val param = LoginParams()
        param.setType(1)
        val data = RetrofitManager.get().apiService().logins("", param).execute().body() as HttpResponse<LoginRegisterResponse>?
        val token = data?.data?.token.toString()
        User.currentUser.accessToken = token
        return chain.proceed(requset.newBuilder().header("",token).build())
    }
}