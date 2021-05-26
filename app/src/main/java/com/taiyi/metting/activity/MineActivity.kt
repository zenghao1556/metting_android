package com.taiyi.metting.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.taiyi.metting.R
import com.taiyi.metting.base.ConstUtils
import com.taiyi.metting.utils.SPUtils

class MineActivity : BaseActivity() {
    private lateinit var back:ImageView
    private lateinit var logout:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mine)

        back = findViewById(R.id.iv_back_btn)
        logout = findViewById(R.id.logout)

        initListener()

    }

    private fun initListener(){
        back.setOnClickListener { finish() }
        logout.setOnClickListener {
            SPUtils.setStringPreferences(
                this@MineActivity,
                SPUtils.PROJECT,
                ConstUtils.USER_KEY,
                ""
            )
            val intent = Intent(
                this@MineActivity,
                LoginActivity::class.java
            ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)

            startActivity(intent)
        }
    }
}