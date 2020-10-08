package com.zgw.company.base.core.api

import com.safframework.log.L
import com.zgw.company.base.core.MyApplication
import com.zgw.company.base.R
import retrofit2.HttpException
import retrofit2.Response
import java.util.regex.Pattern

@Suppress("unused")
sealed class ApiResponse<T>{

    companion object{
        fun <T> create(error:Throwable) : ApiErrorResponse<T> = apiErrorResponse(error)

        fun <T> create(response : Response<HttpResponse<T>>) : ApiResponse<T> {
            return if (response.isSuccessful){
                val body = response.body()
                when{
                    body == null -> apiErrorResponse<T>(ServiceException(response.code().toString(),response.message()))
                    response.code() == 204 -> ApiEmptyResponse<T>()
                    body.isOkStatus -> ApiSuccessResponse(body.data,response.headers()?.get("link"))
                    else -> apiErrorResponse(ServiceException(body.code,body.msg))
                }
            }else{
                apiErrorResponse(HttpException(response))
            }
        }

        private fun <T> apiErrorResponse(error: Throwable): ApiErrorResponse<T> = ApiErrorResponse(
            InnerException.handle(error).message?: MyApplication.instance.resources.getString(R.string.unknown_error))
    }
}

data class ApiSuccessResponse<T>(val data: T?, val links: Map<String, String>) : ApiResponse<T>(){
    constructor(body : T?, linkHeader : String?) : this(data = body,links = linkHeader?.extractLinks() ?: emptyMap())

    val nextPage: Int? by lazy(LazyThreadSafetyMode.NONE) {
        links[NEXT_LINK]?.let { next ->
            val matcher = PAGE_PATTERN.matcher(next)
            if (!matcher.find() || matcher.groupCount() != 1) {
                null
            } else {
                try {
                    Integer.parseInt(matcher.group(1))
                } catch (ex: NumberFormatException) {
                    L.w("ApiSuccessResponse", "cannot parse next page from $next")
                    null
                }
            }
        }
    }

    companion object{
        private val LINK_PATTERN = Pattern.compile("<([^>]*)>[\\s]*;[\\s]*rel=\"([a-zA-Z0-9]+)\"")
        private val PAGE_PATTERN = Pattern.compile("\\bpage=(\\d+)")
        private const val NEXT_LINK = "next"

        private fun String.extractLinks(): Map<String, String>? {
            val links = mutableMapOf<String,String>()
            val matcher = LINK_PATTERN.matcher(this)

            while (matcher.find()){
                val count = matcher.groupCount()
                if (count == 2){
                    links[matcher.group(2)] = matcher.group(1)
                }
            }

            return links
        }
    }
}



class ApiEmptyResponse<T> : ApiResponse<T>()

data class ApiErrorResponse<T>(val errorMessage:String) : ApiResponse<T>()