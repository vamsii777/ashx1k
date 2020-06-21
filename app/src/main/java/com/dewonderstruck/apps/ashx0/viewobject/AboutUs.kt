package com.dewonderstruck.apps.ashx0.viewobject

import androidx.room.Embedded
import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(primaryKeys = ["aboutId"])
class AboutUs(@field:SerializedName("about_id") val aboutId: String, @field:SerializedName("about_title") val aboutTitle: String, @field:SerializedName("about_description") val aboutDescription: String, @field:SerializedName("about_email") val aboutEmail: String, @field:SerializedName("about_phone") val aboutPhone: String, @field:SerializedName("ads_on") val adsOn: String, @field:SerializedName("ads_client") val adsClient: String, @field:SerializedName("ads_slot") val adsSlot: String, @field:SerializedName("analyt_on") val analytOn: String, @field:SerializedName("analyt_track_id") val analytTrackId: String, @field:SerializedName("about_website") val aboutWebsite: String, @field:SerializedName("facebook") val facebook: String, @field:SerializedName("google_plus") val googlePlus: String, @field:SerializedName("instagram") val instagram: String, @field:SerializedName("youtube") val youtube: String, @field:SerializedName("pinterest") val pinterest: String, @field:SerializedName("twitter") val twitter: String, @field:SerializedName("default_photo") @field:Embedded val defaultPhoto: Image, @field:SerializedName("privacypolicy") val privacyPolicy: String)