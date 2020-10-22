package com.taiyi.metting.activity

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.taiyi.metting.R
import com.taiyi.metting.utils.CodeUtils
import com.taiyi.metting.utils.DensityUtil


class LoginActivity : BaseActivity() {

    lateinit var iv_code:ImageView
    lateinit var et_code:EditText
    lateinit var et_pwd:EditText
    lateinit var et_phone:EditText

    lateinit var tv_pwd_login_msg:TextView
    lateinit var tv_code_login_msg:TextView
    lateinit var ll_code:LinearLayout
    lateinit var ll_account:LinearLayout

    lateinit var tv_send_code_btn:TextView

    var bitmap: Bitmap? = null
    var code: String? = null

    //1:账号登录  2:验证码登录
    var loginState = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initView()
        initListener()

    }

    private fun initView(){
        iv_code = findViewById(R.id.image)
        et_code = findViewById(R.id.et_code)
        et_pwd = findViewById(R.id.et_password)
        et_phone = findViewById(R.id.et_phone)
        tv_pwd_login_msg = findViewById(R.id.tv_pwd_login_msg)
        tv_code_login_msg = findViewById(R.id.tv_code_login_msg)
        ll_account = findViewById(R.id.ll_account)
        ll_code = findViewById(R.id.ll_code)
        tv_send_code_btn = findViewById(R.id.tv_send_code_btn)
        bitmap = CodeUtils.getInstance().createBitmap(
            DensityUtil.dp2px(86f), DensityUtil.dp2px(40f),
            Color.parseColor("#F9F8F8")
        )
        //获取当前图片验证码的对应内容用于校验
        code = CodeUtils.getInstance().code

        iv_code.setImageBitmap(bitmap)
    }

    private fun initListener(){
        iv_code.setOnClickListener(View.OnClickListener {
            bitmap = CodeUtils.getInstance().createBitmap(
                DensityUtil.dp2px(86f), DensityUtil.dp2px(
                    40f
                ), Color.parseColor("#F9F8F8")
            )
            code = CodeUtils.getInstance().code
            iv_code.setImageBitmap(bitmap)
        })

        tv_code_login_msg.setOnClickListener(View.OnClickListener {
            if (loginState != 2) {
                tv_code_login_msg.setTextColor(ContextCompat.getColor(this, R.color.color_333333))
                tv_pwd_login_msg.setTextColor(ContextCompat.getColor(this, R.color.color_BBBBBB))
                ll_code.visibility = View.VISIBLE
                ll_account.visibility = View.GONE
                loginState = 2
            }
        })

        tv_pwd_login_msg.setOnClickListener(View.OnClickListener {
            if (loginState != 1) {
                tv_pwd_login_msg.setTextColor(ContextCompat.getColor(this, R.color.color_333333))
                tv_code_login_msg.setTextColor(ContextCompat.getColor(this, R.color.color_BBBBBB))
                ll_account.visibility = View.VISIBLE
                ll_code.visibility = View.GONE
                loginState = 1
            }
        })

        tv_send_code_btn.setOnClickListener(View.OnClickListener {
            countDown()
        })

    }


    /**
     * 倒计时显示
     */
    private fun countDown() {
        val timer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                tv_send_code_btn.isEnabled = false
                tv_send_code_btn.text = "已发送(" + millisUntilFinished / 1000 + ")"
            }

            override fun onFinish() {
                tv_send_code_btn.isEnabled = true
                tv_send_code_btn.text = "发送验证码"
            }
        }.start()
    }

}