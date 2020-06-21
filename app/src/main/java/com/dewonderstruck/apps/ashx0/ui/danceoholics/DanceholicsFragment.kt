package com.dewonderstruck.apps.ashx0.ui.danceoholics

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dewonderstruck.apps.Config
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.binding.FragmentDataBindingComponent
import com.dewonderstruck.apps.ashx0.databinding.FragmentDanceoholicsBinding
import com.dewonderstruck.apps.ashx0.ui.common.PSFragment
import com.dewonderstruck.apps.ashx0.utils.AutoClearedValue
import com.dewonderstruck.apps.ashx0.utils.PSDialogMsg
import com.dewonderstruck.apps.ashx0.viewmodel.user.UserViewModel
import com.facebook.ads.AdSize
import com.facebook.ads.AdView

class DanceholicsFragment : PSFragment() {
    //region Variables
    private val dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    @VisibleForTesting
    private var binding: AutoClearedValue<FragmentDanceoholicsBinding>? = null
    private var userViewModel: UserViewModel? = null
    private var psDialogMsg: PSDialogMsg? = null
    private val videoId = "2Vv-BfVoq4g"
    private val playerType = 0
    private val fragment: Fragment? = null
    private var adView: AdView? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val dataBinding: FragmentDanceoholicsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_danceoholics, container, false, dataBindingComponent)
        binding = AutoClearedValue(this, dataBinding)

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
        view.setAdapter(youTubePlayerAdapter);*/return binding!!.get().root
    }

    override fun initUIAndActions() {
        psDialogMsg = PSDialogMsg(activity, false)

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
        initializeYoutubePlayer()
    }

    private fun initializeYoutubePlayer() {

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
        val youTubePlayerView = binding!!.get().dncyoutubelist
        lifecycle.addObserver(youTubePlayerView)
        /*youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                String videoId = "S0Q4gqBUs7c";
                youTubePlayer.loadVideo(videoId, 0);
            }
        });
*/binding!!.get().fbdnc.setOnClickListener {
            val url = "https://www.facebook.com/danceoholics/"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }
        binding!!.get().twitterdnc.setOnClickListener {
            val url = "https://twitter.com/sudhirdancecom1?s=09"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }
        binding!!.get().dnctvyou.setOnClickListener {
            val url = "https://youtu.be/73KnYMA76ZM"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }
        binding!!.get().dncTextView10.setOnClickListener {
            val url = "https://www.facebook.com/danceoholics/"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }
        binding!!.get().dncTextView11.setOnClickListener {
            val url = "https://twitter.com/sudhirdancecom1?s=09"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }
        binding!!.get().dncTextView12.setOnClickListener {
            val url = "https://youtu.be/73KnYMA76ZM"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }
        adView = AdView(requireActivity(), "944403385999685_944408512665839", AdSize.BANNER_HEIGHT_50)
        // Find the Ad Container
        val adContainer = binding!!.get().dncbannerContainer
        // Add the ad view to your activity layout
        adContainer.addView(adView)
        // Request an ad
        adView!!.loadAd()
    }

    override fun initViewModels() {
        userViewModel = ViewModelProvider(this, viewModelFactory).get(UserViewModel::class.java)
    }

    override fun initAdapters() {}
    override fun initData() {
        val consentStatusIsReady = pref.getBoolean(Config.CONSENTSTATUS_IS_READY_KEY, false)
    }

    override fun onDestroy() {
        if (adView != null) {
            adView!!.destroy()
        }
        super.onDestroy()
    }
}