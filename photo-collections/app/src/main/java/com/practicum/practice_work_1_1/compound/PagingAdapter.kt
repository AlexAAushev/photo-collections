package com.practicum.practice_work_1_1.compound

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.practicum.practice_work_1_1.R
import com.practicum.practice_work_1_1.data.db.LoadCollections
import com.practicum.practice_work_1_1.databinding.CollectionPhotoSimpleBinding
import com.practicum.practice_work_1_1.presentations.feedContext

class PagingAdapter(
    private val onClick: (LoadCollections) -> Unit
) :
    PagingDataAdapter<LoadCollections, MyViewHolder>(DiffUtilCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            CollectionPhotoSimpleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            val sub = item?.cover_photo?.urls?.regular?.contains("plus.unsplash")
            val sumPhotos = feedContext.resources.getQuantityString(
                R.plurals.collection_photo_sum, item?.total_photos!!, item?.total_photos!!
            )
            mainPhoto.load(item?.cover_photo?.urls?.regular) {
                target {
                    progress.isVisible = false
                    interfaceCollection.isVisible = true
                    sumOfPhoto.text = sumPhotos
                    mainPhoto.setImageDrawable(it)
                }
            }
            authorAvatar.load(item?.user?.profile_image?.small) {
                transformations(CircleCropTransformation())
            }
            unsplashPlus.isVisible = sub!!
            collectionName.text = item?.title
            authorName.text = item?.user?.name
            authorNickname.text = item?.user?.username
        }
        holder.binding.root.setOnClickListener {
            item?.let { onClick(item) }
        }
    }
}

class DiffUtilCallback : DiffUtil.ItemCallback<LoadCollections>() {

    override fun areItemsTheSame(oldItem: LoadCollections, newItem: LoadCollections): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(
        oldItem: LoadCollections,
        newItem: LoadCollections
    ): Boolean =
        oldItem == newItem
}

class MyViewHolder(val binding: CollectionPhotoSimpleBinding) :
    RecyclerView.ViewHolder(binding.root)