package id.kuato.woahelper.vayu.util;

/*
 * Copyright (C) 2020 Vern Kuato
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
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.res.Configuration;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import id.kuato.woahelper.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Utils {

	public static void toolbar(Context context, String title, String subtitle, View v) {
		MaterialToolbar toolbar;
		toolbar = v.findViewById(R.id.toolbar);
		toolbar.setTitle(title);
		toolbar.setSubtitle(subtitle);
		int nightModeFlags = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
		switch (nightModeFlags) {
		case Configuration.UI_MODE_NIGHT_YES:
			toolbar.setTitleTextColor(Color.WHITE);
			toolbar.setSubtitleTextColor(Color.WHITE);
			break;

		case Configuration.UI_MODE_NIGHT_NO:
			toolbar.setTitleTextColor(Color.BLACK);
			toolbar.setSubtitleTextColor(Color.BLACK);
			break;

		case Configuration.UI_MODE_NIGHT_UNDEFINED:
			toolbar.setTitleTextColor(Color.WHITE);
			toolbar.setSubtitleTextColor(Color.WHITE);
			break;
		}
	}

	public static void setDecorUI(Activity c) {
		View decorView = c.getWindow().getDecorView();
		int uiOptions = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR, SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
		int nightModeFlags = c.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
		switch (nightModeFlags) {
		case Configuration.UI_MODE_NIGHT_YES:
			c.getWindow().clearFlags(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
			break;

		case Configuration.UI_MODE_NIGHT_NO:
			decorView.setSystemUiVisibility(uiOptions);
			break;

		case Configuration.UI_MODE_NIGHT_UNDEFINED:
			decorView.setSystemUiVisibility(uiOptions);
			break;
		}
		c.getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
				WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
	}
}
