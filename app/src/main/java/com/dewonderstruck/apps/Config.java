package com.dewonderstruck.apps;

import com.dewonderstruck.apps.ashx0.BuildConfig;

/**
 * Created by Vamsi Madduluri on2020.
 * Contact Email : vamsii.wrkhost@gmail.com
 */

public class Config {

    /**
     * AppVersion
     * For your app, you need to change according based on your app version
     */
    public static String APP_VERSION = BuildConfig.VERSION_NAME;
    public static final String DEVELOPER_KEY = "AIzaSyAhfz_luYiPGSv8aCEdcgJZyJpSjEQNOnM";
    // YouTube video id
    public static final String YOUTUBE_VIDEO_CODE = "_oEA18Y8gM0";
    /**
     * APP Setting
     * Set falgit se, your app is production
     * It will turn off the logging Process
     */
    public static boolean IS_DEVELOPMENT = true;

    /**
     * API URL
     * Change your backend url
     */
    public static final String APP_API_URL = "https://connectto.oversee.network/ashtrixx/ddah/v1u2/index.php/";
    public static final String APP_IMAGES_URL = "https://connectto.oversee.network/ashtrixx/ddah/v1u2/uploads/";
    public static final String APP_IMAGES_THUMB_URL = "https://connectto.oversee.network/ashtrixx/ddah/v1u2/uploads/thumbnail/";

    /**
     * API Key
     * If you change here, you need to update in server.
     */
    public static final String API_KEY = "44576831-c979-4457-8c49-f9cbdb6a7690";

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

    public static final String LANGUAGE_CODE = "en";
    public static final String DEFAULT_LANGUAGE = LANGUAGE_CODE;
    public static final String DEFAULT_LANGUAGE_COUNTRY_CODE = "";


    /**
     * Loading Limit Count Setting
     */
    public static final int API_SERVICE_CACHE_LIMIT = 5; // Minutes Cache

    public static final int TRANSACTION_COUNT = 10;

    public static final int TRANSACTION_ORDER_COUNT = 10;

    public static int RATING_COUNT = 30;

    public static int LOAD_FROM_DB = 10; //not increase

    public static int PRODUCT_COUNT = 30;

    public static int LIST_CATEGORY_COUNT = 30;

    public static int LIST_NEW_FEED_COUNT = 30;

    public static int NOTI_LIST_COUNT = 30;

    public static int COMMENT_COUNT = 30;

    public static int COLLECTION_PRODUCT_LIST_LIMIT = 30;

    /**
     * Playstore
     */
    public static String PLAY_STORE_MARKET_URL_FIX = "market://details?id=";
    public static String PLAY_STORE_HTTP_URL_FIX = "http://play.google.com/store/apps/details?id=";


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
    public static final String DECIMAL_PLACES_FORMAT = ",##0.00";

    /**
     * attribute separator
     */
    public static String ATTRIBUTE_SEPARATOR = "#"; // don't change

    /**
     * Image Cache and Loading
     */
    public static int IMAGE_CACHE_LIMIT = 250; // Mb
    public static boolean PRE_LOAD_FULL_IMAGE = true;

    /**
     * Admob Setting
     */
    public static final Boolean SHOW_ADMOB = true;

    /**
     * GDPR Configs
     */
    public static String CONSENTSTATUS_PERSONALIZED = "PERSONALIZED";
    public static String CONSENTSTATUS_NON_PERSONALIZED = "NON_PERSONALIZED";
    public static String CONSENTSTATUS_UNKNOWN = "UNKNOWN";
    public static String CONSENTSTATUS_CURRENT_STATUS = "UNKNOWN";
    public static String CONSENTSTATUS_IS_READY_KEY = "CONSENTSTATUS_IS_READY";

    /**
     * Policy Url
     */
    public static String POLICY_URL = "https://ashtrixx.com";

    /**
     * Facebook login Config
     */
    public static boolean ENABLE_FACEBOOK_LOGIN = false;

    /**
     * Google login Config
     */
    public static boolean ENABLE_GOOGLE_LOGIN = false;

    /**
     * Phone login Config
     */
    public static boolean ENABLE_PHONE_LOGIN = false;

}
