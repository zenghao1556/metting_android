package com.taiyi.metting.activity

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
import com.taiyi.metting.http.HttpClient
import com.taiyi.metting.utils.DensityUtil
import com.taiyi.metting.view.CoverManager
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
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

    lateinit var adapter: TestAdapter
    private var dataList:MutableList<PersonEntity> = mutableListOf()

    private var allData:MutableList<PersonEntity> = mutableListOf()

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


    private fun initView(){
        iv_back_btn = findViewById(R.id.iv_back_btn)
        lv_list = findViewById(R.id.lv_list)
        webView = findViewById<WebView>(R.id.web_view)
        rl_add_layout = findViewById(R.id.rl_add_layout)
        tv_add_btn = findViewById(R.id.tv_add_btn)
        tv_close_btn = findViewById(R.id.tv_close_btn)
        ll_not_data = findViewById(R.id.ll_not_data)
        et_add_user = findViewById(R.id.et_add_user)
        adapter = TestAdapter(this, R.layout.person_item, dataList, object : ItemMoveListener {
            @RequiresApi(Build.VERSION_CODES.KITKAT)
            override fun onMove(x: Float, y: Float, position: Int) {
                webView.evaluateJavascript(
                    "dragMatchSeat(${x / 2},${y / 2})",
                    ValueCallback { })
            }

            @RequiresApi(Build.VERSION_CODES.KITKAT)
            override fun onDown(x: Float, y: Float, position: Int) {
                var data: String = "{'name':'${dataList[position].name}','id':'${dataList[position].id}'}"
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
                    dataList.addAll(positon+1,newData)
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

                ll_not_data.visibility = View.GONE
                et_add_user.visibility = View.VISIBLE
            }else{

            }
        }

        tv_close_btn.setOnClickListener {
            if (tv_close_btn.text == "返回列表"){
                tv_add_btn.text = "新增人员"
                tv_close_btn.text = "关闭列表"

                ll_not_data.visibility = View.VISIBLE
                et_add_user.visibility = View.GONE
            }
        }

        lv_list.adapter = adapter
        iv_back_btn.setOnClickListener { finish() }
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


    fun addAllData(content:String) {
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
                var persons: MutableList<String> = JSONObject.parseArray(pending[name].toString(), String::class.java)
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

    private fun showView(isHaveData:Boolean){
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

}