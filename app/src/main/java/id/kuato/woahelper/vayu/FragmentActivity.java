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

package id.kuato.woahelper.vayu;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import id.kuato.woahelper.R;
import id.kuato.woahelper.databinding.FragmentMainBinding;

public class FragmentActivity extends AppCompatActivity {
  private FragmentMainBinding x;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    x = FragmentMainBinding.inflate(getLayoutInflater());
    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
    setContentView(x.getRoot());
    Window window = getWindow();
    window.setNavigationBarColor(ContextCompat.getColor(this, R.color.colorSurfaceVariant));
    setSupportActionBar(x.toolbarlayout.toolbar);
  }

  @Override
  public void onStart() {
    super.onStart();
    SettingsFragment settingsFragment = new SettingsFragment();
    getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.frame, settingsFragment)
        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
        .commit();
    showBlur(false);
  }

  public void showBlur(boolean show) {
    x.blur.setVisibility(show ? View.VISIBLE : View.GONE);
  }

  @Override
  public void onPause() {
    super.onPause();
  }

  @Override
  public void onResume() {
    super.onResume();
    showBlur(false);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    finish();
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    getSupportFragmentManager().popBackStack();
    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
  }
}
