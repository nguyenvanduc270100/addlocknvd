package com.lgi.applock.adapters

import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.lgi.applock.R
import com.lgi.applock.models.LanguageModel

class LanguageViewAdapter(
    private val languageListItems: List<LanguageModel>,
    private val languageCurrent: String?,
    private val clickListener: OnItemClick
) :
    RecyclerView.Adapter<LanguageViewAdapter.LanguageViewHolder>() {

    private var mSlectedItem: Int = 0

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,

        ): LanguageViewAdapter.LanguageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_language_item, parent, false)
        return LanguageViewHolder(view)
    }

    inner class LanguageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val mImage: ImageView = itemView.findViewById(R.id.imageView)
        val mSelectedCountry: CheckBox = itemView.findViewById(R.id.radioButton)
        val mTextView: TextView = itemView.findViewById(R.id.textView)

        init {
            itemView.setOnClickListener(this)

        }

        override fun onClick(v: View?) {
            // set un-click all
            val copy: Int = mSlectedItem
            mSlectedItem = adapterPosition
            Log.d("Debug", "position $mSlectedItem")
            notifyItemChanged(copy)
            notifyItemChanged(mSlectedItem)
            notifyDataSetChanged()
        }
    }

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        val itemViewModel = languageListItems[position]

        holder.mImage.setImageResource(itemViewModel.image)
        holder.mTextView.text = itemViewModel.country
        //holder.mSelectedCountry.isChecked = itemViewModel.checked

        holder.mSelectedCountry.isChecked = (itemViewModel.code == languageCurrent)
        holder.itemView.setOnClickListener {
            clickListener.onClick(itemViewModel)
        }
    }

    override fun getItemCount(): Int {
        return languageListItems.size
    }

    interface OnItemClick {
        fun onClick(item: LanguageModel)
    }
}