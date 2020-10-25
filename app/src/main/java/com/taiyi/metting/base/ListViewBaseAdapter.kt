package com.taiyi.metting.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter


/**
 * Created by zenghao on 2020/8/25.
 */
abstract class ListViewBaseAdapter<T>(private var context: Context, private var datas: List<T>, private var layoutId: Int) : BaseAdapter() {

    protected var mContext: Context = context
    var mDatas: List<T> = datas
    protected var mInflater: LayoutInflater = LayoutInflater.from(mContext)
    private var mLayoutId: Int = layoutId

    override fun getCount(): Int {
        return if(mDatas!=null && mDatas.isNotEmpty()){
            mDatas.size
        }else{
            0
        }
    }

    override fun getItem(position: Int): T {
        return mDatas[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var holder: ViewHolder = ViewHolder.get(mContext, convertView, parent, mLayoutId, position)
        convert(holder, getItem(position), position)
        return holder.convertView //这一行的代码要注意了
    }

    //将convert方法公布出去
    abstract fun convert(holder: ViewHolder?, t: T, position: Int)


}