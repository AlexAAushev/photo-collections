package com.practicum.practice_work_1_1.data.db

data class LoadPhotoResponse(
    var id: String,
    val created_at: String? = null,
    val width: Int? = 0,
    val height: Int? = 0,
    val alt_description: String? = null,
    val urls: URLS,
    val links: Links? = null,
    val likes: Int? = 0,
    val liked_by_user: Boolean? = false,
    val user: Author
)