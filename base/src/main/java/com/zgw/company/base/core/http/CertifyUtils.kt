package com.zgw.company.base.core.http

import android.content.Context
import android.text.TextUtils
import android.util.Log
import okhttp3.OkHttpClient
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.net.URLEncoder
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.Certificate
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import javax.net.ssl.*

object CertifyUtils {
    private var certifyAssetName = "merculet.crt"

    fun getSSLClientIgnoreExpire(client: OkHttpClient, context: Context, assetsSSLFileName: String = certifyAssetName): OkHttpClient {

        getStream(context, assetsSSLFileName).use {

            //Certificate
            val certificateFactory = CertificateFactory.getInstance("X.509")
            var certificate: Certificate? = null
            val pubSub: String
            val pubIssuer: String
            certificate = certificateFactory.generateCertificate(it)
            val pubSubjectDN = (certificate as X509Certificate).subjectX500Principal
            val pubIssuerDN = certificate.issuerX500Principal
            pubSub = pubSubjectDN.name
            pubIssuer = pubIssuerDN.name

            val trustManagers = arrayOf<X509TrustManager>(object : X509TrustManager {
                @Throws(CertificateException::class)
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
                    try {
                        val tmf = TrustManagerFactory.getInstance("X509")
                        tmf.init(null as KeyStore?)
                        for (trustManager in tmf.trustManagers) {
                            (trustManager as X509TrustManager).checkClientTrusted(chain, authType)
                        }
                    } catch (e: Exception) {
                        throw CertificateException(e)
                    }
                }

                @Throws(CertificateException::class)
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
                    try {
                        val tmf = TrustManagerFactory.getInstance("X509")
                        tmf.init(null as KeyStore?)
                        for (trustManager in tmf.trustManagers) {
                            (trustManager as X509TrustManager).checkServerTrusted(chain, authType)
                        }
                    } catch (e: Exception) {
                        throw CertificateException(e)
                    }

                    if (chain[0].subjectX500Principal.name != pubSub) {
                        throw CertificateException("server's SubjectDN is not equals to client's SubjectDN")
                    }
                    if (chain[0].issuerX500Principal.name != pubIssuer) {
                        throw CertificateException("server's IssuerDN is not equals to client's IssuerDN")
                    }
                }

                override fun getAcceptedIssuers(): Array<X509Certificate?> {
                    return arrayOfNulls(0)
                }
            })

            //SSLContext  and SSLSocketFactory
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustManagers, java.security.SecureRandom())
            val sslSocketFactory = sslContext.socketFactory

            val builder = client.newBuilder()
            builder.sslSocketFactory(sslSocketFactory, trustManagers[0])
            return builder.build()
        }
    }


    fun getTrustAllSSLClient(): SSLContext? {
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            @Throws(CertificateException::class)
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) = Unit

            @Throws(CertificateException::class)
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) = Unit

            override fun getAcceptedIssuers(): Array<X509Certificate?> {
                return arrayOfNulls(0)
            }
        })
        return try {
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, java.security.SecureRandom())
            sslContext
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 忽略所有https证书
     */
    fun getTrustAllSSLClient(client: OkHttpClient): OkHttpClient {
        try {
            val trustManagers = arrayOf<TrustManager>(object : X509TrustManager {
                @Throws(CertificateException::class)
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
                }

                @Throws(CertificateException::class)
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }
            })

            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustManagers, java.security.SecureRandom())
            val sslSocketFactory = sslContext.socketFactory

            val builder = client.newBuilder()
            builder.sslSocketFactory(sslSocketFactory)
            builder.hostnameVerifier { hostname, session ->
                Log.e("verify", hostname)
                true
            }

            return builder.build()
        } catch (e: Exception) {
            return client
        }

    }

    fun getSSLClient(client: OkHttpClient, context: Context, assetsSSLFileName: String): OkHttpClient {
        val inputStream = getStream(context, assetsSSLFileName)
        return getSSLClientByInputStream(client, inputStream)
    }

    fun getSSLClientByCertificateString(client: OkHttpClient, certificate: String): OkHttpClient {
        val inputStream = getStream(certificate)
        return getSSLClientByInputStream(client, inputStream)
    }

    private fun getStream(context: Context, assetsFileName: String): InputStream? {
        try {
            return context.assets.open(assetsFileName)
        } catch (var3: Exception) {
            return null
        }

    }

    private fun getStream(certificate: String): InputStream? {
        try {
            return ByteArrayInputStream(certificate.toByteArray(charset("UTF-8")))
        } catch (var3: Exception) {
            return null
        }

    }

    private fun getSSLClientByInputStream(client: OkHttpClient, inputStream: InputStream?): OkHttpClient {
        var client = client
        if (inputStream != null) {
            val sslSocketFactory = setCertificates(inputStream)
            if (sslSocketFactory != null) {
                client = client.newBuilder().sslSocketFactory(sslSocketFactory).build()
            }
        }
        return client
    }

    private fun setCertificates(vararg certificates: InputStream): SSLSocketFactory? {
        try {
            val certificateFactory = CertificateFactory.getInstance("X.509")

            val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
            keyStore.load(null)

            var index = 0
            for (certificate in certificates) {
                val certificateAlias = Integer.toString(index++)
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate))
                try {
                    certificate.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

            val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            trustManagerFactory.init(keyStore)

            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, trustManagerFactory.trustManagers, SecureRandom())

            return sslContext.socketFactory
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

    }

    fun getHeaderValueEncoded(value: String): String {
        if (TextUtils.isEmpty(value)) return " "
        var i = 0
        val length = value.length
        while (i < length) {
            val c = value[i]
            if (c <= '\u001f' && c != '\t' || c >= '\u007f') {//根据源码okhttp允许[0020-007E]+\t的字符
                try {
                    return URLEncoder.encode(value, "UTF-8")
                } catch (e: Exception) {
                    e.printStackTrace()
                    return " "
                }

            }
            i++
        }
        return value
    }

    fun getHeaderNameEncoded(name: String): String {
        if (TextUtils.isEmpty(name)) return "null"
        var i = 0
        val length = name.length
        while (i < length) {
            val c = name[i]
            if (c <= '\u0020' || c >= '\u007f') {//根据源码okhttp允许[0021-007E]的字符
                try {
                    return URLEncoder.encode(name, "UTF-8")
                } catch (e: Exception) {
                    e.printStackTrace()
                    return " "
                }

            }
            i++
        }
        return name
    }
    val isInternationalHost :Boolean = true

}