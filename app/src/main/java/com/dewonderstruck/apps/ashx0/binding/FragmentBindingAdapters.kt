package com.dewonderstruck.apps.ashx0.binding

import javax.inject.Inject
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import android.graphics.Bitmap
import com.dewonderstruck.apps.ashx0.viewobject.common.SyncStatus
import android.content.SharedPreferences
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.TypedValue
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.bumptech.glide.request.RequestOptions
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.utils.Utils

/**
 * Binding adapters that work with a fragment instance.
 */
class FragmentBindingAdapters @Inject constructor(private val fragment: Fragment?) {
    @BindingAdapter("imageUrl")
    fun bindImage(imageView: ImageView?, imageUrl: String?) {
        var imageUrl: String? = imageUrl
        if ((isValid(imageView, imageUrl))!!) {
            val fullUrl: String? = Config.APP_IMAGES_URL + imageUrl
            imageUrl = Config.APP_IMAGES_THUMB_URL + imageUrl
            Utils.psLog("Image : " + imageUrl)
            if (Config.PRE_LOAD_FULL_IMAGE) {
                Glide.with((fragment)!!).load(fullUrl).thumbnail(Glide.with((fragment)!!).load(imageUrl)).into((imageView)!!)
            } else {
                Glide.with((fragment)!!).load(imageUrl).thumbnail(0.08f).into((imageView)!!)
            }
        } else {
            if (imageView != null) {
                imageView.setImageResource(R.drawable.placeholder_image)
            }
        }
    }

    @BindingAdapter("bindFullImage")
    fun bindFullImage(imageView: ImageView?, imageUrl: String?) {
        var imageUrl: String? = imageUrl
        if ((isValid(imageView, imageUrl))!!) {
            val fullUrl: String? = Config.APP_IMAGES_URL + imageUrl
            imageUrl = Config.APP_IMAGES_THUMB_URL + imageUrl
            Utils.psLog("Image : " + imageUrl)
            Glide.with((fragment)!!).load(fullUrl).thumbnail(Glide.with((fragment)!!).load(imageUrl)).into((imageView)!!)
        } else {
            if (imageView != null) {
                imageView.setImageResource(R.drawable.placeholder_image)
            }
        }
    }

    @BindingAdapter("bindFullImageDrawable")
    fun bindFullImageDrawbale(imageView: ImageView?, drawable: Drawable?) {
        if (imageView != null && drawable != null) {
            Glide.with((fragment)!!).load(drawable).thumbnail(Glide.with((fragment)!!).load(drawable)).into(imageView)
        } else {
            if (imageView != null) {
                imageView.setImageResource(R.drawable.placeholder_image)
            }
        }
    }

    @BindingAdapter("bindFullImageUri")
    fun bindFullImageUri(imageView: ImageView?, uri: Uri?) {
        if (imageView != null && uri != null) {
            Glide.with((fragment)!!).load(uri).thumbnail(Glide.with((fragment)!!).load(uri)).into(imageView)
        } else {
            if (imageView != null) {
                imageView.setImageResource(R.drawable.placeholder_image)
            }
        }
    }

    @BindingAdapter("bindFullImageUri")
    fun bindFullBitmap(imageView: ImageView?, bitmap: Bitmap?) {
        if (imageView != null && bitmap != null) {
            Glide.with((fragment)!!).load(bitmap).thumbnail(Glide.with((fragment)!!).load(bitmap)).into(imageView)
        } else {
            if (imageView != null) {
                imageView.setImageResource(R.drawable.placeholder_image)
            }
        }
    }

    @BindingAdapter("imageCircleUrl")
    fun bindCircleImage(imageView: ImageView?, url: String?) {
        var url: String? = url
        if ((isValid(imageView, url))!!) {
            url = Config.APP_IMAGES_URL + url
            Glide.with((fragment)!!).load(url).apply(RequestOptions.circleCropTransform()
                    .placeholder(R.drawable.circle_default_image)
            ).into((imageView)!!)
        } else {
            if (imageView != null) {
                imageView.setImageResource(R.drawable.circle_default_image)
            }
        }
    }

