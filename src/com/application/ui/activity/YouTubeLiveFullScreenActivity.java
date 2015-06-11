/**
 * 
 */
package com.application.ui.activity;

import android.os.Bundle;

import com.application.utils.AppConstants;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.mobcast.R;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class YouTubeLiveFullScreenActivity extends
		YouTubeFailureRecoveryActivity {
	private static final String TAG = YouTubeLiveFullScreenActivity.class
			.getSimpleName();

	private YouTubePlayerView youTubeView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_youtube_fullscreen);
		initUi();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onInitializationSuccess(YouTubePlayer.Provider provider,
			YouTubePlayer player, boolean wasRestored) {
		if (!wasRestored) {
			player.cueVideo("Q6-nwNx4yJo");
		}
	}

	@Override
	protected YouTubePlayer.Provider getYouTubePlayerProvider() {
		return (YouTubePlayerView) findViewById(R.id.fragmentLiveStreamYouTubePlayerView);
	}

	private void initUi() {
		youTubeView = (YouTubePlayerView) findViewById(R.id.fragmentLiveStreamYouTubePlayerView);
		youTubeView.initialize(AppConstants.YOUTUBE.API_KEY, this);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
