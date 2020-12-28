package com.example.myandroidproject

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class HistoryAdapter(activity: HistoryActivity, val resourceId:Int, data:List<History>):
ArrayAdapter<History>(activity,resourceId,data){
    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view=LayoutInflater.from(context).inflate(resourceId,parent,false)
        val historyname: TextView =view.findViewById(R.id.historyname)
        val history=getItem(position)//获取当前项的History实例
        if(history!=null) {
            historyname.text=history.his
        }
        return view
    }
}