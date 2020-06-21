package com.dewonderstruck.apps.ashx0.repository.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dewonderstruck.apps.AppExecutors
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.ashx0.api.ApiResponse
import com.dewonderstruck.apps.ashx0.api.ApiService
import com.dewonderstruck.apps.ashx0.db.PSCoreDb
import com.dewonderstruck.apps.ashx0.db.UserDao
import com.dewonderstruck.apps.ashx0.repository.common.NetworkBoundResource
import com.dewonderstruck.apps.ashx0.repository.common.PSRepository
import com.dewonderstruck.apps.ashx0.utils.AbsentLiveData
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewobject.ApiStatus
import com.dewonderstruck.apps.ashx0.viewobject.User
import com.dewonderstruck.apps.ashx0.viewobject.UserLogin
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource.Companion.error
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource.Companion.success
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Vamsi Madduluri on 11/17/17.
 * Contact Email : vamsii.wrkhost@gmail.com
 */
@Singleton
class UserRepository //endregion
//region Constructor
@Inject internal constructor(apiService: ApiService?, appExecutors: AppExecutors?, db: PSCoreDb?, //region Variables
                             private val userDao: UserDao) : PSRepository(apiService!!, appExecutors!!, db!!) {
    //endregion
    //region User Repository Functions for ViewModel
    /**
     * Function to login
     *
     * @param apiKey APIKey to access web services
     * @param email User Email
     * @param password User Password
     * @return Login User Data
     */
    fun doLogin(apiKey: String?, email: String, password: String): LiveData<Resource<UserLogin?>> {
        Utils.psLog("Do Login : $email & $password")
        return object : NetworkBoundResource<UserLogin?, User?>(appExecutors) {
            var userId: String? = ""
            protected override fun saveCallResult(item: User) {
                Utils.psLog("SaveCallResult of doLogin.")
                db.beginTransaction()
                try {

                    // set User id
                    userId = item.userId

                    // clear user login data
                    userDao.deleteUserLogin()

                    // insert user data
                    userDao.insert(item)

                    // insert user login
                    val userLogin = UserLogin(userId!!, true, item)
                    userDao.insert(userLogin)
                    db.setTransactionSuccessful()
                } catch (e: Exception) {
                    Utils.psErrorLog("Error in doing transaction of doLogin.", e)
                } finally {
                    db.endTransaction()
                }
            }

            override fun shouldFetch(data: UserLogin?): Boolean {
                // for user login, always should fetch
                return connectivity!!.isConnected
            }

            override fun loadFromDb(): LiveData<UserLogin> {
                Utils.psLog("Load User Login data from database.")
                return if (userId == null || userId == "") {
                    AbsentLiveData.create()
                } else userDao.getUserLoginData(userId)
            }

            override fun createCall(): LiveData<ApiResponse<User?>?> {
                Utils.psLog("Call API Service to do user login.")
                return apiService.postUserLogin(apiKey, email, password)!!
            }

            override fun onFetchFailed(message: String?) {
                Utils.psLog("Fetch Failed in doLogin.")
            }
        }.asLiveData()
    }

    /**
     * Function to get User Login Data.
     *
     * @return UserLogin Data.
     */
    val loginUser: LiveData<List<UserLogin?>?>?
        get() {
            Utils.psLog("Get Login User")
            return userDao.userLoginData
        }

    /**
     * Function to get User
     * @param apiKey APIKey to access to API Service
     * @param userId User Id to fetch
     * @return Login User
     */
    fun getUser(apiKey: String?, userId: String?): LiveData<Resource<User?>> {
        return object : NetworkBoundResource<User?, List<User?>?>(appExecutors) {
            protected override fun saveCallResult(items: List<User?>) {
                Utils.psLog("SaveCallResult of doLogin.")
                db.beginTransaction()
                try {
                    if (items.size > 0) {
                        val item = items[0]
                        // set User id
//                        userId = item.userId;

                        // clear user login data
                        userDao.deleteUserLogin()

                        // insert user data
                        userDao.insert(item)

                        // insert user login
                        val userLogin = UserLogin(userId!!, true, item!!)
                        userDao.insert(userLogin)
                        db.setTransactionSuccessful()
                    }
                } catch (e: Exception) {
                    Utils.psErrorLog("Error in doing transaction of doLogin.", e)
                } finally {
                    db.endTransaction()
                }
            }

            override fun shouldFetch(data: User?): Boolean {
                // for user login, always should fetch
                return connectivity!!.isConnected
            }

            override fun loadFromDb(): LiveData<User> {
                Utils.psLog("Load User Login data from database.")
                return userDao.getUserData(userId)
            }

            override fun createCall(): LiveData<ApiResponse<List<User?>?>?> {
                Utils.psLog("Call API Service to do user login.")
                return apiService.getUser(apiKey, userId)!!
            }

            override fun onFetchFailed(message: String?) {
                Utils.psLog("Fetch Failed in doLogin.")
            }
        }.asLiveData()
    }

    /**
     * Function to register new user.
     *
     * @param apiKey APIKey to access web services
     * @param userName User Name
     * @param email User Email
     * @param password User Password
     * @return Login User Data
     */
    fun registerUser(apiKey: String?, loginUserId: String?, userName: String?, email: String?, password: String?, phone: String?, deviceToken: String?): LiveData<Resource<User?>> {
        val statusLiveData = MutableLiveData<Resource<User?>>() // To update the status to the listener
        appExecutors.networkIO().execute {
            try {

                // Call the API Service
                val response = apiService.postUser(apiKey, Utils.checkUserId(loginUserId), userName, email, password, phone, deviceToken)!!.execute()

                // Wrap with APIResponse Class
                val apiResponse = ApiResponse(response)

                // If response is successful
                if (apiResponse.isSuccessful) {
                    try {
                        db.beginTransaction()
                        if (apiResponse.body != null) {
                            // set User id
                            val userId = apiResponse.body.userId

                            // clear user login data
                            userDao.deleteUserLogin()

                            // insert user data
                            userDao.insert(apiResponse.body)

                            // insert user login
//                        UserLogin userLogin = new UserLogin(userId, true, apiResponse.body);
//                        userDao.insert(userLogin);
                            db.setTransactionSuccessful()
                            statusLiveData.postValue(success(response.body()))
                        }
                    } catch (ne: NullPointerException) {
                        Utils.psErrorLog("Null Pointer Exception : ", ne)
                    } catch (e: Exception) {
                        Utils.psErrorLog("Exception : ", e)
                    } finally {
                        db.endTransaction()
                    }
                } else {
                    statusLiveData.postValue(error(apiResponse.errorMessage, null))
                }
            } catch (e: IOException) {
                statusLiveData.postValue(error(e.message, null))
            }
        }
        return statusLiveData
    }

    /**
     * Function to register FB user.
     *
     * @param apiKey APIKey to access web services
     * @param userName User Name
     * @param email User Email
     * @param imageUrl Image URL
     * @return Login User Data
     */
    fun registerFBUser(apiKey: String?, fbId: String?, userName: String?, email: String?, imageUrl: String?): LiveData<Resource<UserLogin?>> {
        val statusLiveData = MutableLiveData<Resource<UserLogin?>>() // To update the status to the listener
        appExecutors.networkIO().execute {
            try {

                // Call the API Service
                val response = apiService
                        .postFBUser(apiKey, fbId, userName, email, imageUrl)!!.execute()


                // Wrap with APIResponse Class
                val apiResponse = ApiResponse(response)

                // If response is successful
                if (apiResponse.isSuccessful) {
                    try {
                        db.beginTransaction()
                        if (apiResponse.body != null) {
                            // set User id
                            val userId = apiResponse.body.userId

                            // clear user login data
                            userDao.deleteUserLogin()

                            // insert user data
                            userDao.insert(apiResponse.body)

                            // insert user login
                            val userLogin = UserLogin(userId, true, apiResponse.body)
                            userDao.insert(userLogin)
                            db.setTransactionSuccessful()
                            statusLiveData.postValue(success(userLogin))
                        }
                    } catch (ne: NullPointerException) {
                        Utils.psErrorLog("Null Pointer Exception : ", ne)
                    } catch (e: Exception) {
                        Utils.psErrorLog("Exception : ", e)
                    } finally {
                        db.endTransaction()
                    }
                } else {
                    statusLiveData.postValue(error(apiResponse.errorMessage, null))
                }
            } catch (e: IOException) {
                statusLiveData.postValue(error(e.message, null))
            }
        }
        return statusLiveData
    }

    /**
     * Function to update user.
     *
     * @param apiKey APIKey to access web services
     * @param user User Data to update.
     * @return Status of Request.
     */
    fun updateUser(apiKey: String?, user: User): LiveData<Resource<User?>> {
        return object : NetworkBoundResource<User?, User?>(appExecutors) {
            var userId: String? = ""
            private var resultsDb: User? = null
            protected override fun saveCallResult(item: User) {
                Utils.psLog("SaveCallResult of update user.")
                db.beginTransaction()
                try {

//                    if(item.status.equals("success")) {

                    // set User id
                    userId = user.userId

                    // update user data
                    userDao.update(user)

                    // update user login
                    val userLogin = UserLogin(userId!!, true, user)
                    userDao.update(userLogin)
                    //Utils.psLog("userLogin Address : " + userLogin.user.shippingAddress1);
                    db.setTransactionSuccessful()

//                    }
                    resultsDb = item
                } catch (e: Exception) {
                    Utils.psErrorLog("Error in doing transaction of update user.", e)
                } finally {
                    db.endTransaction()
                }
            }

            override fun shouldFetch(data: User?): Boolean {
                // for user update, always should fetch
                return connectivity!!.isConnected
            }

            override fun loadFromDb(): LiveData<User> {
                return if (userId == null || userId == "") {
                    AbsentLiveData.create()
                } else object : LiveData<User?>() {
                    override fun onActive() {
                        super.onActive()
                        value = resultsDb
                    }
                }
            }

            override fun createCall(): LiveData<ApiResponse<User?>?> {
                Utils.psLog("Call API Service to update user.")
                return apiService.putUser(apiKey, user.userId, user.userName, user.userEmail, user.userPhone, user.userAboutMe,
                        user.billingFirstName, user.billingLastName, user.billingCompany, user.billingAddress1, user.billingAddress2,
                        user.billingCountry, user.billingState, user.billingCity, user.billingPostalCode, user.billingEmail, user.billingPhone,
                        user.shippingFirstName, user.shippingLastName, user.shippingCompany, user.shippingAddress1, user.shippingAddress2,
                        user.shippingCountry, user.shippingState, user.shippingCity, user.shippingPostalCode, user.shippingEmail,
                        user.shippingPhone, user.country!!.id, user.city!!.id)!!
            }

            override fun onFetchFailed(message: String?) {
                Utils.psLog("Fetch Failed (updateUser).$message")
            }
        }.asLiveData()
    }

    /**
     * Function to request forgot password
     *
     * @param apiKey APIKey to access web services
     * @param email User Email
     * @return Status Of request.
     */
    fun forgotPassword(apiKey: String?, email: String?): LiveData<Resource<ApiStatus?>> {
        return object : NetworkBoundResource<ApiStatus?, ApiStatus?>(appExecutors) {
            private var resultsDb: ApiStatus? = null
            protected override fun saveCallResult(item: ApiStatus) {
                Utils.psLog("SaveCallResult of forgotPassword")
                resultsDb = item
            }

            override fun shouldFetch(data: ApiStatus?): Boolean {
                // for forgot password, always should fetch
                return connectivity!!.isConnected
            }

            override fun loadFromDb(): LiveData<ApiStatus> {
                return if (resultsDb == null) {
                    AbsentLiveData.create()
                } else object : LiveData<ApiStatus?>() {
                    override fun onActive() {
                        super.onActive()
                        value = resultsDb
                    }
                }
            }

            override fun createCall(): LiveData<ApiResponse<ApiStatus?>?> {
                Utils.psLog("Call API Service to Request Forgot Password.")
                return apiService.postForgotPassword(apiKey, email)!!
            }

            override fun onFetchFailed(message: String?) {
                Utils.psLog("Fetch Failed of forgot Password.")
            }
        }.asLiveData()
    }

    /**
     * Function to Password Update
     *
     * @param apiKey APIKey to access web services
     * @param loginUserId Current Login User Id
     * @param password New Password
     * @return Status of Request.
     */
    fun passwordUpdate(apiKey: String?, loginUserId: String?, password: String?): LiveData<Resource<ApiStatus?>> {
        return object : NetworkBoundResource<ApiStatus?, ApiStatus?>(appExecutors) {
            private var resultsDb: ApiStatus? = null
            protected override fun saveCallResult(item: ApiStatus) {
                Utils.psLog("SaveCallResult of passwordUpdate")
                resultsDb = item
            }

            override fun shouldFetch(data: ApiStatus?): Boolean {
                // for passwordUpdate, always should fetch
                return connectivity!!.isConnected
            }

            override fun loadFromDb(): LiveData<ApiStatus> {
                return if (resultsDb == null) {
                    AbsentLiveData.create()
                } else object : LiveData<ApiStatus?>() {
                    override fun onActive() {
                        super.onActive()
                        value = resultsDb
                    }
                }
            }

            override fun createCall(): LiveData<ApiResponse<ApiStatus?>?> {
                Utils.psLog("Call API Service to update password.")
                return apiService.postPasswordUpdate(apiKey, loginUserId, password)!!
            }

            override fun onFetchFailed(message: String?) {
                Utils.psLog("Fetch Failed of password update.")
            }
        }.asLiveData()
    }

    /**
     * Upload image ( Used in profile image upload now)
     * @param filePath file path of selected image.
     * @param userId user id to set image.
     * @param platform current platform ( " android " )
     * @return User
     */
    fun uploadImage(filePath: String?, userId: String?, platform: String?): LiveData<Resource<User?>> {

        //Init File
        val file = File(filePath)
        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)

        // MultipartBody.Part is used to send also the actual file news_title
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

        // add another part within the multipart request
        val fullName = RequestBody.create(
                MediaType.parse("multipart/form-data"), file.name)
        val platformRB = RequestBody.create(
                MediaType.parse("multipart/form-data"), platform)
        val useIdRB = RequestBody.create(
                MediaType.parse("multipart/form-data"), userId)
        return object : NetworkBoundResource<User?, User?>(appExecutors) {
            // Temp ResultType To Return
            private var resultsDb: User? = null
            var userId = ""
            protected override fun saveCallResult(item: User) {
                Utils.psLog("SaveCallResult")
                db.beginTransaction()
                try {

                    // set User id
                    userId = item.userId

                    // update user data
                    userDao.update(item)

                    // update user login
                    val userLogin = UserLogin(userId, true, item)
                    userDao.update(userLogin)
                    db.setTransactionSuccessful()
                } catch (e: Exception) {
                    Utils.psErrorLog("Error", e)
                } finally {
                    db.endTransaction()
                }
                resultsDb = item
            }

            override fun shouldFetch(data: User?): Boolean {
                // Image upload should always connect to server.
                return connectivity!!.isConnected
            }

            override fun loadFromDb(): LiveData<User> {
                return if (resultsDb == null) {
                    AbsentLiveData.create()
                } else {
                    object : LiveData<User?>() {
                        override fun onActive() {
                            super.onActive()
                            value = resultsDb
                        }
                    }
                }
            }

            override fun createCall(): LiveData<ApiResponse<User?>?> {
                Utils.psLog("Call API Service to upload image.")
                return apiService.doUploadImage(Config.API_KEY, useIdRB, fullName, body, platformRB)!!
            }

            override fun onFetchFailed(message: String?) {
                Utils.psLog("Fetch Failed of uploading image.")
            }
        }.asLiveData()
    }

    fun postGoogleLogin(apiKey: String?, googleId: String?, userName: String?, userEmail: String?, profilePhotoUrl: String?, deviceToken: String?): LiveData<Resource<UserLogin?>> {
        val statusLiveData = MutableLiveData<Resource<UserLogin?>>() // To update the status to the listener
        appExecutors.networkIO().execute {
            try {

                // Call the API Service
                val response = apiService
                        .postGoogleLogin(apiKey, googleId, userName, userEmail, profilePhotoUrl, deviceToken)!!.execute()


                // Wrap with APIResponse Class
                val apiResponse = ApiResponse(response)

                // If response is successful
                if (apiResponse.isSuccessful) {
                    try {
                        db.beginTransaction()
                        if (apiResponse.body != null) {
                            // set User id
                            val userId = apiResponse.body.userId

                            // clear user login data
                            userDao.deleteUserLogin()

                            // insert user data
                            userDao.insert(apiResponse.body)

                            // insert user login
                            val userLogin = UserLogin(userId, true, apiResponse.body)
                            userDao.insert(userLogin)
                            db.setTransactionSuccessful()
                            statusLiveData.postValue(success(userLogin))
                        }
                    } catch (ne: NullPointerException) {
                        Utils.psErrorLog("Null Pointer Exception : ", ne)
                    } catch (e: Exception) {
                        Utils.psErrorLog("Exception : ", e)
                    } finally {
                        db.endTransaction()
                    }
                } else {
                    statusLiveData.postValue(error(apiResponse.errorMessage, null))
                }
            } catch (e: IOException) {
                statusLiveData.postValue(error(e.message, null))
            }
        }
        return statusLiveData
    }

    // Verify User
    fun verificationCodeForUser(userId: String?, code: String?): LiveData<Resource<UserLogin?>> {
        val statusLiveData = MutableLiveData<Resource<UserLogin?>>()
        appExecutors.networkIO().execute {
            try {

                // Call the API Service
                val response = apiService.verifyEmail(Config.API_KEY, userId, code)!!.execute()


                // Wrap with APIResponse Class
                val apiResponse = ApiResponse(response)

                // If response is successful
                if (apiResponse.isSuccessful) {
                    try {
                        db.beginTransaction()
                        if (apiResponse.body != null) {
                            // set User id
                            val user_id = apiResponse.body.userId

                            // clear user login data
                            userDao.deleteUserLogin()

                            // insert user data
                            userDao.insert(apiResponse.body)

                            // insert user login
                            val userLogin = UserLogin(user_id, true, apiResponse.body)
                            userDao.insert(userLogin)
                            db.setTransactionSuccessful()
                            statusLiveData.postValue(success(userLogin))
                        }
                    } catch (ne: NullPointerException) {
                        Utils.psErrorLog("Null Pointer Exception : ", ne)
                    } catch (e: Exception) {
                        Utils.psErrorLog("Exception : ", e)
                    } finally {
                        db.endTransaction()
                    }
                } else {
                    statusLiveData.postValue(error(apiResponse.errorMessage, null))
                }
            } catch (e: IOException) {
                statusLiveData.postValue(error(e.message, null))
            }
        }
        return statusLiveData
    }

    // Resent Verify Code
    fun resentCodeForUser(userEmail: String?): LiveData<Resource<Boolean?>> {
        val statusLiveData = MutableLiveData<Resource<Boolean?>>()
        appExecutors.networkIO().execute {
            val response: Response<ApiStatus>
            try {
                response = apiService.resentCodeAgain(Config.API_KEY, userEmail)!!.execute()
                if (response.isSuccessful) {
                    statusLiveData.postValue(success(true))
                } else {
                    statusLiveData.postValue(error("error", false))
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return statusLiveData
    }

    //endregion
    fun postPhoneLogin(apiKey: String?, phoneId: String?, userName: String?, userPhone: String?, deviceToken: String?): LiveData<Resource<UserLogin?>> {
        val statusLiveData = MutableLiveData<Resource<UserLogin?>>() // To update the status to the listener
        appExecutors.networkIO().execute {
            try {

                // Call the API Service
                val response = apiService
                        .postPhoneLogin(apiKey, phoneId, userName, userPhone, deviceToken)!!.execute()


                // Wrap with APIResponse Class
                val apiResponse = ApiResponse(response)

                // If response is successful
                if (apiResponse.isSuccessful) {
                    try {
                        db.beginTransaction()
                        if (apiResponse.body != null) {
                            // set User id
                            val userId = apiResponse.body.userId

                            // clear user login data
                            userDao.deleteUserLogin()

                            // insert user data
                            userDao.insert(apiResponse.body)

                            // insert user login
                            val userLogin = UserLogin(userId, true, apiResponse.body)
                            userDao.insert(userLogin)
                            db.setTransactionSuccessful()
                            statusLiveData.postValue(success(userLogin))
                        }
                    } catch (ne: NullPointerException) {
                        Utils.psErrorLog("Null Pointer Exception : ", ne)
                    } catch (e: Exception) {
                        Utils.psErrorLog("Exception : ", e)
                    } finally {
                        db.endTransaction()
                    }
                } else {
                    statusLiveData.postValue(error(apiResponse.errorMessage, null))
                }
            } catch (e: IOException) {
                statusLiveData.postValue(error(e.message, null))
            }
        }
        return statusLiveData
    }

}