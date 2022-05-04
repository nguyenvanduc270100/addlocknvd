package com.lgi.applock.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lgi.applock.R
import com.lgi.applock.data.model.DBAppLock
import com.lgi.applock.databinding.HolderItemAppLockBinding
import com.lgi.applock.models.TypeActionUpdate
import com.lgi.applock.models.UpdateEvent
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import org.greenrobot.eventbus.EventBus


class AppLockAdapter(
    private val context: Context,
    private val listener: ItemListener
) :
    RecyclerView.Adapter<AppLockAdapter.ListApplicationViewHolder>() {

    private var listAppLock:List<DBAppLock> = ArrayList()

    fun setData(list: ArrayList<DBAppLock>){
        list.sortBy { it.name }
        this.listAppLock = list
        notifyDataSetChanged()
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
                val appLock = listAppLock[adapterPosition]
                appLock.isLock = if (appLock.isLock == 0) 1 else 0
                bindData(appLock.isLock)
                listener.onClickLock(adapterPosition, appLock)
                EventBus.getDefault().post(UpdateEvent(TypeActionUpdate.Lock, appLock, false))
            }
            bindData(listAppLock[adapterPosition].isLock)

            binding.icFavorite.setOnClickListener {
                val appLock = listAppLock[adapterPosition]
                appLock.isFavorite = if (appLock.isFavorite == 0) 1 else 0
                bindDataFavorite(appLock.isFavorite)
                listener.onClickFavorite(adapterPosition, appLock)
                EventBus.getDefault().post(UpdateEvent(TypeActionUpdate.Favorite, appLock, false))
            }
            bindDataFavorite(listAppLock[adapterPosition].isFavorite)

            Glide
                .with(context)
                .load(context.packageManager.getApplicationIcon(listAppLock[adapterPosition].packagename))
                .diskCacheStrategy(DiskCacheStrategy.ALL).thumbnail(0.5f)
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