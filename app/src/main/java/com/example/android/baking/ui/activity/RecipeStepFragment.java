package com.example.android.baking.ui.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.baking.R;
import com.example.android.baking.data.model.Step;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;

public class RecipeStepFragment extends Fragment {

    public static final String STEP_ID = "step_id";
    private static final String STEP_POSITION = "pos_id";
    private static final String PLAY_WHEN_READY_KEY = "play_when_ready_key";

    @BindView(R.id.nsc_recipe_step_description)
    NestedScrollView mInstructionsContainer;
    @BindView(R.id.exo_player_view)
    SimpleExoPlayerView mExoPlayerView;
    @BindView(R.id.iv_step_image)
    ImageView mImageViewStepImage;
    @BindView(R.id.tv_step_description)
    TextView mTextViewInstructions;

    private SimpleExoPlayer mExoPlayer;
    private Step mStep;
    private Unbinder mLayoutBinder;

    private long mCurrentPosition = 0;
    private boolean mPlayWhenReady = true;

    public RecipeStepFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(STEP_ID)) {
            mStep = getArguments().getParcelable(STEP_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipe_step_detail, container, false);

        checkSavedState(savedInstanceState);

        mLayoutBinder = ButterKnife.bind(this, rootView);

        setRecipeFragmentLayoutViews(mStep);

        return rootView;
    }

    public void checkSavedState(Bundle savedState) {
        if (savedState != null && savedState.containsKey(STEP_POSITION)) {
            mCurrentPosition = savedState.getLong(STEP_POSITION);
            mPlayWhenReady = savedState.getBoolean(PLAY_WHEN_READY_KEY);
        }
    }

    public void setRecipeFragmentLayoutViews(Step mStep){
        mTextViewInstructions.setText(mStep.getDescription());

        if (!mStep.getThumbnailURL().isEmpty()) {
            Picasso.get().load(mStep.getThumbnailURL())
                    .placeholder(R.drawable.ic_placeholdertablet)
                    .into(mImageViewStepImage);
            mImageViewStepImage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(mStep.getVideoURL()))
            initializePlayer(Uri.parse(mStep.getVideoURL()));
        else {
            mInstructionsContainer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mLayoutBinder.unbind();
        Timber.d("onDestroyView");
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putLong(STEP_POSITION, mCurrentPosition);
        outState.putBoolean(PLAY_WHEN_READY_KEY, mPlayWhenReady);
    }

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);

            mExoPlayerView.setPlayer(mExoPlayer);
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                    Util.getUserAgent(getContext(), getString(R.string.app_name)), bandwidthMeter);
            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(mediaUri);
            mExoPlayer.prepare(videoSource);

            // onRestore
            if (mCurrentPosition != 0)
                mExoPlayer.seekTo(mCurrentPosition);

            mExoPlayer.setPlayWhenReady(mPlayWhenReady);
            mExoPlayerView.setVisibility(View.VISIBLE);
        }
    }


    private void releasePlayer() {
        if (mExoPlayer != null) {
            mPlayWhenReady = mExoPlayer.getPlayWhenReady();
            mCurrentPosition = mExoPlayer.getCurrentPosition();

            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }
}