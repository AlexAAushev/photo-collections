package com.practicum.practice_work_1_1.compound

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.practicum.practice_work_1_1.data.repositories.Repository
import com.practicum.practice_work_1_1.data.db.LoadPhotoResponse

class PhotoPagingSource(private val id: String) :
    PagingSource<Int, LoadPhotoResponse>() {
    private val firstPage: Int = 1
    private val repository = Repository()

    override fun getRefreshKey(state: PagingState<Int, LoadPhotoResponse>): Int =
        firstPage

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LoadPhotoResponse> {
        val page = params.key ?: firstPage
        return kotlin.runCatching {
            repository.getPhotosCollection(page = page, id = id)
        }.fold(
            onSuccess = {
                LoadResult.Page(
                    data = it,
                    prevKey = null,
                    nextKey = if (it.isEmpty()) null else page + 1
                )
            },
            onFailure = {
                LoadResult.Error(it)
            }
        )
    }
}