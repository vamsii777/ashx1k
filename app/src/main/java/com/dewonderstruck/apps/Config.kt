package com.dewonderstruck.apps

import com.dewonderstruck.apps.ashx0.BuildConfig

/**
 * Created by Vamsi Madduluri on2020.
 * Contact Email : vamsii.wrkhost@gmail.com
 */
object Config {
    /**
     * AppVersion
     * For your app, you need to change according based on your app version
     */
    var APP_VERSION = BuildConfig.VERSION_NAME
    const val DEVELOPER_KEY = "AIzaSyAhfz_luYiPGSv8aCEdcgJZyJpSjEQNOnM"

    // YouTube video id
    const val YOUTUBE_VIDEO_CODE = "_oEA18Y8gM0"

    /**
     * APP Setting
     * Set falgit se, your app is production
     * It will turn off the logging Process
     */
    @JvmField
    var IS_DEVELOPMENT = true

    /**
     * API URL
     * Change your backend url
     */
    const val APP_API_URL = "https://connectto.oversee.network/ashtrixx/ddah/v1u2/index.php/"
    const val APP_IMAGES_URL = "https://connectto.oversee.network/ashtrixx/ddah/v1u2/uploads/"
    const val APP_IMAGES_THUMB_URL = "https://connectto.oversee.network/ashtrixx/ddah/v1u2/uploads/thumbnail/"

    /**
     * API Key
     * If you change here, you need to update in server.
     */
    const val API_KEY = "44576831-c979-4457-8c49-f9cbdb6a7690"

    /**
     * For default language change, please check
     * LanguageFragment for language code and country code
     * ..............................................................
     * Language             | Language Code     | Country Code
     * ..............................................................
     * "English"            | "en"              | ""
     * "Arabic"             | "ar"              | ""
     * "Chinese (Mandarin)" | "zh"              | ""
     * "French"             | "fr"              | ""
     * "German"             | "de"              | ""
     * "India (Hindi)"      | "hi"              | "rIN"
     * "Indonesian"         | "in"              | ""
     * "Italian"            | "it"              | ""
     * "Japanese"           | "ja"              | ""
     * "Korean"             | "ko"              | ""
     * "Malay"              | "ms"              | ""
     * "Portuguese"         | "pt"              | ""
     * "Russian"            | "ru"              | ""
     * "Spanish"            | "es"              | ""
     * "Thai"               | "th"              | ""
     * "Turkish"            | "tr"              | ""
     * ..............................................................
     */
    const val LANGUAGE_CODE = "en"
    const val DEFAULT_LANGUAGE = LANGUAGE_CODE
    const val DEFAULT_LANGUAGE_COUNTRY_CODE = ""

    /**
     * Loading Limit Count Setting
     */
    const val API_SERVICE_CACHE_LIMIT = 8 // Minutes Cache
    const val TRANSACTION_COUNT = 10
    const val TRANSACTION_ORDER_COUNT = 10
    var RATING_COUNT = 30
    @JvmField
    var LOAD_FROM_DB = 10 //not increase
    @JvmField
    var PRODUCT_COUNT = 40
    @JvmField
    var LIST_CATEGORY_COUNT = 30
    @JvmField
    var LIST_NEW_FEED_COUNT = 30
    var NOTI_LIST_COUNT = 30
    @JvmField
    var COMMENT_COUNT = 30
    var COLLECTION_PRODUCT_LIST_LIMIT = 30

    /**
     * Playstore
     */
    @JvmField
    var PLAY_STORE_MARKET_URL_FIX = "market://details?id="
    @JvmField
    var PLAY_STORE_HTTP_URL_FIX = "http://play.google.com/store/apps/details?id="

    /**
     * Price Format
     * Need to change according to your format that you need
     * E.g.
     * ",##0.00"   => 2,555.00
     * "##0.00"    => 2555.00
     * ".00"       => 2555.00
     * ",##0"      => 2555
     * ",##0.0"    => 2555.0
     */
    const val DECIMAL_PLACES_FORMAT = ",##0.00"

    /**
     * attribute separator
     */
    var ATTRIBUTE_SEPARATOR = "#" // don't change

    /**
     * Image Cache and Loading
     */
    @JvmField
    var IMAGE_CACHE_LIMIT = 350 // Mb
    @JvmField
    var PRE_LOAD_FULL_IMAGE = true

    /**
     * Admob Setting
     */
    const val SHOW_ADMOB = true

    /**
     * GDPR Configs
     */
    var CONSENTSTATUS_PERSONALIZED = "PERSONALIZED"
    var CONSENTSTATUS_NON_PERSONALIZED = "NON_PERSONALIZED"
    var CONSENTSTATUS_UNKNOWN = "UNKNOWN"
    @JvmField
    var CONSENTSTATUS_CURRENT_STATUS = "UNKNOWN"
    var CONSENTSTATUS_IS_READY_KEY = "CONSENTSTATUS_IS_READY"

    /**
     * Policy Url
     */
    var POLICY_URL = "https://ashtrixx.com"

    /**
     * Facebook login Config
     */
    var ENABLE_FACEBOOK_LOGIN = false

    /**
     * Google login Config
     */
    var ENABLE_GOOGLE_LOGIN = false

    /**
     * Phone login Config
     */
    var ENABLE_PHONE_LOGIN = false
}