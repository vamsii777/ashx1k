package com.dewonderstruck.apps.ashx0.utils

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.*
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.MainActivity
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.ui.common.NavigationController2
import com.dewonderstruck.apps.ashx0.viewobject.User
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GooglePlayServicesUtil
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.like.LikeButton
import java.io.*
import java.math.BigDecimal
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

/**
 * Created by Vamsi Madduluri on 7/15/15.
 * Contact Email : vamsii.wrkhost@gmail.com
 */
object Utils {
    private var fromAsset: Typeface? = null
    private var spannableString: SpannableString? = null
    private var currentTypeface: Fonts? = null
    const val NOTI_EXISTS_TO_SHOW = "is_noti_exists_to_show"
    const val NOTI_MSG = "noti_msg"
    const val NOTI_SETTING = "noti_setting"
    const val NOTI_TOKEN = "noti_token"
    const val RESULT_LOAD_IMAGE = 1
    const val RESULT_OK = -1
    const val USER_NO_USER = "nologinuser"
    const val START_OF_OFFSET = "0" // Please don't change!

    //
    const val PLATFORM = "android" // Please don't change!
    private val cacheUnicode: Boolean? = null
    var IMAGE_TYPE_PRODUCT = "product"
    var FILTERING_typeName = "product"
    var FILTERING_typeName_CAT = "category"
    var FILTERING_CATEGORY_typeName = "category"
    var IMAGE_TYPE_ABOUT = "about"

    /*Configuration Counts*/
    var CONFIG_COLLECTION_COUNT = "collection_count"
    var CONFIG_HOME_PRODUCT_COUNT = "home_product_count"
    var CONFIG_PRODUCT_COUNT = "product_count"
    var CONFIG_HOME_CATEGORY_COUNT = "home_category_count"
    var CONFIG_LIST_CATEGORY_COUNT = "list_category_count"
    var CONFIG_NOTI_LIST_COUNT = "noti_list_count"
    var CONFIG_COMMENT_COUNT = "comment_count"

    /*Configuration Counts*/ /*For ProductParameterHolder*/
    var active = "1"
    var FILTERING_INACTIVE = ""
    var FILTERING_TRENDING = "touch_count"
    var FILTERING_FEATURE = "featured_date"
    var latest = "addedDate"
    var price = "originalPrice"
    var FILTERING_ASC = "FILTERING_ASC"
    var FILTERING_DESC = Constants.FILTERING_DESC

    /*For ProductParameterHolder*/ /*PayPal*/
    var REQUEST_CODE__PAYPAL = 1444
    fun hideFirstFab(v: View) {
        v.visibility = View.GONE
        v.translationY = v.height.toFloat()
        v.alpha = 0f
    }

