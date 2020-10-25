package com.taiyi.metting.entity

import java.io.Serializable

data class MeetingResponse(
    var code: Int,
    var count: Int,
    var data: List<Data>,
    var msg: String,
    var result: Boolean
):Serializable{constructor():this(0,0, mutableListOf(),"",false)}

data class Data(
    var address: String,
    var delstate: Any,
    var deltime: Any,
    var id: Int,
    var iscard: Any,
    var ischeck: Any,
    var isface: Any,
    var isgrouplist: Any,
    var isstay: Any,
    var isvote: Any,
    var meetingtime: String,
    var memo: String,
    var modifytime: Any,
    var name: String,
    var reciverlistid: Any,
    var roomid: Int,
    var roomname: String,
    var ruleid: Any
)