    @BindingAdapter("imageCircleUrl")
    fun bindCircleBitmap(imageView: ImageView?, bitmap: Bitmap?) {
        if ((isBitmapValid(imageView, bitmap))!!) {
            Glide.with((fragment)!!).load(bitmap).apply(RequestOptions.circleCropTransform()
                    .placeholder(R.drawable.circle_default_image)
            ).into((imageView)!!)
        } else {
            if (imageView != null) {
                imageView.setImageResource(R.drawable.circle_default_image)
            }
        }
    }

    @BindingAdapter("likeImage")
    fun bindLikeImage(imageView: ImageView?, isLiked: String?) {
        if ((isValid(imageView, isLiked))!!) {
            when (isLiked) {
                SyncStatus.SERVER_SELECTED -> imageView!!.setImageResource(R.drawable.baseline_like_orange_24)
                SyncStatus.SERVER_NOT_SELECTED -> imageView!!.setImageResource(R.drawable.baseline_like_grey_24)
                else -> imageView!!.setImageResource(R.drawable.baseline_like_grey_24)
            }
        } else {
            if (imageView != null) {
                imageView.setImageResource(R.drawable.baseline_like_grey_24)
            }
        }
    }

    @BindingAdapter("favImage")
    fun bindFavouriteImage(imageView: ImageView?, isFavourite: String?) {
        if ((isValid(imageView, isFavourite))!!) {
            when (isFavourite) {
                SyncStatus.SERVER_SELECTED -> imageView!!.setImageResource(R.drawable.liked)
                SyncStatus.SERVER_NOT_SELECTED -> imageView!!.setImageResource(R.drawable.like)
                else -> imageView!!.setImageResource(R.drawable.like)
            }
        } else {
            if (imageView != null) {
                imageView.setImageResource(R.drawable.like)
            }
        }
    }

    @BindingAdapter("imageProfileUrl")
    fun bindProfileImage(imageView: ImageView?, url: String?) {
        var url: String? = url
        if ((isValid(imageView, url))!!) {
            url = Config.APP_IMAGES_URL + url
            Glide.with((fragment)!!).load(url).apply(RequestOptions.circleCropTransform()
                    .placeholder(R.drawable.default_profile)
            ).into((imageView)!!)
        } else {
            if (imageView != null) {
                imageView.setImageResource(R.drawable.default_profile)
            }
        }
    }

    @BindingAdapter("font")
    fun setFont(textView: TextView?, type: String?) {
        val preferences: SharedPreferences? = PreferenceManager.getDefaultSharedPreferences(textView!!.getContext())
        val LANG_CURRENT: String? = preferences!!.getString("Language", Config.DEFAULT_LANGUAGE)
        when (type) {
            "normal" -> //                if(LANG_CURRENT.equals(Config.LANGUAGE_CODE))
//                {
                textView!!.setTypeface(Utils.getTypeFace(textView!!.getContext(), Utils.Fonts.ROBOTO))
            "bold" -> if ((LANG_CURRENT == Config.LANGUAGE_CODE)) {
                textView!!.setTypeface(Utils.getTypeFace(textView!!.getContext(), Utils.Fonts.ROBOTO), Typeface.BOLD)
            }
            "bold_italic" -> if ((LANG_CURRENT == Config.LANGUAGE_CODE)) {
                textView!!.setTypeface(Utils.getTypeFace(textView!!.getContext(), Utils.Fonts.ROBOTO), Typeface.BOLD_ITALIC)
            }
            "italic" -> if ((LANG_CURRENT == Config.LANGUAGE_CODE)) {
                textView!!.setTypeface(Utils.getTypeFace(textView!!.getContext(), Utils.Fonts.ROBOTO), Typeface.ITALIC)
            }
            "medium" -> if ((LANG_CURRENT == Config.LANGUAGE_CODE)) {
                textView!!.setTypeface(Utils.getTypeFace(textView!!.getContext(), Utils.Fonts.ROBOTO_MEDIUM))
            }
            "light" -> if ((LANG_CURRENT == Config.LANGUAGE_CODE)) {
                textView!!.setTypeface(Utils.getTypeFace(textView!!.getContext(), Utils.Fonts.ROBOTO_LIGHT))
            }
            else -> if ((LANG_CURRENT == Config.LANGUAGE_CODE)) {
                textView!!.setTypeface(Utils.getTypeFace(textView!!.getContext(), Utils.Fonts.ROBOTO))
            }
        }
    }

