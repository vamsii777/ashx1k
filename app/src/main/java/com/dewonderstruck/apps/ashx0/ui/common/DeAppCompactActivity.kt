package com.dewonderstruck.apps.ashx0.ui.common

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.utils.Utils
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

/**
 * Parent Activity class of all activity in this project.
 * Created by Vamsi Madduluri on 12/2/17.
 * Contact Email : vamsii.wrkhost@gmail.com
 */
abstract class DeAppCompactActivity : AppCompatActivity(), HasSupportFragmentInjector {
    //region Variables
    @JvmField
    @Inject
    var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>? = null


    @set:Inject
    open var viewModelFactory: ViewModelProvider.Factory? = null


    @set:Inject
    open var navigationController: NavigationController2? = null

    //endregion
    //region Override Methods
    override fun supportFragmentInjector(): DispatchingAndroidInjector<Fragment> {
        return dispatchingAndroidInjector!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    //endregion
    //region Toolbar Init
    protected fun initToolbar(toolbar: Toolbar?, title: String, color: Int): Toolbar? {
        if (toolbar != null) {
            toolbar.title = title
            if (color != 0) {
                try {
                    toolbar.setTitleTextColor(resources.getColor(color))
                } catch (e: Exception) {
                    Utils.psErrorLog("Can't set color.", e)
                }
            } else {
                try {
                    toolbar.setTitleTextColor(resources.getColor(R.color.text__white))
                } catch (e: Exception) {
                    Utils.psErrorLog("Can't set color.", e)
                }
            }
            try {
                setSupportActionBar(toolbar)
            } catch (e: Exception) {
                Utils.psErrorLog("Error in set support action bar.", e)
            }
            try {
                if (supportActionBar != null) {
                    supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                }
            } catch (e: Exception) {
                Utils.psErrorLog("Error in set display home as up enabled.", e)
            }

            /*
               Warning :

               Set Spannable text must call after set Support Action Bar

               The problem is actually that you have a non-String title set on the Toolbar
               at the time you're calling setSupportActionBar. It will end up in ToolbarWidgetWrapper,
               where it will be used when you click the navigation menu. You can use any CharSequence (e.g. Spannable)
               after calling setSuppportActionBar.

               https://stackoverflow.com/questions/27023802/clicking-toolbar-navigation-icon-crashes-the-app/27044868

             */if (title != "") {
                setToolbarText(toolbar, title)
            }
        } else {
            Utils.psLog("Toolbar is null")
        }
        return toolbar
    }

    protected fun initToolbar(toolbar: Toolbar?, title: String): Toolbar? {
        if (toolbar != null) {
            toolbar.title = title
            try {
                toolbar.setTitleTextColor(resources.getColor(R.color.text__white))
            } catch (e: Exception) {
                Utils.psErrorLog("Can't set color.", e)
            }
            try {
                setSupportActionBar(toolbar)
            } catch (e: Exception) {
                Utils.psErrorLog("Error in set support action bar.", e)
            }
            try {
                if (supportActionBar != null) {
                    supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                }
            } catch (e: Exception) {
                Utils.psErrorLog("Error in set display home as up enabled.", e)
            }

            /*
               Warning :

               Set Spannable text must call after set Support Action Bar

               The problem is actually that you have a non-String title set on the Toolbar
               at the time you're calling setSupportActionBar. It will end up in ToolbarWidgetWrapper,
               where it will be used when you click the navigation menu. You can use any CharSequence (e.g. Spannable)
               after calling setSuppportActionBar.

               https://stackoverflow.com/questions/27023802/clicking-toolbar-navigation-icon-crashes-the-app/27044868

             */if (title != "") {
                setToolbarText(toolbar, title)
            }
        } else {
            Utils.psLog("Toolbar is null")
        }
        return toolbar
    }

    fun setToolbarText(toolbar: Toolbar, text: String) {
        Utils.psLog("Set Toolbar Text : $text")
        toolbar.title = Utils.getSpannableString(toolbar.context, text, Utils.Fonts.ROBOTO)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item != null) {
            Utils.psLog("Clicked " + item.itemId)
            when (item.itemId) {
                android.R.id.home -> {
                    onBackPressed()
                    return true
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //endregion
    //region Setup Fragment
    protected fun setupFragment(fragment: Fragment?) {
        try {
            this.supportFragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment!!)
                    .commitAllowingStateLoss()
        } catch (e: Exception) {
            Utils.psErrorLog("Error! Can't replace fragment.", e)
        }
    }

    protected fun setupFragment(fragment: Fragment?, frameId: Int) {
        try {
            this.supportFragmentManager.beginTransaction()
                    .replace(frameId, fragment!!)
                    .commitAllowingStateLoss()
        } catch (e: Exception) {
            Utils.psErrorLog("Error! Can't replace fragment.", e)
        }
    } //endregion
}