package com.dewonderstruck.apps.ashx0.ui.language

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.binding.FragmentDataBindingComponent
import com.dewonderstruck.apps.ashx0.databinding.FragmentLanguageBinding
import com.dewonderstruck.apps.ashx0.ui.apploading.AppLoadingActivity
import com.dewonderstruck.apps.ashx0.ui.common.DeFragment
import com.dewonderstruck.apps.ashx0.utils.AutoClearedValue
import com.dewonderstruck.apps.ashx0.utils.Constants
import com.dewonderstruck.apps.ashx0.utils.LanguageData
import com.dewonderstruck.apps.ashx0.utils.PSDialogMsg
import java.util.*
import javax.inject.Inject

class LanguageFragment : DeFragment() {
    //region Variables
    @set:Inject
    lateinit var sharedPreferences: SharedPreferences
    private val dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    private var psDialogMsg: PSDialogMsg? = null

    @VisibleForTesting
    private var binding: AutoClearedValue<FragmentLanguageBinding>? = null
    private var selectedPosition = 0
    private val languageDataList = Arrays.asList(
            LanguageData("English", "en", ""),
            LanguageData("Arabic", "ar", ""),
            LanguageData("Spanish", "es", ""),
            LanguageData("Chinese (Mandarin)", "zh", ""),
            LanguageData("French", "fr", ""),
            LanguageData("German", "de", ""),
            LanguageData("India (Hindi)", "hi", "rIN"),
            LanguageData("Indonesian", "in", ""),
            LanguageData("Italian", "it", ""),
            LanguageData("Japanese", "ja", ""),
            LanguageData("Korean", "ko", ""),
            LanguageData("Malay", "ms", ""),
            LanguageData("Portuguese", "pt", ""),
            LanguageData("Russian", "ru", ""),
            LanguageData("Thai", "th", ""),
            LanguageData("Turkish", "tr", ""))

    //region Override Methods
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val dataBinding = DataBindingUtil.inflate<ViewDataBinding>(inflater, R.layout.fragment_language, container, false, dataBindingComponent) as FragmentLanguageBinding
        binding = AutoClearedValue(this, dataBinding)
        val LANG_CURRENT = sharedPreferences!!.getString(Constants.LANGUAGE_CODE, Config.DEFAULT_LANGUAGE)
        val CURRENT_COUNTRY_CODE = sharedPreferences!!.getString(Constants.LANGUAGE_COUNTRY_CODE, Config.DEFAULT_LANGUAGE_COUNTRY_CODE)
        for (i in languageDataList.indices) {
            val languageData = languageDataList[i]
            if (LANG_CURRENT == languageData.languageLocalCode && CURRENT_COUNTRY_CODE == languageData.languageCountry) {
                selectedPosition = i
                binding!!.get().languageTextView.text = languageData.languageName
                break
            }
        }
        if (selectedPosition == 0) {
            if (languageDataList != null && languageDataList.size > 0) {
                binding!!.get().languageTextView.text = languageDataList[0].languageName
            }
        }
        return binding!!.get().root
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun initUIAndActions() {
        psDialogMsg = PSDialogMsg(activity, false)

        //fadeIn Animation
        fadeIn(binding!!.get().root)
        binding!!.get().cardLanguageView.setOnClickListener { v: View? ->
            val adapter = LanguageSelectionListAdapter(languageDataList, context, selectedPosition)
            if (context != null) {
                val mBuilder = AlertDialog.Builder(context!!)
                mBuilder.setTitle(getString(R.string.language__title))
                mBuilder.setSingleChoiceItems(adapter, -1) { dialogInterface: DialogInterface, i: Int ->
                    psDialogMsg!!.showConfirmDialog(getString(R.string.language__language_change, languageDataList[i].languageName), getString(R.string.app__ok), getString(R.string.app__cancel))
                    psDialogMsg!!.show()
                    psDialogMsg!!.okButton.setOnClickListener { v1: View? ->
                        changeLang(languageDataList[i].languageLocalCode, languageDataList[i].languageCountry)
                        if (activity != null) {
                            activity!!.finish()
                            startActivity(Intent(activity, AppLoadingActivity::class.java))
                            psDialogMsg!!.cancel()
                        }
                    }
                    psDialogMsg!!.cancelButton.setOnClickListener { v1: View? -> psDialogMsg!!.cancel() }
                    binding!!.get().languageTextView.text = languageDataList[i].languageName
                    dialogInterface.dismiss()
                }
                val mDialog = mBuilder.create()
                mDialog.show()
            }
        }
    }

    override fun initViewModels() {}
    override fun initAdapters() {}
    override fun initData() {}
    private fun changeLang(lang: String, countryCode: String) {
        val editor = sharedPreferences!!.edit()
        editor.putString(Constants.LANGUAGE_CODE, lang)
        editor.putString(Constants.LANGUAGE_COUNTRY_CODE, countryCode)
        editor.apply()
    }
}