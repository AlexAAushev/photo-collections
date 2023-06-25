package com.practicum.practice_work_1_1.data.db

data class UserInfoResponse(
    val id: String,
    val updated_at: String,
    val username: String,
    val name: String,
    val first_name: String,
    val last_name: String,
    val twitter_username: String? = null,
    val portfolio_url: String? = null,
    val bio: String? = null,
    val location: String? = null,
    val profile_image: ProfileImage,
    val instagram_username: String? = null,
    val total_collections: Int,
    val total_likes: Int,
    val total_photos: Int,
    val followers_count: Int,
    val following_count: Int,
    val downloads: Int,
    val email: String,
    val photos: List<LoadPhotoResponse>
)