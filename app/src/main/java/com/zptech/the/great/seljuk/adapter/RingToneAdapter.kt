package com.zptech.the.great.seljuk.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.zptech.the.great.seljuk.R
import com.zptech.the.great.seljuk.databinding.ItemPlayerBinding
import com.zptech.the.great.seljuk.isPermissionGranted
import com.zptech.the.great.seljuk.model.RingTone
import com.zptech.the.great.seljuk.requestPermission
import com.zptech.the.great.seljuk.ui.BottomSheetFragment
import com.zptech.the.great.seljuk.ui.DetailPlayerActivity
import com.zptech.the.great.seljuk.ui.MainActivity

class RingToneAdapter(var list: List<RingTone>, val mainActivity: MainActivity) :
    RecyclerView.Adapter<RingToneAdapter.Holder>() {

    var currentPosition: Int = 0


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {

        val binding: ItemPlayerBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_player, parent, false
        )
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(list[position], position)

    }

    fun setcurrent(position: Int) {
        currentPosition = position

    }

    inner class Holder(private val binding: ItemPlayerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.holder = this
        }

        fun bind(ringtone: RingTone, position: Int) {
            with(binding) {
                binding.model = ringtone
                binding.executePendingBindings()

            }
        }

        fun onClick(view: View, ringtone: RingTone) {
            mainActivity.startActivity(
                Intent(
                    mainActivity,
                    DetailPlayerActivity::class.java
                ).putExtra("position", adapterPosition)
            )
            mainActivity.showAd();
        }

        fun onOptionClick(view: View, ringtone: RingTone) {
                val bottomSheetFragment = BottomSheetFragment(ringtone)
                bottomSheetFragment.show(mainActivity.supportFragmentManager, "dialog")
        }
    }
}