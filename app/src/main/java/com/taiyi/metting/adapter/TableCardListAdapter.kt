package com.taiyi.metting.adapter

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import com.taiyi.metting.R
import com.taiyi.metting.base.ListViewBaseAdapter
import com.taiyi.metting.base.ViewHolder
import com.taiyi.metting.entity.MeetingListResponse
import com.taiyi.metting.entity.TableCardReponse

class TableCardListAdapter(context: Context, layoutRes: Int, mData: List<TableCardReponse.TableCardInfo>, listener: SelectMeetingClickLisener):ListViewBaseAdapter<TableCardReponse.TableCardInfo>(context,mData,layoutRes) {
    private var listener:SelectMeetingClickLisener = listener
    override fun convert(holder: ViewHolder?, t: TableCardReponse.TableCardInfo, position: Int) {
        var meeting_room_name: TextView? = holder!!.getView<TextView>(R.id.meeting_room_name)
        var meeting_name:TextView? = holder!!.getView(R.id.meeting_name)
        var tv_choonse_btn:TextView? =  holder!!.getView(R.id.tv_choonse_btn)
        var right_icon:ImageView?= holder!!.getView(R.id.right_icon)

        meeting_room_name?.text = t.name
        tv_choonse_btn?.setOnClickListener { listener.selectMeeting(t.id.toString()) }
    }

    interface SelectMeetingClickLisener{
        fun selectMeeting(id:String)
    }
}
