package com.dewonderstruck.apps.ashx0.repository.product

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.dewonderstruck.apps.AppExecutors
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.ashx0.api.ApiResponse
import com.dewonderstruck.apps.ashx0.api.ApiService
import com.dewonderstruck.apps.ashx0.db.PSCoreDb
import com.dewonderstruck.apps.ashx0.db.ProductDao
import com.dewonderstruck.apps.ashx0.repository.common.NetworkBoundResource
import com.dewonderstruck.apps.ashx0.repository.common.PSRepository
import com.dewonderstruck.apps.ashx0.utils.Constants
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewobject.*
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource.Companion.error
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource.Companion.success
import com.dewonderstruck.apps.ashx0.viewobject.holder.ProductParameterHolder
import retrofit2.Response
import java.io.IOException
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Vamsi Madduluri on 9/18/18.
 * Contact Email : vamsii.wrkhost@gmail.com
 */
@Singleton
class ProductRepository @Inject constructor(apiService: ApiService?, appExecutors: AppExecutors?, db: PSCoreDb?, productDao: ProductDao) : PSRepository(apiService!!, appExecutors!!, db!!) {
    //region Variables
    private val productDao: ProductDao
    private var isSelected: String? = null

    //endregion
    //region Constructor
    @JvmField
    @Inject
    var pref: SharedPreferences? = null

    //endregion
    //region Get Product List
    fun getProductListByKey(productParameterHolder: ProductParameterHolder, loginUserId: String?, limit: String, offset: String?): LiveData<Resource<List<Product?>?>> {
        return object : NetworkBoundResource<List<Product?>?, List<Product?>?>(appExecutors) {
            protected override fun saveCallResult(itemList: List<Product?>) {
                Utils.psLog("SaveCallResult of getProductListByKey.")
                try {
                    db.beginTransaction()
                    val mapKey = productParameterHolder.keyForProductMap
                    db.productMapDao()!!.deleteByMapKey(mapKey)
                    db.productDao()!!.insertAll(itemList)
                    val dateTime = Utils.getDateTime()
                    for (i in itemList.indices) {
                        db.productMapDao()!!.insert(ProductMap(mapKey + itemList[i]!!.id, mapKey, itemList[i]!!.id, i + 1, dateTime))
                    }
                    db.setTransactionSuccessful()
                } catch (e: Exception) {
                    Utils.psErrorLog("Error in doing transaction of getProductListByKey.", e)
                } finally {
                    db.endTransaction()
                }
            }

            override fun shouldFetch(data: List<Product?>?): Boolean {

                // Recent news always load from server
                return connectivity!!.isConnected
            }

            override fun loadFromDb(): LiveData<List<Product?>> {
                Utils.psLog("Load getProductListByKey From Db")
                val mapKey = productParameterHolder.keyForProductMap
                return if (limit != java.lang.String.valueOf(Config.LOAD_FROM_DB)) {
                    productDao.getProductsByKey(mapKey)
                } else {
                    productDao.getProductsByKeyByLimit(mapKey, limit)
                }
            }

            override fun createCall(): LiveData<ApiResponse<List<Product?>?>?> {
                Utils.psLog("Call API Service to getProductListByKey.")
                Utils.psLog("holder.searchTerm : " + productParameterHolder.search_term)
                Utils.psLog("holder.catId : " + productParameterHolder.catId)
                Utils.psLog("holder.subCatId : " + productParameterHolder.subCatId)
                Utils.psLog("holder.isFeatured : " + productParameterHolder.isFeatured)
                Utils.psLog("holder.isDiscount : " + productParameterHolder.isDiscount)
                Utils.psLog("holder.isAvailable : " + productParameterHolder.isAvailable)
                Utils.psLog("holder.max_price : " + productParameterHolder.max_price)
                Utils.psLog("holder.min_price : " + productParameterHolder.min_price)
                Utils.psLog("holder.overallRating : " + productParameterHolder.overall_rating)
                Utils.psLog("holder.order_by : " + productParameterHolder.order_by)
                Utils.psLog("holder.order_type : " + productParameterHolder.order_type)
                return apiService.searchProduct(Config.API_KEY, Utils.checkUserId(loginUserId), limit, offset, productParameterHolder.search_term, productParameterHolder.catId,
                        productParameterHolder.subCatId, productParameterHolder.isFeatured, productParameterHolder.isDiscount, productParameterHolder.isAvailable, productParameterHolder.max_price,
                        java.lang.String.valueOf(productParameterHolder.min_price), productParameterHolder.overall_rating, productParameterHolder.order_by, productParameterHolder.order_type)!!
            }

            override fun onFetchFailed(message: String?) {
                Utils.psLog("Fetch Failed (getProductListByKey) : $message")
            }
        }.asLiveData()
    }