    @BindingAdapter("font")
    fun setFont(checkBox: CheckBox?, type: String?) {
        val preferences: SharedPreferences? = PreferenceManager.getDefaultSharedPreferences(checkBox!!.getContext())
        val LANG_CURRENT: String? = preferences!!.getString("Language", Config.DEFAULT_LANGUAGE)
        when (type) {
            "normal" -> //                if(LANG_CURRENT.equals(Config.LANGUAGE_CODE))
//                {
                checkBox!!.setTypeface(Utils.getTypeFace(checkBox!!.getContext(), Utils.Fonts.ROBOTO))
            "bold" -> if ((LANG_CURRENT == Config.LANGUAGE_CODE)) {
                checkBox!!.setTypeface(Utils.getTypeFace(checkBox!!.getContext(), Utils.Fonts.ROBOTO), Typeface.BOLD)
            }
            "bold_italic" -> if ((LANG_CURRENT == Config.LANGUAGE_CODE)) {
                checkBox!!.setTypeface(Utils.getTypeFace(checkBox!!.getContext(), Utils.Fonts.ROBOTO), Typeface.BOLD_ITALIC)
            }
            "italic" -> if ((LANG_CURRENT == Config.LANGUAGE_CODE)) {
                checkBox!!.setTypeface(Utils.getTypeFace(checkBox!!.getContext(), Utils.Fonts.ROBOTO), Typeface.ITALIC)
            }
            "medium" -> if ((LANG_CURRENT == Config.LANGUAGE_CODE)) {
                checkBox!!.setTypeface(Utils.getTypeFace(checkBox!!.getContext(), Utils.Fonts.ROBOTO_MEDIUM))
            }
            "light" -> if ((LANG_CURRENT == Config.LANGUAGE_CODE)) {
                checkBox!!.setTypeface(Utils.getTypeFace(checkBox!!.getContext(), Utils.Fonts.ROBOTO_LIGHT))
            }
            else -> if ((LANG_CURRENT == Config.LANGUAGE_CODE)) {
                checkBox!!.setTypeface(Utils.getTypeFace(checkBox!!.getContext(), Utils.Fonts.ROBOTO))
            }
        }
    }

