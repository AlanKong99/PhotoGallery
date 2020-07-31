package com.bignerdranch.android.photogallery

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow

class PhotoGalleryViewModel(private val app: Application): AndroidViewModel(app) {

    private var currentResult: Flow<PagingData<GalleryItem>>? = null

    private val flickrFetchr = FlickrFetchr()
    var searchTerm = ""

    fun getPhoto(query: String): Flow<PagingData<GalleryItem>>{
        val lastResult = currentResult
        if (query == QueryPreferences.getStoredQuery(app) && lastResult != null) {
            return lastResult
        }
        setPhotoQuery(query)
        val newResult: Flow<PagingData<GalleryItem>> = flickrFetchr.getPhoto(query)
                .cachedIn(viewModelScope)
        currentResult = newResult
        return newResult
    }

    fun setPhotoQuery(query:String = ""){
        QueryPreferences.setStoredQuery(app,query)
        searchTerm = query
    }
}