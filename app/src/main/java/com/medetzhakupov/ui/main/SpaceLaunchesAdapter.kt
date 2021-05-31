package com.medetzhakupov.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.medetzhakupov.R
import com.medetzhakupov.data.model.SpaceLaunch
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.space_launch_item.view.image
import kotlinx.android.synthetic.main.space_launch_item.view.location_name
import kotlinx.android.synthetic.main.space_launch_item.view.title

class SpaceLaunchesAdapter(private val onSpaceLaunchSelected: (SpaceLaunch) -> Unit) :
    ListAdapter<SpaceLaunch, SpaceLaunchesAdapter.VH>(ResultDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.space_launch_item, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bindTo(getItem(position)) { onSpaceLaunchSelected.invoke(it) }
    }

    class VH(view: View) : RecyclerView.ViewHolder(view) {

        private val title: TextView = view.title
        private val locationName: TextView = view.location_name
        private val image: ImageView = view.image

        fun bindTo(spaceLaunch: SpaceLaunch, clickListener: (SpaceLaunch) -> Unit) {
            Picasso.get().load(spaceLaunch.image).into(image)
            title.text = spaceLaunch.name
            locationName.text = spaceLaunch.pad.location.name
            itemView.setOnClickListener { clickListener.invoke(spaceLaunch) }
        }
    }
}

class ResultDiff : DiffUtil.ItemCallback<SpaceLaunch>() {
    override fun areItemsTheSame(oldItem: SpaceLaunch, newItem: SpaceLaunch): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: SpaceLaunch, newItem: SpaceLaunch): Boolean {
        return oldItem == newItem
    }
}