package com.example.speechrecogner

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter(
    var context: Context,
    mTextList: ArrayList<FirebaseData>) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    private var mTextList: ArrayList<FirebaseData> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return mTextList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user: FirebaseData = mTextList[position]
        holder.mTime.text = user.time
        holder.mMessage.text = user.message
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var mTime: TextView = v.findViewById(R.id.timeStamp)
        var mMessage: TextView = v.findViewById(R.id.historyItem)
    }
    init {
        this.mTextList = mTextList
    }
}