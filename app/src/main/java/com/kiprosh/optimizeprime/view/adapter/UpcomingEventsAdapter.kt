package com.kiprosh.optimizeprime.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.kiprosh.optimizeprime.R
import com.kiprosh.optimizeprime.dummy.DummyContent.DummyItem

class UpcomingEventsAdapter(
    private val values: List<DummyItem>, private val actionListener: ActionListener
) : RecyclerView.Adapter<UpcomingEventsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_upcoming_event_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.llMain.setOnClickListener { actionListener.onItemClick() }
        if (position == 0){
            holder.ivLock.visibility = INVISIBLE
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val llMain: LinearLayout = view.findViewById(R.id.ll_main)
        val ivLock: AppCompatImageView = view.findViewById(R.id.iv_lock)

        override fun toString(): String {
            return ""
        }
    }

    interface ActionListener {
        fun onItemClick()
    }
}