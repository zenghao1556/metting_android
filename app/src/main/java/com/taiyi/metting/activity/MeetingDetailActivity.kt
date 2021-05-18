package com.taiyi.metting.activity

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.*
import android.widget.*
import androidx.annotation.RequiresApi
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.taiyi.metting.MyApplication
import com.taiyi.metting.R
import com.taiyi.metting.adapter.TestAdapter
import com.taiyi.metting.adapter.TestAdapter.ItemMoveListener
import com.taiyi.metting.entity.MeetingListResponse
import com.taiyi.metting.entity.PersonEntity
import com.taiyi.metting.entity.SavePersonEntity
import com.taiyi.metting.http.HttpClient
import com.taiyi.metting.utils.DensityUtil
import com.taiyi.metting.view.CoverManager
import okhttp3.*
import java.io.IOException

class MeetingDetailActivity : BaseActivity() {
    lateinit var meetingData:MeetingListResponse.MeetingEntity
    lateinit var iv_back_btn:ImageView
    lateinit var lv_list:ListView
    lateinit var webView: WebView
    lateinit var rl_add_layout:RelativeLayout
    lateinit var tv_close_btn:TextView
    lateinit var tv_add_btn:TextView
    lateinit var ll_not_data:LinearLayout
    lateinit var et_add_user:EditText

    lateinit var iv_swich_btn:ImageView
    lateinit var iv_edit_btn:ImageView
    lateinit var iv_add_btn:ImageView
    lateinit var iv_move_btn:ImageView
    lateinit var iv_delete_btn:ImageView
    lateinit var iv_save_btn:ImageView

    lateinit var rl_right_control:RelativeLayout

    lateinit var adapter: TestAdapter
    private var dataList:MutableList<PersonEntity> = mutableListOf()

