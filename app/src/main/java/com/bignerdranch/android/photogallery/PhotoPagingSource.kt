package com.bignerdranch.android.photogallery

import androidx.paging.PagingSource
import com.bignerdranch.android.photogallery.api.FlickrApi
import com.bignerdranch.android.photogallery.api.FlickrReponse
import retrofit2.HttpException
import java.io.IOException

private const val PHOTO_START_PAGE = 1

class PhotoPagingSource(
    private val flickrApi: FlickrApi,
    private val query: String
) : PagingSource<Int, GalleryItem>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GalleryItem> {
        val position = params.key?: PHOTO_START_PAGE
        return try {
            val galleryItems: List<GalleryItem> = if (query.isBlank()){
                flickrApi.fetchPhotos()
            } else {
                flickrApi.searchPhotos(query)
            }
            LoadResult.Page(
                data = galleryItems,
                prevKey = if (position == PHOTO_START_PAGE) null else position - 1,
                nextKey =  if (galleryItems.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}