package com.dewonderstruck.apps.ashx0.viewmodel.user

import androidx.arch.core.util.Function
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.ashx0.repository.user.UserRepository
import com.dewonderstruck.apps.ashx0.utils.AbsentLiveData
import com.dewonderstruck.apps.ashx0.utils.Constants
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewobject.ApiStatus
import com.dewonderstruck.apps.ashx0.viewobject.User
import com.dewonderstruck.apps.ashx0.viewobject.UserLogin
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import javax.inject.Inject

/**
 * Created by Vamsi Madduluri on 12/12/17.
 * Contact Email : vamsii.wrkhost@gmail.com
 */
class UserViewModel @Inject constructor(private val repository: UserRepository) : ViewModel() {
    //region Variables
    val loadingState = MutableLiveData<Boolean>()
    @JvmField
    var isLoading = false
    var profileImagePath = Constants.EMPTY_STRING
    var countryId = Constants.EMPTY_STRING
    var cityId = Constants.EMPTY_STRING
    var countryName = Constants.EMPTY_STRING
    var cityName = Constants.EMPTY_STRING
    var user: User? = null

    // for Login
    val userLoginStatus: LiveData<Resource<UserLogin>>
    private val doUserLoginObj = MutableLiveData<User>()

    // for get User
    private val userData: LiveData<Resource<User>>
    private val userObj = MutableLiveData<String>()

    // for register
    val registerUser: LiveData<Resource<User>>
    private val registerUserObj = MutableLiveData<UserTmpDataHolder>()

    // for register FB
    val registerFBUserData: LiveData<Resource<UserLogin>>
    private val registerFBUserObj = MutableLiveData<TmpDataHolder>()

    // For Getting Login User Data
    // for getting login user from db
    val loginUser: LiveData<List<UserLogin>>
    private val userLoginObj = MutableLiveData<String>()

    // for update user
    val updateUserData: LiveData<Resource<User>>
    private val updateUserObj = MutableLiveData<User>()

    // for forgot password
    private val forgotpasswordData: LiveData<Resource<ApiStatus>>
    private val forgotPasswordObj = MutableLiveData<String>()

    // for password update
    private val passwordUpdateData: LiveData<Resource<ApiStatus>>
    private val passwordUpdateObj = MutableLiveData<TmpDataHolder>()

    // for image upload
    private val imgObj = MutableLiveData<String>()

    // for google login
    val googleLoginData: LiveData<Resource<UserLogin>>
    private val googleLoginObj = MutableLiveData<TmpDataHolder>()

    //for verification code
    val emailVerificationUser: LiveData<Resource<UserLogin>>
    private val verificationEmailObj = MutableLiveData<TmpDataHolder>()

    //for resent verification code
    val resentVerifyCodeData: LiveData<Resource<Boolean>>
    private val resentVerifyCodeObj = MutableLiveData<resentCodeTmpDataHolder>()

    // for phone login
    val phoneLoginData: LiveData<Resource<UserLogin>>
    private val phoneLoginObj = MutableLiveData<TmpDataHolder>()

    //endregion
    //region Methods
    // For loading status
    fun setLoadingState(state: Boolean) {
        isLoading = state
        loadingState.value = state
    }

    // For Login User
    fun setUserLogin(obj: User) {
        setLoadingState(true)
        doUserLoginObj.value = obj
    }

    fun setLoginUser() {
        userLoginObj.value = "load"
    }

    // For User Data
    fun getUser(userId: String): LiveData<Resource<User>> {
        userObj.value = userId
        return userData
    }

    // For Delete Login User
    fun deleteUserLogin(user: User?): LiveData<Resource<Boolean>> {
        return if (user == null) {
            AbsentLiveData.create()
        } else repository.delete(user)
    }

