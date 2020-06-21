package com.dewonderstruck.apps.ashx0.ui.comment.detail;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.PreferenceManager;

import com.dewonderstruck.apps.Config;

import com.dewonderstruck.apps.ashx0.databinding.ActivityCommentDetailBinding;
import com.dewonderstruck.apps.ashx0.ui.common.PSAppCompactActivity;
import com.dewonderstruck.apps.ashx0.utils.Constants;
import com.dewonderstruck.apps.ashx0.utils.MyContextWrapper;
import com.dewonderstruck.apps.ashx0.R;
import androidx.databinding.DataBindingUtil;

/**
 * Created by Vamsi Madduluri
 * Contact Email : vamsii.wrkhost@gmail.com
 * Website : http://www.dewonderstruck.com
 */
public class CommentDetailActivity extends PSAppCompactActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityCommentDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_comment_detail);

        // Init all UI
        initUI(binding);
    }

    @Override
    protected void attachBaseContext(Context newBase) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(newBase);
        String CURRENT_LANG_CODE = preferences.getString(Constants.LANGUAGE_CODE, Config.DEFAULT_LANGUAGE);
        String CURRENT_LANG_COUNTRY_CODE = preferences.getString(Constants.LANGUAGE_COUNTRY_CODE, Config.DEFAULT_LANGUAGE_COUNTRY_CODE);

        super.attachBaseContext(MyContextWrapper.wrap(newBase, CURRENT_LANG_CODE, CURRENT_LANG_COUNTRY_CODE, true));
    }


    //region Private Methods

    private void initUI(ActivityCommentDetailBinding binding) {

        // Toolbar
        initToolbar(binding.toolbar, getResources().getString(R.string.comment__detail_title));

        // setup Fragment
        CommentDetailFragment commentDetailFragment = new CommentDetailFragment();
        setupFragment(commentDetailFragment);
        // Or you can call like this
        //setupFragment(new NewsListFragment(), R.id.content_frame);

    }

    //endregion

}
