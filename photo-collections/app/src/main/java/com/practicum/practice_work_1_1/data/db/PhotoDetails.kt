package com.practicum.practice_work_1_1.data.db

data class PhotoDetails(
    val id: String,
    val downloads: Int,
    val likes: Int,
    val liked_by_user: Boolean,
    val exif: Camera,
    val location: Location,
    val tags: List<TAGS>,
    val urls: URLS,
    val links: Links,
    val user: Author
)