    fun getNextPageProductListByKey(productParameterHolder: ProductParameterHolder, loginUserId: String?, limit: String?, offset: String?): LiveData<Resource<Boolean?>> {
        val statusLiveData = MediatorLiveData<Resource<Boolean?>>()
        prepareRatingValueForServer(productParameterHolder)
        val apiResponse = apiService.searchProduct(Config.API_KEY, Utils.checkUserId(loginUserId), limit, offset, productParameterHolder.search_term, productParameterHolder.catId,
                productParameterHolder.subCatId, productParameterHolder.isFeatured, productParameterHolder.isDiscount, productParameterHolder.isAvailable, productParameterHolder.max_price,
                java.lang.String.valueOf(productParameterHolder.min_price), productParameterHolder.overall_rating, productParameterHolder.order_by, productParameterHolder.order_type)
        statusLiveData.addSource(apiResponse!!) { response: ApiResponse<List<Product?>?>? ->
            statusLiveData.removeSource(apiResponse)
            if (response!!.isSuccessful) {
                if (response.body != null) {
                    appExecutors.diskIO().execute {
                        try {
                            db.beginTransaction()
                            db.productDao()!!.insertAll(response.body)
                            val finalIndex = db.productMapDao()!!.getMaxSortingByValue(productParameterHolder.keyForProductMap)
                            val startIndex = finalIndex + 1
                            val mapKey = productParameterHolder.keyForProductMap
                            val dateTime = Utils.getDateTime()
                            for (i in response.body.indices) {
                                db.productMapDao()!!.insert(ProductMap(mapKey + response.body.get(i)!!.id, mapKey, response.body.get(i)!!.id, startIndex + i, dateTime))
                            }
                            db.setTransactionSuccessful()
                        } catch (ne: NullPointerException) {
                            Utils.psErrorLog("Null Pointer Exception : ", ne)
                        } catch (e: Exception) {
                            Utils.psErrorLog("Exception : ", e)
                        } finally {
                            db.endTransaction()
                        }
                        statusLiveData.postValue(success(true))
                    }
                } else {
                    statusLiveData.postValue(error(response.errorMessage, null))
                }
            } else {
                statusLiveData.postValue(error(response.errorMessage, null))
            }
        }
        return statusLiveData
    }

    //endregion
    //region Get Related Product
    fun getRelatedList(apiKey: String?, productId: String?, catId: String?): LiveData<Resource<List<Product?>?>> {
        return object : NetworkBoundResource<List<Product?>?, List<Product?>?>(appExecutors) {
            protected override fun saveCallResult(itemList: List<Product?>) {
                Utils.psLog("SaveCallResult of related products.")
                db.beginTransaction()
                try {
                    productDao.deleteAllRelatedProducts()
                    productDao.deleteAllBasedOnRelated()
                    productDao.insertAll(itemList)
                    for (item in itemList) {
                        productDao.insert(RelatedProduct(item!!.id))
                    }
                    db.setTransactionSuccessful()
                } catch (e: Exception) {
                    Utils.psErrorLog("Error in doing transaction of related list.", e)
                } finally {
                    db.endTransaction()
                }
            }

            override fun shouldFetch(data: List<Product?>?): Boolean {

                // Recent news always load from server
                return connectivity!!.isConnected
            }

            override fun loadFromDb(): LiveData<List<Product?>> {
                Utils.psLog("Load related From Db")
                return productDao.allRelatedProducts
            }

            override fun createCall(): LiveData<ApiResponse<List<Product?>?>?> {
                Utils.psLog("Call API Service to get related.")
                return apiService.getProductDetailRelatedList(apiKey, productId, catId)!!
            }

            override fun onFetchFailed(message: String?) {
                Utils.psLog("Fetch Failed (getRelated) : $message")
            }
        }.asLiveData()
    }

