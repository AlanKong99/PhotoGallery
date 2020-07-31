package com.bignerdranch.android.photogallery

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.bignerdranch.android.photogallery.api.FlickrApi
import com.bignerdranch.android.photogallery.api.FlickrReponse
import com.bignerdranch.android.photogallery.api.PhotoInterceptor
import com.bignerdranch.android.photogallery.api.PhotoResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "FlickrFetchr"

class FlickrFetchr {

    private val flickrApi:FlickrApi = FlickrApi.newFlickrApi()

    fun getPhoto(query: String): Flow<PagingData<GalleryItem>>{
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {PhotoPagingSource(flickrApi,query)}
        ).flow
    }

    fun fetchPhotosRequest(query: String): Call<List<GalleryItem>>{
        return if (query.isBlank()) {
            flickrApi.fetchInterestingPhotos()
        } else {
            flickrApi.searchYouWant(query)
        }
    }


    @WorkerThread
    fun fetchPhoto(url:String): Bitmap?{
        val response:Response<ResponseBody> = flickrApi.fetchUrlBytes(url).execute()
        val bitmap = response.body()?.byteStream()?.use(BitmapFactory::decodeStream)
        Log.i(TAG, "Decode bitmap = $bitmap from Response=$response")
        return bitmap
    }

    companion object {
        private const val PAGE_SIZE = 10
    }

}