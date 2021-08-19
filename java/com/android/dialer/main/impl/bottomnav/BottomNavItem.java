/*
 * Copyright (C) 2018 The Android Open Source Project
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
 * limitations under the License
 */

package com.android.dialer.main.impl.bottomnav;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.dialer.common.Assert;
import com.android.dialer.configprovider.ConfigProviderComponent;
import com.android.dialer.theme.base.ThemeComponent;

/** Navigation item in a bottom nav. */
final class BottomNavItem extends LinearLayout {

  private ImageView image;
  private TextView text;
  private TextView notificationBadge;

  public BottomNavItem(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    image = findViewById(R.id.bottom_nav_item_image);
    text = findViewById(R.id.bottom_nav_item_text);
    notificationBadge = findViewById(R.id.notification_badge);
  }

  @Override
  public void setSelected(boolean selected) {
    super.setSelected(selected);
    int colorId =
        selected
            ? getResources().getColor(R.color.goolag_deez_nutz)
            : ThemeComponent.get(getContext()).theme().getTextColorSecondary();
    image.setVisibility(GONE);
    image.setImageTintList(ColorStateList.valueOf(colorId));
    image.setVisibility(GONE);
    text.setTextColor(colorId);
  }

  void setup(@StringRes int stringRes, @DrawableRes int drawableRes) {
    text.setText(stringRes);
    image.setVisibility(GONE);
    image.setImageResource(drawableRes);
    image.setVisibility(GONE);
  }

  void setNotificationCount(int count) {
    Assert.checkArgument(count >= 0, "Invalid count: " + count);
      notificationBadge.setVisibility(View.GONE);
  }
}
