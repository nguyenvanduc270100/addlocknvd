package com.bienlongtuan.applocker.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.bienlongtuan.applocker.R
import com.bienlongtuan.applocker.models.AppLockModel

class AppLockAdapter(private val listAppLock: List<AppLockModel>) :
    RecyclerView.Adapter<AppLockAdapter.ListApplicationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListApplicationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.holder_item_app_lock, parent, false)
        return ListApplicationViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListApplicationViewHolder, position: Int) {
        holder.bindView(listAppLock[position])
    }

    override fun getItemCount(): Int {
        return listAppLock.size
    }


    class ListApplicationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvNameApp: AppCompatTextView = itemView.findViewById(R.id.tvName)
        private val tvDescribe: AppCompatTextView = itemView.findViewById(R.id.tvDescribe)

        fun bindView(appLockModel: AppLockModel) {
            with(appLockModel) {
                tvNameApp.text = appLockModel.name
                tvDescribe.text = appLockModel.describe
            }

        }
    }
}