package com.taiyi.metting.adapter

import android.content.Context
import android.widget.TextView
import com.taiyi.metting.R
import com.taiyi.metting.base.ListViewBaseAdapter
import com.taiyi.metting.base.ViewHolder
import com.taiyi.metting.entity.MeetingListResponse

class MeetingListAdapter(context: Context, layoutRes: Int, mData: List<MeetingListResponse.MeetingEntity>):ListViewBaseAdapter<MeetingListResponse.MeetingEntity>(context,mData,layoutRes) {
    override fun convert(holder: ViewHolder?, t: MeetingListResponse.MeetingEntity, position: Int) {
        var meeting_name: TextView? = holder!!.getView<TextView>(R.id.meeting_name)
        var location:TextView? = holder!!.getView(R.id.tv_location)
        var startTime:TextView? =  holder!!.getView(R.id.tv_start_time)
        var endTime:TextView?= holder!!.getView(R.id.tv_end_time)

        meeting_name?.text = t.name
        location?.text = t.address
        startTime?.text = t.meetingtime
    }
}
