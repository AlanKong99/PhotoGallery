package com.bignerdranch.android.photogallery.api

import com.bignerdranch.android.photogallery.GalleryItem
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

private const val secondObjectName = "photos"
private const val jsonArrayName = "photo"

class PhotoDeserializer : JsonDeserializer<List<GalleryItem>> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): List<GalleryItem> {
        val secondJsonObject =  json?.asJsonObject?.get(secondObjectName)
        val jsonArray =  secondJsonObject?.asJsonObject?.get(jsonArrayName)?.asJsonArray
        var galleryItems = mutableListOf<GalleryItem>()
        jsonArray?.forEach {jsonItem ->
            val title = jsonItem.asJsonObject.get("title").asString
            val id =  jsonItem.asJsonObject.get("id").asString
            val url = jsonItem.asJsonObject.get("url_s").asString
            val owner = jsonItem.asJsonObject.get("owner").asString
            val newGalleryItem = GalleryItem(title, id, url, owner)
            galleryItems.add(newGalleryItem)
        }
        return galleryItems.toList()
    }
}