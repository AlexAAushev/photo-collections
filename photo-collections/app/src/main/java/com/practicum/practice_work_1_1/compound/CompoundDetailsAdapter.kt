package com.practicum.practice_work_1_1.compound

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.practicum.practice_work_1_1.data.db.LoadPhotoResponse
import com.practicum.practice_work_1_1.databinding.PhotosSimpleBinding

class CompoundDetailsAdapter(
    private val onClick: (LoadPhotoResponse) -> Unit
) : PagingDataAdapter<LoadPhotoResponse, MyViewHolderSecond>(DiffUtilCallbackPhoto()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderSecond {
        return MyViewHolderSecond(
            PhotosSimpleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolderSecond, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            authorName.text = item?.user?.name
            authorNickname.text = item?.user?.username
            sumLikes.text = item?.likes.toString()
            if (item?.liked_by_user == true) indexLike.setColorFilter(Color.RED)
            authorAvatar.load(item?.user?.profile_image?.small) {
                transformations(CircleCropTransformation())
            }
            mainPhoto.load(item?.urls?.small) {
                target {
                    progress.isVisible = false
                    interfacePhoto.isVisible = true
                    mainPhoto.setImageDrawable(it)
                }
            }
        }
        holder.binding.root.setOnClickListener {
            item?.let { onClick(item) }
        }
    }
}

class DiffUtilCallbackPhoto : DiffUtil.ItemCallback<LoadPhotoResponse>() {

    override fun areItemsTheSame(oldItem: LoadPhotoResponse, newItem: LoadPhotoResponse): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(
        oldItem: LoadPhotoResponse,
        newItem: LoadPhotoResponse
    ): Boolean =
        oldItem == newItem
}

class MyViewHolderSecond(val binding: PhotosSimpleBinding) :
    RecyclerView.ViewHolder(binding.root)