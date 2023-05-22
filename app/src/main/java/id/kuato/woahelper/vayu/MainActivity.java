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
import com.google.android.material.snackbar.Snackbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.Button;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import id.kuato.woahelper.BuildConfig;
import id.kuato.woahelper.R;
import id.kuato.woahelper.databinding.ActivityMainBinding;
import id.kuato.woahelper.function.IntentAction;
import id.kuato.woahelper.function.ProvisionModem;
import id.kuato.woahelper.function.Parameters;
import id.kuato.woahelper.function.ProvisionSensor;
import id.kuato.woahelper.preference.VernPreference;
import id.kuato.woahelper.util.Command;
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
      "dd if=/dev/block/by-name/boot of=/mnt/sdcard/woahelper/boot.img";
  private static final String uefiname = "Prebuilt (v2.1.0)";
  public static String selecteduefi = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    x = ActivityMainBinding.inflate(getLayoutInflater());

    setContentView(x.getRoot());

    Window window = getWindow();
    window.setNavigationBarColor(ContextCompat.getColor(this, R.color.colorSurfaceVariant));

    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
    Drawable icon =
        ResourcesCompat.getDrawable(getResources(), R.drawable.ic_launcher_foreground, null);
    int colorPrimary = getColor(R.color.colorPrimary);

    setSupportActionBar(x.toolbarlayout.toolbar);

    boolean isKernelBackup = getIntent().getBooleanExtra("boot_backup", false);
    boolean isNtfsSupport = getIntent().getBooleanExtra("support_ntfs", false);
    boolean isWindowsInstalled = getIntent().getBooleanExtra("windows_installed", false);
    boolean isWindowsMounted = new Parameters().windowsIsMounted();
    if (isNtfsSupport) {
      x.mainbutton.cvFlashNtfs.setVisibility(View.GONE);
    } else {
      x.mainbutton.cvProvisionModem.setVisibility(View.GONE);
      x.mainbutton.cvProvisionSensor.setVisibility(View.GONE);
    }
    x.dashboard.tvWindowsMounted.setText(
        !isWindowsMounted
            ? String.format(getString(R.string.windows_mounted), getString(R.string.no))
            : String.format(getString(R.string.windows_mounted), getString(R.string.yes)));

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
    x.dashboard.tvUefiVersion.setText(String.format(getString(R.string.uefi_version), uefiname));
    /*  x.dashboard.menubar.cvGuide.setOnClickListener(
            view -> {
              new IntentAction(this, "https://github.com/Icesito68/Port-Windows-11-Poco-X3-pro");
            });

        x.dashboard.menubar.cvGroup.setOnClickListener(
            view -> {
              new IntentAction(this, "https://t.me/winonvayu");
            });
    */
    x.mainbutton.cvFlashNtfs.setOnClickListener(
        view -> {
          showDialog(
              getString(R.string.flash_ntfs),
              "Success",
              "magisk --install-module " + modulepath + "/ntfs.zip");
        });

    x.mainbutton.cvQuickboot.setOnClickListener(
        view -> {
          showDialog(
              getString(R.string.quickboot_question),
              getString(R.string.reboot_windows),
              provmodem
                  + " && dd if="
                  + uefipath
                  + "/"
                  + uefi
                  + " of=/dev/block/by-name/boot && reboot");
        });

    x.mainbutton.cvProvisionSensor.setOnClickListener(
        view -> {
          dialogSensors();
        });

    x.mainbutton.cvProvisionModem.setOnClickListener(
        view -> {
          dialogModem();
        });

    x.mainbutton.cvFlashUefi.setOnClickListener(
        view -> {
          showDialog(
              String.format(getString(R.string.flash_uefi_question), panel, ram),
              String.format(getString(R.string.flash_uefi_successfull), panel, ram),
              "dd if=" + uefipath + "/" + uefi + " of=/dev/block/by-name/boot");
        });

    x.mainbutton.cvBackupBoot.setOnClickListener(
        view -> {
          try {
            Command.executeCommand("mkdir /mnt/sdcard/woahelper");
          } catch (Exception io) {
            io.printStackTrace();
          }
          showDialog(
              getString(R.string.backup_boot_question),
              getString(R.string.backup_successfull),
              backupBootCommand);
          x.dashboard.tvBackupStatus.setText(
              String.format(getString(R.string.backup_status), getString(R.string.yes)));
        });

    x.mainbutton.cvSelectUefi.setOnClickListener(
        view -> {
          selectUefiDialog();
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
      case R.id.menu_settings:
        startSettingsActivity();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  public void startSettingsActivity() {
    Intent intent = new Intent(getApplicationContext(), FragmentActivity.class);
    ActivityOptionsCompat options =
        ActivityOptionsCompat.makeCustomAnimation(this, R.anim.fade_in, R.anim.fade_out);
    ActivityCompat.startActivity(this, intent, options.toBundle());
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

    x.dashboard.tvRamvalue.setText(String.format(getString(R.string.ramvalue), ram));
    x.dashboard.tvPanel.setText(String.format(getString(R.string.paneltype), panel.toUpperCase()));
  }

  @Override
  public void onBackPressed() {
    Drawable backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.dialog_background);
    showBlur(true);
    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
    builder.setMessage(R.string.exit_app);
    builder.setBackground(backgroundDrawable);
    AlertDialog dialog = builder.create();
    Window window = dialog.getWindow();
    if (window != null) {
      window.setWindowAnimations(R.style.DialogAnimation);
    }
    dialog.setOnShowListener(
        dialogInterface -> {
          Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
          positiveButton.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        });
    dialog.setButton(
        DialogInterface.BUTTON_POSITIVE,
        getString(R.string.yes),
        (dialogInterface, which) -> finish());
    dialog.setOnCancelListener(
        dialogInterface -> {
          showBlur(false);
          dialog.dismiss();
        });
    dialog.show();
  }

  public void showDialog(String message1, String message2, String command) {
    Drawable backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.dialog_background);
    showBlur(true);
    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
    builder.setMessage(message1);
    builder.setBackground(backgroundDrawable);
    AlertDialog dialog = builder.create();
    Window window = dialog.getWindow();
    if (window != null) {
      window.setWindowAnimations(R.style.DialogAnimation);
    }

    dialog.setOnShowListener(
        dialogInterface -> {
          Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
          positiveButton.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        });
    dialog.setButton(
        DialogInterface.BUTTON_POSITIVE,
        getString(R.string.yes),
        (dialogInterface, which) ->
            handler.postDelayed(
                () -> {
                  try {
                    Command.executeCommand(command);
                  } catch (Exception error) {
                    error.printStackTrace();
                  }
                  snackBar(message2);
                  showBlur(false);
                },
                500));
    dialog.setOnCancelListener(
        dialogInterface -> {
          showBlur(false);
          dialog.dismiss();
        });
    dialog.show();
  }

  public void dialogModem() {
    Drawable backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.dialog_background);
    showBlur(true);
    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
    builder.setMessage(getString(R.string.dump_modem_question));
    builder.setBackground(backgroundDrawable);
    AlertDialog dialog = builder.create();
    Window window = dialog.getWindow();
    if (window != null) {
      window.setWindowAnimations(R.style.DialogAnimation);
    }

    dialog.setOnShowListener(
        dialogInterface -> {
          Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
          positiveButton.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        });
    dialog.setButton(
        DialogInterface.BUTTON_POSITIVE,
        getString(R.string.yes),
        (dialogInterface, which) ->
            handler.postDelayed(
                () -> {
                  new ProvisionModem();
                  snackBar(getString(R.string.provision_modem));
                  showBlur(false);
                },
                1000));
    dialog.setOnCancelListener(
        dialogInterface -> {
          showBlur(false);
          dialog.dismiss();
        });
    dialog.show();
  }

  public void dialogSensors() {
    Drawable backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.dialog_background);
    showBlur(true);
    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
    builder.setMessage(getString(R.string.dump_sensors_question));

    builder.setBackground(backgroundDrawable);
    AlertDialog dialog = builder.create();
    Window window = dialog.getWindow();
    if (window != null) {
      window.setWindowAnimations(R.style.DialogAnimation);
    }

    dialog.setOnShowListener(
        dialogInterface -> {
          Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
          positiveButton.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        });
    dialog.setButton(
        DialogInterface.BUTTON_POSITIVE,
        getString(R.string.yes),
        (dialogInterface, which) ->
            handler.postDelayed(
                () -> {
                  new ProvisionSensor();
                  snackBar(getString(R.string.provision_sensors));
                  showBlur(false);
                },
                1000));
    dialog.setOnCancelListener(
        dialogInterface -> {
          showBlur(false);
          dialog.dismiss();
        });
    dialog.show();
  }

  public void selectUefiDialog() {
    Drawable backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.dialog_background);
    showBlur(true);
    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
    if (selecteduefi == null) {
      builder.setMessage("Select UEFI from storage");
    } else {
      builder.setMessage("Flash " + selecteduefi + " now?");
    }
    builder.setBackground(backgroundDrawable);
    AlertDialog dialog = builder.create();
    Window window = dialog.getWindow();
    if (window != null) {
      window.setWindowAnimations(R.style.DialogAnimation);
    }
    dialog.setOnShowListener(
        dialogInterface -> {
          Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
          Button selectbutton = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);
          positiveButton.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        });

    dialog.setButton(
        DialogInterface.BUTTON_POSITIVE,
        "Flash",
        (dialogInterface, which) ->
            handler.postDelayed(
                () -> {
                  if (selecteduefi == null) {
                    snackBar("Selected UEFI from storage first before flashing it");
                  } else {
                    try {
                      Command.executeCommand(
                          "dd if=" + selecteduefi + "/" + uefi + " of=/dev/block/by-name/boot");
                    } catch (Exception error) {
                      error.printStackTrace();
                    }
                    snackBar("Selected UEFI has been flashed into boot partitions");
                  }
                  showBlur(false);
                },
                1000));

    dialog.setButton(
        DialogInterface.BUTTON_NEUTRAL,
        "Select",
        (dialogInterface, which) -> {
          Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
          intent.setType("*/*");
          startActivityForResult(intent, PICK_IMAGE_REQUEST);
          x.dashboard.tvUefiVersion.setText(
              String.format(getString(R.string.uefi_version), selecteduefi));
          x.dashboard.tvUefiVersion.invalidate();
          showBlur(false);
        });

    dialog.setOnCancelListener(
        dialogInterface -> {
          showBlur(false);
          dialog.dismiss();
        });
    dialog.show();
  }

  private static final int PICK_IMAGE_REQUEST = 1;

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == PICK_IMAGE_REQUEST
        && resultCode == RESULT_OK
        && data != null
        && data.getData() != null) {
      Uri selectedFileUri = data.getData();
      String imagePath = selectedFileUri.getPath();
      selecteduefi = imagePath;
      x.mainbutton.tvSelectUefiSubtitle.setText("UEFI Selected path:\n" + selecteduefi);
      x.dashboard.tvUefiVersion.setText(
          String.format(getString(R.string.uefi_version), selecteduefi));
    }
  }

  public void snackBar(String msg) {
    Snackbar snackbar = Snackbar.make(x.mainview, msg, Snackbar.LENGTH_LONG);
    snackbar.show();
  }
}
