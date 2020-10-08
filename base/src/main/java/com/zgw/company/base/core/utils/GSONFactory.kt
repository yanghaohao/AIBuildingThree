package com.zgw.company.base.core.utils

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import com.zgw.company.base.core.api.HttpResponse
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okio.Buffer
import retrofit2.Converter
import retrofit2.Retrofit
import java.io.IOException
import java.io.OutputStreamWriter
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.nio.charset.Charset

class GSONFactory private constructor(val gson : Gson) : Converter.Factory() {

    companion object{
        fun create(gson: Gson = Gson()) : GSONFactory {
            return GSONFactory(gson)
        }
    }

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        val beanType = TypeToken.get(type).type

        val listType = ParameterizedTypeImpl(HttpResponse::class.java, arrayOf(beanType))
        val adapter = gson.getAdapter(TypeToken.get(listType))
        return GsonResponseBodyConverter(gson, adapter)
    }

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<Annotation>,
        methodAnnotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody>? {
        val adapter = gson.getAdapter(TypeToken.get(type))
        return GsonRequestBodyConverter(gson,adapter)
    }
}

internal class GsonResponseBodyConverter<T>(
    private val gson: Gson,
    private val adapter: TypeAdapter<T>
): Converter<ResponseBody, T> {

    @Throws(IOException::class)
    override fun convert(value: ResponseBody): T {
        val jsonReader = gson.newJsonReader(value.charStream())
        jsonReader.use {
            return adapter.read(it)
        }
    }

}

internal class GsonRequestBodyConverter<T>(private val gson: Gson,private val adapter:TypeAdapter<T>) : Converter<T,RequestBody>{

    @Throws(IOException::class)
    override fun convert(value: T): RequestBody {
        val buffer = Buffer()
        val writer = OutputStreamWriter(buffer.outputStream(), UTF_8)
        val jsonWriter = gson.newJsonWriter(writer)
        adapter.write(jsonWriter, value)
        jsonWriter.close()
        return RequestBody.create(MEDIA_TYPE, buffer.readByteString())
    }

    companion object {
        private val MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8")
        private val UTF_8 = Charset.forName("UTF-8")
    }
}

class ParameterizedTypeImpl(private val raw : Class<*>, private val args : Array<Type>) : ParameterizedType{
    override fun getRawType(): Type = raw
    override fun getOwnerType(): Type? = null
    override fun getActualTypeArguments(): Array<Type> = args
}