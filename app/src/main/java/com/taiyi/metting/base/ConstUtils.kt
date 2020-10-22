package com.taiyi.metting.base

/**
 * Created by zenghao on 2020/7/11.
 */
object ConstUtils {
    const val USER_KEY = "user"
    const val TAG_FEATURE_POSITION = 0
    const val URI_HIDE_TITLE = "uriHideTitle"
    const val URI_SHOW_TITLE = "uriShowTitle"
    fun getGapTime(duration: Long): String {
        var time = ""
        val minute = duration / 60000
        val seconds = duration % 60000
        val second = Math.round(seconds.toFloat() / 1000).toLong()
        if (minute < 10) {
            time += "0"
        }
        time += "$minute:"
        if (second < 10) {
            time += "0"
        }
        time += second
        return time
    }
}