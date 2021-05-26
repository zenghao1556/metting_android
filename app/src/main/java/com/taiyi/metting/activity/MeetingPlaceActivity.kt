package com.taiyi.metting.activity

import android.content.Intent
import android.os.Bundle
import android.widget.*
import com.alibaba.fastjson.JSONObject
import com.taiyi.metting.MyApplication
import com.taiyi.metting.R
import com.taiyi.metting.adapter.MeetingListAdapter
import com.taiyi.metting.base.ConstUtils
import com.taiyi.metting.entity.MeetingListResponse
import com.taiyi.metting.http.HttpClient
import com.taiyi.metting.utils.PullToRefreshView
import com.taiyi.metting.utils.SPUtils
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class MeetingPlaceActivity : BaseActivity(),PullToRefreshView.OnFooterRefreshListener, PullToRefreshView.OnHeaderRefreshListener {

    lateinit var rtr_view: PullToRefreshView
    lateinit var lv_metting_list: ListView
    lateinit var et_content:EditText
    lateinit var tv_search_btn:TextView
    lateinit var iv_back_btn:ImageView

    private var currentPage:Int = 1

    lateinit var adapter:MeetingListAdapter

    private var dataList:MutableList<MeetingListResponse.MeetingEntity> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meeting_place)

        tv_search_btn = findViewById(R.id.tv_search_btn)
        rtr_view = findViewById(R.id.rtr_view)
        lv_metting_list = findViewById(R.id.lv_metting_list)
        iv_back_btn = findViewById(R.id.iv_back_btn)
        et_content = findViewById(R.id.et_content)
        rtr_view.setOnFooterRefreshListener(this)
        rtr_view.setOnHeaderRefreshListener(this)
        adapter = MeetingListAdapter(this, R.layout.meeting_item, dataList)
        lv_metting_list.adapter = adapter

        tv_search_btn.setOnClickListener {
            dataList.clear()
            adapter.notifyDataSetChanged()
            getData(et_content.text.toString())
        }

        lv_metting_list.setOnItemClickListener { _, _, position, _ ->
            var intent = Intent(this, MeetingDetailActivity::class.java)
            intent.putExtra("data", dataList[position])
            startActivity(intent)
        }

        iv_back_btn.setOnClickListener { finish() }
        getData("")
    }


    private fun getData(meeting: String){
        val request = Request.Builder()
            .url("https://f.longjuli.com/meeting/meetingSearch?meeting=$meeting&limit=10&page=$currentPage")
            .addHeader("token", MyApplication.getInstance()?.token)
            .get()
            .build()

        HttpClient.instance.httpClient?.newCall(request)?.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

            }

            override fun onResponse(call: Call, response: Response) {
                when {
                    response.code() == 200 -> {
                        var data = response.body()?.string()
                        var response: MeetingListResponse = JSONObject.parseObject(
                            data,
                            MeetingListResponse::class.java
                        )
                        if (response.code == 0) {
                            runOnUiThread(Runnable {
                                dataList.addAll(response.data)
                                adapter.notifyDataSetChanged()
                                rtr_view.onFooterRefreshComplete()
                                rtr_view.onHeaderRefreshComplete()
                            })
                        } else if (response.code == 300) {
                            runOnUiThread(Runnable {
                                SPUtils.setStringPreferences(
                                    this@MeetingPlaceActivity,
                                    SPUtils.PROJECT,
                                    ConstUtils.USER_KEY,
                                    ""
                                )
                                Toast.makeText(
                                    this@MeetingPlaceActivity,
                                    "登录过期，请重新登录",
                                    Toast.LENGTH_SHORT
                                ).show()

                                val intent = Intent(
                                    this@MeetingPlaceActivity,
                                    LoginActivity::class.java
                                ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)

                                startActivity(intent)
                            })
                        }

                    }
                    else -> {
                        runOnUiThread(Runnable {
                            Toast.makeText(
                                this@MeetingPlaceActivity,
                                "服务异常,请稍后重试",
                                Toast.LENGTH_SHORT
                            ).show()
                        })

                    }
                }


            }

        })
    }

    override fun onFooterRefresh(view: PullToRefreshView?) {
        currentPage++
        getData(et_content.text.toString())
    }

    override fun onHeaderRefresh(view: PullToRefreshView?) {
        currentPage = 1
        dataList.clear()
        adapter.notifyDataSetChanged()
        getData(et_content.text.toString())
    }
}