    private var allData:MutableList<PersonEntity> = mutableListOf()

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meeting_detail)

        meetingData= intent.getSerializableExtra("data") as MeetingListResponse.MeetingEntity

        CoverManager.getInstance().init(this)
        CoverManager.getInstance().setMaxDragDistance(150)
        CoverManager.getInstance().setExplosionTime(150)

        initView()

        getPersonData()
    }


    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun initView(){
        iv_back_btn = findViewById(R.id.iv_back_btn)
        lv_list = findViewById(R.id.lv_list)
        webView = findViewById<WebView>(R.id.web_view)
        rl_add_layout = findViewById(R.id.rl_add_layout)
        tv_add_btn = findViewById(R.id.tv_add_btn)
        tv_close_btn = findViewById(R.id.tv_close_btn)
        ll_not_data = findViewById(R.id.ll_not_data)
        et_add_user = findViewById(R.id.et_add_user)

        rl_right_control = findViewById(R.id.rl_right_control)
        iv_swich_btn = findViewById(R.id.iv_swich_btn)
        iv_edit_btn = findViewById(R.id.iv_edit_btn)
        iv_add_btn = findViewById(R.id.iv_add_btn)
        iv_move_btn = findViewById(R.id.iv_move_btn)
        iv_delete_btn = findViewById(R.id.iv_delete_btn)
        iv_save_btn = findViewById(R.id.iv_save_btn)
        adapter = TestAdapter(this, R.layout.person_item, dataList, object : ItemMoveListener {
            @RequiresApi(Build.VERSION_CODES.KITKAT)
            override fun onMove(x: Float, y: Float, position: Int) {
                webView.evaluateJavascript(
                    "dragMatchSeat(${x / 2},${y / 2})",
                    ValueCallback { })
            }

            @RequiresApi(Build.VERSION_CODES.KITKAT)
            override fun onDown(x: Float, y: Float, position: Int) {
                var data: String =
                    "{'name':'${dataList[position].name}','id':'${dataList[position].id}'}"
                webView.evaluateJavascript(
                    "dragEndMatchSeat(${x / 2},${y / 2},${data})",
                    ValueCallback { })
                dataList.removeAt(position)
                adapter.notifyDataSetChanged()
            }

        })

        lv_list.setOnItemClickListener { _, _, positon, l ->
            var personEntity:PersonEntity = dataList[positon]
            if (!personEntity.isExpand && personEntity.id == 0){
                if (personEntity.data.isNotEmpty()){
                    dataList[positon].isExpand = true
                    var newData:MutableList<PersonEntity> = mutableListOf();
                    for (p in personEntity.data){
                        var json:JSONObject = JSON.parseObject(p)
                        var ej = PersonEntity()
                        ej.name = json["name"].toString()
                        ej.isExpand = false
                        ej.id = json["id"] as Int
                        ej.data = mutableListOf()
                        newData.add(ej)
                    }
                    dataList.addAll(positon + 1, newData)
                    adapter.notifyDataSetChanged()
                }
            }else{
                if (personEntity.id == 0 && personEntity.isExpand){
                    var state = dataList.size -1
                    for (index in positon+1 until dataList.size){
                        if (dataList[index].id == 0){
                            state = index -1
                            break
                        }
                    }

                    for (d in state downTo positon+1){
                        dataList.removeAt(d)
                        adapter.notifyDataSetChanged()
                    }
                    dataList[positon].isExpand = false
                }
            }
        }

        tv_add_btn.setOnClickListener {
            if (tv_add_btn.text == "新增人员"){
                tv_add_btn.text = "导入"
                tv_close_btn.text = "返回列表"

                et_add_user.visibility = View.VISIBLE
                ll_not_data.visibility = View.GONE
                et_add_user.visibility = View.VISIBLE
            }else{
                if (et_add_user.text.isNotEmpty()){
                    var data = et_add_user.text.toString().replace("\n", ",")
                    val requestBody: RequestBody = FormBody.Builder()
                        .add("attendees", data)
                        .add("meetingId", meetingData.id.toString()).build()
                    val request = Request.Builder()
                        .addHeader("token", MyApplication.getInstance()?.token)
                        .url("https://f.longjuli.com/meetingcanhui/savePerson")
                        .post(requestBody)
                        .build()
                    HttpClient.instance.httpClient?.newCall(request)?.enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            runOnUiThread(Runnable {
                                Toast.makeText(
                                    this@MeetingDetailActivity,
                                    "导入失败",
                                    Toast.LENGTH_SHORT
                                ).show()
                            })
                        }

                        override fun onResponse(call: Call, response: Response) {
                            runOnUiThread(Runnable {
                                var data = response.body()?.string()
                                var personEntity: SavePersonEntity = JSONObject.parseObject(
                                    data,
                                    SavePersonEntity::class.java
                                )
                                Toast.makeText(
                                    this@MeetingDetailActivity,
                                    personEntity.msg,
                                    Toast.LENGTH_SHORT
                                ).show()
                                if (personEntity.code == "0") {
                                    getPersonData()
                                }
                            })
                        }

                    })
                }
            }
        }

        tv_close_btn.setOnClickListener {
            if (tv_close_btn.text == "返回列表"){
                tv_add_btn.text = "新增人员"
                tv_close_btn.text = "关闭列表"

                ll_not_data.visibility = View.VISIBLE
                et_add_user.visibility = View.GONE
            }else{
                rl_right_control.visibility = View.GONE
            }
        }

        lv_list.adapter = adapter
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
        iv_add_btn.setOnClickListener {
            webView.evaluateJavascript(
                "addSeat()",
                ValueCallback { })


        }
        iv_move_btn.setOnClickListener {
            if (rl_right_control.visibility == View.VISIBLE){
                rl_right_control.visibility = View.GONE
            }else{
                rl_right_control.visibility = View.VISIBLE
            }
        }
        iv_delete_btn.setOnClickListener {
            webView.evaluateJavascript(
                "deleteSeats()",
                ValueCallback { })
        }

        iv_save_btn.setOnClickListener {
            webView.evaluateJavascript(
                "saveSeats()",
                ValueCallback { })

        }


        initWebView()
    }

    private fun initWebView(){
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
        //设置为可调用js方法
        webView.settings.javaScriptEnabled = true
        webView.addJavascriptInterface(this, "H5JsMeeting")
        webView.loadUrl("https://m.longjuli.com/meet/pad/seatmap.html?meetingid=${meetingData.id}&token=${MyApplication.getInstance()?.token}")
    }




    private fun getPersonData(){
        val request = Request.Builder()
            .url("https://m.longjuli.com/v1/attendees/pending?meeting_id=${meetingData.id}&filter_attribute=")
            .addHeader("token", MyApplication.getInstance()?.token)
            .get()
            .build()

        HttpClient.instance.httpClient?.newCall(request)?.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

            }

            override fun onResponse(call: Call, response: Response) {
                var data = response.body()?.string()
                runOnUiThread(Runnable {
                    if (data != null) {
                        addAllData(data)
                        initData()
                    }
                    adapter.notifyDataSetChanged()
                })
            }

        })
    }


    fun addAllData(content: String) {
        var response: Map<String, Any> = DensityUtil.getMapForJson(content)
        var pending: Map<String, Any> = DensityUtil.getMapForJson(response["pending"].toString())
        val iterator = pending.keys
        if (iterator.isNotEmpty()){
            showView(true)
            for (name in iterator) {
                var person = PersonEntity()
                person.name = name
                person.isExpand = false
                person.id = 0
                var persons: MutableList<String> = JSONObject.parseArray(
                    pending[name].toString(),
                    String::class.java
                )
                person.data = persons
                allData.add(person)
                if (persons!=null && persons.size>0){
                    for (p in persons){
                        var json:JSONObject = JSON.parseObject(p)
                        var ej = PersonEntity()
                        ej.name = json["name"].toString()
                        ej.isExpand = false
                        ej.id = json["id"] as Int
                        ej.data = mutableListOf()
                        allData.add(ej)
                    }
                }
            }
        }else{
            showView(false)
        }

    }

    private fun showView(isHaveData: Boolean){
        if (isHaveData){
            lv_list.visibility = View.VISIBLE
            rl_add_layout.visibility = View.GONE
        }else{
            lv_list.visibility = View.GONE
            rl_add_layout.visibility = View.VISIBLE
        }

    }

    private fun initData(){
        if (allData.size >0){
            for (p in allData){
                if (p.id == 0){
                    dataList.add(p)
                }
            }

        }
    }


    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun inPutDialog(context: Context, type: Int, data: String) {
        var builder = AlertDialog.Builder(context).create()
        //通过布局填充器获login_layout
        var view = layoutInflater.inflate(R.layout.input_dialog, null)
        //获取两个文本编辑框（密码这里不做登陆实现，仅演示）
        var name = view.findViewById<View>(R.id.message) as EditText
        var negtive = view.findViewById<View>(R.id.negtive)
        var positive = view.findViewById<View>(R.id.positive)
        builder.setView(view) //设置login_layout为对话提示框
        builder.setCancelable(false) //设置为不可取消

        if (data.isNotEmpty()){
            name.setText(data)
        }
        negtive.setOnClickListener {
            builder.dismiss()
        }
        positive.setOnClickListener {
            val name = name.text.toString().trim()
            if (name!=null &&  name.isNotEmpty()){

                runOnUiThread(Runnable {
                    webView.evaluateJavascript("promptInputDialog('${type}','${name}')",
                        ValueCallback { })
                })

                builder.dismiss()
            }
        }
        builder.show() //显示Dialog对话框
        builder.window?.setLayout(DensityUtil.dp2px(440f),DensityUtil.dp2px(230f))
    }


    private fun confirmDialog(context: Context,msg: String) {
        var view = layoutInflater.inflate(R.layout.confirm_dialog, null)
        var builder = AlertDialog.Builder(context)
            .setView(view)
            .setCancelable(false)
            .create()

        var title  = view.findViewById<TextView>(R.id.title)
        var positive = view.findViewById<Button>(R.id.positive)
        title.text = msg
        positive.setOnClickListener {
            builder.dismiss()
        }
        builder.show() //显示Dialog对话框
        builder.window?.setLayout(DensityUtil.dp2px(440f),DensityUtil.dp2px(230f))
    }


    @RequiresApi(Build.VERSION_CODES.KITKAT)
    @JavascriptInterface
    fun showInputDialog(type:Int,data:String) {
        inPutDialog(this,type,data)
    }

    @JavascriptInterface
    fun showTipsDialog(msg:String) {
        confirmDialog(this,msg)
    }

    @JavascriptInterface
    fun checkLoginState():Boolean{
        return !MyApplication.getInstance()?.token.isNullOrEmpty()
    }

    override fun onDestroy() {
        webView.clearCache(true)
        super.onDestroy()
    }

}