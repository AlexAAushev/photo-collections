package com.practicum.practice_work_1_1.data.db

data class Author(
    val id: String? = null,
    val name: String,
    val username: String,
    val profile_image: UserPhoto,
    val bio: String
)