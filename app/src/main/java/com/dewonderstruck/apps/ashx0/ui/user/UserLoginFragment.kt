package com.dewonderstruck.apps.ashx0.ui.user

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.VisibleForTesting
import androidx.constraintlayout.motion.widget.MotionScene
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.binding.FragmentDataBindingComponent
import com.dewonderstruck.apps.ashx0.databinding.FragmentUserLoginBinding
import com.dewonderstruck.apps.ashx0.ui.common.DeFragment
import com.dewonderstruck.apps.ashx0.utils.AutoClearedValue
import com.dewonderstruck.apps.ashx0.utils.Constants
import com.dewonderstruck.apps.ashx0.utils.PSDialogMsg
import com.dewonderstruck.apps.ashx0.utils.Utils
import com.dewonderstruck.apps.ashx0.viewmodel.user.UserViewModel
import com.dewonderstruck.apps.ashx0.viewobject.User
import com.dewonderstruck.apps.ashx0.viewobject.UserLogin
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource
import com.dewonderstruck.apps.ashx0.viewobject.common.Status
import com.facebook.*
import com.facebook.login.LoginResult
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import org.json.JSONException
import org.json.JSONObject

/**
 * UserLoginFragment
 */
class UserLoginFragment() : DeFragment() {
    //region Variables
    private val dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    private var userViewModel: UserViewModel? = null
    private var psDialogMsg: PSDialogMsg? = null
    private var checkFlag = false

    @VisibleForTesting
    private var binding: AutoClearedValue<FragmentUserLoginBinding>? = null
    private var prgDialog: AutoClearedValue<ProgressDialog>? = null
    private var callbackManager: CallbackManager? = null
    private var mGoogleSignInClient: GoogleSignInClient? = null

    //endregion
    private var token: String? = null

    //Firebase test
    private var mAuth: FirebaseAuth? = null

