package com.dewonderstruck.apps.ashx0.viewobject

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import com.google.gson.annotations.SerializedName

/**
 * Created by Vamsi Madduluri on 9/17/18.
 * Contact Email : vamsii.wrkhost@gmail.com
 */
@Entity(primaryKeys = ["id"])
class Product(@field:SerializedName("id") val id: String, @field:SerializedName("cat_id") val catId: String, @field:SerializedName("sub_cat_id") val subCatId: String, @field:SerializedName("is_discount") val isDiscount: String, @field:SerializedName("is_featured") val isFeatured: String, @field:SerializedName("is_available") val isAvailable: String, @field:SerializedName("code") val code: String, @field:SerializedName("name") val name: String, @field:SerializedName("description") val description: String, @field:SerializedName("search_tag") val searchTag: String, @field:SerializedName("highlight_information") val highlightInformation: String, @field:SerializedName("status") val status: String, @field:SerializedName("added_date") val addedDate: String, @field:SerializedName("added_user_id") val addedUserId: String, @field:SerializedName("updated_date") val updatedDate: String, @field:SerializedName("updated_user_id") val updatedUserId: String, @field:SerializedName("updated_flag") val updatedFlag: String, @field:SerializedName("deleted_flag") val deletedFlag: String, @field:SerializedName("added_date_str") val addedDateStr: String, @field:SerializedName("shipping_cost") val shippingCost: String, @field:SerializedName("minimum_order") val minimumOrder: String, @field:SerializedName("product_unit") val productUnit: String, @field:SerializedName("product_measurement") val productMeasurement: String, @field:SerializedName("default_photo") @field:Embedded val defaultPhoto: Image, @field:SerializedName("category") @field:Embedded(prefix = "category_") val category: Category, @field:SerializedName("sub_category") @field:Embedded(prefix = "subCategory_") val subCategory: SubCategory, @field:SerializedName("rating_details") @field:Embedded(prefix = "ratingDetails_") var ratingDetails: RatingDetail, @field:SerializedName("like_count") val likeCount: Int, @field:SerializedName("image_count") val imageCount: Int, @field:SerializedName("favourite_count") val favouriteCount: Int, @field:SerializedName("touch_count") val touchCount: Int, @field:SerializedName("featured_date") val featuredDate: String, @field:SerializedName("comment_header_count") val commentHeaderCount: Int, @field:SerializedName("original_price") val originalPrice: Float, @field:SerializedName("unit_price") val unitPrice: Float, @field:SerializedName("discount_amount") val discountAmount: Float, @field:SerializedName("currency_short_form") val currencyShortForm: String, @field:SerializedName("currency_symbol") val currencySymbol: String, @field:SerializedName("discount_percent") val discountPercent: Float, @field:SerializedName("discount_value") val discountValue: Float, @field:SerializedName("is_liked") val isLiked: String, @field:SerializedName("is_favourited") val isFavourited: String, @field:SerializedName("overall_rating") val overallRating: String, @field:SerializedName("trans_status") val transStatus: String, @field:SerializedName("product_link") val productLink: String) {

    @kotlin.jvm.JvmField
    @SerializedName("attributes_header")
    @Ignore
    var attributesHeaderList: List<ProductAttributeHeader>? = null

    @kotlin.jvm.JvmField
    @SerializedName("colors")
    @Ignore
    var productColorList: List<ProductColor>? = null

    @kotlin.jvm.JvmField
    @SerializedName("specs")
    @Ignore
    var productSpecsList: List<ProductSpecs>? = null

}