package com.practicum.practice_work_1_1.profile

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.practicum.practice_work_1_1.data.db.LoadPhotoResponse
import com.practicum.practice_work_1_1.databinding.PhotosSimpleBinding

class ProfileAdapter(
    private val onClick: (LoadPhotoResponse) -> Unit
) :
    PagingDataAdapter<LoadPhotoResponse, MyViewHolderAccount>(DiffUtilCallbackAccount()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderAccount {
        return MyViewHolderAccount(
            PhotosSimpleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolderAccount, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            authorName.text = ""
            authorNickname.text = ""
            sumLikes.text = item?.likes.toString()
            if (item?.liked_by_user == true) indexLike.setColorFilter(Color.YELLOW)
            authorAvatar.alpha = 0f
            mainPhoto.load(item?.urls?.regular) {
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

class DiffUtilCallbackAccount : DiffUtil.ItemCallback<LoadPhotoResponse>() {
    override fun areItemsTheSame(oldItem: LoadPhotoResponse, newItem: LoadPhotoResponse): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(
        oldItem: LoadPhotoResponse,
        newItem: LoadPhotoResponse
    ): Boolean =
        oldItem == newItem
}

class MyViewHolderAccount(val binding: PhotosSimpleBinding) :
    RecyclerView.ViewHolder(binding.root)