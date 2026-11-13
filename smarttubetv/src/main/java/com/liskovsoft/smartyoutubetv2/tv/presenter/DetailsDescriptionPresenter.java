/*
 * Copyright (c) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.liskovsoft.smartyoutubetv2.tv.presenter;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.ViewGroup;
import android.util.TypedValue;

import androidx.leanback.widget.AbstractDetailsDescriptionPresenter;

import com.liskovsoft.smartyoutubetv2.common.app.models.data.Video;

public class DetailsDescriptionPresenter extends AbstractDetailsDescriptionPresenter {

    @Override
    protected void onBindDescription(ViewHolder viewHolder, Object item) {
        Video video = (Video) item;

        if (video != null) {
            // Keep the playback title discreet and anchor it to the upper-right edge.
            viewHolder.getTitle().setText(video.getTitle());
            viewHolder.getTitle().setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            viewHolder.getTitle().setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
            viewHolder.getTitle().setMaxLines(2);
            viewHolder.getTitle().setEllipsize(TextUtils.TruncateAt.END);
            ViewGroup.LayoutParams titleParams = viewHolder.getTitle().getLayoutParams();
            if (titleParams != null) {
                titleParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                viewHolder.getTitle().setLayoutParams(titleParams);
            }

            viewHolder.getSubtitle().setText(video.getAuthor());
            viewHolder.getBody().setText(video.getSecondTitle());
        }
    }
}
