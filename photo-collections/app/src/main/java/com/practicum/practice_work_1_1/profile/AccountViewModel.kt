package com.practicum.practice_work_1_1.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.practicum.practice_work_1_1.data.db.LoadPhotoResponse
import kotlinx.coroutines.flow.Flow

class AccountViewModel : ViewModel() {
    lateinit var id: String
    val pagedPhoto: Flow<PagingData<LoadPhotoResponse>> =
        Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { AccountPhotoPagingSource(id) }).flow.cachedIn(viewModelScope)
}