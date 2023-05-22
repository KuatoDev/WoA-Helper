package id.kuato.woahelper.vayu;

/*
 * Copyright (C) 2020 Vern Kuato
 */
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.transition.Fade;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import id.kuato.woahelper.R;
import id.kuato.woahelper.databinding.FragmentMainBinding;
import id.kuato.woahelper.function.IntentAction;

public class SettingsFragment extends PreferenceFragmentCompat
    implements PreferenceManager.OnPreferenceTreeClickListener {
  private FragmentMainBinding x;

  @Override
  public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
    setPreferencesFromResource(R.xml.settings, rootKey);
    setEnterTransition(new Fade());
    setExitTransition(new Fade());
    x = FragmentMainBinding.inflate(getLayoutInflater());
    x.toolbarlayout.toolbar.setTitle(R.string.app_name);
    Drawable icon = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_cog, null);
    x.toolbarlayout.toolbar.setNavigationIcon(icon);
    getActivity().setContentView(x.getRoot());
    setDefaultValues();
    setAboutPreference();
    setGroupPreference();
    setChannelPreference();
    setInstagramPreference();
    setYoutubePreference();
    setCreditsPreference();
    setDonatePreference();
    showBlur(false);
  }

  private void setDefaultValues() {
    PreferenceManager.setDefaultValues(getActivity(), R.xml.settings, false);
  }

  private void setAboutPreference() {
    PackageManager manager = getActivity().getPackageManager();
    Preference aboutPref = findPreference(getString(R.string.key_about));
    aboutPref.setIcon(R.drawable.ic_android);

    try {
      PackageInfo info = manager.getPackageInfo(requireContext().getPackageName(), 0);
      aboutPref.setSummary("Package informations");
      aboutPref.setOnPreferenceClickListener(
          preference -> {
            showBlur(true);
            Drawable backgroundDrawable =
                ContextCompat.getDrawable(getActivity(), R.drawable.dialog_background);
            new MaterialAlertDialogBuilder(getActivity())
                .setTitle(R.string.key_about)
                .setBackground(backgroundDrawable)
                .setMessage(
                    getString(
                        R.string.summary_about,
                        info.packageName,
                        info.versionName,
                        info.getLongVersionCode()))
                .setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_cog, null))
                .setOnCancelListener(
                    dialog -> {
                      showBlur(false);
                      dialog.dismiss();
                    })
                .show();

            return true;
          });
    } catch (NameNotFoundException e) {
      e.printStackTrace();
    }
  }

  private void setGroupPreference() {
    Preference groupPref = findPreference(getString(R.string.key_group));
    groupPref.setIcon(R.drawable.ic_group);
    groupPref.setOnPreferenceClickListener(
        preference -> {
          dialog(
              R.drawable.ic_group,
              "Community",
              "Discuss about this app here with us",
              "https://t.me/+u4MX3zTsAnNhYWI1");
          return true;
        });
  }

  private void setChannelPreference() {
    Preference channelPref = findPreference(getString(R.string.key_channel));
    channelPref.setIcon(R.drawable.ic_telegram);
    channelPref.setOnPreferenceClickListener(
        preference -> {
          dialog(
              R.drawable.ic_telegram,
              "Update Channel",
              "Check out any updates here",
              "https://t.me/vernkuato");
          return true;
        });
  }

  private void setInstagramPreference() {
    Preference instagramPref = findPreference(getString(R.string.key_instagram));
    instagramPref.setIcon(R.drawable.ic_instagram);
    instagramPref.setOnPreferenceClickListener(
        preference -> {
          dialog(
              R.drawable.ic_instagram,
              "Instagram",
              "Follow my Instagram",
              "https://instagram.com/kuatodev");
          return true;
        });
  }

  private void setYoutubePreference() {
    Preference youtubePref = findPreference(getString(R.string.key_youtube));
    youtubePref.setIcon(R.drawable.ic_youtube);
    youtubePref.setOnPreferenceClickListener(
        preference -> {
          dialog(
              R.drawable.ic_youtube,
              "YouTube",
              "Subscribe my YouTube Channel",
              "https://youtube.com/kuatodev");
          return true;
        });
  }

  private void setCreditsPreference() {
    showBlur(true);
    Preference creditsPref = findPreference(getString(R.string.key_credits));
    creditsPref.setOnPreferenceClickListener(
        preference -> {
          showCredits();
          return true;
        });
  }

  private void setDonatePreference() {
    Preference donatePref = findPreference(getString(R.string.key_donate));
    donatePref.setTitle(getString(R.string.donate));
    donatePref.setIcon(R.drawable.ic_gift);
    donatePref.setSummary(getString(R.string.donate_summary));
    donatePref.setOnPreferenceClickListener(
        preference -> {
          donate();
          return true;
        });
  }

  public void showBlur(boolean show) {
    x.blur.setVisibility(show ? View.VISIBLE : View.GONE);
  }

  public void dialog(int icon, String title, String msg, String url) {
    showBlur(true);
    Drawable backgroundDrawable =
        ContextCompat.getDrawable(getActivity(), R.drawable.dialog_background);
    new MaterialAlertDialogBuilder(getActivity())
        .setTitle(title)
        .setBackground(backgroundDrawable)
        .setMessage(msg)
        .setIcon(ResourcesCompat.getDrawable(getResources(), icon, null))
        .setPositiveButton(
            R.string.yes,
            (dialog, which) -> {
              new IntentAction(getContext(), url);
              showBlur(false);
            })
        .setOnCancelListener(
            dialog -> {
              showBlur(false);
              dialog.dismiss();
            })
        .show();
  }

  public void showCredits() {
    showBlur(true);
    final Dialog dialog = new Dialog(getActivity());
    dialog.setContentView(R.layout.preference_credits);
    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    dialog.setCancelable(true);
    dialog.setOnCancelListener(
        new DialogInterface.OnCancelListener() {
          @Override
          public void onCancel(DialogInterface dialog) {
            showBlur(false);
            dialog.dismiss();
          }
        });
    dialog.show();
  }

  public void donate() {
    showBlur(true);
    Drawable backgroundDrawable =
        ContextCompat.getDrawable(getActivity(), R.drawable.dialog_background);
    new MaterialAlertDialogBuilder(getActivity())
        .setTitle(getString(R.string.donate))
        .setBackground(backgroundDrawable)
        .setMessage(getString(R.string.donate_thank))
        .setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_gift, null))
        .setPositiveButtonIcon(
            ResourcesCompat.getDrawable(getResources(), R.drawable.ic_paypal, null))
        .setPositiveButton(
            "PayPal",
            (dialog, which) -> {
              new IntentAction(getContext(), "https://www.paypal.me/vernkuato");
              showBlur(false);
            })
        .setNeutralButtonIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_rp, null))
        .setNeutralButton(
            "Saweria",
            (dialog, which) -> {
              new IntentAction(getContext(), "https://saweria.co/kuatodev");
              showBlur(false);
            })
        .setOnCancelListener(
            dialog -> {
              showBlur(false);
              dialog.dismiss();
            })
        .show();
  }
}
