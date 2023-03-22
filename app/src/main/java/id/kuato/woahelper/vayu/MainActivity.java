package id.kuato.woahelper.vayu;
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
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.core.content.res.ResourcesCompat;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
//import com.itsaky.androidide.logsender.LogSender; 
import id.kuato.woahelper.BuildConfig;
import id.kuato.woahelper.R;
import id.kuato.woahelper.databinding.ActivityMainBinding;
import id.kuato.woahelper.function.IntentAction;
import id.kuato.woahelper.function.ProvisionModem;
import id.kuato.woahelper.function.ProvisionSensor;
import id.kuato.woahelper.preference.VernPreference;

import id.kuato.woahelper.util.ShellUtils;
import id.kuato.woahelper.vayu.util.Utils;
import java.io.File;

public class MainActivity extends AppCompatActivity {
  private ActivityMainBinding x;
  private Handler handler = new Handler(Looper.getMainLooper());
  private static String ram;
  private static String panel;
  private static String uefi;
  private static String provmodem;
  private static String provsensors;
  private static String dumpSensorsTitle;
  private static String dumpModemTitle;
  private static String devRight;
  private static String backupBootTitle;
  private static String backupBootSubtitle;
  private static String dumpSensorsSubtitle;
  private static String dumpModemSubtitle;
  private static String quickbootTitle;
  private static String quickbootSubtitle;
  private static String modulepath;
  private static String uefipath;
  private static final String backupBootCommand =
      "su -c dd if=/dev/block/by-name/boot of=/mnt/sdcard/woahelper/boot.img";
  private static final String uefiname = "v2.1.0";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    LogSender.startLogging(this);
    super.onCreate(savedInstanceState);
    x = ActivityMainBinding.inflate(getLayoutInflater());

    setContentView(x.getRoot());

    Utils.setDecorUI(this);
    Drawable icon =
        ResourcesCompat.getDrawable(getResources(), R.drawable.ic_launcher_foreground, null);
    int colorPrimary = getColor(R.color.colorPrimary);

    setSupportActionBar(x.toolbarlayout.toolbar);

    boolean isKernelBackup = getIntent().getBooleanExtra("boot_backup", false);
    boolean isNtfsSupport = getIntent().getBooleanExtra("support_ntfs", false);
    boolean isWindowsInstalled = getIntent().getBooleanExtra("windows_installed", false);

    if (isNtfsSupport) {
      x.mainbutton.cvFlashNtfs.setVisibility(View.GONE);
    } else {
      x.mainbutton.cvProvisionModem.setVisibility(View.GONE);
      x.mainbutton.cvProvisionSensor.setVisibility(View.GONE);
    }

    x.dashboard.tvBackupStatus.setText(
        !isKernelBackup
            ? String.format(getString(R.string.backup_status), getString(R.string.no))
            : String.format(getString(R.string.backup_status), getString(R.string.yes)));

    x.dashboard.tvNtfsSupport.setText(
        !isNtfsSupport
            ? String.format(getString(R.string.support_ntfs), getString(R.string.no))
            : String.format(getString(R.string.support_ntfs), getString(R.string.yes)));

    x.dashboard.tvWindowsInstalled.setText(
        !isWindowsInstalled
            ? String.format(getString(R.string.windows_installed), getString(R.string.no))
            : String.format(getString(R.string.windows_installed), getString(R.string.yes)));
    if (!isWindowsInstalled) {
      x.mainbutton.cvFlashUefi.setVisibility(View.GONE);
      x.mainbutton.cvProvisionModem.setVisibility(View.GONE);
      x.mainbutton.cvProvisionSensor.setVisibility(View.GONE);
      x.mainbutton.cvQuickboot.setVisibility(View.GONE);
    }
    x.toolbarlayout.toolbar.setTitle("WOA Helper");
    x.toolbarlayout.toolbar.setSubtitle(
        String.format(getString(R.string.app_subtitle), BuildConfig.VERSION_NAME));
    x.toolbarlayout.toolbar.setNavigationIcon(icon);
    x.toolbarlayout.toolbar.inflateMenu(R.menu.menu);

    x.dashboard.menubar.cvGuide.setOnClickListener(
        view -> {
          IntentAction.intent(this, "https://github.com/Icesito68/Port-Windows-11-Poco-X3-pro");
        });

    x.dashboard.menubar.cvGroup.setOnClickListener(
        view -> {
          IntentAction.intent(this, "https://t.me/winonvayu");
        });