    //endregion
    //region Get Favourite Product
    fun getFavouriteList(apiKey: String?, loginUserId: String?, offset: String?): LiveData<Resource<List<Product?>?>> {
        return object : NetworkBoundResource<List<Product?>?, List<Product?>?>(appExecutors) {
            protected override fun saveCallResult(itemList: List<Product?>) {
                Utils.psLog("SaveCallResult of related products.")
                db.beginTransaction()
                try {
                    productDao.deleteAllFavouriteProducts()
                    productDao.insertAll(itemList)
                    for (i in itemList.indices) {
                        productDao.insertFavourite(FavouriteProduct(itemList[i]!!.id, i + 1))
                    }
                    db.setTransactionSuccessful()
                } catch (e: Exception) {
                    Utils.psErrorLog("Error in doing transaction of related list.", e)
                } finally {
                    db.endTransaction()
                }
            }

            override fun shouldFetch(data: List<Product?>?): Boolean {

                // Recent news always load from server
                return connectivity!!.isConnected
            }

            override fun loadFromDb(): LiveData<List<Product?>> {
                Utils.psLog("Load related From Db")
                return productDao.allFavouriteProducts
            }

            override fun createCall(): LiveData<ApiResponse<List<Product?>?>?> {
                Utils.psLog("Call API Service to get related.")
                return apiService.getFavouriteList(apiKey, Utils.checkUserId(loginUserId), java.lang.String.valueOf(Config.PRODUCT_COUNT), offset)!! //////////////////////////////////////
            }

            override fun onFetchFailed(message: String?) {
                Utils.psLog("Fetch Failed (getRelated) : $message")
            }
        }.asLiveData()
    }

    fun getNextPageFavouriteProductList(loginUserId: String?, offset: String?): LiveData<Resource<Boolean?>> {
        val statusLiveData = MediatorLiveData<Resource<Boolean?>>()
        val apiResponse = apiService.getFavouriteList(Config.API_KEY, Utils.checkUserId(loginUserId), java.lang.String.valueOf(Config.PRODUCT_COUNT), offset)
        statusLiveData.addSource(apiResponse!!) { response: ApiResponse<List<Product?>?>? ->
            statusLiveData.removeSource(apiResponse)
            if (response!!.isSuccessful) {
                appExecutors.diskIO().execute {
                    try {
                        db.beginTransaction()
                        if (response.body != null) {
                            var lastIndex = db.productDao()!!.maxSortingFavourite
                            lastIndex = lastIndex + 1
                            for (i in response.body.indices) {
                                db.productDao()!!.insertFavourite(FavouriteProduct(response.body.get(i)!!.id, lastIndex + i))
                            }
                            db.productDao()!!.insertAll(response.body)
                        }
                        db.setTransactionSuccessful()
                    } catch (ne: NullPointerException) {
                        Utils.psErrorLog("Null Pointer Exception : ", ne)
                    } catch (e: Exception) {
                        Utils.psErrorLog("Exception : ", e)
                    } finally {
                        db.endTransaction()
                    }
                    statusLiveData.postValue(success(true))
                }
            } else {
                statusLiveData.postValue(error(response.errorMessage, null))
            }
        }
        return statusLiveData
    }