    // Upload Image
    fun uploadImage(filePath: String?, userId: String?): LiveData<Resource<User>> {
        imgObj.value = "PS"
        return Transformations.switchMap(imgObj) { obj: String? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<User>>()
            }
            repository.uploadImage(filePath, userId, Constants.PLATFORM)
        }
    }

    // Update User
    fun setUpdateUserObj(user: User) {
        //Utils.psLog("Update Address : " + user.shippingAddress1);
        updateUserObj.value = user
    }

    // Register User
    //    public LiveData<Resource<UserLogin>> registerUser(User user) {
    //        registerUserObj.setValue(user);
    //        return registerUserData;
    //    }
    fun setRegisterUser(user: User) {
        val tmpDataHolder = UserTmpDataHolder(user)
        registerUserObj.value = tmpDataHolder
    }

    // Register User
    fun registerFBUser(fbId: String, name: String, email: String, imageUrl: String) {
        val tmpDataHolder = TmpDataHolder()
        tmpDataHolder.fbId = fbId
        tmpDataHolder.name = name
        tmpDataHolder.email = email
        tmpDataHolder.imageUrl = imageUrl
        registerFBUserObj.value = tmpDataHolder
    }

    // Forgot password
    fun forgotPassword(email: String): LiveData<Resource<ApiStatus>> {
        forgotPasswordObj.value = email
        return forgotpasswordData
    }

    // Forgot password
    fun passwordUpdate(loginUserId: String, password: String): LiveData<Resource<ApiStatus>> {
        val holder = TmpDataHolder()
        holder.loginUserId = loginUserId
        holder.password = password
        passwordUpdateObj.value = holder
        return passwordUpdateData
    }

    // phone login User
    fun setGoogleLoginUser(googleId: String, name: String, email: String, imageUrl: String, deviceToken: String) {
        val tmpDataHolder = TmpDataHolder()
        tmpDataHolder.googleId = googleId
        tmpDataHolder.name = name
        tmpDataHolder.email = email
        tmpDataHolder.imageUrl = imageUrl
        tmpDataHolder.deviceToken = deviceToken
        googleLoginObj.value = tmpDataHolder
    }

    fun setEmailVerificationUser(loginUserId: String, code: String) {
        val tmpDataHolder = TmpDataHolder()
        tmpDataHolder.loginUserId = loginUserId
        tmpDataHolder.code = code
        verificationEmailObj.value = tmpDataHolder
    }

    fun setResentVerifyCodeObj(userEmail: String) {
        val tmpDataHolder = resentCodeTmpDataHolder(userEmail)
        resentVerifyCodeObj.setValue(tmpDataHolder)
    }

    // phone login User
    fun setPhoneLoginUser(phoneId: String, name: String, phone: String, deviceToken: String) {
        val tmpDataHolder = TmpDataHolder()
        tmpDataHolder.phoneId = phoneId
        tmpDataHolder.name = name
        tmpDataHolder.phone = phone
        tmpDataHolder.deviceToken = deviceToken
        phoneLoginObj.value = tmpDataHolder
    }

    //endregion
    //region Tmp Holder
    internal class TmpDataHolder {
        var loginUserId = ""
        var password = ""
        var fbId = ""
        var name = ""
        var email = ""
        var imageUrl = ""
        var googleId = ""
        var code = ""
        var deviceToken = ""
        var phoneId = ""
        var phone = ""
    }

    internal class UserTmpDataHolder(var user: User)

    private class resentCodeTmpDataHolder internal constructor(var userEmail: String)  //endregion

    //endregion
    //region Constructor
    init {

        // Login User
        userLoginStatus = Transformations.switchMap(doUserLoginObj) { obj: User? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<UserLogin>>()
            }
            Utils.psLog("UserViewModel : doUserLoginData")
            repository.doLogin(Config.API_KEY, obj.userEmail, obj.userPassword)
        }

        // Register User
        registerUser = Transformations.switchMap(registerUserObj) { obj: UserTmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<User>>()
            }
            Utils.psLog("UserViewModel : registerUserData")
            repository.registerUser(Config.API_KEY, obj.user.userId, obj.user.userName, obj.user.userEmail, obj.user.userPassword, obj.user.userPhone, obj.user.deviceToken)
        }

        // Register FB User
        registerFBUserData = Transformations.switchMap(registerFBUserObj) { obj: TmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<UserLogin>>()
            }
            Utils.psLog("UserViewModel : registerFBUserData")
            repository.registerFBUser(Config.API_KEY, obj.fbId, obj.name, obj.email, obj.imageUrl)
        }

        // Get User Data
        loginUser = Transformations.switchMap(userLoginObj) { obj: String? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<List<UserLogin>>()
            }
            Utils.psLog("UserViewModel : userLoginData")
            repository.loginUser
        }

        // Get User Data
        userData = Transformations.switchMap(userObj, Function { userId ->
            if (userId == null) {
                return@Function AbsentLiveData.create<Resource<User>>()
            }
            Utils.psLog("UserViewModel : userLoginData")
            repository.getUser(Config.API_KEY, userId)
        })

        // Update User
        updateUserData = Transformations.switchMap(updateUserObj, Function { obj ->
            if (obj == null) {
                return@Function AbsentLiveData.create<Resource<User>>()
            }
            Utils.psLog("UserViewModel : updateUserData")
            repository.updateUser(Config.API_KEY, updateUserObj.value)
        })

        // Forgot Password
        forgotpasswordData = Transformations.switchMap(forgotPasswordObj) { obj: String? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<ApiStatus>>()
            }
            Utils.psLog("UserViewModel : forgotPasswordData")
            repository.forgotPassword(Config.API_KEY, obj)
        }

        // Password Update
        passwordUpdateData = Transformations.switchMap(passwordUpdateObj) { obj: TmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<ApiStatus>>()
            }
            Utils.psLog("UserViewModel : passwordUpdateData")
            repository.passwordUpdate(Config.API_KEY, obj.loginUserId, obj.password)
        }

        // google login User
        googleLoginData = Transformations.switchMap(googleLoginObj) { obj: TmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<UserLogin>>()
            }
            Utils.psLog("UserViewModel : googleLoginData")
            repository.postGoogleLogin(Config.API_KEY, obj.googleId, obj.name, obj.email, obj.imageUrl, obj.deviceToken)
        }

        // User Verify Email
        emailVerificationUser = Transformations.switchMap(verificationEmailObj) { obj: TmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<UserLogin>>()
            }
            repository.verificationCodeForUser(obj.loginUserId, obj.code)
        }

        // Resent Verify Code
        resentVerifyCodeData = Transformations.switchMap(resentVerifyCodeObj) { obj: resentCodeTmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<Boolean>>()
            }
            repository.resentCodeForUser(obj.userEmail)
        }

        // phone login User
        phoneLoginData = Transformations.switchMap(phoneLoginObj) { obj: TmpDataHolder? ->
            if (obj == null) {
                return@switchMap AbsentLiveData.create<Resource<UserLogin>>()
            }
            Utils.psLog("UserViewModel : phoneLoginData")
            repository.postPhoneLogin(Config.API_KEY, obj.phoneId, obj.name, obj.phone, obj.deviceToken)
        }
    }
}