    //region Override Methods
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        FacebookSdk.addLoggingBehavior(LoggingBehavior.REQUESTS)
        callbackManager = CallbackManager.Factory.create()
        // Inflate the layout for this fragment
        val dataBinding: FragmentUserLoginBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_login, container, false, dataBindingComponent)
        binding = AutoClearedValue(this, dataBinding)
        if (activity != null) {
            requireActivity().window.setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        }
        return binding!!.get().root
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, Constants.REQUEST_CODE__GOOGLE_SIGN)
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d(MotionScene.TAG, "firebaseAuthWithGoogle:" + acct.id)
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        if (activity != null) {
            mAuth!!.signInWithCredential(credential)
                    .addOnCompleteListener((activity)!!) { task: Task<AuthResult?> ->
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(MotionScene.TAG, "signInWithCredential:success")
                            val user: FirebaseUser? = mAuth!!.getCurrentUser()
                            if (user != null) {
                                userViewModel!!.setGoogleLoginUser(user.getUid(), user.getDisplayName(), user.getEmail(), user.getPhotoUrl().toString(), token)
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(MotionScene.TAG, "signInWithCredential:failure", task.getException())
                            Toast.makeText(getActivity(), "SignIn Failed", Toast.LENGTH_LONG).show()
                        }
                    }
        }
    }

    override fun initUIAndActions() {
        if (Config.SHOW_ADMOB && connectivity.isConnected) {
            val adRequest = AdRequest.Builder()
                    .build()
            binding!!.get().adView3.loadAd(adRequest)
            val adRequest2 = AdRequest.Builder()
                    .build()
            binding!!.get().adView3.loadAd(adRequest2)
        } else {
            binding!!.get().adView3.visibility = View.GONE
            binding!!.get().adView3.visibility = View.GONE
        }
        dataBindingComponent.fragmentBindingAdapters.bindFullImageDrawbale(binding!!.get().bgImageView, resources.getDrawable(R.drawable.hotel_cell_background))
        psDialogMsg = PSDialogMsg(activity, false)

        // Init Dialog
        prgDialog = AutoClearedValue(this, ProgressDialog(activity))
        //prgDialog.get().setMessage(getString(R.string.message__please_wait));
        prgDialog!!.get().setMessage((Utils.getSpannableString(context, getString(R.string.message__please_wait), Utils.Fonts.MM_FONT)))
        prgDialog!!.get().setCancelable(false)

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()
        binding!!.get().phoneLoginButton.setOnClickListener({ v: View? -> Utils.navigateAfterPhoneLogin(getActivity(), navigationController) })
        //end

        // Configure Google Sign In
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        if (activity != null) {
            mGoogleSignInClient = GoogleSignIn.getClient((activity)!!, googleSignInOptions)
        }
        //end

        //fadeIn Animation
        fadeIn(binding!!.get().root)
        binding!!.get().loginButton.setOnClickListener { view: View? ->
            Utils.hideKeyboard(getActivity())
            if (connectivity.isConnected()) {
                val userEmail: String = binding!!.get().emailEditText.getText().toString().trim { it <= ' ' }
                val userPassword: String = binding!!.get().passwordEditText.getText().toString().trim { it <= ' ' }
                Utils.psLog("Email $userEmail")
                Utils.psLog("Password $userPassword")
                if ((userEmail == "")) {
                    psDialogMsg!!.showWarningDialog(getString(R.string.error_message__blank_email), getString(R.string.app__ok))
                    psDialogMsg!!.show()
                    return@setOnClickListener
                }
                if ((userPassword == "")) {
                    psDialogMsg!!.showWarningDialog(getString(R.string.error_message__blank_password), getString(R.string.app__ok))
                    psDialogMsg!!.show()
                    return@setOnClickListener
                }
                if (!userViewModel!!.isLoading) {
                    updateLoginBtnStatus()
                    doSubmit(userEmail, userPassword)
                }
            } else {
                psDialogMsg!!.showWarningDialog(getString(R.string.no_internet_error), getString(R.string.app__ok))
                psDialogMsg!!.show()
            }
        }
        binding!!.get().registerButton.setOnClickListener({ view: View? -> Utils.navigateAfterRegister(getActivity(), navigationController) })
        binding!!.get().forgotPasswordButton.setOnClickListener({ view: View? -> Utils.navigateAfterForgotPassword(getActivity(), navigationController) })
        if (Config.ENABLE_FACEBOOK_LOGIN) {
            binding!!.get().fbLoginButton.visibility = View.VISIBLE
        } else {
            binding!!.get().fbLoginButton.visibility = View.GONE
        }
        if (Config.ENABLE_GOOGLE_LOGIN) {
            binding!!.get().googleSignInButton.visibility = View.VISIBLE
        } else {
            binding!!.get().googleSignInButton.visibility = View.GONE
        }
        binding!!.get().googleSignInButton.setOnClickListener({ v: View? -> signIn() })


        //for check privacy and policy
        binding!!.get().privacyPolicyCheckbox.setOnClickListener(View.OnClickListener {
            if (binding!!.get().privacyPolicyCheckbox.isChecked) {
//                    Toast.makeText(getContext(), "Is Check ", Toast.LENGTH_SHORT).show();
                navigationController.navigateToTermsAndConditionsActivity(this!!.requireActivity(), Constants.SHOP_TERMS)
                checkFlag = true
                binding!!.get().view29.visibility = View.GONE
                binding!!.get().view31.visibility = View.GONE
                binding!!.get().phoneSignInView.visibility = View.GONE
                binding!!.get().fbLoginButton.isEnabled = true
                binding!!.get().googleSignInButton.isEnabled = true
                binding!!.get().phoneLoginButton.isEnabled = true
            } else {
//                    Toast.makeText(getContext(), "Not Check ", Toast.LENGTH_SHORT).show();
                checkFlag = false
                binding!!.get().view29.visibility = View.VISIBLE
                binding!!.get().view31.visibility = View.VISIBLE
                binding!!.get().phoneSignInView.visibility = View.VISIBLE
                binding!!.get().fbLoginButton.isEnabled = false
                binding!!.get().googleSignInButton.isEnabled = false
                binding!!.get().phoneLoginButton.isEnabled = false
            }
        })
        if (!checkFlag) {
            binding!!.get().view29.visibility = View.VISIBLE
            binding!!.get().view31.visibility = View.VISIBLE
            binding!!.get().phoneSignInView.visibility = View.VISIBLE
            binding!!.get().fbLoginButton.isEnabled = false
            binding!!.get().phoneLoginButton.isEnabled = false
        } else {
            binding!!.get().view29.visibility = View.GONE
            binding!!.get().view31.visibility = View.GONE
            binding!!.get().phoneSignInView.visibility = View.GONE
            binding!!.get().fbLoginButton.isEnabled = true
            binding!!.get().phoneLoginButton.isEnabled = true
        }
        binding!!.get().view29.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                Toast.makeText(context, "You need to check is on.", Toast.LENGTH_SHORT).show()
            }
        })
        binding!!.get().view31.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                Toast.makeText(context, "You need to check is on.", Toast.LENGTH_SHORT).show()
            }
        })
        binding!!.get().phoneSignInView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                Toast.makeText(context, "You need to check is on.", Toast.LENGTH_SHORT).show()
            }
        })
        if (Config.ENABLE_FACEBOOK_LOGIN && Config.ENABLE_GOOGLE_LOGIN) {
            binding!!.get().fbLoginButton.visibility = View.VISIBLE
            binding!!.get().googleSignInButton.visibility = View.VISIBLE
            binding!!.get().privacyPolicyCheckbox.visibility = View.VISIBLE
        } else if (Config.ENABLE_FACEBOOK_LOGIN) {
            binding!!.get().fbLoginButton.visibility = View.VISIBLE
            binding!!.get().googleSignInButton.visibility = View.GONE
            binding!!.get().privacyPolicyCheckbox.visibility = View.VISIBLE
        } else if (Config.ENABLE_GOOGLE_LOGIN) {
            binding!!.get().fbLoginButton.visibility = View.GONE
            binding!!.get().googleSignInButton.visibility = View.VISIBLE
            binding!!.get().privacyPolicyCheckbox.visibility = View.VISIBLE
        } else {
            binding!!.get().fbLoginButton.visibility = View.GONE
            binding!!.get().googleSignInButton.visibility = View.GONE
            binding!!.get().privacyPolicyCheckbox.visibility = View.GONE
        }
        if (Config.ENABLE_PHONE_LOGIN) {
            binding!!.get().phoneLoginButton.visibility = View.VISIBLE
        } else {
            binding!!.get().phoneLoginButton.visibility = View.GONE
        }
        if (Config.ENABLE_FACEBOOK_LOGIN || Config.ENABLE_GOOGLE_LOGIN || Config.ENABLE_PHONE_LOGIN) {
            binding!!.get().privacyPolicyCheckbox.visibility = View.VISIBLE
        } else {
            binding!!.get().privacyPolicyCheckbox.visibility = View.GONE
        }
        val onClickListener = View.OnClickListener { v: View? ->
            psDialogMsg!!.showWarningDialog(getString(R.string.error_message__to_check_agreement), getString(R.string.app__ok))
            psDialogMsg!!.show()
        }
        binding!!.get().view29.setOnClickListener(onClickListener)
        binding!!.get().view31.setOnClickListener(onClickListener)
        binding!!.get().phoneSignInView.setOnClickListener(onClickListener)
    }

    private fun updateLoginBtnStatus() {
        if (userViewModel!!.isLoading) {
            binding!!.get().loginButton.text = resources.getString(R.string.message__loading)
        } else {
            binding!!.get().loginButton.text = resources.getString(R.string.login__login)
        }
    }

    private fun doSubmit(email: String, password: String) {

        //prgDialog.get().show();
        userViewModel!!.setUserLogin(User(
                "",
                "",
                "",
                "",
                "",
                email,
                email,
                "",
                password,
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                this!!.token!!,
                "",
                "",
                "",
                null,
                null
        ))
        userViewModel!!.isLoading = true
    }

    override fun initViewModels() {
        userViewModel = ViewModelProvider(this, viewModelFactory).get(UserViewModel::class.java)
    }

    override fun initAdapters() {}
    override fun initData() {
        token = pref.getString(Constants.NOTI_TOKEN, Constants.USER_NO_DEVICE_TOKEN)
        userViewModel!!.loadingState.observe(this, Observer { loadingState: Boolean? ->
            if (loadingState != null && loadingState) {
                prgDialog!!.get().show()
            } else {
                prgDialog!!.get().cancel()
            }
            updateLoginBtnStatus()
        })
        userViewModel!!.userLoginStatus.observe(this, Observer { listResource: Resource<UserLogin?>? ->
            if (listResource != null) {
                Utils.psLog("Got Data" + listResource.message + listResource.toString())
                when (listResource.status) {
                    Status.LOADING -> {
                    }
                    Status.SUCCESS ->                         // Success State
                        // Data are from Server
                        if (listResource.data != null) {
                            try {
                                Utils.updateUserLoginData(pref, listResource.data.user)
                                Utils.navigateAfterUserLogin(getActivity(), navigationController)
                            } catch (ne: NullPointerException) {
                                Utils.psErrorLog("Null Pointer Exception.", ne)
                            } catch (e: Exception) {
                                Utils.psErrorLog("Error in getting notification flag data.", e)
                            }
                            userViewModel!!.setLoadingState(false)
                        }
                    Status.ERROR -> {
                        // Error State
                        psDialogMsg!!.showErrorDialog(listResource.message, getString(R.string.app__ok))
                        psDialogMsg!!.show()
                        userViewModel!!.setLoadingState(false)
                    }
                    else ->                         // Default
                        userViewModel!!.setLoadingState(false)
                }
            } else {

                // Init Object or Empty Data
                Utils.psLog("Empty Data")
            }
        })
        binding!!.get().fbLoginButton.setPermissions(listOf("email"))
        binding!!.get().fbLoginButton.fragment = this
        binding!!.get().fbLoginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                val request = GraphRequest.newMeRequest(loginResult.accessToken
                ) { `object`: JSONObject?, response: GraphResponse? ->
                    var name: String? = ""
                    var email: String? = ""
                    var id: String = ""
                    val imageURL: String = ""
                    try {
                        if (`object` != null) {
                            name = `object`.getString("name")
                        }
                        //link.setText(object.getString("link"));
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    try {
                        if (`object` != null) {
                            email = `object`.getString("email")
                        }
                        //link.setText(object.getString("link"));
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    try {
                        if (`object` != null) {
                            id = `object`.getString("id")
                        }
                        //link.setText(object.getString("link"));
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    if (!(id == "")) {
                        prgDialog!!.get().show()
                        userViewModel!!.registerFBUser(id, name, email, imageURL)
                    }
                }
                val parameters = Bundle()
                parameters.putString("fields", "email,name,id")
                request.parameters = parameters
                request.executeAsync()
            }

            override fun onCancel() {
                Utils.psLog("OnCancel.")
            }

            override fun onError(e: FacebookException) {
                Utils.psLog("OnError.")
            }
        })
        userViewModel!!.registerFBUserData.observe(this, Observer { listResource: Resource<UserLogin?>? ->
            if (listResource != null) {
                Utils.psLog("Got Data" + listResource.message + listResource.toString())
                when (listResource.status) {
                    Status.LOADING ->                         // Loading State
                        // Data are from Local DB
                        prgDialog!!.get().show()
                    Status.SUCCESS ->                         // Success State
                        // Data are from Server
                        if (listResource.data != null) {
                            try {
                                Utils.updateUserLoginData(pref, listResource.data.user)
                                Utils.navigateAfterUserLogin(getActivity(), navigationController)
                            } catch (ne: NullPointerException) {
                                Utils.psErrorLog("Null Pointer Exception.", ne)
                            } catch (e: Exception) {
                                Utils.psErrorLog("Error in getting notification flag data.", e)
                            }
                            userViewModel!!.isLoading = false
                            prgDialog!!.get().cancel()
                        }
                    Status.ERROR -> {
                        // Error State
                        userViewModel!!.isLoading = false
                        prgDialog!!.get().cancel()
                    }
                    else -> {
                    }
                }
            } else {

                // Init Object or Empty Data
                Utils.psLog("Empty Data")
            }
        })
        userViewModel!!.googleLoginData.observe(this, Observer { listResource: Resource<UserLogin?>? ->
            if (listResource != null) {
                Utils.psLog("Got Data" + listResource.message + listResource.toString())
                when (listResource.status) {
                    Status.LOADING ->                         // Loading State
                        // Data are from Local DB
                        prgDialog!!.get().show()
                    Status.SUCCESS ->                         // Success State
                        // Data are from Server
                        if (listResource.data != null) {
                            try {
                                Utils.updateUserLoginData(pref, listResource.data.user)
                                Utils.navigateAfterUserLogin(getActivity(), navigationController)
                            } catch (ne: NullPointerException) {
                                Utils.psErrorLog("Null Pointer Exception.", ne)
                            } catch (e: Exception) {
                                Utils.psErrorLog("Error in getting notification flag data.", e)
                            }
                            userViewModel!!.isLoading = false
                            prgDialog!!.get().cancel()
                        }
                    Status.ERROR -> {
                        // Error State
                        userViewModel!!.isLoading = false
                        prgDialog!!.get().cancel()
                        psDialogMsg!!.showErrorDialog(listResource.message, getString(R.string.app__ok))
                        psDialogMsg!!.show()
                    }
                    else -> {
                    }
                }
            } else {

                // Init Object or Empty Data
                Utils.psLog("Empty Data")
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager!!.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == Constants.REQUEST_CODE__GOOGLE_SIGN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                account?.let { firebaseAuthWithGoogle(it) }
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(MotionScene.TAG, "Google sign in failed", e)
                // ...
            }
        }
    }
}