    //endregion
    //region Get Product List by CatId
    fun getProductListByCatId(apiKey: String?, loginUserId: String?, offset: String?, catId: String?): LiveData<Resource<List<Product?>?>> {
        return object : NetworkBoundResource<List<Product?>?, List<Product?>?>(appExecutors) {
            protected override fun saveCallResult(itemList: List<Product?>) {
                Utils.psLog("SaveCallResult of recent products.")
                db.beginTransaction()
                try {
                    productDao.deleteAllProductListByCatIdVOs()
                    productDao.insertAll(itemList)
                    for (item in itemList) {
                        productDao.insert(ProductListByCatId(item!!.id, item.catId))
                    }
                    db.setTransactionSuccessful()
                } catch (e: Exception) {
                    Utils.psErrorLog("Error in doing transaction of product list by catId .", e)
                } finally {
                    db.endTransaction()
                }
            }

            override fun shouldFetch(data: List<Product?>?): Boolean {

                // Recent news always load from server
                return connectivity!!.isConnected
            }

            override fun loadFromDb(): LiveData<List<Product?>> {
                Utils.psLog("Load product From Db by catId")
                return productDao.getAllProductListByCatId(catId)
            }

            override fun createCall(): LiveData<ApiResponse<List<Product?>?>?> {
                Utils.psLog("Call API Service to get product list by catId.")
                return apiService.getProductListByCatId(apiKey, Utils.checkUserId(loginUserId), java.lang.String.valueOf(Config.PRODUCT_COUNT), offset, catId)!!
            }

            override fun onFetchFailed(message: String?) {
                Utils.psLog("Fetch Failed (get product list by catId) : $message")
            }
        }.asLiveData()
    }

    // get next page Product List by catId
    fun getNextPageProductListByCatId(loginUserId: String?, offset: String?, catId: String?): LiveData<Resource<Boolean?>> {
        val statusLiveData = MediatorLiveData<Resource<Boolean?>>()
        val apiResponse = apiService.getProductListByCatId(Config.API_KEY, Utils.checkUserId(loginUserId), java.lang.String.valueOf(Config.PRODUCT_COUNT), offset, catId)
        statusLiveData.addSource(apiResponse!!) { response: ApiResponse<List<Product?>?>? ->
            statusLiveData.removeSource(apiResponse)
            if (response!!.isSuccessful) {
                appExecutors.diskIO().execute {
                    try {
                        db.beginTransaction()
                        if (response.body != null) {
                            for (item in response.body) {
                                db.productDao()!!.insert(ProductListByCatId(item!!.id, item!!.catId))
                            }
                            db.productDao()!!.insertAll(response.body)
                        }
                        db.setTransactionSuccessful()
                    } catch (ne: NullPointerException) {
                        Utils.psErrorLog("Null Pointer Exception : ", ne)
                    } catch (e: Exception) {
                        Utils.psErrorLog("Exception : ", e)
                    } finally {
                        db.endTransaction()
                    }
                    statusLiveData.postValue(success(true))
                }
            } else {
                statusLiveData.postValue(error(response.errorMessage, null))
            }
        }
        return statusLiveData
    }

