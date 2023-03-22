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
import android.content.res.Configuration;
import android.graphics.Color;
import android.view.View;
import android.view.WindowManager;
import id.kuato.woahelper.util.AppUtils;

public class Utils {
  public static void setDecorUI(Activity activity) {
    View decorView = activity.getWindow().getDecorView();
    int uiOptions = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
    int nightModeFlags =
        activity.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
    if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
      activity.getWindow().getDecorView().setSystemUiVisibility(0);
      activity.getWindow().setStatusBarColor(Color.BLACK);
      activity.getWindow().setNavigationBarColor(Color.BLACK);
    } else {
      decorView.setSystemUiVisibility(uiOptions);
      activity.getWindow().setStatusBarColor(Color.WHITE);
      activity.getWindow().setNavigationBarColor(Color.WHITE);
    }
    activity
        .getWindow()
        .setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
  }
}