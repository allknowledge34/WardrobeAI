package com.example.wardrobeai.models

import com.example.wardrobeai.helpers.exists
import com.example.wardrobeai.helpers.read
import com.example.wardrobeai.helpers.write
import com.example.wardrobeai.models.outfit.OutfitModel
import com.example.wardrobeai.models.outfit.OutfitStore
import android.content.Context
import android.net.Uri
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.google.gson.reflect.TypeToken
import timber.log.Timber
import java.lang.reflect.Type
import java.util.Date

const val OUTFIT_JSON_FILE = "outfits.json"

val outfitGsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .registerTypeAdapter(Date::class.java, DateParser())
    .create()

val outfitListType: Type = object : TypeToken<ArrayList<OutfitModel>>() {}.type

class OutfitJSONStore(private val context: Context) : OutfitStore {

    private var outfits = mutableListOf<OutfitModel>()

    init {
        // If the JSON file with outfits exists, deserialize the data.
        if (exists(context, OUTFIT_JSON_FILE)) {
            deserialize()
        }
    }


    override fun findAll(): List<OutfitModel> {
        logAll()
        return outfits
    }

    override fun create(outfit: OutfitModel) {
        outfit.id = generateRandomId()
        outfits.add(outfit)
        serialize()
    }

    override fun update(outfit: OutfitModel) {
        val outfitList = findAll() as ArrayList<OutfitModel>
        val foundOutfit: OutfitModel? = outfitList.find { o -> o.id == outfit.id }
        if (foundOutfit != null) {
            foundOutfit.title = outfit.title
            foundOutfit.description = outfit.description
            foundOutfit.season = outfit.season
            foundOutfit.lastWorn = outfit.lastWorn
            foundOutfit.clothingItems = outfit.clothingItems
        }
        serialize()
    }

    override fun delete(outfit: OutfitModel) {
        outfits.remove(outfit)
        serialize()
        logAll()
    }
    private fun serialize() {
        val jsonString = outfitGsonBuilder.toJson(outfits, outfitListType)
        write(context, OUTFIT_JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, OUTFIT_JSON_FILE)
        outfits = outfitGsonBuilder.fromJson(jsonString, outfitListType)
    }

    private fun logAll() {
        outfits.forEach { Timber.i("$it") }
    }
}

class DateParser : JsonDeserializer<Date>, JsonSerializer<Date> {

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Date {
        return Date(json?.asLong ?: 0)
    }

    override fun serialize(src: Date?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(src?.time ?: 0)
    }
}