    //endregion
    //region Get Product detail
    fun getProductDetail(apiKey: String?, productId: String?, historyFlag: String, userId: String?): LiveData<Resource<Product?>> {
        return object : NetworkBoundResource<Product?, Product?>(appExecutors) {
            protected override fun saveCallResult(itemList: Product) {
                Utils.psLog("SaveCallResult of recent products.")
                db.beginTransaction()
                try {
                    productDao.insert(itemList)
                    db.productColorDao()!!.deleteProductColorById(productId)
                    db.productColorDao()!!.insertAll(itemList.productColorList)
                    db.productSpecsDao()!!.deleteProductSpecsById(productId)
                    db.productSpecsDao()!!.insertAll(itemList.productSpecsList)
                    db.productAttributeHeaderDao()!!.deleteProductAttributeHeaderById(productId)
                    db.productAttributeHeaderDao()!!.insertAll(itemList.attributesHeaderList)
                    for (i in itemList.attributesHeaderList!!.indices) {
                        db.productAttributeDetailDao()!!.deleteProductAttributeDetailById(productId, itemList.attributesHeaderList!![i].id)
                        db.productAttributeDetailDao()!!.insertAll(itemList.attributesHeaderList!![i].attributesDetailList)
                    }
                    if (historyFlag == Constants.ONE) {
                        db.historyDao()!!.insert(HistoryProduct(productId!!, itemList.name, itemList.defaultPhoto.imgPath, Utils.getDateTime()))
                    }
                    db.setTransactionSuccessful()
                } catch (e: Exception) {
                    Utils.psErrorLog("Error in doing transaction of discount list.", e)
                } finally {
                    db.endTransaction()
                }
            }

            override fun shouldFetch(data: Product?): Boolean {

                // Recent news always load from server
                return connectivity!!.isConnected
            }

            override fun loadFromDb(): LiveData<Product> {
                Utils.psLog("Load discount From Db")
                return productDao.getProductById(productId)
            }

            override fun createCall(): LiveData<ApiResponse<Product?>?> {
                Utils.psLog("Call API Service to get discount.")
                return apiService.getProductDetail(apiKey, productId, Utils.checkUserId(userId))!!
            }

            override fun onFetchFailed(message: String?) {
                Utils.psLog("Fetch Failed (getDiscount) : $message")
            }
        }.asLiveData()
    }

    //endregion
    //region Favourite post
    fun uploadFavouritePostToServer(product_id: String?, userId: String?): LiveData<Resource<Boolean?>> {
        val statusLiveData = MutableLiveData<Resource<Boolean?>>()
        appExecutors.networkIO().execute {
            try {
                try {
                    db.beginTransaction()
                    isSelected = db.productDao()!!.selectFavouriteById(product_id)
                    if (isSelected == Constants.ONE) {
                        db.productDao()!!.updateProductForFavById(product_id, Constants.ZERO)
                    } else {
                        db.productDao()!!.updateProductForFavById(product_id, Constants.ONE)
                    }
                    db.setTransactionSuccessful()
                } catch (ne: NullPointerException) {
                    Utils.psErrorLog("Null Pointer Exception : ", ne)
                } catch (e: Exception) {
                    Utils.psErrorLog("Exception : ", e)
                } finally {
                    db.endTransaction()
                }

                // Call the API Service
                val response: Response<Product?>
                response = apiService.setPostFavourite(Config.API_KEY, product_id, userId)!!.execute()

                // Wrap with APIResponse Class
                val apiResponse = ApiResponse(response)

                // If response is successful
                if (apiResponse.isSuccessful) {
                    try {
                        db.beginTransaction()
                        if (apiResponse.body != null) {
                            db.productDao()!!.insert(apiResponse.body)
                            if (isSelected == Constants.ONE) {
                                db.productDao()!!.deleteFavouriteProductByProductId(apiResponse.body.id)
                            } else {
                                var lastIndex = db.productDao()!!.maxSortingFavourite
                                lastIndex = lastIndex + 1
                                db.productDao()!!.insertFavourite(FavouriteProduct(apiResponse.body.id, lastIndex))
                            }
                        }
                        db.setTransactionSuccessful()
                    } catch (ne: NullPointerException) {
                        Utils.psErrorLog("Null Pointer Exception : ", ne)
                    } catch (e: Exception) {
                        Utils.psErrorLog("Exception : ", e)
                    } finally {
                        db.endTransaction()
                    }
                    statusLiveData.postValue(success(apiResponse.nextPage != null))
                } else {
                    try {
                        db.beginTransaction()
                        isSelected = db.productDao()!!.selectFavouriteById(product_id)
                        if (isSelected == Constants.ONE) {
                            db.productDao()!!.updateProductForFavById(product_id, Constants.ZERO)
                        } else {
                            db.productDao()!!.updateProductForFavById(product_id, Constants.ONE)
                        }
                        db.setTransactionSuccessful()
                    } catch (ne: NullPointerException) {
                        Utils.psErrorLog("Null Pointer Exception : ", ne)
                    } catch (e: Exception) {
                        Utils.psErrorLog("Exception : ", e)
                    } finally {
                        db.endTransaction()
                    }
                    statusLiveData.postValue(error(apiResponse.errorMessage, false))
                }
            } catch (e: IOException) {
                statusLiveData.postValue(error(e.message, false))
            }
        }
        return statusLiveData
    }

