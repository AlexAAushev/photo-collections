package com.practicum.practice_work_1_1.data.repositories

import com.practicum.practice_work_1_1.data.api.retrofit
import com.practicum.practice_work_1_1.data.db.LoadCollections
import com.practicum.practice_work_1_1.data.db.LoadPhotoResponse
import com.practicum.practice_work_1_1.data.db.PhotoDetails
import com.practicum.practice_work_1_1.data.db.UserInfoResponse
import com.practicum.practice_work_1_1.presentations.searchRequest

class Repository {

    suspend fun getUsersPhoto(page: Int, id: String): List<LoadPhotoResponse> {
        return retrofit.loadUsersPhoto(id = id, page = page)
    }

    suspend fun getMyProfile(): UserInfoResponse {
        return retrofit.loadUserInfo()
    }

    suspend fun getPhotosCollection(page: Int, id: String): List<LoadPhotoResponse> {
        return retrofit.loadCollectionsDetails(id = id, page = page)
    }

    suspend fun getCollections(page: Int): List<LoadCollections> {
        return retrofit.loadCollections(page = page)
    }

    suspend fun unsetLike(id: String): PhotoDetails {
        return retrofit.loadPhotoDetailsUnlike(id = id).photo
    }

    suspend fun setLike(id: String): PhotoDetails {
        return retrofit.loadPhotoDetailsLike(id = id).photo
    }

    suspend fun getDetailsPhoto(id: String): PhotoDetails {
        return retrofit.loadPhotoDetails(id = id)
    }

    suspend fun getPhoto(page: Int): List<LoadPhotoResponse> {
        val answer = when (searchRequest) {
            "" -> retrofit.loadPhotos(page = page)
            else -> retrofit.searchPhotos(page = page).results
        }
        return answer
    }
}