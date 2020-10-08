package com.zgw.company.base.core.utils

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.os.Environment
import android.os.StatFs
import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.zgw.company.base.core.MyApplication
import java.io.*
import java.security.cert.X509Certificate
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import kotlin.math.roundToInt

object FileUtils {

    private const val FILE_SAVE_PATH = "mw_cache"
    private val DO_NOT_VERIFY = HostnameVerifier { _, _ -> true }
    private const val TAG = "com.zgw.company.base.core.utils.FileUtils"


    val totalInternalMemorySize: Long
        get() {
            val path = Environment.getDataDirectory()
            val stat = StatFs(path.path)
            var blockSize: Long = 0
            var totalBlocks: Long = 0

            blockSize = stat.blockSizeLong
            totalBlocks = stat.blockCountLong

            return totalBlocks * blockSize
        }

    fun getMWCachePath(context: Context): String {
        return getDiskCacheDir(context, FILE_SAVE_PATH)!!.toString() + File.separator
    }


    fun loadImage(url: String, filename: String): Bitmap? {
        return try {
            val fis = FileInputStream(url + filename)
            BitmapFactory.decodeStream(fis)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            null
        }

    }

    private fun trustAllHosts() {
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }

            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}

            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
        })

        try {
            val sc = SSLContext.getInstance("TLS")
            sc.init(null, trustAllCerts, java.security.SecureRandom())
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.socketFactory)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun hasExternalCache(context: Context): Boolean {
        return (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()
                && checkPermissions(context, "android.permission.WRITE_EXTERNAL_STORAGE")
                && context.externalCacheDir != null)
    }

    @JvmStatic
    fun checkPermissions(context: Context?, permission: String): Boolean {
        val localPackageManager = context!!.applicationContext.packageManager
        return localPackageManager.checkPermission(
            permission,
            context.applicationContext.packageName
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun getDiskCacheDir(context: Context, fileDir: String?): File? {

        var cacheDirectory: File?
        cacheDirectory = if (hasExternalCache(context)) {
            context.externalCacheDir
        } else {
            context.cacheDir
        }
        if (cacheDirectory == null) {
            cacheDirectory = context.cacheDir
            if (cacheDirectory == null) {
                return null
            }
        }
        if (fileDir != null) {
            val file = File(cacheDirectory, fileDir)
            return if (!file.exists() && !file.mkdir()) {
                cacheDirectory
            } else {
                file
            }
        }
        return cacheDirectory
    }

    fun delFile(path: String) {
        if (!TextUtils.isEmpty(path)) {
            val file = File(path)
            if (file.exists()) {
                file.delete()
            }
        }
    }

    fun compressFile(oldpath: String, newPath: String): File? {
        var compressBitmap: Bitmap? = decodeFile(oldpath)
        var newBitmap: Bitmap? = ratingImage(oldpath, compressBitmap)
        val os = ByteArrayOutputStream()
        newBitmap!!.compress(Bitmap.CompressFormat.PNG, 60, os)
        val bytes = os.toByteArray()

        var file: File? = null
        try {
            file = getFileFromBytes(bytes, newPath)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (!newBitmap.isRecycled) {
                newBitmap.recycle()
                newBitmap = null
            }
            if (compressBitmap != null) {
                if (!compressBitmap.isRecycled) {
                    compressBitmap.recycle()
                    compressBitmap = null
                }
            }
        }
        return file
    }

    private fun ratingImage(filePath: String, bitmap: Bitmap?): Bitmap {
        val degree = readPictureDegree(filePath)
        return rotatingImageView(degree, bitmap)
    }

    fun rotatingImageView(angle: Int, bitmap: Bitmap?): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle.toFloat())
        return Bitmap.createBitmap(
            bitmap!!, 0, 0,
            bitmap.width, bitmap.height, matrix, true
        )
    }

    fun readPictureDegree(path: String): Int {
        var degree = 0
        try {
            val exifInterface = ExifInterface(path)
            val orientation = exifInterface.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
            degree = when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90
                ExifInterface.ORIENTATION_ROTATE_180 -> 180
                ExifInterface.ORIENTATION_ROTATE_270 -> 270
                else -> 0
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return degree
    }

    fun getFileFromBytes(b: ByteArray, outputFile: String): File? {
        var ret: File? = null
        var stream: BufferedOutputStream? = null
        try {
            ret = File(outputFile)
            val fstream = FileOutputStream(ret)
            stream = BufferedOutputStream(fstream)
            stream.write(b)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            closeQuietly(stream)
        }
        return ret
    }

    fun decodeFile(fPath: String): Bitmap {
        val opts = BitmapFactory.Options()
        opts.inJustDecodeBounds = true
        opts.inDither = false
        opts.inPurgeable = true
        opts.inInputShareable = true
        opts.inPreferredConfig = Bitmap.Config.RGB_565
        BitmapFactory.decodeFile(fPath, opts)
        val requiredSize = 200
        var scale = 1
        if (opts.outHeight > requiredSize || opts.outWidth > requiredSize) {
            val heightRatio = (opts.outHeight.toFloat() / requiredSize.toFloat()).roundToInt()
            val widthRatio = (opts.outWidth.toFloat() / requiredSize.toFloat()).roundToInt()
            scale = heightRatio.coerceAtMost(widthRatio)
        }
        opts.inJustDecodeBounds = false
        opts.inSampleSize = scale
        return BitmapFactory.decodeFile(fPath, opts).copy(Bitmap.Config.ARGB_8888, false)
    }

    fun getFileType(path: String): String? {
        val file = File(path)
        if (file.exists() && file.isFile) {
            val fileName = file.name
            return fileName.substring(fileName.lastIndexOf(".") + 1)
        }

        return null
    }

    fun closeQuietly(closeable: Closeable?) {
        if (closeable != null) {
            try {
                closeable.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    fun getJson(context: Context, fileName: String): String {
        val stringBuilder = StringBuilder()
        try {
            val assetManager = context.assets
            val bf = BufferedReader(InputStreamReader(assetManager.open("$fileName.json"), "UTF-8"))
            var read: String? = null
            while ({ read = bf.readLine();read }() != null) {
                stringBuilder.append(read)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return stringBuilder.toString()
    }

    @JvmStatic
    fun <T> handleVirtualData(clazz: Class<T>): LiveData<T> {
        val live = MutableLiveData<T>()
        val filepath = "virtualdata" + "/" + clazz.simpleName
        val response = getJson(MyApplication.instance, filepath)
        val data = Gson().fromJson(response, clazz)
        live.value = data
        return live
    }
}