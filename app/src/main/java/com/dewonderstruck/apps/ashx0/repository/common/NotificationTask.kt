package com.dewonderstruck.apps.ashx0.repository.common

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.ashx0.api.ApiResponse
import com.dewonderstruck.apps.ashx0.api.ApiService
import com.dewonderstruck.apps.ashx0.utils.Constants
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource.Companion.error
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource.Companion.success
import com.google.firebase.iid.FirebaseInstanceId
import javax.inject.Inject

/**
 * For register/un-register token to server to able to send notification
 * Created by Vamsi Madduluri on 12/12/17.
 * Contact Email : vamsii.wrkhost@gmail.com
 */
class NotificationTask //endregion
//region Constructor
(private val context: Context, private val service: ApiService, private val platform: String, private val isRegister: Boolean, private var token: String) : Runnable {
    //region Variables
    @JvmField
    @Inject
    var prefs: SharedPreferences? = null
    private val statusLiveData = MutableLiveData<Resource<Boolean?>>()

    //endregion
    //region Override Methods
    override fun run() {
        try {
            prefs = PreferenceManager.getDefaultSharedPreferences(context)
            if (isRegister) {
                if (token == "") {
                    // Get Token for notification registration
                    token = FirebaseInstanceId.getInstance().token!!
                }
                Utils.psLog("Token : $token")
                if (token == "") {
                    statusLiveData.postValue(error("Token is null.", true))
                    return
                }

                // Call the API Service
                val response = service.rawRegisterNotiToken(Config.API_KEY, platform, token)!!.execute()

                // Wrap with APIResponse Class
                val apiResponse = ApiResponse(response)

                // If response is successful
                if (apiResponse.isSuccessful) {
                    if (apiResponse.body != null) {
                        Utils.psLog("API Status : " + apiResponse.body.status)
                        if (apiResponse.body.status == "success") {
                            val editor = prefs.edit()
                            editor.putBoolean(Constants.NOTI_SETTING, true).apply()
                            editor.putString(Constants.NOTI_TOKEN, token).apply()
                        }
                    }
                    statusLiveData.postValue(success(true))
                } else {
                    statusLiveData.postValue(error(apiResponse.errorMessage, true))
                }
            } else { // Un-register

                // Get Token
                val token = prefs.getString(Constants.NOTI_TOKEN, "")
                if (token != "") {

                    // Call unregister service to server
                    val response = service.rawUnregisterNotiToken(Config.API_KEY, platform, token)!!.execute()

                    // Parse it to ApiResponse
                    val apiResponse = ApiResponse(response)

                    // If response is successful
                    if (apiResponse.isSuccessful) {
                        if (apiResponse.body != null) {
                            Utils.psLog("API Status : " + apiResponse.body.status)
                            if (apiResponse.body.status == "success") {
                                val editor = prefs.edit()
                                editor.putBoolean(Constants.NOTI_SETTING, false).apply()
                                editor.putString(Constants.NOTI_TOKEN, "-").apply()
                            }
                        }
                        statusLiveData.postValue(success(true))
                    } else {
                        statusLiveData.postValue(error(apiResponse.errorMessage, true))
                    }

                    // Clear notification setting
                } else {
                    statusLiveData.postValue(error("Token is null.", true))
                }
            }
        } catch (e: Exception) {
            statusLiveData.postValue(error(e.message, true))
        }
    }
    //endregion
    //region public SyncCategory Methods
    /**
     * This function will return Status of Process
     * @return statusLiveData
     */
    fun getStatusLiveData(): LiveData<Resource<Boolean?>> {
        return statusLiveData
    } //endregion

}