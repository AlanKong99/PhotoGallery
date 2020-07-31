package com.bignerdranch.android.photogallery.api

import com.bignerdranch.android.photogallery.GalleryItem
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface FlickrApi{
    @GET("services/rest/?method=flickr.interestingness.getList")
    suspend fun fetchPhotos():List<GalleryItem>

    @GET("services/rest/?method=flickr.interestingness.getList")
    fun fetchInterestingPhotos():Call<List<GalleryItem>>

    @GET
    fun fetchUrlBytes(@Url url:String): Call<ResponseBody>

    @GET("services/rest?method=flickr.photos.search")
    suspend fun searchPhotos(@Query("text") query:String): List<GalleryItem>

    @GET("services/rest?method=flickr.photos.search")
    fun searchYouWant(@Query("text") query:String): Call<List<GalleryItem>>

    companion object {
        fun newFlickrApi(): FlickrApi{
            val client = OkHttpClient.Builder()
                .addInterceptor(PhotoInterceptor())
                .build()

            val gson = GsonBuilder().registerTypeAdapter(
                List::class.java,
                PhotoDeserializer()
            ).create()

            val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl("https://api.flickr.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()

            return retrofit.create(FlickrApi::class.java)
        }
    }
}