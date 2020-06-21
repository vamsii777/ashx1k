package com.dewonderstruck.apps.ashx0.repository.basket

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dewonderstruck.apps.AppExecutors
import com.dewonderstruck.apps.ashx0.api.ApiService
import com.dewonderstruck.apps.ashx0.db.BasketDao
import com.dewonderstruck.apps.ashx0.db.PSCoreDb
import com.dewonderstruck.apps.ashx0.repository.common.PSRepository
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewobject.Basket
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource.Companion.error
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource.Companion.success
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BasketRepository //endregion
//region Constructor
@Inject internal constructor(apiService: ApiService?, appExecutors: AppExecutors?, db: PSCoreDb?, //region Variables
                             private val basketDao: BasketDao) : PSRepository(apiService!!, appExecutors!!, db!!) {

    //endregion
    //    region Basket Repository Functions for ViewModel
    // insert Product
    fun saveProduct(basketId: Int, productId: String?, count: Int, selectedAttributes: String?, selectedColorId: String?, selectedColorValue: String?, selectedAttributePrice: String?, basketPrice: Float, basketOriginalPrice: Float, shopId: String?, priceStr: String?): LiveData<Resource<Boolean?>> {
        val statusLiveData = MutableLiveData<Resource<Boolean?>>()
        appExecutors.diskIO().execute {
            try {
                db.beginTransaction()
                if (basketId == 0) {
                    val id = basketDao.getBasketId(productId, selectedAttributes, selectedColorId)
                    if (id > 0) {
                        basketDao.updateBasketById(id, count)
                    } else {
                        basketDao.insert(Basket(productId!!, count, selectedAttributes!!, selectedColorId!!, selectedColorValue!!, selectedAttributePrice!!, basketPrice, basketOriginalPrice, shopId!!, priceStr!!))
                    }
                } else {
                    val basket = Basket(productId!!, count, selectedAttributes!!, selectedColorId!!, selectedColorValue!!, selectedAttributePrice!!, basketPrice, basketOriginalPrice, shopId!!, priceStr!!)
                    basket.id = basketId
                    basketDao.update(basket)
                }
                db.setTransactionSuccessful()
            } catch (ne: NullPointerException) {
                Utils.psErrorLog("Null Pointer Exception : ", ne)
                statusLiveData.postValue(error(ne.message, false))
            } catch (e: Exception) {
                Utils.psErrorLog("Exception : ", e)
                statusLiveData.postValue(error(e.message, false))
            } finally {
                db.endTransaction()
            }
            statusLiveData.postValue(success(true))
        }
        return statusLiveData
    }

    //endregion
    // update Product by id
    fun updateProduct(id: Int, count: Int): LiveData<Resource<Boolean?>> {
        val statusLiveData = MutableLiveData<Resource<Boolean?>>()
        appExecutors.diskIO().execute {
            try {
                db.beginTransaction()
                basketDao.updateBasketById(id, count)
                db.setTransactionSuccessful()
            } catch (ne: NullPointerException) {
                Utils.psErrorLog("Null Pointer Exception : ", ne)
                statusLiveData.postValue(error(ne.message, false))
            } catch (e: Exception) {
                Utils.psErrorLog("Exception : ", e)
                statusLiveData.postValue(error(e.message, false))
            } finally {
                db.endTransaction()
            }
            statusLiveData.postValue(success(true))
        }
        return statusLiveData
    }

    //endregion
    // delete Product by id
    fun deleteProduct(id: Int): LiveData<Resource<Boolean?>> {
        val statusLiveData = MutableLiveData<Resource<Boolean?>>()
        appExecutors.diskIO().execute {
            try {
                db.beginTransaction()
                basketDao.deleteBasketById(id)
                db.setTransactionSuccessful()
            } catch (ne: NullPointerException) {
                Utils.psErrorLog("Null Pointer Exception : ", ne)
                statusLiveData.postValue(error(ne.message, false))
            } catch (e: Exception) {
                Utils.psErrorLog("Exception : ", e)
                statusLiveData.postValue(error(e.message, false))
            } finally {
                db.endTransaction()
            }
            statusLiveData.postValue(success(true))
        }
        return statusLiveData
    }

    //endregion
    fun deleteStoredBasket(): LiveData<Resource<Boolean?>> {
        val statusLiveData = MutableLiveData<Resource<Boolean?>>()
        appExecutors.diskIO().execute {
            try {
                db.beginTransaction()
                basketDao.deleteAllBasket()
                db.setTransactionSuccessful()
            } catch (ne: NullPointerException) {
                Utils.psErrorLog("Null Pointer Exception : ", ne)
                statusLiveData.postValue(error(ne.message, false))
            } catch (e: Exception) {
                Utils.psErrorLog("Exception : ", e)
                statusLiveData.postValue(error(e.message, false))
            } finally {
                db.endTransaction()
            }
            statusLiveData.postValue(success(true))
        }
        return statusLiveData
    }

    //Get basket
    val allBasketList: LiveData<List<Basket?>?>?
        get() = basketDao.allBasketList

    //endregion
    val allBasketWithProduct: LiveData<List<Basket>>
        get() {
            val basketList = MutableLiveData<List<Basket>>()
            appExecutors.diskIO().execute {
                val groupList = db.basketDao()!!.allBasketWithProduct
                appExecutors.mainThread().execute { basketList.setValue(groupList) }
            }
            return basketList
        }

}