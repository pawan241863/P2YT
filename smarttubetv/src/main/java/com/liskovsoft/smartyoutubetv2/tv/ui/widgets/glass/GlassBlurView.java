package com.liskovsoft.smartyoutubetv2.tv.ui.widgets.glass;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

/**
 * TV-friendly frosted glass surface.
 *
 * Uses a live backdrop capture instead of a translucent color-only overlay.
 * BlurView's RenderScript implementation supports the app's Android 5.0+
 * minimum API; the tinted drawable remains as a graceful visual fallback.
 */
public class GlassBlurView extends BlurView {
    private boolean blurConfigured;

    public GlassBlurView(Context context) {
        super(context);
    }

    public GlassBlurView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GlassBlurView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        configureBackdropBlur();
    }

    private void configureBackdropBlur() {
        if (blurConfigured) {
            return;
        }

        View decor = getRootView();
        View content = decor == null ? null : decor.findViewById(android.R.id.content);
        ViewGroup blurRoot = content instanceof ViewGroup
                ? (ViewGroup) content
                : (decor instanceof ViewGroup ? (ViewGroup) decor : null);

        if (blurRoot == null || blurRoot == this) {
            return;
        }

        try {
            Drawable clearDrawable = blurRoot.getBackground();
            if (clearDrawable == null && decor != null) {
                clearDrawable = decor.getBackground();
            }

            setupWith(blurRoot, new RenderScriptBlur(getContext()))
                    .setFrameClearDrawable(clearDrawable)
                    .setBlurRadius(16f)
                    .setOverlayColor(Color.parseColor("#66202D40"));
            blurConfigured = true;
        } catch (RuntimeException ignored) {
            // Keep the translucent glass background if a device/GPU cannot blur.
        }
    }
}
