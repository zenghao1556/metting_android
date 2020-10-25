package com.taiyi.metting.activity

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.taiyi.metting.R
import com.taiyi.metting.entity.MeetingListResponse

class MeetingDetailActivity : BaseActivity() {
    lateinit var meetingData:MeetingListResponse.MeetingEntity
    lateinit var meeting_name:TextView
    lateinit var iv_back_btn:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meeting_detail)
        meeting_name = findViewById(R.id.meeting_name)
        iv_back_btn = findViewById(R.id.iv_back_btn)
        meetingData= intent.getSerializableExtra("data") as MeetingListResponse.MeetingEntity
        meeting_name.text = meetingData.name

        iv_back_btn.setOnClickListener { finish() }
    }
}