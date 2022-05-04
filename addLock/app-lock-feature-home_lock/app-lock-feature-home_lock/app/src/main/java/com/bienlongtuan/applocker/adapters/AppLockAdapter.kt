package com.bienlongtuan.applocker.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.bienlongtuan.applocker.R
import com.bienlongtuan.applocker.models.AppLockModel
import com.bienlongtuan.applocker.utils.Constants
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken




class AppLockAdapter(private val context: Context, private val onListener: OnClickListener) :
    RecyclerView.Adapter<AppLockAdapter.ListApplicationViewHolder>() {

    private var listAppLock:List<AppLockModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListApplicationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.holder_item_app_lock, parent, false)
        return ListApplicationViewHolder(view)
    }

     fun setData(list: ArrayList<AppLockModel>){
        this.listAppLock = list
    }

    override fun onBindViewHolder(holder: ListApplicationViewHolder, position: Int) {
        val sharedPreference =  context.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE)
        var listFavorite: ArrayList<AppLockModel> = ArrayList()
        val stringListFavorite = sharedPreference.getString(Constants.LIST_FAVORITE, "")
        if(stringListFavorite?.isEmpty() == false) listFavorite = Gson().fromJson(
            stringListFavorite,
            object : TypeToken<List<AppLockModel?>?>() {}.type
        )

        if(listAppLock[position].isFavorite) holder.btnFavorite.setImageResource(R.drawable.ic_favorite_selected)
            else holder.btnFavorite.setImageResource(R.drawable.ic_favorite)

        if(listAppLock[position].isLock) holder.btnLock.setImageResource(R.drawable.ic_lock_selected)
        else holder.btnLock.setImageResource(R.drawable.ic_lock)

        holder.bindView(listAppLock[position])
        holder.btnLock.setOnClickListener{
            listAppLock[position].isLock = !listAppLock[position].isLock
            onListener.onClickLock(position, listAppLock[position].isLock)
        }
        holder.btnFavorite.setOnClickListener {
            listAppLock[position].isFavorite = !listAppLock[position].isFavorite
            onListener.onClickFavorite(position, listAppLock[position].isFavorite)
            if(listAppLock[position].isFavorite) listFavorite.add(listAppLock[position])
                else {
                    if(listFavorite.size > 0){
                        for (i in listFavorite.indices){
                            if(listFavorite[i].name == listAppLock[position].name){
                                listFavorite.removeAt(i)
                                break
                            }
                        }
                    }
                }

            sharedPreference.edit().putString(Constants.LIST_FAVORITE, Gson().toJson(listFavorite)).apply()
        }
    }

    override fun getItemCount(): Int {
        return listAppLock.size
    }


    class ListApplicationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvNameApp: AppCompatTextView = itemView.findViewById(R.id.tvName)
        private val tvDescribe: AppCompatTextView = itemView.findViewById(R.id.tvDescribe)
        val btnFavorite: AppCompatImageView = itemView.findViewById(R.id.icFavorite)
        val btnLock: AppCompatImageView = itemView.findViewById(R.id.icLock)

        fun bindView(appLockModel: AppLockModel) {
            with(appLockModel) {
                tvNameApp.text = appLockModel.name
                tvDescribe.text = appLockModel.describe
            }

        }
    }

     interface OnClickListener{
        fun onClickFavorite(position:Int, isFavorite: Boolean)
        fun onClickLock(position: Int, isLock: Boolean)
    }
}