    @BindingAdapter("font")
    fun setFont(editText: EditText?, type: String?) {
        val preferences: SharedPreferences? = PreferenceManager.getDefaultSharedPreferences(editText!!.getContext())
        val LANG_CURRENT: String? = preferences!!.getString("Language", Config.DEFAULT_LANGUAGE)
        when (type) {
            "normal" -> //                if(LANG_CURRENT.equals(Config.LANGUAGE_CODE))
//                {
//                    textView.setTypeface(Utils.getTypeFace(textView.getContext(),Utils.Fonts.ROBOTO));
//                }else {
                editText!!.setTypeface(Utils.getTypeFace(editText!!.getContext(), Utils.Fonts.ROBOTO))
            "bold" -> if ((LANG_CURRENT == Config.LANGUAGE_CODE)) {
                editText!!.setTypeface(Utils.getTypeFace(editText!!.getContext(), Utils.Fonts.ROBOTO), Typeface.BOLD)
            } else {
                editText!!.setTypeface(Utils.getTypeFace(editText!!.getContext(), Utils.Fonts.MM_FONT), Typeface.BOLD)
            }
            "bold_italic" -> if ((LANG_CURRENT == Config.LANGUAGE_CODE)) {
                editText!!.setTypeface(Utils.getTypeFace(editText!!.getContext(), Utils.Fonts.ROBOTO), Typeface.BOLD_ITALIC)
            } else {
                editText!!.setTypeface(Utils.getTypeFace(editText!!.getContext(), Utils.Fonts.MM_FONT), Typeface.BOLD_ITALIC)
            }
            "italic" -> if ((LANG_CURRENT == Config.LANGUAGE_CODE)) {
                editText!!.setTypeface(Utils.getTypeFace(editText!!.getContext(), Utils.Fonts.ROBOTO), Typeface.ITALIC)
            } else {
                editText!!.setTypeface(Utils.getTypeFace(editText!!.getContext(), Utils.Fonts.MM_FONT), Typeface.ITALIC)
            }
            "medium" -> if ((LANG_CURRENT == Config.LANGUAGE_CODE)) {
                editText!!.setTypeface(Utils.getTypeFace(editText!!.getContext(), Utils.Fonts.ROBOTO_MEDIUM))
            } else {
                editText!!.setTypeface(Utils.getTypeFace(editText!!.getContext(), Utils.Fonts.MM_FONT), Typeface.BOLD)
            }
            "light" -> if ((LANG_CURRENT == Config.LANGUAGE_CODE)) {
                editText!!.setTypeface(Utils.getTypeFace(editText!!.getContext(), Utils.Fonts.ROBOTO_LIGHT))
            } else {
                editText!!.setTypeface(Utils.getTypeFace(editText!!.getContext(), Utils.Fonts.MM_FONT), Typeface.BOLD)
            }
            else -> if ((LANG_CURRENT == Config.LANGUAGE_CODE)) {
                editText!!.setTypeface(Utils.getTypeFace(editText!!.getContext(), Utils.Fonts.ROBOTO))
            } else {
                editText!!.setTypeface(Utils.getTypeFace(editText!!.getContext(), Utils.Fonts.MM_FONT), Typeface.BOLD)
            }
        }
    }

    @BindingAdapter("font")
    fun setFont(button: Button?, type: String?) {
//        switch (type) {
//            case "normal":
//                button.setTypeface(Utils.getTypeFace(button.getContext(), Utils.Fonts.MM_FONT));
//                break;
//            case "medium":
//                button.setTypeface(Utils.getTypeFace(button.getContext(), Utils.Fonts.MM_FONT));
//                break;
//            case "light":
//                button.setTypeface(Utils.getTypeFace(button.getContext(), Utils.Fonts.MM_FONT));
//                break;
//            default:
//                button.setTypeface(Utils.getTypeFace(button.getContext(), Utils.Fonts.MM_FONT));
//                break;
//
//
//        }
        when (type) {
            "normal" -> button!!.setTypeface(Utils.getTypeFace(button!!.getContext(), Utils.Fonts.ROBOTO))
            "medium" -> button!!.setTypeface(Utils.getTypeFace(button!!.getContext(), Utils.Fonts.ROBOTO_MEDIUM))
            "light" -> button!!.setTypeface(Utils.getTypeFace(button!!.getContext(), Utils.Fonts.ROBOTO_LIGHT))
            else -> button!!.setTypeface(Utils.getTypeFace(button!!.getContext(), Utils.Fonts.ROBOTO))
        }
    }

