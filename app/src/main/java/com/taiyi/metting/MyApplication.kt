package com.taiyi.metting

import android.app.Application
import com.taiyi.metting.entity.LoginEntity

class MyApplication: Application() {

    var token:String = ""

    lateinit var loginEntity:LoginEntity
    companion object {
        var app: MyApplication? = null
        fun getInstance(): MyApplication? {
            return app
        }
    }


    override fun onCreate() {
        super.onCreate()
        app = this
    }
}