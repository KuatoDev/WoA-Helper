package id.kuato.woahelper.util;
/*
 *  * Copyright (C) 2020 Vern Kuato
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  */
import android.content.res.Resources;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.content.res.Configuration;
import android.util.TypedValue;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.graphics.Insets;
import android.view.View;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.ViewCompat;
import android.widget.Toast;
import java.net.InetAddress;
import android.net.ConnectivityManager;
import android.content.Context;
import java.util.Calendar;

public class AppUtils {

  public static boolean isNetworkConnected(Context context) {
    ConnectivityManager cm =
        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkCapabilities capabilities = cm.getNetworkCapabilities(cm.getActiveNetwork());
    return capabilities != null
        && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));
  }

  public static boolean isInternetAvailable() {
    try {
      InetAddress ipAddr = InetAddress.getByName("google.com");
      return ipAddr.isReachable(1000); // Timeout 1 second
    } catch (Exception e) {
      return false;
    }
  }

  public static float dipToPixels(Context context, float dipValue) {
    DisplayMetrics metrics = context.getResources().getDisplayMetrics();
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
  }

  public static void showToast(Context context, String message) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
  }

  public static void addSystemWindowInsetToPadding(
      View view, boolean left, boolean top, boolean right, boolean bottom) {
    ViewCompat.setOnApplyWindowInsetsListener(
        view,
        new OnApplyWindowInsetsListener() {
          @Override
          public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat windowInsets) {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            int paddingLeft, paddingTop, paddingRight, paddingBottom;
            if (left) {
              paddingLeft = insets.left;
            } else {
              paddingLeft = 0;
            }
            if (top) {
              paddingTop = insets.top;
            } else {
              paddingTop = 0;
            }
            if (right) {
              paddingRight = insets.right;
            } else {
              paddingRight = 0;
            }
            if (bottom) {
              paddingBottom = insets.bottom;
            } else {
              paddingBottom = 0;
            }
            v.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
            return windowInsets;
          }
        });
  }

  public static void addSystemWindowInsetToMargin(
      View view, boolean left, boolean top, boolean right, boolean bottom) {
    ViewCompat.setOnApplyWindowInsetsListener(
        view,
        new OnApplyWindowInsetsListener() {
          @Override
          public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat windowInsets) {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            int marginLeft, marginTop, marginRight, marginBottom;
            if (left) {
              marginLeft = insets.left;
            } else {
              marginLeft = 0;
            }
            if (top) {
              marginTop = insets.top;
            } else {
              marginTop = 0;
            }
            if (right) {
              marginRight = insets.right;
            } else {
              marginRight = 0;
            }
            if (bottom) {
              marginBottom = insets.bottom;
            } else {
              marginBottom = 0;
            }
            if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
              ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
              p.setMargins(marginLeft, marginTop, marginRight, marginBottom);
              v.requestLayout();
            }
            return windowInsets;
          }
        });
  }

  public static int getNavigationBarHeight(Context context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
      Resources resources = context.getResources();
      int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
      if (resourceId > 0) {
        return resources.getDimensionPixelSize(resourceId);
      }
    }
    return 0;
  }

  public static boolean isDarkTheme(Context context) {
    int currentNightMode =
        context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
    if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
      return true;
    } else {
      return false;
    }
  }
}
