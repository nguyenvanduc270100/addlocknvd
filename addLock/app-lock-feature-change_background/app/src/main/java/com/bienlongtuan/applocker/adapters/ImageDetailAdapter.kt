package com.bienlongtuan.applocker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bienlongtuan.applocker.adapters.v2.ImageAdapterV2
import com.bienlongtuan.applocker.databinding.ItemImageDetailBinding
import com.bienlongtuan.applocker.helpers.ImageInfo
import com.bumptech.glide.Glide

class ImageDetailAdapter(var images: ArrayList<ImageInfo>, var listener: ImageAdapterV2.ImageClickListener) :
    RecyclerView.Adapter<ImageDetailAdapter.ImageDetailHolder>() {

    class ImageDetailHolder(var binding: ItemImageDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageDetailHolder {
        return ImageDetailHolder(
            ItemImageDetailBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ImageDetailHolder, position: Int) {
        val imageInfo = images[position]
        Glide.with(holder.binding.root.context).load(imageInfo.imagePath).into(holder.binding.image)
        holder.binding.image.setOnClickListener {
            listener.onImageClick(imageInfo)
        }
    }

    override fun getItemCount(): Int {
        return images.size
    }
}