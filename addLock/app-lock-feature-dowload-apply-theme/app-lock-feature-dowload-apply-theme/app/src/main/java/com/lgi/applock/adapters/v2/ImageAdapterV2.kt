package com.lgi.applock.adapters.v2

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lgi.applock.adapters.ImageDetailAdapter
import com.lgi.applock.databinding.ItemImageBinding
import com.lgi.applock.helpers.ImageDirectory
import com.lgi.applock.helpers.ImageInfo

class ImageAdapterV2(private var imagesDirectory: ImageDirectory?, private var listener: ImageClickListener) :
    RecyclerView.Adapter<ImageAdapterV2.ImageHolder>() {

    interface ImageClickListener {
        fun onImageClick(imageInfo: ImageInfo)
    }

    class ImageHolder(val binding: ItemImageBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        return ImageHolder(
            ItemImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        val imageDirectory = imagesDirectory
        holder.binding.tvDirectoryTitle.text = imageDirectory?.parentName
        holder.binding.images.layoutManager =
            GridLayoutManager(holder.binding.root.context, 3, GridLayoutManager.VERTICAL, false)
        val adapter = imageDirectory?.let { ImageDetailAdapter(it.listFile, listener) }
        holder.binding.images.adapter = adapter
    }

    override fun getItemCount(): Int {
        return 1
    }
}