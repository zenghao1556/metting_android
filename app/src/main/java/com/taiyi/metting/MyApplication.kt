package com.taiyi.metting

import android.app.Application

class MyApplication: Application() {

    var token:String = ""

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