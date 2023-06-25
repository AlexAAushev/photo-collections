package com.practicum.practice_work_1_1.data.db

data class LoadCollections(
    val id: String,
    val title: String,
    val description: String,
    val total_photos: Int,
    val private: Boolean,
    val user: Author,
    val cover_photo: PhotoDetails,
    val tags: List<TAGS>
)