package com.taiyi.metting.activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.alibaba.fastjson.JSON
import com.taiyi.metting.MyApplication
import com.taiyi.metting.R
import com.taiyi.metting.entity.TableCardReponse
import com.taiyi.metting.event.WebsocketEvent
import com.taiyi.metting.http.WebSocketManager
import com.taiyi.metting.inter.IReceiveMessage
import com.taiyi.metting.utils.DensityUtil
import com.taiyi.metting.utils.ToastUtil
import com.taiyi.metting.view.CoverManager
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MeetingOperateActivity : BaseActivity() {
    lateinit var meetingData: TableCardReponse.TableCardInfo
    lateinit var iv_back_btn: ImageView
    lateinit var webView: WebView

    lateinit var iv_swich_btn: ImageView
    lateinit var iv_edit_btn: ImageView
    lateinit var iv_show_detail_btn:ImageView
    lateinit var iv_set_parameter: ImageView
    lateinit var iv_show_control:ImageView
    lateinit var iv_check_status:ImageView
    lateinit var iv_change_status:ImageView
    lateinit var iv_clear_data:ImageView

    lateinit var ll_ready: RelativeLayout
    lateinit var ll_start: RelativeLayout
    lateinit var ll_pause: RelativeLayout
    lateinit var ll_resume: RelativeLayout
    lateinit var ll_stop: RelativeLayout
    lateinit var ll_restart: RelativeLayout
    lateinit var ll_reset: RelativeLayout

    lateinit var ready_icon: ImageView
    lateinit var start_icon: ImageView
    lateinit var pause_icon: ImageView
    lateinit var resume_icon: ImageView
    lateinit var stop_icon: ImageView
    lateinit var restart_icon: ImageView
    lateinit var reset_icon: ImageView

    lateinit var rl_right_control: RelativeLayout


    private var currentType: Int = -1
    private var waitState = false

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meeting_operate)
        EventBus.getDefault().register(this)
        meetingData = intent.getSerializableExtra("data") as TableCardReponse.TableCardInfo
        /*CoverManager.getInstance().init(this)
        CoverManager.getInstance().setMaxDragDistance(150)
        CoverManager.getInstance().setExplosionTime(150)*/
        initView()
    }


    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun initView() {
        iv_back_btn = findViewById(R.id.iv_back_btn)
        webView = findViewById<WebView>(R.id.web_view)
        ready_icon = findViewById(R.id.ready_icon)
        start_icon = findViewById(R.id.start_icon)
        pause_icon = findViewById(R.id.pause_icon)
        resume_icon = findViewById(R.id.resume_icon)
        stop_icon = findViewById(R.id.stop_icon)
        restart_icon = findViewById(R.id.restart_icon)
        reset_icon = findViewById(R.id.reset_icon)

        rl_right_control = findViewById(R.id.rl_right_control)
        iv_swich_btn = findViewById(R.id.iv_swich_btn)
        iv_edit_btn = findViewById(R.id.iv_edit_btn)
        iv_show_detail_btn = findViewById(R.id.iv_show_detail_btn)
        iv_set_parameter = findViewById(R.id.iv_set_parameter)
        iv_show_control = findViewById(R.id.iv_show_control)
        iv_check_status = findViewById(R.id.iv_check_status)
        iv_change_status = findViewById(R.id.iv_change_status)
        iv_clear_data = findViewById(R.id.iv_clear_data)

        ll_ready = findViewById(R.id.ll_ready)
        ll_start = findViewById(R.id.ll_start)
        ll_pause = findViewById(R.id.ll_pause)
        ll_resume = findViewById(R.id.ll_resume)
        ll_stop = findViewById(R.id.ll_stop)
        ll_restart = findViewById(R.id.ll_restart)
        ll_reset = findViewById(R.id.ll_reset)

        iv_back_btn.setOnClickListener { finish() }

        iv_swich_btn.setOnClickListener {
            webView.evaluateJavascript(
                "exchangeSeats()",
                ValueCallback { })
        }
        iv_edit_btn.setOnClickListener {
            webView.evaluateJavascript(
                "editSeat()",
                ValueCallback { })
        }
        iv_show_detail_btn.setOnClickListener {
            webView.evaluateJavascript(
                "showSeat()",
                ValueCallback { })
        }
        iv_clear_data.setOnClickListener {
            confirmDialog(this,"清除缓存会把全部席签恢复至初始状态，是否确定清除？",object:ConfirmListener{
                override fun positive() {
                    webView.evaluateJavascript(
                        "clearCache()",
                        ValueCallback { })
                    setViewToGray()
                    ll_restart.setBackgroundColor(ContextCompat.getColor(this@MeetingOperateActivity,R.color.color_f9f8f8))
                }

                override fun cancle() {
                }

            })
        }
        iv_check_status.setOnClickListener {

            confirmDialog(this,"错漏重发是给全部超时席签重新发送当前指令，是否确定发送？",object:ConfirmListener{
                override fun positive() {
                    webView.evaluateJavascript(
                        "checkStatus()",
                        ValueCallback { })
                    setViewToGray()
                    ll_restart.setBackgroundColor(ContextCompat.getColor(this@MeetingOperateActivity,R.color.color_f9f8f8))
                }

                override fun cancle() {
                }

            })

        }

        iv_set_parameter.setOnClickListener {
            webView.evaluateJavascript(
                "setThreadParameter()",
                ValueCallback { })
        }

        iv_change_status.setOnClickListener {
            confirmDialog(this,"同步状态是把选中的座区状态直接改成当前正常状态，是否确定同步？",object:ConfirmListener{
                override fun positive() {
                    webView.evaluateJavascript(
                        "changeStatus()",
                        ValueCallback { })
                    setViewToGray()
                    ll_restart.setBackgroundColor(ContextCompat.getColor(this@MeetingOperateActivity,R.color.color_f9f8f8))
                }

                override fun cancle() {
                }

            })

        }
        iv_show_control.setOnClickListener {
            if (rl_right_control.visibility == View.VISIBLE){
                rl_right_control.visibility = View.GONE
            }else{
                rl_right_control.visibility = View.VISIBLE
            }
        }

        ll_ready.setOnClickListener {
            confirmDialog(this,"请确认是否会议准备",object:ConfirmListener{
                override fun positive() {
                    webView.evaluateJavascript(
                        "changeSeatStatus(1)",
                        ValueCallback { })
                    setViewToGray()
                    ll_restart.setBackgroundColor(ContextCompat.getColor(this@MeetingOperateActivity,R.color.color_f9f8f8))
                }

                override fun cancle() {
                }

            })

        }

        ll_start.setOnClickListener {
            confirmDialog(this,"请确认是否会议开始",object:ConfirmListener{
                override fun positive() {
                    webView.evaluateJavascript(
                        "changeSeatStatus(2)",
                        ValueCallback { })
                    setViewToGray()
                    ll_restart.setBackgroundColor(ContextCompat.getColor(this@MeetingOperateActivity,R.color.color_f9f8f8))
                }

                override fun cancle() {
                }

            })
            /*if (!waitState) {
                if (currentType == 1 || currentType == 5 || currentType == 6) {
                    waitState = true
                    webView.evaluateJavascript(
                        "changeSeatStatus(2)",
                        ValueCallback { })
                    setViewToGray()
                    ll_start.setBackgroundColor(ContextCompat.getColor(this, R.color.color_f9f8f8))
                }
            }*/
        }

        ll_pause.setOnClickListener {
            confirmDialog(this,"请确认是否会议暂停",object:ConfirmListener{
                override fun positive() {
                    webView.evaluateJavascript(
                        "changeSeatStatus(3)",
                        ValueCallback { })
                    setViewToGray()
                    ll_restart.setBackgroundColor(ContextCompat.getColor(this@MeetingOperateActivity,R.color.color_f9f8f8))
                }

                override fun cancle() {
                }

            })
            /*if (!waitState) {
                if (currentType == 1 || currentType == 4) {
                    waitState = true
                    webView.evaluateJavascript(
                        "changeSeatStatus(3)",
                        ValueCallback { })
                    setViewToGray()
                    ll_pause.setBackgroundColor(ContextCompat.getColor(this, R.color.color_f9f8f8))
                }
            }*/
        }
        ll_resume.setOnClickListener {
            confirmDialog(this,"请确认是否会议恢复",object:ConfirmListener{
                override fun positive() {
                    webView.evaluateJavascript(
                        "changeSeatStatus(4)",
                        ValueCallback { })
                    setViewToGray()
                    ll_restart.setBackgroundColor(ContextCompat.getColor(this@MeetingOperateActivity,R.color.color_f9f8f8))
                }

                override fun cancle() {
                }

            })
            /*if (!waitState) {
                if (currentType == 3) {
                    waitState = true
                    webView.evaluateJavascript(
                        "changeSeatStatus(4)",
                        ValueCallback { })
                    ll_resume.setBackgroundColor(ContextCompat.getColor(this, R.color.color_f9f8f8))
                }
            }*/
        }
        ll_stop.setOnClickListener {
            confirmDialog(this,"请确认是否会议结束",object:ConfirmListener{
                override fun positive() {
                    webView.evaluateJavascript(
                        "changeSeatStatus(5)",
                        ValueCallback { })
                    setViewToGray()
                    ll_restart.setBackgroundColor(ContextCompat.getColor(this@MeetingOperateActivity,R.color.color_f9f8f8))
                }

                override fun cancle() {
                }

            })
            /*if (!waitState) {
                if (currentType == 2 || currentType == 3 || currentType == 4) {
                    waitState = true
                    webView.evaluateJavascript(
                        "changeSeatStatus(5)",
                        ValueCallback { })
                    setViewToGray()
                    ll_stop.setBackgroundColor(ContextCompat.getColor(this, R.color.color_f9f8f8))
                }
            }*/
        }

        ll_restart.setOnClickListener {
            confirmDialog(this,"请确认是否会议重启",object:ConfirmListener{
                override fun positive() {
                    webView.evaluateJavascript(
                        "changeSeatStatus(6)",
                        ValueCallback { })
                    setViewToGray()
                    ll_restart.setBackgroundColor(ContextCompat.getColor(this@MeetingOperateActivity,R.color.color_f9f8f8))
                }

                override fun cancle() {
                }

            })
            /*if (!waitState) {
                if (currentType == 2 || currentType == 3 || currentType == 4 || currentType == 5 || currentType == 6) {
                    waitState = true
                    webView.evaluateJavascript(
                        "changeSeatStatus(6)",
                        ValueCallback { })
                    setViewToGray()
                    ll_restart.setBackgroundColor(ContextCompat.getColor(this,R.color.color_f9f8f8)
                    )
                }
            }*/

        }
        ll_reset.setOnClickListener {
            confirmDialog(this,"请确认是否会议重置，设备重置之后需要重新绑定席签",object:ConfirmListener{
                override fun positive() {
                    webView.evaluateJavascript(
                        "changeSeatStatus(7)",
                        ValueCallback { })
                    setViewToGray()
                    ll_restart.setBackgroundColor(ContextCompat.getColor(this@MeetingOperateActivity,R.color.color_f9f8f8))
                }

                override fun cancle() {
                }

            })
            /*if (!waitState) {
                if (currentType == 2 || currentType == 3 || currentType == 4 || currentType == 5 || currentType == 6) {
                    waitState = true
                    webView.evaluateJavascript(
                        "changeSeatStatus(7)",
                        ValueCallback { })
                    setViewToGray()
                    ll_reset.setBackgroundColor(ContextCompat.getColor(this, R.color.color_f9f8f8))
                }
            }*/
        }

        if (meetingData.communicationType == 1){
            ll_pause.visibility = View.GONE
            ll_resume.visibility = View.GONE
            ll_stop.visibility = View.GONE
            ll_reset.visibility = View.GONE
        }
        initWebView()
    }


    fun setViewToGray() {
        ll_ready.setBackgroundColor(ContextCompat.getColor(this, R.color.color_ecebeb))
        ll_start.setBackgroundColor(ContextCompat.getColor(this, R.color.color_ecebeb))
        ll_pause.setBackgroundColor(ContextCompat.getColor(this, R.color.color_ecebeb))
        ll_resume.setBackgroundColor(ContextCompat.getColor(this, R.color.color_ecebeb))
        ll_stop.setBackgroundColor(ContextCompat.getColor(this, R.color.color_ecebeb))
        ll_restart.setBackgroundColor(ContextCompat.getColor(this, R.color.color_ecebeb))
        ll_reset.setBackgroundColor(ContextCompat.getColor(this, R.color.color_ecebeb))
    }

    private fun initWebView() {
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                return false
            }
        }

        //设置WebChromeClient类
        webView.webChromeClient = object : WebChromeClient() {
            override fun onJsAlert(
                view: WebView?,
                url: String?,
                message: String?,
                result: JsResult?
            ): Boolean {
                return super.onJsAlert(view, url, message, result)
            }
        }
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.settings.javaScriptEnabled = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.builtInZoomControls = true
        webView.settings.useWideViewPort = true
        webView.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        //设置为可调用js方法
        webView.settings.javaScriptEnabled = true
        webView.addJavascriptInterface(this, "H5JsMeeting")

        webView.loadUrl("https://m.longjuli.com/meet/pad/meetingmap.html?meetingid=${meetingData.presentmeeting}&username=${MyApplication.getInstance()?.loginEntity?.phone}&token=${MyApplication.getInstance()?.token}&communicationType=${meetingData.communicationType}")
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onDestroy() {

        EventBus.getDefault().unregister(this)
        webView.evaluateJavascript(
            "stopMeetWebSocket()",
            ValueCallback { })
        webView.clearCache(true)
        super.onDestroy()
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun webSocketEvent(event: WebsocketEvent) {
        webView.evaluateJavascript(
            "meetWebSocketMessage(${event.msg})",
            ValueCallback { })
    }

    @SuppressLint("JavascriptInterface")
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    @JavascriptInterface
    fun showInputDialog(type: Int, data: String) {
        var builder = AlertDialog.Builder(this).create()
        //通过布局填充器获login_layout
        var view = layoutInflater.inflate(R.layout.send_txt_dialog, null)
        //获取两个文本编辑框（密码这里不做登陆实现，仅演示）
        var name = view.findViewById<View>(R.id.message) as EditText
        var txtCount = view.findViewById<View>(R.id.txtCount) as EditText
        var negtive = view.findViewById<View>(R.id.negtive)
        var positive = view.findViewById<View>(R.id.positive)
        var send = view.findViewById<View>(R.id.send)
        builder.setView(view) //设置login_layout为对话提示框
        builder.setCancelable(false) //设置为不可取消

        txtCount.visibility = if (meetingData.communicationType == 1) View.VISIBLE else View.GONE

        if (data.isNotEmpty()) {
            name.setText(data)
        }
        negtive.setOnClickListener {
            builder.dismiss()
        }
        positive.setOnClickListener {
            val name = name.text.toString().trim()
            val count = txtCount.text.toString().trim()

            if (name != null && name.isNotEmpty()) {
                if (meetingData.communicationType == 1 && count.isNullOrEmpty()){
                    return@setOnClickListener
                }
                runOnUiThread(Runnable {
                    webView.evaluateJavascript("promptInputDialog('${type}','${name}','${count}')",
                        ValueCallback { })
                })

                builder.dismiss()
            }
        }
        send.setOnClickListener{
            runOnUiThread(Runnable {
                webView.evaluateJavascript("promptInputDialog('99','发送生僻字','')",
                    ValueCallback { })
            })
            builder.dismiss()
        }
        builder.show() //显示Dialog对话框
        builder.window?.setLayout(DensityUtil.dp2px(440f), LinearLayout.LayoutParams.WRAP_CONTENT)
    }


    private fun confirmDialog(context: Context, msg: String) {
        var view = layoutInflater.inflate(R.layout.confirm_dialog, null)
        var builder = AlertDialog.Builder(context)
            .setView(view)
            .setCancelable(false)
            .create()

        var title = view.findViewById<TextView>(R.id.title)
        var positive = view.findViewById<Button>(R.id.positive)
        title.text = msg
        positive.setOnClickListener {
            builder.dismiss()
        }
        builder.show() //显示Dialog对话框
        builder.window?.setLayout(DensityUtil.dp2px(440f), DensityUtil.dp2px(230f))
    }


    private fun confirmDialog(context: Context, msg: String,listener:ConfirmListener) {
        var view = layoutInflater.inflate(R.layout.confirm_check_dialog, null)
        var builder = AlertDialog.Builder(context)
            .setView(view)
            .setCancelable(false)
            .create()

        var title = view.findViewById<TextView>(R.id.title)
        var positive = view.findViewById<Button>(R.id.positive)
        var cancle = view.findViewById<Button>(R.id.cancle)
        title.text = msg
        positive.setOnClickListener {
            listener?.positive()
            builder.dismiss()
        }
        cancle.setOnClickListener {
            listener?.cancle()
            builder.dismiss()
        }
        builder.show() //显示Dialog对话框
        builder.window?.setLayout(DensityUtil.dp2px(440f), DensityUtil.dp2px(230f))
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    @JavascriptInterface
    fun finishSeatStatus(type: Int) {
        runOnUiThread(Runnable {
            kotlin.run {
                when (type) {
                    1 -> {
                        ll_ready.setBackgroundColor(
                            ContextCompat.getColor(
                                this,
                                R.color.color_ffce16
                            )
                        )
                        ready_icon.visibility = View.VISIBLE
                    }
                    2 -> {
                        ll_ready.setBackgroundColor(
                            ContextCompat.getColor(
                                this,
                                R.color.color_ffce16
                            )
                        )
                        ll_start.setBackgroundColor(
                            ContextCompat.getColor(
                                this,
                                R.color.color_ffce16
                            )
                        )
                        ready_icon.visibility = View.VISIBLE
                        start_icon.visibility = View.VISIBLE

                        ll_pause.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                        ll_resume.setBackgroundColor(
                            ContextCompat.getColor(
                                this,
                                R.color.color_ecebeb
                            )
                        )
                        ll_stop.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                        ll_restart.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                        ll_reset.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                    }
                    3 -> {
                        ll_ready.setBackgroundColor(
                            ContextCompat.getColor(
                                this,
                                R.color.color_ffce16
                            )
                        )
                        ll_start.setBackgroundColor(
                            ContextCompat.getColor(
                                this,
                                R.color.color_ffce16
                            )
                        )
                        ll_pause.setBackgroundColor(
                            ContextCompat.getColor(
                                this,
                                R.color.color_ecebeb
                            )
                        )
                        ll_resume.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                        ll_stop.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                        ll_restart.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                        ll_reset.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                        ready_icon.visibility = View.VISIBLE
                        start_icon.visibility = View.VISIBLE
                    }
                    4 -> {
                        ll_ready.setBackgroundColor(
                            ContextCompat.getColor(
                                this,
                                R.color.color_ffce16
                            )
                        )
                        ll_start.setBackgroundColor(
                            ContextCompat.getColor(
                                this,
                                R.color.color_ffce16
                            )
                        )
                        ll_pause.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                        ll_resume.setBackgroundColor(
                            ContextCompat.getColor(
                                this,
                                R.color.color_ecebeb
                            )
                        )
                        ll_stop.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                        ll_restart.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                        ll_reset.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                        ready_icon.visibility = View.VISIBLE
                        start_icon.visibility = View.VISIBLE
                    }
                    5 -> {
                        ll_ready.setBackgroundColor(
                            ContextCompat.getColor(
                                this,
                                R.color.color_ffce16
                            )
                        )
                        ll_start.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                        ll_pause.setBackgroundColor(
                            ContextCompat.getColor(
                                this,
                                R.color.color_ecebeb
                            )
                        )
                        ll_resume.setBackgroundColor(
                            ContextCompat.getColor(
                                this,
                                R.color.color_ecebeb
                            )
                        )
                        ll_stop.setBackgroundColor(
                            ContextCompat.getColor(
                                this,
                                R.color.color_ecebeb
                            )
                        )
                        ll_restart.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                        ll_reset.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                        ready_icon.visibility = View.VISIBLE
                        start_icon.visibility = View.GONE
                    }
                    6 -> {
                        ll_ready.setBackgroundColor(
                            ContextCompat.getColor(
                                this,
                                R.color.color_ffce16
                            )
                        )
                        ll_start.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                        ll_pause.setBackgroundColor(
                            ContextCompat.getColor(
                                this,
                                R.color.color_ecebeb
                            )
                        )
                        ll_resume.setBackgroundColor(
                            ContextCompat.getColor(
                                this,
                                R.color.color_ecebeb
                            )
                        )
                        ll_stop.setBackgroundColor(
                            ContextCompat.getColor(
                                this,
                                R.color.color_ecebeb
                            )
                        )
                        ll_restart.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                        ll_reset.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                        ready_icon.visibility = View.VISIBLE
                        start_icon.visibility = View.GONE
                    }
                    7 -> {
                        ll_ready.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                        ll_start.setBackgroundColor(
                            ContextCompat.getColor(
                                this,
                                R.color.color_ecebeb
                            )
                        )
                        ll_pause.setBackgroundColor(
                            ContextCompat.getColor(
                                this,
                                R.color.color_ecebeb
                            )
                        )
                        ll_resume.setBackgroundColor(
                            ContextCompat.getColor(
                                this,
                                R.color.color_ecebeb
                            )
                        )
                        ll_stop.setBackgroundColor(
                            ContextCompat.getColor(
                                this,
                                R.color.color_ecebeb
                            )
                        )
                        ll_restart.setBackgroundColor(
                            ContextCompat.getColor(
                                this,
                                R.color.color_ecebeb
                            )
                        )
                        ll_reset.setBackgroundColor(
                            ContextCompat.getColor(
                                this,
                                R.color.color_ecebeb
                            )
                        )
                        ready_icon.visibility = View.GONE
                        start_icon.visibility = View.GONE
                    }
                }
                currentType = type
                waitState = false
            }
        })

    }

    @JavascriptInterface
    fun showTipsDialog(msg: String) {
        confirmDialog(this, msg)
    }

    @JavascriptInterface
    fun showToast(msg: String,time:Int) {
        ToastUtil.showToast(this,msg,R.color.colorBg,time)
    }

    @JavascriptInterface
    fun checkLoginState(): Boolean {
        return !MyApplication.getInstance()?.token.isNullOrEmpty()
    }

    @JavascriptInterface
    fun showResetDialog(title: String,type:Int) {
        confirmDialog(this, title,object:ConfirmListener{
            @RequiresApi(Build.VERSION_CODES.KITKAT)
            override fun positive() {
                runOnUiThread(Runnable {
                    webView.evaluateJavascript(
                        "promptConfirmDialog(${type})",
                        ValueCallback { })
                })
            }

            override fun cancle() {
            }

        })
    }


    override fun onResume() {
        super.onResume()
        connectWebsocket()
    }

    private fun connectWebsocket() {
        Thread {
            WebSocketManager.getInstance().connect(object : IReceiveMessage {
                override fun onConnectSuccess() {}
                override fun onConnectFailed() {}
                override fun onClose() {}
                override fun onMessage(text: String?) {
                    var event = WebsocketEvent()
                    event.msg = text.toString()
                    Log.e("socket log",event.msg)
                    EventBus.getDefault().post(event)
                }
            }, "ws://f.longjuli.com/cardWebSocket/"+MyApplication.getInstance()?.loginEntity?.phone)
        }.start()
    }

    interface ConfirmListener{
        fun positive()
        fun cancle()
    }


}