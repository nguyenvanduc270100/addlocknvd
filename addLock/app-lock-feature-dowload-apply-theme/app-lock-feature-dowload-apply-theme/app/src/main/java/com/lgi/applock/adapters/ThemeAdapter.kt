package com.lgi.applock.adapters

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lgi.applock.databinding.ItemRcvThemeBinding
import com.lgi.applock.models.Image
import java.io.File


class ThemeAdapter(
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<ThemeAdapter.ViewHolder>() {
    private var listTheme: List<Image> = listOf()

    fun setList(list: List<Image>) {
        listTheme = list
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onClickClick(theme: Image)
    }
    inner class ViewHolder(val binding: ItemRcvThemeBinding) : RecyclerView.ViewHolder(binding.root)

    // inside the onCreateViewHolder inflate the view of SingleItemBinding
    // and return new ViewHolder object containing this layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRcvThemeBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(listTheme[position]){
                val photoUri: Uri = Uri.fromFile(File(this.url))
                Glide
                    .with(holder.itemView.context)
                    .load(this.link)
                    .into(holder.binding.image)
            }
        }
        holder.binding.image.setOnClickListener {
            listener.onClickClick(listTheme[position])
        }
    }

    // return the size of languageList
    override fun getItemCount(): Int {
        return listTheme.size
    }
}