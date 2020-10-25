package com.taiyi.metting.base

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


/**
 * Created by zenghao on 2020/8/25.
 */
class ViewHolder(context: Context?, parent: ViewGroup?, layoutId: Int, private var mPosition: Int) {
    private val mViews: SparseArray<View> = SparseArray()
    val convertView: View = LayoutInflater.from(context).inflate(layoutId, parent, false)

    /*
    通过viewId获取控件
     */
    //使用的是泛型T,返回的是View的子类
    fun <T : View?> getView(viewId: Int): T? {
        var view = mViews[viewId]
        if (view == null) {
            view = convertView.findViewById(viewId)
            mViews.put(viewId, view)
        }
        return view as T?
    }

    companion object {
         fun get(context: Context?, convertView: View?, parent: ViewGroup?, layoutId: Int, position: Int): ViewHolder {
            return if (convertView == null) {
                ViewHolder(context, parent, layoutId, position)
            } else {
                val holder = convertView.tag as ViewHolder
                holder.mPosition = position //即使ViewHolder是复用的，但是position记得更新一下
                holder
            }
        }
    }

    init {
        convertView.tag = this
    }
}