    //endregion
    //region Touch count post
    fun uploadTouchCountPostToServer(userId: String?, typeId: String?, typeName: String?): LiveData<Resource<Boolean?>> {
        val statusLiveData = MutableLiveData<Resource<Boolean?>>()
        appExecutors.networkIO().execute {
            val response: Response<ApiStatus>
            try {
                response = apiService.setrawPostTouchCount(Config.API_KEY, typeId, typeName, Utils.checkUserId(userId))!!.execute()
                val apiResponse = ApiResponse(response)
                if (apiResponse.isSuccessful) {
                    statusLiveData.postValue(success(true))
                } else {
                    statusLiveData.postValue(error(apiResponse.errorMessage, false))
                }
            } catch (e: IOException) {
                statusLiveData.postValue(error(e.message, false))
            }
        }
        return statusLiveData
    }

    //endregion
    //region Get Product color
    fun getProductColor(productId: String?): LiveData<List<ProductColor?>?>? {
        return db.productColorDao()!!.getProductColorById(productId)
    }

    //endregion
    //region Get Product specs
    fun getProductSpecs(productId: String?): LiveData<List<ProductSpecs?>?>? {
        return db.productSpecsDao()!!.getProductSpecsById(productId)
    }

    //endregion
    //region Get Product attribute header
    fun getProductAttributeHeader(productId: String?): LiveData<List<ProductAttributeHeader>> {
        val hotelInfoGroupList = MutableLiveData<List<ProductAttributeHeader>>()
        appExecutors.diskIO().execute {
            val groupList = db.productAttributeHeaderDao()!!.getProductAttributeHeaderAndDetailById(productId)
            appExecutors.mainThread().execute { hotelInfoGroupList.setValue(groupList) }
        }
        return hotelInfoGroupList
    }

    //endregion
    //region Get Product attribute detail
    fun getProductAttributeDetail(productId: String?, headerId: String?): LiveData<List<ProductAttributeDetail?>?>? {
        return db.productAttributeDetailDao()!!.getProductAttributeDetailById(productId, headerId)
    }

    //endregion
    //region Get history
    fun getAllHistoryList(offset: String?): LiveData<List<HistoryProduct?>?>? {
        return db.historyDao()!!.getAllHistoryProductListData(offset)
    }

    //endregion
    //region Support Functions
    private fun prepareRatingValueForServer(productParameterHolder: ProductParameterHolder) {
        val ratingValue: MutableList<String> = ArrayList()
        if (!productParameterHolder.rating_value_one.isEmpty()) {
            ratingValue.add(productParameterHolder.rating_value_one)
        }
        if (!productParameterHolder.rating_value_two.isEmpty()) {
            ratingValue.add(productParameterHolder.rating_value_two)
        }
        if (!productParameterHolder.rating_value_three.isEmpty()) {
            ratingValue.add(productParameterHolder.rating_value_three)
        }
        if (!productParameterHolder.rating_value_four.isEmpty()) {
            ratingValue.add(productParameterHolder.rating_value_four)
        }
        if (!productParameterHolder.rating_value_five.isEmpty()) {
            ratingValue.add(productParameterHolder.rating_value_five)
        }
        val selectedStars: String
        val stringBuilder = StringBuilder()
        for (i in ratingValue.indices) {
            if (i == ratingValue.size - 1) {
                stringBuilder.append(ratingValue[i])
            } else {
                stringBuilder.append(ratingValue[i]).append(",")
            }
        }
        selectedStars = stringBuilder.toString()
        Utils.psLog(selectedStars + "selected rating stars")
        productParameterHolder.overall_rating = selectedStars
    } //endregion

    init {
        Utils.psLog("Inside ProductRepository")
        this.productDao = productDao
    }
}