package com.dewonderstruck.apps.ashx0.ui.danceoholics;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviderKt;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dewonderstruck.apps.ashx0.utils.YouTubePlayerAdapter;

import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.dewonderstruck.apps.Config;
import com.dewonderstruck.apps.MainActivity;
import com.dewonderstruck.apps.ashx0.R;
import com.dewonderstruck.apps.ashx0.binding.FragmentDataBindingComponent;
import com.dewonderstruck.apps.ashx0.databinding.FragmentDanceoholicsBinding;
import com.dewonderstruck.apps.ashx0.databinding.FragmentSettingBinding;
import com.dewonderstruck.apps.ashx0.ui.common.PSFragment;
import com.dewonderstruck.apps.ashx0.ui.setting.SettingFragment;
import com.dewonderstruck.apps.ashx0.utils.AutoClearedValue;
import com.dewonderstruck.apps.ashx0.utils.Constants;
import com.dewonderstruck.apps.ashx0.utils.GetSizeTaskForGlide;
import com.dewonderstruck.apps.ashx0.utils.PSDialogMsg;
import com.dewonderstruck.apps.ashx0.utils.Utils;
import com.dewonderstruck.apps.ashx0.viewmodel.user.UserViewModel;
import com.facebook.login.LoginManager;
import com.google.ads.consent.ConsentForm;
import com.google.ads.consent.ConsentFormListener;
import com.google.ads.consent.ConsentStatus;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayerSupportFragmentX;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class DanceholicsFragment extends PSFragment {
    //region Variables
    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    @VisibleForTesting
    private AutoClearedValue<FragmentDanceoholicsBinding> binding;
    private UserViewModel userViewModel;
    private PSDialogMsg psDialogMsg;
    private String videoId ="2Vv-BfVoq4g";
    private int playerType;
    private Fragment fragment;
    private AdView adView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        FragmentDanceoholicsBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_danceoholics, container, false, dataBindingComponent);
        binding = new AutoClearedValue<>(this, dataBinding);

        /*RecyclerView view  = binding.get().dncyoutubelist;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        view.setLayoutManager(linearLayoutManager);
        Bundle arguments = getArguments();
        int playerType = 0;
        if (arguments != null) {
            playerType = arguments.getInt("playerType");
        }*/
      /*  ArrayList<String> videoIds = new ArrayList<>();
        videoIds.add("2Vv-BfVoq4g");
        videoIds.add("D5drYkLiLI8");
        videoIds.add("K0ibBPhiaG0");
        videoIds.add("ebXbLfLACGM");
        videoIds.add("mWRsgZuwf_8");
        YouTubePlayerAdapter youTubePlayerAdapter = new YouTubePlayerAdapter(videoIds, this, playerType);
        view.setAdapter(youTubePlayerAdapter);*/
        return binding.get().getRoot();
    }



    @Override
    protected void initUIAndActions() {

        psDialogMsg = new PSDialogMsg(getActivity(), false);

       /* binding.get().notificationSettingTextView.setText(binding.get().notificationSettingTextView.getText().toString());
        binding.get().editProfileTextView.setText(binding.get().editProfileTextView.getText().toString());
        binding.get().logOutTextView.setText(binding.get().logOutTextView.getText().toString());
        binding.get().appInfoTextView.setText(binding.get().appInfoTextView.getText().toString());
*/

        //binding.get().appVersionTextView.setText(String.format("%s%s%s", getString(R.string.setting__version_no)," ", Config.APP_VERSION));

        //<iframe width="697" height="392" src="https://www.youtube.com/embed/_V5bZIpZ4GU" frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>

       /* String myVideoYoutubeId = "_V5bZIpZ4GU";

        binding.get().youtubeDnc.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
            public void onPageFinished(WebView view, String url) {
                // do your stuff here
                binding.get().pgdnc.setVisibility(View.GONE);
            }
        });

        WebSettings webSettings = binding.get().youtubeDnc.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        //web.setPadding(0, 0, 0, 0);
        webSettings.setUseWideViewPort(true);
        binding.get().youtubeDnc.loadUrl("https://www.youtube.com/embed/" + myVideoYoutubeId);*/
        initializeYoutubePlayer();

    }

    private void initializeYoutubePlayer() {

     /*   ImageLoader imageLoader = (imageView, url, height, width) -> Glide.with(imageView.getContext()).load(url).centerCrop().into(imageView);
*/
        /*youTubePlayerFragment = (YouTubePlayerSupportFragmentX) requireActivity().getSupportFragmentManager()
                .findFragmentById(R.id.youtube_player_fragment);

        if (youTubePlayerFragment == null)
            return;


        youTubePlayerFragment.initialize(Config.DEVELOPER_KEY, new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                                boolean wasRestored) {
                if (!wasRestored) {
                    youTubePlayer = player;

                    //set the player style default
                    youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                    youTubePlayer.loadVideo("2zNSgSzhBfM");
                    //cue the 1st video by default
                    //youTubePlayer.cueVideo("73KnYMA76ZM");
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {
                //print or show error if initialization failed
                Log.e(TAG, "Youtube Player View initialization failed");
            }
        });*/

        /*YouTubePlayerView playerView = new YouTubePlayerView(getContext());
        playerView.initPlayer(Config.DEVELOPER_KEY, videoId, "https://cdn.rawgit.com/flipkart-incubator/inline-youtube-view/60bae1a1/youtube-android/youtube_iframe_player.html", playerType, null, this, imageLoader);
*/
        YouTubePlayerView youTubePlayerView = binding.get().dncyoutubelist;
        getLifecycle().addObserver(youTubePlayerView);
        /*youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                String videoId = "S0Q4gqBUs7c";
                youTubePlayer.loadVideo(videoId, 0);
            }
        });
*/
        binding.get().fbdnc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://www.facebook.com/danceoholics/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        binding.get().twitterdnc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://twitter.com/sudhirdancecom1?s=09";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        binding.get().dnctvyou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://youtu.be/73KnYMA76ZM";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        binding.get().dncTextView10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://www.facebook.com/danceoholics/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        binding.get().dncTextView11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://twitter.com/sudhirdancecom1?s=09";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        binding.get().dncTextView12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://youtu.be/73KnYMA76ZM";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        adView = new AdView(requireActivity(), "944403385999685_944408512665839", AdSize.BANNER_HEIGHT_50);
        // Find the Ad Container
        LinearLayout adContainer = binding.get().dncbannerContainer;
        // Add the ad view to your activity layout
        adContainer.addView(adView);
        // Request an ad
        adView.loadAd();


    }


    @Override
    protected void initViewModels() {
        userViewModel = new ViewModelProvider(this, viewModelFactory).get(UserViewModel.class);
    }

    @Override
    protected void initAdapters() {

    }

    @Override
    protected void initData() {
        boolean consentStatusIsReady = pref.getBoolean(Config.CONSENTSTATUS_IS_READY_KEY, false);

    }

    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }



}
