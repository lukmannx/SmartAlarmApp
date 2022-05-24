package com.lukman.smartalarm.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lukman.smartalarm.databinding.ItemRowReminderAlarmBinding
import com.lukman.smartalarm.data.Alarm

class AlarmAdapter() : RecyclerView.Adapter<AlarmAdapter.MyViewHolder>() {

     val listAlarm: ArrayList<Alarm> = arrayListOf()

    class MyViewHolder(val binding: ItemRowReminderAlarmBinding) : RecyclerView.ViewHolder(binding.root) //untuk inisalisasi layout yg mau dipakai

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MyViewHolder(
        ItemRowReminderAlarmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val alarm = listAlarm[position]
        holder.binding.apply{
            itemDateAlarm.text = alarm.date
            itemTimeAlarm.text = alarm.date
            itemNoteAlarm.text = alarm.date
        }
    }

    override fun getItemCount() = listAlarm.size

    //TODO 2 -> Perbarui code dari github
    fun setData(list : List<Alarm>){
        listAlarm.clear()
        listAlarm.addAll(list)
    }
}