    fun twistFab(v: View, rotate: Boolean): Boolean {
        v.animate().setDuration(300)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                    }
                })
                .rotation(if (rotate) 165f else 0f)
        return rotate
    }

    fun showFab(v: View) {
        v.visibility = View.VISIBLE
        v.alpha = 0f
        v.translationY = v.height.toFloat()
        v.animate()
                .setDuration(300)
                .translationY(0f)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                    }
                })
                .alpha(1f)
                .start()
    }

    fun hideFab(v: View) {
        v.visibility = View.VISIBLE
        v.alpha = 1f
        v.translationY = 0f
        v.animate()
                .setDuration(300)
                .translationY(v.height.toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        v.visibility = View.GONE
                        super.onAnimationEnd(animation)
                    }
                }).alpha(0f)
                .start()
    }

    fun callPhone(fragment: Fragment, phoneNo: String) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            // Older Version No need to request Permission
            val dial = "tel:$phoneNo"
            fragment.startActivity(Intent(Intent.ACTION_CALL, Uri.parse(dial)))
        } else {
            // Need to request Permission
            if (fragment.activity != null) {
                if (ContextCompat.checkSelfPermission(fragment.requireActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    fragment.requestPermissions(arrayOf(
                            Manifest.permission.CALL_PHONE
                    ), Constants.REQUEST_CODE__PHONE_CALL_PERMISSION)
                } else {
                    val dial = "tel:$phoneNo"
                    fragment.startActivity(Intent(Intent.ACTION_CALL, Uri.parse(dial)))
                }
            }
        }
    }

    private val suffixes: NavigableMap<Long, String> = TreeMap()
    fun numberFormat(value: Long): String {
        //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
        if (value == Long.MIN_VALUE) return numberFormat(Long.MIN_VALUE + 1)
        if (value < 0) return "-" + numberFormat(-value)
        if (value < 1000) return java.lang.Long.toString(value) //deal with easy case
        val e = suffixes.floorEntry(value)
        val divideBy = e.key
        val suffix = e.value
        val truncated = value / (divideBy / 10) //the number part of the output times 10
        val hasDecimal = truncated < 100 && truncated / 10.0 != (truncated / 10).toDouble()
        return if (hasDecimal) (truncated / 10.0).toString() + suffix else (truncated / 10).toString() + suffix
    }

    fun round(d: Float, decimalPlace: Int): Float {
        var bd = BigDecimal(java.lang.Float.toString(d))
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP)
        return bd.toFloat()
    }

    fun priceFormat(amount: Float): String {
        return NumberFormat.getNumberInstance(Locale.US).format(round(amount, 2).toDouble())
    }

    @JvmStatic
    val dateTime: String
        get() {
            val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
            val date = Date()
            return dateFormat.format(date)
        }

    fun toggleUpDownWithAnimation(view: View): Boolean {
        return if (view.rotation == 0f) {
            view.animate().setDuration(150).rotation(180f)
            true
        } else {
            view.animate().setDuration(150).rotation(0f)
            false
        }
    }

    //    public static AppLanguage appLanguage = new AppLanguage();
    //
    //
    //    public static int getDrawableInt(Context context, String name) {
    //        return context.getResources().getIdentifier(name, "drawable", context.getPackageName());
    //    }
    //
    //    public static void setImageToImageView(Context context, ImageView imageView, int drawable) {
    //        RequestOptions requestOptions = new RequestOptions()
    //                .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
    //                .skipMemoryCache(true);
    //
    //        Glide.with(context)
    //                .load(drawable)
    //                .apply(requestOptions)
    //                .transition(DrawableTransitionOptions.withCrossFade())
    //                .into(imageView);
    //    }
    @JvmStatic
    fun checkUserId(loginUserId: String): String {
        var loginUserId = loginUserId
        if (loginUserId.trim { it <= ' ' } == "") {
            loginUserId = Constants.USER_NO_USER
        }
        return loginUserId
    }

    fun getToolbarHeight(context: Context): Int {
        val styledAttributes = context.theme.obtainStyledAttributes(intArrayOf(R.attr.actionBarSize))
        val toolbarHeight = styledAttributes.getDimension(0, 0f).toInt()
        styledAttributes.recycle()
        return toolbarHeight
    }

    @SuppressLint("RestrictedApi")
    fun removeShiftMode(view: BottomNavigationView) {
        val menuView = view.getChildAt(0) as BottomNavigationMenuView
        try {
            val shiftingMode = menuView.javaClass.getDeclaredField("mShiftingMode")
            shiftingMode.isAccessible = true
            shiftingMode.setBoolean(menuView, false)
            shiftingMode.isAccessible = false
            for (i in 0 until menuView.childCount) {
                val item = menuView.getChildAt(i) as BottomNavigationItemView
                //item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                item.setChecked(item.itemData.isChecked)
            }
        } catch (e: NoSuchFieldException) {
            Log.e("ERROR NO SUCH FIELD", "Unable to get shift mode field")
        } catch (e: IllegalAccessException) {
            Log.e("ERROR ILLEGAL ALG", "Unable to change value of shift mode")
        }
    }

    fun setMargins(v: View, l: Int, t: Int, r: Int, b: Int) {
        if (v.layoutParams is MarginLayoutParams) {
            val p = v.layoutParams as MarginLayoutParams
            p.setMargins(l, t, r, b)
            v.requestLayout()
        }
    }

    fun pxToDp(context: Context, px: Int): Int {
        val displayMetrics = context.resources.displayMetrics
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
    }

    fun dpToPx(context: Context, dp: Int): Int {
        val displayMetrics = context.resources.displayMetrics
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    fun getScreenSize(activity: Activity): Point {
        val display = activity.windowManager.defaultDisplay
        val size = Point()
        try {
            display.getSize(size)
        } catch (e: NoSuchMethodError) {
            // For lower than api 11
            size.x = display.width
            size.y = display.height
        }
        return size
    }

    val isAndroid_5_0: Boolean
        get() {
            val version = Build.VERSION.RELEASE
            if (version != null && version != "") {
                val versionDetail = version.split("\\.".toRegex()).toTypedArray()
                Log.d("TEAMPS", "0 : " + versionDetail[0] + " 1 : " + versionDetail[1])
                if (versionDetail[0] == "5") {
                    if (versionDetail[1] == "0" || versionDetail[1] == "00") {
                        return true
                    }
                }
            }
            return false
        }

    @JvmStatic
    fun psLog(log: String?) {
        if (Config.IS_DEVELOPMENT) {
            Log.d("TEAMPS", log!!)
        }
    }

    fun isGooglePlayServicesOK(activity: Activity?): Boolean {
        val GPS_ERRORDIALOG_REQUEST = 9001
        val isAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity)
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true
        } else if (GooglePlayServicesUtil.isUserRecoverableError(isAvailable)) {
            val dialog = GooglePlayServicesUtil.getErrorDialog(isAvailable, activity, GPS_ERRORDIALOG_REQUEST)
            dialog.show()
        } else {
            Toast.makeText(activity, "Can't connect to Google Play services", Toast.LENGTH_SHORT).show()
        }
        return false
    }

    fun getScreenHeight(activity: Activity): Int {
        val displaymetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displaymetrics)
        return displaymetrics.heightPixels
    }

    fun getScreenWidth(activity: Activity): Int {
        val displaymetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displaymetrics)
        return displaymetrics.widthPixels
    }

    fun isEmailFormatValid(email: String?): Boolean {
        var isValid = false
        val expression = "^[\\w.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(email)
        if (matcher.matches()) {
            isValid = true
        }
        return isValid
    }

    fun saveBitmapImage(context: Context, b: Bitmap, picName: String?) {
        val fos: FileOutputStream
        try {
            fos = context.openFileOutput(picName, Context.MODE_APPEND)
            b.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.close()
        } catch (e: FileNotFoundException) {
            Log.d("TEAMPS", "file not found")
            e.printStackTrace()
        } catch (e: IOException) {
            Log.d("TEAMPS", "io exception")
            e.printStackTrace()
        }
    }

    fun loadBitmapImage(context: Context, picName: String?): Bitmap? {
        var b: Bitmap? = null
        val fis: FileInputStream
        try {
            fis = context.openFileInput(picName)
            b = BitmapFactory.decodeStream(fis)
            fis.close()
        } catch (e: FileNotFoundException) {
            Log.d("TEAMPS", "file not found")
            e.printStackTrace()
        } catch (e: IOException) {
            Log.d("TEAMPS", "io exception")
            e.printStackTrace()
        }
        return b
    }

    @JvmStatic
    fun getTypeFace(context: Context, fonts: Fonts): Typeface? {
        if (currentTypeface == fonts) {
            if (fromAsset == null) {
                if (fonts == Fonts.NOTO_SANS) {
                    fromAsset = Typeface.createFromAsset(context.assets, "fonts/NotoSans-Regular.ttf")
                } else if (fonts == Fonts.ROBOTO) {
//                    fromAsset = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
                    fromAsset = Typeface.createFromAsset(context.assets, "fonts/Product-Sans-Regular.ttf")
                } else if (fonts == Fonts.ROBOTO_MEDIUM) {
                    fromAsset = Typeface.createFromAsset(context.assets, "fonts/Product-Sans-Bold.ttf")
                } else if (fonts == Fonts.ROBOTO_LIGHT) {
                    fromAsset = Typeface.createFromAsset(context.assets, "fonts/Product-Sans-Regular.ttf")
                } else if (fonts == Fonts.MM_FONT) {
                    fromAsset = Typeface.createFromAsset(context.assets, "fonts/mymm.ttf")
                }
            }
        } else {
            fromAsset = if (fonts == Fonts.NOTO_SANS) {
                Typeface.createFromAsset(context.assets, "fonts/NotoSans-Regular.ttf")
            } else if (fonts == Fonts.ROBOTO) {
//                fromAsset = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
                Typeface.createFromAsset(context.assets, "fonts/Product-Sans-Regular.ttf")
            } else if (fonts == Fonts.ROBOTO_MEDIUM) {
                Typeface.createFromAsset(context.assets, "fonts/Product-Sans-Bold.ttf")
            } else if (fonts == Fonts.ROBOTO_LIGHT) {
                Typeface.createFromAsset(context.assets, "fonts/Product-Sans-Regular.ttf")
            } else if (fonts == Fonts.MM_FONT) {
                Typeface.createFromAsset(context.assets, "fonts/mymm.ttf")
            } else {
                Typeface.createFromAsset(context.assets, "fonts/Product-Sans-Regular.ttf")
            }

            //fromAsset = Typeface.createFromAsset(activity.getAssets(), "fonts/Roboto-Italic.ttf");
            currentTypeface = fonts
        }
        return fromAsset
    }

    fun getSpannableString(context: Context, str: String?, font: Fonts): SpannableString? {
        spannableString = SpannableString(str)
        spannableString!!.setSpan(PSTypefaceSpan("", getTypeFace(context, font)), 0, spannableString!!.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spannableString
    }

    fun getUnRotatedImage(imagePath: String?, rotatedBitmap: Bitmap): Bitmap? {
        var rotate = 0
        try {
            val imageFile = File(imagePath)
            val exif = ExifInterface(imageFile.absolutePath)
            val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_270 -> rotate = 270
                ExifInterface.ORIENTATION_ROTATE_180 -> rotate = 180
                ExifInterface.ORIENTATION_ROTATE_90 -> rotate = 90
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
        val matrix = Matrix()
        matrix.postRotate(rotate.toFloat())
        return Bitmap.createBitmap(rotatedBitmap, 0, 0, rotatedBitmap.width, rotatedBitmap.height, matrix,
                true)
    }

    val lineNumber: Int
        get() = Thread.currentThread().stackTrace[4].lineNumber

    fun getClassName(obj: Any): String {
        return "" + obj.javaClass
    }

    fun psErrorLog(log: String?, obj: Any) {
        try {
            Log.d("TEAMPS", log!!)
            Log.d("TEAMPS", "Line : $lineNumber")
            Log.d("TEAMPS", "Class : " + getClassName(obj))
        } catch (ee: Exception) {
            Log.d("TEAMPS", "Error in psErrorLog")
        }
    }

    @JvmStatic
    fun psErrorLog(log: String?, e: Exception) {
        try {
            val l = e.stackTrace[0]
            Log.d("TEAMPS", log!!)
            Log.d("TEAMPS", "Line : " + l.lineNumber)
            Log.d("TEAMPS", "Method : " + l.methodName)
            Log.d("TEAMPS", "Class : " + l.className)
        } catch (ee: Exception) {
            Log.d("TEAMPS", "Error in psErrorLogE")
        }
    }

    fun unbindDrawables(view: View) {
        try {
            if (view.background != null) {
                view.background.callback = null
            }
            if (view is ViewGroup) {
                for (i in 0 until view.childCount) {
                    unbindDrawables(view.getChildAt(i))
                }
                if (view !is AdapterView<*>) {
                    view.removeAllViews()
                }
            }
        } catch (e: Exception) {
            psErrorLog("Error in Unbind", e)
        }
    }

    fun isStoragePermissionGranted(activity: Activity): Boolean {
        return if (Build.VERSION.SDK_INT >= 23) {
            if (activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                psLog("Permission is granted")
                true
            } else {
                psLog("Permission is revoked")
                ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
                false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            psLog("Permission is granted")
            true
        }
    }

    // Sleep Me
    fun sleepMe(millis: Int) {
        try {
            Thread.sleep(millis.toLong())
        } catch (e: InterruptedException) {
            psErrorLog("InterruptedException", e)
        } catch (e: Exception) {
            psErrorLog("Exception", e)
        }
    }

    fun hideKeyboard(activity: Activity) {
        try {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (imm != null) {
                if (activity.currentFocus != null) {
                    imm.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
                }
            }
        } catch (e: Exception) {
            psErrorLog("Error in hide keyboard.", e)
        }
    }

    //Ad
    //    public static void initInterstitialAd(Context context, String adKey) {
    //        //load ad
    //        AdRequest adRequest = new AdRequest.Builder().build();
    //
    //        InterstitialAd interstitial;
    //        // Prepare the Interstitial Ad
    //        interstitial = new InterstitialAd(context);
    //
    //        // Insert the Ad Unit ID
    //        interstitial.setAdUnitId(adKey);
    //
    //        interstitial.loadAd(adRequest);
    //
    //        // Prepare an Interstitial Ad Listener
    //        interstitial.setAdListener(new AdListener() {
    //            public void onAdLoaded() {
    //                // Call displayInterstitial() function
    //                displayInterstitial(interstitial);
    //            }
    //        });
    //    }
    //    public static void displayInterstitial(InterstitialAd interstitial) {
    //        // If Ads are loaded, show Interstitial else show nothing.
    //        if (interstitial.isLoaded()) {
    //            interstitial.show();
    //        }
    //    }
    fun toggleUporDown(v: View): Boolean {
        return if (v.rotation == 0f) {
            v.animate().setDuration(150).rotation(180f)
            true
        } else {
            v.animate().setDuration(150).rotation(0f)
            false
        }
    }

    //    public static void setConfigCountToShared(int value, SharedPreferences pref, String name) {
    //
    //        if (name.equals(CONFIG_COLLECTION_COUNT)) {
    //            SharedPreferences.Editor editor = pref.edit();
    //            editor.putInt(CONFIG_COLLECTION_COUNT, value);
    //            editor.apply();
    //        } else if (name.equals(CONFIG_HOME_PRODUCT_COUNT)) {
    //            SharedPreferences.Editor editor = pref.edit();
    //            editor.putInt(CONFIG_HOME_PRODUCT_COUNT, value);
    //            editor.apply();
    //        } else if (name.equals(CONFIG_PRODUCT_COUNT)) {
    //            SharedPreferences.Editor editor = pref.edit();
    //            editor.putInt(CONFIG_PRODUCT_COUNT, value);
    //            editor.apply();
    //        } else if (name.equals(CONFIG_HOME_CATEGORY_COUNT)) {
    //            SharedPreferences.Editor editor = pref.edit();
    //            editor.putInt(CONFIG_HOME_CATEGORY_COUNT, value);
    //            editor.apply();
    //        } else if (name.equals(CONFIG_LIST_CATEGORY_COUNT)) {
    //            SharedPreferences.Editor editor = pref.edit();
    //            editor.putInt(CONFIG_LIST_CATEGORY_COUNT, value);
    //            editor.apply();
    //        } else if (name.equals(CONFIG_NOTI_LIST_COUNT)) {
    //            SharedPreferences.Editor editor = pref.edit();
    //            editor.putInt(CONFIG_NOTI_LIST_COUNT, value);
    //            editor.apply();
    //        } else if (name.equals(CONFIG_COMMENT_COUNT)) {
    //            SharedPreferences.Editor editor = pref.edit();
    //            editor.putInt(CONFIG_COMMENT_COUNT, value);
    //            editor.apply();
    //        }
    //
    //    }
    fun setDatesToShared(start_date: String?, end_date: String?, pref: SharedPreferences) {
        val editor = pref.edit()
        editor.putString(Constants.SHOP_START_DATE, start_date)
        editor.putString(Constants.SHOP_END_DATE, end_date)
        editor.apply()
    }

    fun format(value: Double): String {
        return try {
            val nf = NumberFormat.getNumberInstance(Locale.US)
            val df = nf as DecimalFormat
            df.applyPattern(Config.DECIMAL_PLACES_FORMAT)
            df.format(value)
        } catch (e: Exception) {
            psErrorLog("", e)
            Constants.ZERO
        }
    }

    //    public static int getLimitCount(SharedPreferences preferences, String name) {
    //
    //        if (name.equals(Constants.CONFIG_COLLECTION_COUNT)) {
    //
    //            if (Config.COLLECTION_PRODUCT_LIST_LIMIT_SERVER == 0) {
    //
    //                Config.COLLECTION_PRODUCT_LIST_LIMIT_SERVER = preferences.getInt(Constants.CONFIG_COLLECTION_COUNT, Config.DEFAULT_COUNT);
    //
    //                if (Config.COLLECTION_PRODUCT_LIST_LIMIT_SERVER > 0) {
    //
    //                    return Config.COLLECTION_PRODUCT_LIST_LIMIT_SERVER;
    //
    //                } else {
    //
    //                    return Config.COLLECTION_PRODUCT_LIST_LIMIT;
    //
    //                }
    //
    //            } else {
    //
    //                return Config.COLLECTION_PRODUCT_LIST_LIMIT_SERVER;
    //
    //            }
    //
    //        } else if (name.equals(Constants.CONFIG_HOME_PRODUCT_COUNT)) {
    //
    //            if (Config.HOME_PRODUCT_COUNT_SERVER == 0) {
    //
    //                Config.HOME_PRODUCT_COUNT_SERVER = preferences.getInt(Constants.CONFIG_HOME_PRODUCT_COUNT, Config.DEFAULT_COUNT);
    //
    //                if (Config.HOME_PRODUCT_COUNT_SERVER > 0) {
    //
    //                    return Config.HOME_PRODUCT_COUNT_SERVER;
    //
    //                } else {
    //
    //                    return Config.HOME_PRODUCT_COUNT;
    //
    //                }
    //            } else {
    //                return Config.HOME_PRODUCT_COUNT_SERVER;
    //            }
    //
    //        } else if (name.equals(Constants.CONFIG_PRODUCT_COUNT)) {
    //
    //            if (Config.PRODUCT_COUNT_SERVER == 0) {
    //
    //                Config.PRODUCT_COUNT_SERVER = preferences.getInt(Constants.CONFIG_PRODUCT_COUNT, Config.DEFAULT_COUNT);
    //
    //                if (Config.PRODUCT_COUNT_SERVER > 0) {
    //
    //                    return Config.PRODUCT_COUNT_SERVER;
    //
    //                } else {
    //
    //                    return Config.PRODUCT_COUNT;
    //
    //                }
    //            } else {
    //
    //                return Config.PRODUCT_COUNT_SERVER;
    //
    //            }
    //        } else if (name.equals(Constants.CONFIG_HOME_CATEGORY_COUNT)) {
    //
    //            if (Config.HOME_CATEGORY_COUNT_SERVER == 0) {
    //
    //                Config.HOME_CATEGORY_COUNT_SERVER = preferences.getInt(Constants.CONFIG_HOME_CATEGORY_COUNT, Config.DEFAULT_COUNT);
    //
    //                if (Config.HOME_CATEGORY_COUNT_SERVER > 0) {
    //
    //                    return Config.HOME_CATEGORY_COUNT_SERVER;
    //
    //                } else {
    //
    //                    return Config.HOME_CATEGORY_COUNT;
    //
    //                }
    //            } else {
    //
    //                return Config.HOME_CATEGORY_COUNT_SERVER;
    //
    //            }
    //
    //        } else if (name.equals(Constants.CONFIG_LIST_CATEGORY_COUNT)) {
    //
    //            if (Config.LIST_CATEGORY_COUNT_SERVER == 0) {
    //
    //                Config.LIST_CATEGORY_COUNT_SERVER = preferences.getInt(Constants.CONFIG_LIST_CATEGORY_COUNT, Config.DEFAULT_COUNT);
    //
    //                if (Config.LIST_CATEGORY_COUNT_SERVER > 0) {
    //
    //                    return Config.LIST_CATEGORY_COUNT_SERVER;
    //
    //                } else {
    //
    //                    return Config.LIST_CATEGORY_COUNT;
    //
    //                }
    //            } else {
    //
    //                return Config.LIST_CATEGORY_COUNT_SERVER;
    //
    //            }
    //        } else if (name.equals(Constants.CONFIG_NOTI_LIST_COUNT)) {
    //
    //            if (Config.NOTI_LIST_COUNT_SERVER == 0) {
    //
    //                Config.NOTI_LIST_COUNT_SERVER = preferences.getInt(Constants.CONFIG_NOTI_LIST_COUNT, Config.DEFAULT_COUNT);
    //
    //                if (Config.NOTI_LIST_COUNT_SERVER > 0) {
    //
    //                    return Config.NOTI_LIST_COUNT_SERVER;
    //
    //                } else {
    //
    //                    return Config.NOTI_LIST_COUNT;
    //
    //                }
    //
    //            } else {
    //
    //                return Config.NOTI_LIST_COUNT_SERVER;
    //
    //            }
    //
    //        } else if (name.equals(Constants.CONFIG_COMMENT_COUNT)) {
    //
    //            if (Config.COMMENT_COUNT_SERVER == 0) {
    //
    //                Config.COMMENT_COUNT_SERVER = preferences.getInt(Constants.CONFIG_COMMENT_COUNT, Config.DEFAULT_COUNT);
    //
    //                if (Config.COMMENT_COUNT_SERVER > 0) {
    //
    //                    return Config.COMMENT_COUNT_SERVER;
    //
    //                } else {
    //
    //                    return Config.COMMENT_COUNT;
    //
    //                }
    //
    //            } else {
    //
    //                return Config.COMMENT_COUNT_SERVER;
    //
    //            }
    //
    //        } else {
    //
    //            return Config.DEFAULT_COUNT;
    //
    //        }
    //
    //    }
    fun updateUserLoginData(pref: SharedPreferences, user: User) {
        addUserLoginData(pref, user, user.userPassword)
        deleteUserVerifyData(pref)
    }

    fun registerUserLoginData(pref: SharedPreferences, user: User, password: String?) {
        addUserLoginData(pref, user, password)
        addUserVerifyData(pref, user, password)
    }

    fun addUserLoginData(pref: SharedPreferences, user: User, password: String?) {
        pref.edit().putString(Constants.FACEBOOK_ID, user.facebookId).apply()
        //        pref.edit().putString(Constants.PHONE_ID, user.phoneId).apply();
        pref.edit().putString(Constants.GOOGLE_ID, user.googleId).apply()
        pref.edit().putString(Constants.USER_PHONE, user.userPhone).apply()
        pref.edit().putString(Constants.USER_ID, user.userId).apply()
        pref.edit().putString(Constants.USER_NAME, user.userName).apply()
        pref.edit().putString(Constants.USER_EMAIL, user.userEmail).apply()
        pref.edit().putString(Constants.USER_PASSWORD, password).apply()
    }

    private fun deleteUserVerifyData(pref: SharedPreferences) {
        pref.edit().putString(Constants.USER_EMAIL_TO_VERIFY, Constants.EMPTY_STRING).apply()
        pref.edit().putString(Constants.USER_PASSWORD_TO_VERIFY, Constants.EMPTY_STRING).apply()
        pref.edit().putString(Constants.USER_NAME_TO_VERIFY, Constants.EMPTY_STRING).apply()
        pref.edit().putString(Constants.USER_ID_TO_VERIFY, Constants.EMPTY_STRING).apply()
    }

    private fun addUserVerifyData(pref: SharedPreferences, user: User, password: String?) {
        pref.edit().putString(Constants.USER_EMAIL_TO_VERIFY, user.userEmail).apply()
        pref.edit().putString(Constants.USER_PASSWORD_TO_VERIFY, password).apply()
        pref.edit().putString(Constants.USER_NAME_TO_VERIFY, user.userName).apply()
        pref.edit().putString(Constants.USER_ID_TO_VERIFY, user.userId).apply()
    }

    fun navigateAfterUserLogin(activity: Activity?, navigationController: NavigationController2) {
        if (activity is MainActivity) {
            activity.setToolbarText(activity.binding!!.toolbar, activity.getString(R.string.profile__title))
            navigationController.navigateToUserProfile((activity as MainActivity?)!!)
        } else {
            try {
                activity?.finish()
            } catch (e: Exception) {
                psErrorLog("Error in closing parent activity.", e)
            }
        }
    }

    fun navigateAfterUserRegister(activity: Activity?, navigationController: NavigationController2) {
        if (activity is MainActivity) {
            activity.setToolbarText(activity.binding!!.toolbar, activity.getString(R.string.verify_email))
            navigationController.navigateToVerifyEmail((activity as MainActivity?)!!)
        } else {
            navigationController.navigateToVerifyEmailActivity(activity!!)
            try {
                activity?.finish()
            } catch (e: Exception) {
                psErrorLog("Error in closing parent activity.", e)
            }
        }
    }

    fun navigateAfterForgotPassword(activity: Activity?, navigationController: NavigationController2) {
        if (activity is MainActivity) {
            navigationController.navigateToUserForgotPassword((activity as MainActivity?)!!)
        } else {
            navigationController.navigateToUserForgotPasswordActivity(activity!!)
            try {
                activity?.finish()
            } catch (e: Exception) {
                psErrorLog("Error in closing activity.", e)
            }
        }
    }

    fun navigateToLogin(activity: Activity?, navigationController: NavigationController2) {
        if (activity is MainActivity) {
            navigationController.navigateToUserLogin((activity as MainActivity?)!!)
        } else {
            navigationController.navigateToUserLoginActivity(activity!!)
            try {
                activity?.finish()
            } catch (e: Exception) {
                psErrorLog("Error in closing activity.", e)
            }
        }
    }

    fun navigateAfterRegister(activity: Activity?, navigationController: NavigationController2) {
        if (activity is MainActivity) {
            navigationController.navigateToUserRegister((activity as MainActivity?)!!)
        } else {
            navigationController.navigateToUserRegisterActivity(activity!!)
            try {
                activity?.finish()
            } catch (e: Exception) {
                psErrorLog("Error in closing activity.", e)
            }
        }
    }

    fun navigateOnUserVerificationFragment(pref: SharedPreferences, user: User?, navigationController: NavigationController2, activity: MainActivity) {
        val fragmentType = pref.getString(Constants.USER_ID_TO_VERIFY, Constants.EMPTY_STRING)
        if (fragmentType != null && fragmentType.isEmpty()) {
            if (user == null) {
                activity.setToolbarText(activity.binding!!.toolbar, activity.getString(R.string.login__login))
                navigationController.navigateToUserLogin(activity)
            } else {
                activity.setToolbarText(activity.binding!!.toolbar, activity.getString(R.string.profile__title))
                navigationController.navigateToUserProfile(activity)
            }
        } else {
            activity.setToolbarText(activity.binding!!.toolbar, activity.getString(R.string.verify_email))
            navigationController.navigateToVerifyEmail(activity)
        }
    }

    fun navigateOnUserVerificationActivity(userIdToVerify: String, loginUserId: String,
                                           psDialogMsg: PSDialogMsg, activity: Activity,
                                           navigationController: NavigationController2,
                                           callback: NavigateOnUserVerificationActivityCallback
    ) {
        if (userIdToVerify.isEmpty()) {
            if (loginUserId == "") {
                psDialogMsg.showInfoDialog(activity.getString(R.string.error_message__login_first), activity.getString(R.string.app__ok))
                psDialogMsg.show()
                psDialogMsg.okButton.setOnClickListener { v1: View? ->
                    psDialogMsg.cancel()
                    navigationController.navigateToUserLoginActivity(activity)
                }
            } else {
                callback.onSuccess()
            }
        } else {
            navigationController.navigateToVerifyEmailActivity(activity)
        }
    }

    fun navigateOnUserVerificationActivityFromFav(userIdToVerify: String, loginUserId: String,
                                                  psDialogMsg: PSDialogMsg, activity: Activity,
                                                  navigationController: NavigationController2,
                                                  likeButton: LikeButton,
                                                  callback: NavigateOnUserVerificationActivityCallback
    ) {
        if (userIdToVerify.isEmpty()) {
            if (loginUserId == "") {
                likeButton.isLiked = false
                psDialogMsg.showInfoDialog(activity.getString(R.string.error_message__login_first), activity.getString(R.string.app__ok))
                psDialogMsg.show()
                psDialogMsg.okButton.setOnClickListener { v1: View? ->
                    psDialogMsg.cancel()
                    navigationController.navigateToUserLoginActivity(activity)
                }
            } else {
                callback.onSuccess()
            }
        } else {
            likeButton.isLiked = false
            navigationController.navigateToVerifyEmailActivity(activity)
        }
    }

    fun navigateAfterLogin(activity: Activity?, navigationController: NavigationController2) {
        if (activity is MainActivity) {
            navigationController.navigateToUserLogin((activity as MainActivity?)!!)
        } else {
            navigationController.navigateToUserLoginActivity(activity!!)
            try {
                activity?.finish()
            } catch (e: Exception) {
                psErrorLog("Error in closing activity.", e)
            }
        }
    }

    fun navigateAfterPhoneVerify(activity: Activity?, navigationController: NavigationController2, number: String?, username: String?) {
        if (activity is MainActivity) {
            navigationController.navigateToPhoneVerifyFragment((activity as MainActivity?)!!, number, username)
        } else {
            navigationController.navigateToPhoneVerifyActivity(activity!!, number, username)
            try {
                activity?.finish()
            } catch (e: Exception) {
                psErrorLog("Error in closing activity.", e)
            }
        }
    }

    fun navigateAfterPhoneLogin(activity: Activity?, navigationController: NavigationController2) {
        if (activity is MainActivity) {
            navigationController.navigateToPhoneLoginFragment((activity as MainActivity?)!!)
        } else {
            navigationController.navigateToPhoneLoginActivity(activity!!)
            try {
                activity?.finish()
            } catch (e: Exception) {
                psErrorLog("Error in closing activity.", e)
            }
        }
    }

    enum class Fonts {
        ROBOTO, NOTO_SANS, ROBOTO_LIGHT, ROBOTO_MEDIUM, MM_FONT
    }

    enum class LoadingDirection {
        top, bottom, none
    }

    interface NavigateOnUserVerificationActivityCallback {
        fun onSuccess()
    }

    init {
        suffixes[1000L] = "k"
        suffixes[1000000L] = "M"
        suffixes[1000000000L] = "G"
        suffixes[1000000000000L] = "T"
        suffixes[1000000000000000L] = "P"
        suffixes[1000000000000000000L] = "E"
    }
}