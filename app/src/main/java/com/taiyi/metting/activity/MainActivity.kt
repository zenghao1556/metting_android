package com.taiyi.metting.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.taiyi.metting.R

class MainActivity : BaseActivity() {
    lateinit var tv_dzzp: ImageView
    lateinit var tv_zwq:ImageView
    lateinit var tv_zu:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv_dzzp = findViewById(R.id.tv_dzzp)
        tv_zwq = findViewById(R.id.tv_zwq)
        tv_zu = findViewById(R.id.tv_zu)

        tv_zwq.setOnClickListener(View.OnClickListener { startActivity(Intent(this,MeetingPlaceActivity::class.java)) })
        tv_dzzp.setOnClickListener { startActivity(Intent(this,TableCardActivity::class.java)) }
    }
}