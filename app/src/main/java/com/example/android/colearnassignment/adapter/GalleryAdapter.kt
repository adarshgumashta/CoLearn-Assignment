package com.example.android.colearnassignment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.colearnassignment.R
import com.example.android.colearnassignment.databinding.CardThumbnailBinding
import com.example.android.colearnassignment.loadImage
import com.example.android.colearnassignment.model.remote.UnSplashImage

class GalleryAdapter(private val clickListener: (UnSplashImage) -> Unit) :
    PagingDataAdapter<UnSplashImage, RecyclerView.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        GalleryViewHolder(
            CardThumbnailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val thumbNail = getItem(position)
        (holder as GalleryViewHolder).apply {
            thumbNail?.urls?.let {
                itemView.context.loadImage(it.thumb!!, binding.thumbNail, null, R.color.black)
                itemView.setOnClickListener { clickListener(thumbNail) }
            }
        }
    }

    class GalleryViewHolder(val binding: CardThumbnailBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        object DiffCallback : DiffUtil.ItemCallback<UnSplashImage>() {
            override fun areItemsTheSame(oldItem: UnSplashImage, newItem: UnSplashImage) =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: UnSplashImage, newItem: UnSplashImage) =
                oldItem.id == newItem.id
        }
    }
}