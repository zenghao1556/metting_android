package com.taiyi.metting.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.taiyi.metting.MyApplication
import com.taiyi.metting.R
import com.taiyi.metting.entity.LoginEntity
import com.taiyi.metting.http.HttpClient
import com.taiyi.metting.utils.CodeUtils
import com.taiyi.metting.utils.DensityUtil
import okhttp3.*
import java.io.IOException


class LoginActivity : BaseActivity() {

    lateinit var iv_code:ImageView
    lateinit var et_code:EditText
    lateinit var et_pwd:EditText
    lateinit var et_phone:EditText
    lateinit var et_code_number:EditText

    lateinit var et_phone_number:EditText

    lateinit var tv_pwd_login_msg:TextView
    lateinit var tv_code_login_msg:TextView
    lateinit var ll_code:LinearLayout
    lateinit var ll_account:LinearLayout

    lateinit var tv_send_code_btn:TextView

    lateinit var tv_login_btn:TextView

    var bitmap: Bitmap? = null
    var code: String? = null

    //1:账号登录  2:验证码登录  15621308386
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
        tv_login_btn = findViewById(R.id.tv_login_btn)
        tv_pwd_login_msg = findViewById(R.id.tv_pwd_login_msg)
        tv_code_login_msg = findViewById(R.id.tv_code_login_msg)
        et_code_number = findViewById(R.id.et_code_number)
        ll_account = findViewById(R.id.ll_account)
        ll_code = findViewById(R.id.ll_code)
        tv_send_code_btn = findViewById(R.id.tv_send_code_btn)
        et_phone_number = findViewById(R.id.et_phone_number)
        bitmap = CodeUtils.getInstance().createBitmap(
            DensityUtil.dp2px(86f), DensityUtil.dp2px(40f),
            Color.parseColor("#F9F8F8")
        )
        //获取当前图片验证码的对应内容用于校验
        code = CodeUtils.getInstance().code

        et_code.setText(code)
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

        et_phone_number.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) =
                if (CodeUtils.getInstance().PhoneNumber(p0.toString())) {
                    tv_send_code_btn.isEnabled = true
                    tv_send_code_btn.background = ContextCompat.getDrawable(
                        this@LoginActivity,
                        R.drawable.shape_edit_bg_right
                    )
                } else {
                    tv_send_code_btn.isEnabled = false
                    tv_send_code_btn.background = ContextCompat.getDrawable(
                        this@LoginActivity,
                        R.drawable.shape_edit_bg_right_gray
                    )
                }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        tv_send_code_btn.setOnClickListener(View.OnClickListener {
            val requestBody: RequestBody = FormBody.Builder()
                .add("phone", et_phone_number.text.toString()).build()
            val request = Request.Builder()
                .url("https://f.longjuli.com/code/getsmscode")
                .post(requestBody)
                .build()
            HttpClient.instance.httpClient?.newCall(request)?.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                }

                override fun onResponse(call: Call, response: Response) {

                }

            })
            countDown()
        })

        tv_login_btn.setOnClickListener(View.OnClickListener {
            if (loginState == 2) {
                if (CodeUtils.getInstance()
                        .PhoneNumber(et_phone_number.text.toString()) && et_code_number.text.toString().length >= 4
                ) {
                    val requestBody: RequestBody = FormBody.Builder()
                        .add("vcode", et_code_number.text.toString())
                        .add("call", et_phone_number.text.toString())
                        .add("remember", "true").build()
                    val request = Request.Builder()
                        .url("https://f.longjuli.com/code/getsmscode")
                        .post(requestBody)
                        .build()
                    HttpClient.instance.httpClient?.newCall(request)?.enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            runOnUiThread(Runnable {
                                Toast.makeText(this@LoginActivity, "登录失败", Toast.LENGTH_SHORT)
                                    .show()
                            })
                        }

                        override fun onResponse(call: Call, response: Response) {
                            runOnUiThread(Runnable {
                                if (response.code() == 200){
                                    var data = response.body()?.string()
                                    var loginEntity: LoginEntity = JSONObject.parseObject(data,LoginEntity::class.java)
                                    if (loginEntity.code == "0"){
                                        MyApplication.getInstance()?.token = loginEntity.token
                                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                                    }else{
                                        Toast.makeText(this@LoginActivity, loginEntity.msg, Toast.LENGTH_SHORT).show()
                                    }
                                }else{
                                    Toast.makeText(this@LoginActivity, "服务异常,请稍后重试", Toast.LENGTH_SHORT).show()
                                }

                            })
                        }

                    })
                } else {
                    Toast.makeText(this, "手机号或验证码格式不正确", Toast.LENGTH_SHORT).show()
                }
            } else {
                var phone = et_phone.text.toString()
                var pwd = et_pwd.text.toString()
                var number = et_code.text.toString()

                if (phone.isEmpty() || !CodeUtils.getInstance().PhoneNumber(phone)) {
                    Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show()
                    return@OnClickListener
                }

                if (pwd.isEmpty()) {
                    Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show()
                    return@OnClickListener
                }

                if (number.isEmpty()) {
                    Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show()
                    return@OnClickListener
                }

                if (!number.equals(code)) {
                    Toast.makeText(this, "验证码错误", Toast.LENGTH_SHORT).show()
                    return@OnClickListener
                }
                val requestBody: RequestBody = FormBody.Builder()
                    .add("username", phone)
                    .add("password", pwd).build()
                val request = Request.Builder()
                    .url("https://f.longjuli.com/loginApp")
                    .post(requestBody)
                    .build()
                HttpClient.instance.httpClient?.newCall(request)?.enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        runOnUiThread(Runnable {
                            Toast.makeText(this@LoginActivity, "登录失败", Toast.LENGTH_SHORT).show()
                        })
                    }

                    override fun onResponse(call: Call, response: Response) {
                        runOnUiThread(Runnable {
                            var data = response.body()?.string()
                            if (response.code() == 200){
                                var loginEntity: LoginEntity = JSONObject.parseObject(data,LoginEntity::class.java)
                                if (loginEntity.code == "0"){
                                    MyApplication.getInstance()?.token = loginEntity.token
                                    MyApplication.getInstance()?.loginEntity = loginEntity
                                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                                    finish()
                                }else{
                                    Toast.makeText(this@LoginActivity, loginEntity.msg, Toast.LENGTH_SHORT).show()
                                }
                            }else{
                                Toast.makeText(this@LoginActivity, "服务异常,请稍后重试", Toast.LENGTH_SHORT).show()
                            }


                        })
                    }

                })
            }
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