    x.mainbutton.cvFlashNtfs.setOnClickListener(
        view -> {
          showDialog(
              getString(R.string.flash_ntfs),
              "Success",
              "su -c magisk --install-module " + modulepath + "/ntfs.zip");
        });

    x.mainbutton.cvQuickboot.setOnClickListener(
        view -> {
          showDialog(
              getString(R.string.quickboot_question),
              getString(R.string.reboot_windows),
              provmodem
                  + " && su -c dd if="
                  + uefipath
                  + "/"
                  + uefi
                  + " of=/dev/block/by-name/boot && su -c reboot");
        });

    x.mainbutton.cvProvisionSensor.setOnClickListener(
        view -> {
          dialogSensors();
        });

    x.mainbutton.cvProvisionModem.setOnClickListener(
        view -> {
          dialogSensors();
        });

    x.mainbutton.cvFlashUefi.setOnClickListener(
        view -> {
          showDialog(
              String.format(getString(R.string.flash_uefi_question), panel, ram),
              String.format(getString(R.string.flash_uefi_successfull), panel, ram),
              "su -c dd if=" + uefipath + "/" + uefi + " of=/dev/block/by-name/boot");
        });

    x.mainbutton.cvBackupBoot.setOnClickListener(
        view -> {
          try {
            ShellUtils.executeCommand("su -c mkdir /mnt/sdcard/woahelper");
          } catch (Exception io) {
            io.printStackTrace();
          }
          showDialog(
              getString(R.string.backup_boot_question),
              getString(R.string.backup_successfull),
              backupBootCommand);
          x.dashboard.tvBackupStatus.setText(
              isKernelBackup
                  ? String.format(getString(R.string.backup_status), getString(R.string.no))
                  : String.format(getString(R.string.backup_status), getString(R.string.yes)));
        });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu, menu);
    if (menu instanceof MenuBuilder) ((MenuBuilder) menu).setOptionalIconsVisible(true);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.menu_youtube:
        Youtube();
        return true;
      case R.id.menu_telegram:
        Channel();
        return true;
      case R.id.menu_paypal:
        Paypal();
        return true;
      case R.id.menu_instagram:
        Instagram();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  public void Paypal() {
    IntentAction.intent(this, "https://www.paypal.me/vernkutato");
  }

  public void Instagram() {
    IntentAction.intent(this, "https://www.instagram.com/kutodev/");
  }

  public void Youtube() {
    IntentAction.intent(this, "https://www.youtube.com/cuatzstress");
  }

  public void Channel() {
    IntentAction.intent(this, "https/t.me/vernkuato/");
  }

  public void showBlur(boolean show) {
    x.blur.setVisibility(show ? View.VISIBLE : View.GONE);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    finishAffinity();
  }

  @Override
  public void onStart() {
    super.onStart();
    handler.post(
        new Runnable() {
          @Override
          public void run() {
            setupUI();
          }
        });
  }

  public boolean isUEFIfound() {
    File uefiFolder = new File(getFilesDir(), "UEFI/" + VernPreference.getUEFI(this));
    File modulesFolder = new File(getFilesDir(), "MODULES/ntfs.zip");
    VernPreference.setModulesPath(this, modulesFolder.getAbsolutePath());
    VernPreference.setUEFIPath(this, uefiFolder.getAbsolutePath());
    if (!uefiFolder.exists() || !modulesFolder.exists()) {
      return false;
    } else {
      return true;
    }
  }

  public void setupUI() {
    showBlur(false);
    if (!isUEFIfound()) {
      x.mainbutton.cvFlashUefi.setVisibility(View.GONE);
    }

    String ram = VernPreference.getRAM(this);
    String panel = VernPreference.getPanel(this);
    String uefi = VernPreference.getUEFI(this);
    String dumpSensorsTitle = getString(R.string.dump_sensors_title);
    String dumpModemTitle = getString(R.string.dump_modem_title);
    String devRight = getString(R.string.devright);
    String backupBootTitle = getString(R.string.backup_boot_title);
    String backupBootSubtitle = getString(R.string.backup_boot_subtitle);
    String dumpSensorsSubtitle = getString(R.string.dump_sensors_subtitle);
    String dumpModemSubtitle = getString(R.string.dump_modem_subtitle);
    String quickbootTitle = getString(R.string.quickboot_title);
    String quickbootSubtitle = getString(R.string.quickboot_subtitle);
    String modulepath = VernPreference.getModulesPath(this);
    String uefipath = VernPreference.getUEFIPath(this);

    x.mainbutton.tvDumpSensor.setText(dumpSensorsTitle);
    x.mainbutton.tvDumpModem.setText(dumpModemTitle);
    x.tvAppCreator.setText(devRight);
    x.mainbutton.tvBackupBoot.setText(backupBootTitle);
    x.mainbutton.tvBackupSubtitle.setText(backupBootSubtitle);
    x.mainbutton.tvSensorSubtitle.setText(dumpSensorsSubtitle);
    x.mainbutton.tvModemSubtitle.setText(dumpModemSubtitle);
    x.mainbutton.tvQuickBoot.setText(quickbootTitle);
    x.mainbutton.tvBootSubtitle.setText(quickbootSubtitle);
    x.dashboard.tvDevice.setText(
        String.format(getString(R.string.device), android.os.Build.DEVICE).toUpperCase());
    x.mainbutton.tvFlashUefi.setText(getString(R.string.flash_uefi_title));
    x.mainbutton.tvUefiSubtitle.setText(
        String.format(getString(R.string.flash_uefi_subtitle), panel, ram));

    x.dashboard.tvUefiVersion.setText(String.format(getString(R.string.uefi_version), uefiname));
    x.dashboard.tvRamvalue.setText(String.format(getString(R.string.ramvalue), ram));
    x.dashboard.tvPanel.setText(String.format(getString(R.string.paneltype), panel.toUpperCase()));
  }

  @Override
  public void onBackPressed() {
    showBlur(true);
    new MaterialAlertDialogBuilder(this)
        .setTitle(R.string.exit_app)
        .setPositiveButton(R.string.exit, (dialog, which) -> finish())
        .setNegativeButton(R.string.cancel, (dialog, which) -> showBlur(false))
        .setCancelable(false)
        .show();
  }

  public void showDialog(String message1, String message2, String command) {
    final Dialog dialog = new Dialog(MainActivity.this);
    dialog.setContentView(R.layout.dialog);
    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    MaterialButton yesButton = dialog.findViewById(R.id.yes);
    MaterialButton dismissButton = dialog.findViewById(R.id.dismiss);
    TextView messageView = dialog.findViewById(R.id.messages);

    yesButton.setVisibility(View.VISIBLE);
    showBlur(true);
    messageView.setText(message1);
    yesButton.setText(R.string.yes);

    yesButton.setOnClickListener(
        v -> {
          yesButton.setVisibility(View.GONE);
          dismissButton.setVisibility(View.GONE);
          messageView.setText(R.string.please_wait);
          handler.postDelayed(
              () -> {
                try {
                  String result = ShellUtils.executeCommand(command);
                  messageView.setText(result + "\n" + message2);
                  dismissButton.setVisibility(View.VISIBLE);
                } catch (Exception error) {
                  error.printStackTrace();
                }
              },
              1000);
        });

    dismissButton.setText(R.string.dismiss);
    dismissButton.setOnClickListener(
        v -> {
          showBlur(false);
          dialog.dismiss();
        });

    dialog.setCancelable(false);
    dialog.show();
  }

  public void dialogModem() {
    showBlur(true);
    new MaterialAlertDialogBuilder(MainActivity.this)
        .setTitle(getString(R.string.provision_modem))
        .setMessage(getString(R.string.dump_modem_question))
        .setPositiveButton(
            R.string.yes,
            (dialog, which) -> {
              new ProvisionModem();
            })
        .setNegativeButton(
            R.string.cancel,
            (dialog, which) -> {
              dialog.dismiss();
            })
        .setCancelable(false)
        .show();
  }

  public void dialogSensors() {
    showBlur(true);
    new MaterialAlertDialogBuilder(MainActivity.this)
        .setTitle(getString(R.string.provision_sensors))
        .setMessage(getString(R.string.dump_sensors_question))
        .setPositiveButton(
            R.string.yes,
            (dialog, which) -> {
              new ProvisionSensor();
            })
        .setNegativeButton(
            R.string.cancel,
            (dialog, which) -> {
              dialog.dismiss();
            })
        .setCancelable(false)
        .show();
  }
  /*
  public void showRootDialog() {
    showBlur(false);
    new MaterialAlertDialogBuilder(this)
        .setTitle(R.string.error_title)
        .setMessage(R.string.need_root_message)
        .setPositiveButton(R.string.exit, (dialog, which) -> finish())
        .setCancelable(false)
        .show();
  }*/
}
