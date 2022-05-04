package com.bienlongtuan.applocker.adapters

import android.content.Context
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
import com.bienlongtuan.applocker.data.model.DBAppLock
import com.bienlongtuan.applocker.databinding.HolderItemAppLockBinding
import com.bienlongtuan.applocker.models.AppLockModel
import com.bienlongtuan.applocker.models.UpdateEvent
import com.bienlongtuan.applocker.utils.Constants
import com.bienlongtuan.applocker.utils.TabLayoutEnum
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.greenrobot.eventbus.EventBus


class AppLockAdapter(
    private val context: Context,
    private val listener: ItemListener
) :
    RecyclerView.Adapter<AppLockAdapter.ListApplicationViewHolder>() {

    private var listAppLock:List<DBAppLock> = ArrayList()

    fun setData(list: ArrayList<DBAppLock>){
        this.listAppLock = list
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListApplicationViewHolder {
        val view = HolderItemAppLockBinding.bind(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.holder_item_app_lock, parent, false)
        )
        return ListApplicationViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListApplicationViewHolder, position: Int) {
        holder.bindView()
    }

    override fun onBindViewHolder(
        holder: ListApplicationViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty() && payloads[0] is Int) {
            holder.bindData(payloads[0] as Int)
        } else
            super.onBindViewHolder(holder, position, payloads)
    }

    override fun getItemCount(): Int {
        return listAppLock.size
    }


    inner class ListApplicationViewHolder(private val binding: HolderItemAppLockBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindView() {
            binding.tvName.text = listAppLock[adapterPosition].name
            binding.tvDescribe.text = listAppLock[adapterPosition].packagename
            binding.icLock.setOnClickListener {
                listener.onClickLock(adapterPosition, listAppLock[adapterPosition])
                notifyItemChanged(adapterPosition, listAppLock[adapterPosition])
            }
            bindData(listAppLock[adapterPosition].isLock)
            binding.icFavorite.setOnClickListener {
                listener.onClickFavorite(adapterPosition, listAppLock[adapterPosition])
                notifyItemChanged(adapterPosition, listAppLock[adapterPosition])
                EventBus.getDefault().post(UpdateEvent(true))
            }
            bindDataFavorite(listAppLock[adapterPosition].isFavorite)
            Glide
                .with(context)
                .asDrawable()
                .load(context.packageManager.getApplicationIcon(listAppLock[adapterPosition].packagename))
                .thumbnail(0.5f)
                .into(binding.icApp)
        }

        fun bindData(locked: Int) {
            val iconLock = if (locked == 1) {
                R.drawable.ic_lock_app
            } else {
                R.drawable.ic_un_lock_app
            }

            Glide
                .with(context)
                .load(iconLock)
                .thumbnail(0.5f)
                .into(binding.icLock)
        }

        private fun bindDataFavorite(isFavorite: Int) {
            val iconFavorite = if (isFavorite == 1) {
                R.drawable.ic_favorite_selected
            } else {
                R.drawable.ic_favorite
            }

            Glide
                .with(context)
                .load(iconFavorite)
                .thumbnail(0.5f)
                .into(binding.icFavorite)
        }
    }


    interface ItemListener {
        fun onClickLock(position: Int, packageApp: DBAppLock)
        fun onClickFavorite(position: Int, packageApp: DBAppLock)
    }
}