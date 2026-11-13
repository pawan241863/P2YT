package com.liskovsoft.smartyoutubetv2.tv.ui.main;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.liskovsoft.smartyoutubetv2.common.app.presenters.SplashPresenter;
import com.liskovsoft.smartyoutubetv2.common.app.views.SplashView;
import com.liskovsoft.smartyoutubetv2.common.misc.MotherActivity;

public class SplashActivity extends MotherActivity implements SplashView {
    private static final String TAG = SplashActivity.class.getSimpleName();
    private static final long SPLASH_ANIMATION_DURATION_MS = 850L;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private final Runnable mFinishSplash = this::finishSafely;
    private Intent mNewIntent;
    private SplashPresenter mPresenter;
    private AnimatorSet mLogoAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mNewIntent = getIntent();
        showStartupAnimation();

        mPresenter = SplashPresenter.instance(this);
        mPresenter.setView(this);
        mPresenter.onViewInitialized();
    }

    private void showStartupAnimation() {
        FrameLayout splashRoot = new FrameLayout(this);
        splashRoot.setBackgroundColor(Color.rgb(5, 5, 5));

        ImageView logo = new ImageView(this);
        logo.setImageResource(com.liskovsoft.smartyoutubetv2.tv.R.drawable.psquare_circle_logo);
        logo.setScaleType(ImageView.ScaleType.FIT_CENTER);
        logo.setAlpha(0f);
        logo.setScaleX(0.72f);
        logo.setScaleY(0.72f);

        int logoSize = (int) (144 * getResources().getDisplayMetrics().density + 0.5f);
        FrameLayout.LayoutParams logoParams = new FrameLayout.LayoutParams(logoSize, logoSize, Gravity.CENTER);
        splashRoot.addView(logo, logoParams);
        setContentView(splashRoot);

        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(logo, "alpha", 0f, 1f);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(logo, "scaleX", 0.72f, 1.08f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(logo, "scaleY", 0.72f, 1.08f, 1f);

        mLogoAnimator = new AnimatorSet();
        mLogoAnimator.playTogether(fadeIn, scaleX, scaleY);
        mLogoAnimator.setDuration(SPLASH_ANIMATION_DURATION_MS);
        mLogoAnimator.setInterpolator(new DecelerateInterpolator());
        mLogoAnimator.start();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        mNewIntent = intent;

        if (mPresenter != null) {
            mPresenter.onViewInitialized();
        }
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacks(mFinishSplash);
        if (mLogoAnimator != null) {
            mLogoAnimator.cancel();
        }
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onViewDestroyed();
        }
    }

    @Override
    public Intent getNewIntent() {
        return mNewIntent;
    }

    @Override
    public void finishView() {
        // Let the logo reveal complete before handing control to the requested screen.
        mHandler.removeCallbacks(mFinishSplash);
        mHandler.postDelayed(mFinishSplash, SPLASH_ANIMATION_DURATION_MS);
    }

    private void finishSafely() {
        try {
            finish();
        } catch (NullPointerException e) {
            // Guard against a rare framework race while the activity stack is changing.
            e.printStackTrace();
        }
    }
}
