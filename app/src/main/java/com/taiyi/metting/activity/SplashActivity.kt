package com.taiyi.metting.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.fastjson.JSON
import com.taiyi.metting.MyApplication
import com.taiyi.metting.R
import com.taiyi.metting.base.ConstUtils
import com.taiyi.metting.entity.LoginEntity
import com.taiyi.metting.utils.SPUtils

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        initUser()
    }

    private fun initUser() {
        var data: String? = SPUtils.getStringPreference(this, SPUtils.PROJECT, ConstUtils.USER_KEY, null)
        if (data != null && data.isNotEmpty()) {
            val user: LoginEntity = JSON.parseObject(data, LoginEntity::class.java)
            MyApplication.getInstance()?.loginEntity = user
            MyApplication.getInstance()?.token = user.token
            startActivity(Intent(SplashActivity@this,MainActivity::class.java))
        }else{
            startActivity(Intent(SplashActivity@this,LoginActivity::class.java))
        }
        finish()
    }
}