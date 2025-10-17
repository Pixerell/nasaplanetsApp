package com.example.ricknmortycharacters.data.db

import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class Converters {
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    private val listType = Types.newParameterizedType(List::class.java, String::class.java)
    private val listAdapter = moshi.adapter<List<String>>(listType)

    private val locAdapter = moshi.adapter(LocationRef::class.java)

    @TypeConverter
    fun fromEpisodeList(list: List<String>): String = listAdapter.toJson(list)

    @TypeConverter
    fun toEpisodeList(json: String?): List<String> =
        if (json.isNullOrEmpty()) emptyList() else listAdapter.fromJson(json) ?: emptyList()

    @TypeConverter
    fun fromLocation(loc: LocationRef): String = locAdapter.toJson(loc)

    @TypeConverter
    fun toLocation(json: String?): LocationRef =
        if (json.isNullOrEmpty()) LocationRef("", "") else locAdapter.fromJson(json) ?: LocationRef("", "")
}
