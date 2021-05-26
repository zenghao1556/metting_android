package com.taiyi.metting.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.*
import com.alibaba.fastjson.JSONObject
import com.taiyi.metting.MyApplication
import com.taiyi.metting.R
import com.taiyi.metting.adapter.TableCardListAdapter
import com.taiyi.metting.base.ConstUtils
import com.taiyi.metting.entity.LoginEntity
import com.taiyi.metting.entity.MeetingListResponse
import com.taiyi.metting.entity.TableCardReponse
import com.taiyi.metting.http.HttpClient
import com.taiyi.metting.utils.DensityUtil
import com.taiyi.metting.utils.PullToRefreshView
import com.taiyi.metting.utils.SPUtils
import okhttp3.*
import java.io.IOException


class TableCardActivity : BaseActivity(),PullToRefreshView.OnFooterRefreshListener, PullToRefreshView.OnHeaderRefreshListener {

    lateinit var rtr_view: PullToRefreshView
    lateinit var lv_metting_list: ListView
    lateinit var et_content:EditText
    lateinit var tv_search_btn:TextView
    lateinit var iv_back_btn:ImageView

    private var currentPage:Int = 1

    lateinit var adapter:TableCardListAdapter

    private var dataList:MutableList<TableCardReponse.TableCardInfo> = mutableListOf()

    private var meetingList = arrayListOf<String>()
    private var meetingListObject = mutableListOf<MeetingListResponse.MeetingEntity>()
    private var spinnerAdapter: ArrayAdapter<String>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_table_card)

        tv_search_btn = findViewById(R.id.tv_search_btn)
        rtr_view = findViewById(R.id.rtr_view)
        lv_metting_list = findViewById(R.id.lv_metting_list)
        iv_back_btn = findViewById(R.id.iv_back_btn)
        et_content = findViewById(R.id.et_content)
        rtr_view.setOnFooterRefreshListener(this)
        rtr_view.setOnHeaderRefreshListener(this)
        adapter = TableCardListAdapter(
            this,
            R.layout.table_card_item,
            dataList,
            object : TableCardListAdapter.SelectMeetingClickLisener {
                override fun selectMeeting(id: String) {
                    getMeetingList(id)
                }

            })
        lv_metting_list.adapter = adapter

        tv_search_btn.setOnClickListener {
            dataList.clear()
            adapter.notifyDataSetChanged()
            getData(et_content.text.toString())
        }

        lv_metting_list.setOnItemClickListener { _, _, position, _ ->
            var intent = Intent(this, MeetingOperateActivity::class.java)
            intent.putExtra("data", dataList[position])
            startActivity(intent)
        }

        iv_back_btn.setOnClickListener { finish() }
        getData("")
    }


    private fun getData(meeting: String){

        val request = Request.Builder()
            .url("https://f.longjuli.com/roomtemplate/findRoomTemplateByPad?limit=7&page=$currentPage&room=${meeting}")
            .addHeader("token", MyApplication.getInstance()?.token)
            .get()
            .build()

        HttpClient.instance.httpClient?.newCall(request)?.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: Response) {
                var data = response.body()?.string()

                var response: TableCardReponse = JSONObject.parseObject(
                    data,
                    TableCardReponse::class.java
                )

                runOnUiThread(Runnable {
                    if (response.code == 0){
                        dataList.addAll(response.data)
                        adapter.notifyDataSetChanged()
                        rtr_view.onFooterRefreshComplete()
                        rtr_view.onHeaderRefreshComplete()
                    }else{
                        Toast.makeText(
                            this@TableCardActivity,
                            response.msg,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                })

            }

        })
    }

    private fun getMeetingList(roomid: String){

        val request = Request.Builder()
            .url("https://f.longjuli.com/meeting/findRoomByMeeting?roomid=$roomid")
            .addHeader("token", MyApplication.getInstance()?.token)
            .get()
            .build()

        HttpClient.instance.httpClient?.newCall(request)?.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

            }

            override fun onResponse(call: Call, response: Response) {
                if (response.code() == 200){

                }
                var data = response.body()?.string()
                var response: MeetingListResponse = JSONObject.parseObject(
                    data,
                    MeetingListResponse::class.java
                )

                if (response.code  == 0){
                    if (response.data.size>0){
                        meetingList.clear()
                        for (data in response.data){
                            meetingList.add(data.meetingtime)
                        }
                        meetingListObject.addAll(response.data)
                    }



                    if (meetingList.size>0){

                        runOnUiThread(Runnable {
                            kotlin.run {
                                selectMeetingDialog(this@TableCardActivity,"选择会议")
                            }
                        })


                    }
                }else if (response.code == 300) {
                    runOnUiThread(Runnable {
                        Toast.makeText(
                            this@TableCardActivity,
                            "登录过期，请重新登录",
                            Toast.LENGTH_SHORT
                        ).show()
                        SPUtils.setStringPreferences(
                            this@TableCardActivity,
                            SPUtils.PROJECT,
                            ConstUtils.USER_KEY,
                            ""
                        )
                        val intent = Intent(
                            this@TableCardActivity,
                            LoginActivity::class.java
                        ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)

                        startActivity(intent)
                    })
                }



            }

        })
    }


    private fun selectMeetingDialog(context: Context, msg: String) {
        spinnerAdapter = ArrayAdapter(context,R.layout.support_simple_spinner_dropdown_item,meetingList)
        var view = layoutInflater.inflate(R.layout.select_meeting_dialog, null)
        var builder = AlertDialog.Builder(context)
            .setView(view)
            .setCancelable(false)
            .create()

        var title  = view.findViewById<TextView>(R.id.title)
        var positive = view.findViewById<Button>(R.id.positive)
        var cancle = view.findViewById<Button>(R.id.cancle)
        var spinner = view.findViewById<Spinner>(R.id.spinner)
        spinner.adapter = spinnerAdapter
        title.text = msg
        positive.setOnClickListener {
            builder.dismiss()
        }
        cancle.setOnClickListener {
            builder.dismiss()
        }
        builder.show() //显示Dialog对话框
        builder.window?.setLayout(DensityUtil.dp2px(440f), DensityUtil.dp2px(360f))
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

    private fun updateMeeting(roomid:String,meetingId:String){
        val requestBody: RequestBody = FormBody.Builder()
            .add("presentmeeting", meetingId)
            .add("id", roomid).build()
        val request = Request.Builder()
            .url("https://f.longjuli.com/roomtemplate/updateRoomtemplate")
            .post(requestBody)
            .build()
        HttpClient.instance.httpClient?.newCall(request)?.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread(Runnable {
                    Toast.makeText(this@TableCardActivity, "修改失败", Toast.LENGTH_SHORT)
                        .show()
                })
            }

            override fun onResponse(call: Call, response: Response) {
                runOnUiThread(Runnable {
                    var data = response.body()?.string()
                    var loginEntity: LoginEntity = JSONObject.parseObject(data, LoginEntity::class.java)
                    if (loginEntity.code == "0"){
                        MyApplication.getInstance()?.token = loginEntity.token
                        startActivity(Intent(this@TableCardActivity, MainActivity::class.java))
                    }else{
                        Toast.makeText(this@TableCardActivity, loginEntity.msg, Toast.LENGTH_SHORT).show()
                    }
                })
            }

        })
    }
}