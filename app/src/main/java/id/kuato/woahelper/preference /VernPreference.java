package id.kuato.woahelper.preference;

/*
 *  * Copyright (C) 2020 Vern Kuato
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or valued to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  */
import android.content.Context;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;

public class VernPreference {

  static final String KEY_UEFI = "UEFI Name";
  static final String KEY_PANEL = "Touch Panel";
  static final String KEY_RAM = "Memory";
  static final String KEY_MODULES_PATH = "Modules Path";
  static final String KEY_UEFI_PATH = "UEFI path";
  static final String KEY_AGREEMENT = "Agreement";

  private static SharedPreferences getSharedPreference(Context context) {
    return PreferenceManager.getDefaultSharedPreferences(context);
  }

  public static void setAgreement(Context context, boolean value) {
    SharedPreferences.Editor editor = getSharedPreference(context).edit();
    editor.putBoolean(KEY_AGREEMENT, value);
    editor.apply();
  }

  public static boolean getAgreement(Context context) {
    return getSharedPreference(context).getBoolean(KEY_AGREEMENT, false);
  }

  public static void setUEFI(Context context, String value) {
    SharedPreferences.Editor editor = getSharedPreference(context).edit();
    editor.putString(KEY_UEFI, value);
    editor.apply();
  }

  public static String getUEFI(Context context) {
    return getSharedPreference(context).getString(KEY_UEFI, "");
  }

  public static void setPanel(Context context, String value) {
    SharedPreferences.Editor editor = getSharedPreference(context).edit();
    editor.putString(KEY_PANEL, value);
    editor.apply();
  }

  public static String getPanel(Context context) {
    return getSharedPreference(context).getString(KEY_PANEL, "");
  }

  public static void setRAM(Context context, String value) {
    SharedPreferences.Editor editor = getSharedPreference(context).edit();
    editor.putString(KEY_RAM, value);
    editor.apply();
  }

  public static String getRAM(Context context) {
    return getSharedPreference(context).getString(KEY_RAM, "");
  }

  public static void setUEFIPath(Context context, String value) {
    SharedPreferences.Editor editor = getSharedPreference(context).edit();
    editor.putString(KEY_UEFI_PATH, value);
    editor.apply();
  }

  public static String getUEFIPath(Context context) {
    return getSharedPreference(context).getString(KEY_UEFI_PATH, "");
  }

  public static void setModulesPath(Context context, String value) {
    SharedPreferences.Editor editor = getSharedPreference(context).edit();
    editor.putString(KEY_MODULES_PATH, value);
    editor.apply();
  }

  public static String getModulesPath(Context context) {
    return getSharedPreference(context).getString(KEY_MODULES_PATH, "");
  }
}
