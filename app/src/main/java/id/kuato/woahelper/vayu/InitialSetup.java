package id.kuato.woahelper.vayu;
/*
 *  * Copyright (C) 2020 Vern Kuato
 *  */
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.topjohnwu.superuser.Shell;
import com.topjohnwu.superuser.Shell;
import id.kuato.woahelper.function.Parameters;
import id.kuato.woahelper.R;
import id.kuato.woahelper.databinding.LayoutInitialSetupBinding;
import id.kuato.woahelper.function.AnimateTransition;
import id.kuato.woahelper.function.CopyAssets;
import id.kuato.woahelper.function.Formatter;
import id.kuato.woahelper.function.RAM;
import id.kuato.woahelper.preference.VernPreference;
import id.kuato.woahelper.util.Command;
import id.kuato.woahelper.BuildConfig;
import java.io.File;
import java.io.IOException;

public class InitialSetup extends AppCompatActivity {
  private LayoutInitialSetupBinding v;
  private static final int REQUEST_STORAGE_PERMISSION = 1001;
  private static final String UEFI_FOLDER_NAME = "UEFI";
  private static final String NTFS_ZIP_FILE_NAME = "ntfs.zip";
  private static final String MODULES_FOLDER_NAME = "MODULES";
  private static int ramvalue = 0;
  private static String panel = "unknown";
  String run;
  Handler handler = new Handler(Looper.getMainLooper());

  static {
    Shell.enableVerboseLogging = BuildConfig.DEBUG;
    Shell.setDefaultBuilder(
        Shell.Builder.create().setFlags(Shell.FLAG_REDIRECT_STDERR).setTimeout(10));
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    v = LayoutInitialSetupBinding.inflate(getLayoutInflater());
    setContentView(v.getRoot());
  }

  @Override
  public void onStart() {
    AnimateTransition.blink(v.tvSetup);
    super.onStart();
    setupUI();
  }

  public void setupUI() {
    if (ContextCompat.checkSelfPermission(
            InitialSetup.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(
          InitialSetup.this,
          new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
          REQUEST_STORAGE_PERMISSION);
      return;
    }
    if (!VernPreference.getAgreement(InitialSetup.this)) {
      showAgreementDialog();
    } else {
      AnimateTransition.blink(v.tvSetup);
      v.tvSetup.setText("Extracting binaries successful");
      startActivity();
    }
  }

  public void extractBinaries() {
    v.tvSetup.setText("Identify Memory");
    try {
      int ram = Integer.parseInt(Formatter.extractNumberFromString(new RAM().getMemory(this)));
      ramvalue = (ram > 600) ? 8 : 6;
    } catch (NumberFormatException e) {
      Log.e("InitialSetup", "Failed to parse RAM value", e);
    }
    v.tvSetup.setText("Identify Touchpanel");
    try {
      run = Command.executeCommand("cat /proc/cmdline");
    } catch (Exception e) {
      Log.e("InitialSetup", "Failed to identify panel and RAM", e);
    }
    if (run.contains("j20s_42")) {
      panel = "huaxing";
    } else if (run.contains("j20s_36")) {
      panel = "tianma";
    }
    VernPreference.setPanel(this, panel);
    VernPreference.setRAM(this, String.valueOf(ramvalue));
    String UEFI_FILE_NAME = panel + ramvalue + ".img";
    VernPreference.setUEFI(this, UEFI_FILE_NAME);
    File uefiFolder = new File(getFilesDir(), UEFI_FOLDER_NAME);
    File modulesFolder = new File(getFilesDir(), MODULES_FOLDER_NAME);
    VernPreference.setModulesPath(this, modulesFolder.getAbsolutePath());
    VernPreference.setUEFIPath(this, uefiFolder.getAbsolutePath());
    v.tvSetup.setText("Extracting binaries...");
    if (!uefiFolder.exists() && !uefiFolder.mkdir()) {
      Log.e("InitialSetup", "Failed to create UEFI folder");
      return;
    }

    if (!modulesFolder.exists() && !modulesFolder.mkdir()) {
      Log.e("InitialSetup", "Failed to create modules folder");
      return;
    }
    if (uefiFolder.exists() && modulesFolder.exists()) {
      try {
        if (UEFI_FILE_NAME != null && NTFS_ZIP_FILE_NAME != null) {
          CopyAssets.copyAssetFile(this, UEFI_FILE_NAME, uefiFolder.getAbsolutePath());
          CopyAssets.copyAssetFile(this, NTFS_ZIP_FILE_NAME, modulesFolder.getAbsolutePath());
          setupUI();
        } else {
          v.tvSetup.setText("Extracting binaries Failed");
          Log.e("CopyAssets", "One or both file names are null");
        }

      } catch (IOException e) {
        Log.e("InitialSetup", "Failed to copy binary files", e);
        // Display an error message to the user
        Toast.makeText(this, "Failed to extract binary files", Toast.LENGTH_SHORT).show();
      }
      return;
    }
  }

  public void startActivity() {
    Intent i = new Intent(this, MainActivity.class);
    i.putExtra("boot_backup", new Parameters().kernelHasBackup());
    i.putExtra("support_ntfs", new Parameters().hasSupportNTFS());
    i.putExtra("windows_installed", new Parameters().isWindowsInstalled());
    startActivity(i);
    finishAffinity();
  }

  @Override
  public void onRequestPermissionsResult(
      int requestCode, String[] permissions, int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == REQUEST_STORAGE_PERMISSION) {
      if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        extractBinaries();
      } else {
        v.tvSetup.setText("Storage permission is required");
      }
    }
  }

  public void showAgreementDialog() {
    new MaterialAlertDialogBuilder(InitialSetup.this)
        .setTitle(getString(R.string.agreement_title))
        .setMessage(getString(R.string.agreement, getString(R.string.agreement)))
        .setNegativeButton(R.string.not_agree, (dialog, which) -> finishAffinity())
        .setPositiveButton(
            R.string.agree,
            (dialog, which) -> {
              VernPreference.setAgreement(InitialSetup.this, true);
              setupUI();
              dialog.dismiss();
            })
        .setCancelable(false)
        .show();
  }
}
