package com.lgi.applock.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lgi.applock.databinding.ItemRcvThemeBinding
import com.lgi.applock.models.Theme


class ThemeAdapter(
    var listTheme: List<Theme>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<ThemeAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onClickClick(theme: Theme)
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
                binding.image.setImageResource(this.img as Int)
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