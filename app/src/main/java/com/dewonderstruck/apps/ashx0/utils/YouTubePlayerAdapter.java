package com.dewonderstruck.apps.ashx0.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dewonderstruck.apps.Config;
import com.dewonderstruck.apps.ashx0.R;
import com.flipkart.youtubeview.YouTubePlayerView;
import com.flipkart.youtubeview.models.ImageLoader;

import java.util.ArrayList;

public class YouTubePlayerAdapter extends RecyclerView.Adapter<YouTubePlayerAdapter.YouTubePlayerViewHolder> {

    private ArrayList<String> videoIds;
    private Fragment fragment;
    private int playerType;

    private ImageLoader imageLoader = new ImageLoader() {
        @Override
        public void loadImage(@NonNull ImageView imageView, @NonNull String url, int height, int width) {
            Glide.with(imageView.getContext()).load(url).centerCrop().into(imageView);
        }
    };

    public YouTubePlayerAdapter(ArrayList<String> contents, Fragment fragment, int playerType) {
        this.videoIds = contents;
        this.fragment = fragment;
        this.playerType = playerType;
    }

    @Override
    public int getItemCount() {
        return videoIds.size();
    }

    @NonNull
    @Override
    public YouTubePlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.youtube_player, parent, false);
        return new YouTubePlayerViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull final YouTubePlayerViewHolder holder, int position) {
        YouTubePlayerView playerView = holder.playerView;
        String videoId = videoIds.get(position);

        playerView.initPlayer(Config.DEVELOPER_KEY, videoId, "https://cdn.rawgit.com/flipkart-incubator/inline-youtube-view/60bae1a1/youtube-android/youtube_iframe_player.html", playerType, null, fragment, imageLoader);
        /*if you want to have your custom icon instead of default icon. Use the below code
            and add your own image as below. Make sure to call below method after initPlayer
         */
        //playerView.overridePlayIcon("https://i.ibb.co/ZKZ2qdc/ic-launcher.png");

        //if you have the resource id then pass it as below
        //playerView.overridePlayIcon(R.mipmap.ic_launcher);
    }

    static class YouTubePlayerViewHolder extends RecyclerView.ViewHolder {
        YouTubePlayerView playerView;

        YouTubePlayerViewHolder(View view) {
            super(view);
            playerView = view.findViewById(R.id.youtube_player_view);
        }
    }
}