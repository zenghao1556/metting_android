package com.taiyi.metting.http

import okhttp3.MediaType
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

/**
 * Created by zenghao on 2020/10/22.
 */
class HttpClient {
    private var mHttpClient: OkHttpClient? = null

    //设置读超时
    ////设置写超时
    //设置连接超时
    //是否自动重连
    val httpClient: OkHttpClient?
        get() {
            if (null == mHttpClient) {
                mHttpClient = OkHttpClient.Builder()
                    .readTimeout(5, TimeUnit.SECONDS) //设置读超时
                    .writeTimeout(5, TimeUnit.SECONDS) ////设置写超时
                    .connectTimeout(15, TimeUnit.SECONDS) //设置连接超时
                    .pingInterval(40,TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true) //是否自动重连
                    .build()
            }
            return mHttpClient
        }

    companion object {
        val instance = HttpClient()
        private val JSON = MediaType.parse("multipart/form-data; charset=utf-8")
    }
}