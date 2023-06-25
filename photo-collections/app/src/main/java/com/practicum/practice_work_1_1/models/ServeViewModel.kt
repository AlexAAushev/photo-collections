package com.practicum.practice_work_1_1.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.practicum.practice_work_1_1.AppDatabase
import com.practicum.practice_work_1_1.FeedPhoto
import com.practicum.practice_work_1_1.exhibit.PhotoPagingSource
import com.practicum.practice_work_1_1.exhibit.ExhibitMediator
import com.practicum.practice_work_1_1.presentations.searchRequest
import kotlinx.coroutines.flow.Flow


class ServeViewModel(
    private val feedDatabase: AppDatabase
    ) : ViewModel() {
    @OptIn(ExperimentalPagingApi::class)
    val pagedPhoto: Flow<PagingData<FeedPhoto>> =
        when (searchRequest) {
            "" -> {
                Pager(
                    config = PagingConfig(pageSize = 10),
                    pagingSourceFactory = {
                        feedDatabase.getFeedPhotoDao().getAll()
                    },
                    remoteMediator = ExhibitMediator(
                        feedDatabase,
                    )
                ).flow.cachedIn(viewModelScope)
            }
            else -> {
                Pager(
                    config = PagingConfig(pageSize = 10),
                    pagingSourceFactory = { PhotoPagingSource() }).flow.cachedIn(viewModelScope)
            }
        }
}