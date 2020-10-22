package com.taiyi.metting.utils

import android.util.Log

/**
 * Log utilities that can control whether print the log.
 */
object LogUtil {
    private const val LOG_LEVEL = 6
    private const val VERBOSE = 5
    private const val DEBUG = 4
    private const val INFO = 3
    private const val WARN = 2
    private const val ERROR = 1
    fun v(tag: String?, msg: String?) {
        if (LOG_LEVEL > VERBOSE) {
            Log.v(tag, msg!!)
        }
    }

    fun d(tag: String?, msg: String?) {
        if (LOG_LEVEL > DEBUG) {
            Log.d(tag, msg!!)
        }
    }

    fun i(tag: String?, msg: String?) {
        if (LOG_LEVEL > INFO) {
            Log.i(tag, msg!!)
        }
    }

    fun w(tag: String?, msg: String?) {
        if (LOG_LEVEL > WARN) {
            Log.w(tag, msg!!)
        }
    }

    fun e(tag: String?, msg: String?) {
        if (LOG_LEVEL > ERROR) {
            Log.e(tag, msg!!)
        }
    }
}