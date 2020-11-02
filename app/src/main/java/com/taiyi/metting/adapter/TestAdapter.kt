package com.taiyi.metting.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import com.taiyi.metting.R
import com.taiyi.metting.entity.PersonEntity
import com.taiyi.metting.view.WaterDrop

class TestAdapter(context: Context, layoutRes: Int, mData: List<PersonEntity>, listener: ItemMoveListener):BaseAdapter() {
    var data:List<PersonEntity> = mData
    var context:Context = context
    var layoutRes:Int = layoutRes
    var listener:ItemMoveListener = listener

    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(p0: Int): Any {
        return data[p0]
    }

    override fun getItemId(p0: Int): Long {
        return  0
    }

    override fun getView(position: Int, p1: View?, p2: ViewGroup?): View {
        var view = View.inflate(context, layoutRes,null)

        if (data[position].id == 0){
            view = View.inflate(context, R.layout.parent_item,null)
            var icon = view.findViewById<ImageView>(R.id.hand_icon)
            var name: TextView = view.findViewById(R.id.person_name)
            name.text = data[position].name +"(${data[position].data.size})"
            if (data[position].isExpand){
                icon.setImageResource(R.mipmap.dwon_arrow)
            }else{
                icon.setImageResource(R.mipmap.up_arrow)
            }

        }else{
            var name: WaterDrop = view.findViewById(R.id.person_name)
            name.setText(data[position].name)

            name.setOnDragCompeteListener { x, y ->
                listener.onDown(x,y,position)
            }
            name.setOnDragChangeListener { x, y ->
                listener.onMove(x,y,position)
            }
        }



        return view
    }

    interface ItemMoveListener{
        fun onMove(x:Float,y:Float,position: Int)
        fun onDown(x:Float,y:Float,position:Int)
    }
}