    @BindingAdapter("textSize")
    fun setTextSize(textView: TextView?, dimenType: String?) {
        var dimenPix: Float = 0f
        when (dimenType) {
            "font_h1_size" -> {
                dimenPix = textView!!.getResources().getDimensionPixelOffset(R.dimen.font_h1_size).toFloat()
                textView!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimenPix)
            }
            "font_h2_size" -> {
                dimenPix = textView!!.getResources().getDimensionPixelOffset(R.dimen.font_h2_size).toFloat()
                textView!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimenPix)
            }
            "font_h3_size" -> {
                dimenPix = textView!!.getResources().getDimensionPixelOffset(R.dimen.font_h3_size).toFloat()
                textView!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimenPix)
            }
            "font_h4_size" -> {
                dimenPix = textView!!.getResources().getDimensionPixelOffset(R.dimen.font_h4_size).toFloat()
                textView!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimenPix)
            }
            "font_h5_size" -> {
                dimenPix = textView!!.getResources().getDimensionPixelOffset(R.dimen.font_h5_size).toFloat()
                textView!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimenPix)
            }
            "font_h6_size" -> {
                dimenPix = textView!!.getResources().getDimensionPixelOffset(R.dimen.font_h6_size).toFloat()
                textView!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimenPix)
            }
            "font_h7_size" -> {
                dimenPix = textView!!.getResources().getDimensionPixelOffset(R.dimen.font_h7_size).toFloat()
                textView!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimenPix)
            }
            "font_title_size" -> {
                dimenPix = textView!!.getResources().getDimensionPixelOffset(R.dimen.font_title_size).toFloat()
                textView!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimenPix)
            }
            "font_body_size" -> {
                dimenPix = textView!!.getResources().getDimensionPixelOffset(R.dimen.font_body_size).toFloat()
                textView!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimenPix)
            }
            "font_body_s_size" -> {
                dimenPix = textView!!.getResources().getDimensionPixelOffset(R.dimen.font_body_s_size).toFloat()
                textView!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimenPix)
            }
            "font_body_xs_size" -> {
                dimenPix = textView!!.getResources().getDimensionPixelOffset(R.dimen.font_body_xs_size).toFloat()
                textView!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimenPix)
            }
        }
    }

    @BindingAdapter("textSize")
    fun setTextSize(editText: EditText?, dimenType: String?) {
        var dimenPix: Float = 0f
        when (dimenType) {
            "edit_text" -> {
                dimenPix = editText!!.getResources().getDimensionPixelOffset(R.dimen.edit_text__size).toFloat()
                editText!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimenPix)
            }
        }
    }

    @BindingAdapter("textSize")
    fun setTextSize(button: Button?, dimenType: String?) {
        var dimenPix: Float = 0f
        when (dimenType) {
            "button_text" -> {
                dimenPix = button!!.getResources().getDimensionPixelOffset(R.dimen.button__text_size).toFloat()
                button!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimenPix)
            }
        }
    }

    //    @BindingAdapter("youTubeImage")
    //    public void bindYouTubeImage(ImageView imageView, String youTubeId) {
    //
    //        if(isValid(imageView, youTubeId)) {
    //
    //            String url = String.format(Config.YOUTUBE_IMAGE_BASE_URL, youTubeId);
    //            Glide.with(fragment).load(url).apply(new RequestOptions()
    //                    .placeholder(R.drawable.default_image)
    //                    .centerCrop()
    //                    .dontAnimate()
    //                    .dontTransform()).into(imageView);
    //
    //        } else {
    //
    //            if(imageView != null) {
    //                imageView.setImageResource(R.drawable.default_image);
    //            }
    //        }
    //    }
    private fun isValid(imageView: ImageView?, url: String?): Boolean? {
        return !((url == null
                ) || (imageView == null
                ) || (fragment == null
                ) || (url == ""))
    }

    private fun isBitmapValid(imageView: ImageView?, bitmap: Bitmap?): Boolean? {
        val emptyBitmap: Bitmap? = Bitmap.createBitmap(bitmap!!.getWidth(), bitmap!!.getHeight(), bitmap!!.getConfig())
        return !((imageView == null
                ) || (fragment == null
                ) || bitmap!!.sameAs(emptyBitmap))
    }

}