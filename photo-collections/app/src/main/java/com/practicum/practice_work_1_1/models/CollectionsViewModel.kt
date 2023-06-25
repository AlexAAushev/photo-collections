package com.practicum.practice_work_1_1.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.practicum.practice_work_1_1.compound.PagingSource
import com.practicum.practice_work_1_1.compound.PhotoPagingSource
import com.practicum.practice_work_1_1.data.db.LoadCollections
import com.practicum.practice_work_1_1.data.db.LoadPhotoResponse
import kotlinx.coroutines.flow.Flow

class CollectionsViewModel : ViewModel() {
    lateinit var id: String

    val pagedPhoto: Flow<PagingData<LoadCollections>> =
        Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { PagingSource() }).flow.cachedIn(viewModelScope)

    val pagedPhotoCollection: Flow<PagingData<LoadPhotoResponse>> =
        Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { PhotoPagingSource(id) }).flow.cachedIn(viewModelScope)
}