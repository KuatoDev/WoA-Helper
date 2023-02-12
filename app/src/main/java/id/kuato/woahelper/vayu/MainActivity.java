package id.kuato.woahelper.vayu;
/*
 *  * Copyright (C) 2022 Vern Kuato
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
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.BlendModeColorFilterCompat;
import androidx.core.graphics.BlendModeCompat;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
//import com.itsaky.androidide.logsender.LogSender;
import id.kuato.woahelper.BuildConfig;
import id.kuato.woahelper.R;
import id.kuato.woahelper.databinding.ActivityMainBinding;
import id.kuato.woahelper.preference.VernPreference;
import id.kuato.woahelper.util.CopyAssets;
import id.kuato.woahelper.util.Security;
import id.kuato.woahelper.util.ShellUtils;
import id.kuato.woahelper.util.Variant;

public class MainActivity extends AppCompatActivity {

  private ActivityMainBinding x;

  String ram;
  String panel;
  String uefi;
  String internalpath = "/data/data/id.kuato.woahelper/files/";
  Handler handler = new Handler();

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    LogSender.startLogging(this);
    super.onCreate(savedInstanceState);

    x = ActivityMainBinding.inflate(getLayoutInflater());

    setContentView(x.getRoot());
    Drawable iconToolbar =
        ResourcesCompat.getDrawable(getResources(), R.drawable.ic_launcher_foreground, null);
    setSupportActionBar(x.toolbarlayout.toolbar);
    x.toolbarlayout.toolbar.setTitle(getString(R.string.app_title));
    x.toolbarlayout.toolbar.setSubtitle(
        String.format(getString(R.string.app_subtitle), BuildConfig.VERSION_NAME));
    x.toolbarlayout.toolbar.setNavigationIcon(iconToolbar);

    x.mainbutton.tvDumpSensor.setText(getString(R.string.dump_sensors_title));
    x.mainbutton.tvDumpModem.setText(getString(R.string.dump_modem_title));
    x.tvAppCreator.setText(getString(R.string.devright));
    x.mainbutton.tvBackupBoot.setText(getString(R.string.backup_boot_title));
    x.mainbutton.tvBackupSubtitle.setText(getString(R.string.backup_boot_subtitle));
    x.mainbutton.tvSensorSubtitle.setText(getString(R.string.dump_sensors_subtitle));
    x.mainbutton.tvModemSubtitle.setText(getString(R.string.dump_modem_subtitle));
    x.mainbutton.tvQuickBoot.setText(getString(R.string.quickboot_title));
    x.mainbutton.tvBootSubtitle.setText(getString(R.string.quickboot_subtitle));

    x.dashboard.menubar.tvGuide.setSelected(true);
    x.dashboard.menubar.tvNtfs.setSelected(true);
    x.dashboard.menubar.tvGroup.setSelected(true);

    final Dialog dialog = new Dialog(MainActivity.this);
    dialog.setContentView(R.layout.dialog);
    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    MaterialButton yesButton = dialog.findViewById(R.id.yes);
    MaterialButton dismissButton = dialog.findViewById(R.id.dismiss);
    TextView messages = dialog.findViewById(R.id.messages);
    ImageView icons = dialog.findViewById(R.id.icon);

    Drawable uefi = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_uefi, null);
    Drawable boot = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_disk, null);
    Drawable sensors = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_sensor, null);
    Drawable modem = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_modem, null);

    Drawable icon =
        ResourcesCompat.getDrawable(getResources(), R.drawable.ic_launcher_foreground, null);
    icon.setColorFilter(
        BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
            getColor(R.color.colorPrimary), BlendModeCompat.SRC_IN));

    x.dashboard.menubar.cvGuide.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            String url = "https://github.com/Icesito68/Port-Windows-11-Poco-X3-pro";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
          }
        });

    x.dashboard.menubar.cvGroup.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            String url = "https://t.me/winonvayu";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
          }
        });

    x.dashboard.menubar.cvFlashNtfs.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            yesButton.setVisibility(View.VISIBLE);
            ShowBlur();
            icons.setImageDrawable(icon);
            messages.setText(getString(R.string.flash_ntfs));
            yesButton.setText(getString(R.string.yes));
            yesButton.setOnClickListener(
                new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                    yesButton.setVisibility(View.GONE);
                    dismissButton.setVisibility(View.GONE);
                    messages.setText(getString(R.string.please_wait));
                    handler.postDelayed(
                        new Runnable() {
                          @Override
                          public void run() {
                            try {
                              String run =
                                  ShellUtils.Executer(
                                      "su -c magisk --install-module "
                                          + internalpath
                                          + " ntfs-3g-magisk.zip");
                              messages.setText(run);
                              dismissButton.setVisibility(View.VISIBLE);
                            } catch (Exception error) {
                              error.printStackTrace();
                            }
                          }
                        },
                        1000);
                  }
                });
            dismissButton.setText(getString(R.string.dismiss));
            dismissButton.setOnClickListener(
                new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                    HideBlur();
                    dialog.dismiss();
                  }
                });
            dialog.setCancelable(false);
            dialog.show();
          }
        });

    x.mainbutton.cvQuickboot.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            yesButton.setVisibility(View.VISIBLE);
            ShowBlur();
            icons.setImageDrawable(icon);
            messages.setText(getString(R.string.quickboot_question));
            yesButton.setText(getString(R.string.yes));
            yesButton.setOnClickListener(
                new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                    yesButton.setVisibility(View.GONE);
                    dismissButton.setVisibility(View.GONE);
                    messages.setText(getString(R.string.please_wait));
                    handler.postDelayed(
                        new Runnable() {
                          @Override
                          public void run() {
                            try {
                              ShellUtils.Executer(
                                  " su -c dd if=/dev/block/by-name/modemst1 of=/sdcard/bootmodem_fs1 "
                                      + "&& su -c dd if=/dev/block/by-name/modemst2 of=/sdcard/bootmodem_fs2 "
                                      + "&& su -c rm -r /mnt/Windows; su -c mkdir /mnt/Windows "
                                      + "&& su -c mount.ntfs /dev/block/by-name/win /mnt/Windows "
                                      + "&& su -c mv /sdcard/bootmodem_fs1 /mnt/Windows/Windows/System32/DriverStore/FileRepository/qcremotefs8150.inf_arm64_4271239f52792d6b/ "
                                      + "&& su -c mv /sdcard/bootmodem_fs2 /mnt/Windows/Windows/System32/DriverStore/FileRepository/qcremotefs8150.inf_arm64_4271239f52792d6b/ "
                                      + "&& su -c umount /mnt/Windows && su -c dd if="
                                      + internalpath
                                      + uefi
                                      + " of=/dev/block/by-name/boot && su -c reboot");
                              messages.setText(getString(R.string.reboot_windows));
                              dismissButton.setVisibility(View.VISIBLE);
                            } catch (Exception error) {
                              error.printStackTrace();
                            }
                          }
                        },
                        1000);
                  }
                });
            dismissButton.setText(getString(R.string.dismiss));
            dismissButton.setOnClickListener(
                new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                    HideBlur();
                    dialog.dismiss();
                  }
                });
            dialog.setCancelable(false);
            dialog.show();
          }
        });

    x.mainbutton.cvDumpSensor.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            yesButton.setVisibility(View.VISIBLE);
            ShowBlur();
            icons.setImageDrawable(sensors);
            messages.setText(getString(R.string.dump_sensors_question));
            yesButton.setText(getString(R.string.yes));
            yesButton.setOnClickListener(
                new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                    yesButton.setVisibility(View.GONE);
                    dismissButton.setVisibility(View.GONE);
                    messages.setText(getString(R.string.please_wait));
                    handler.postDelayed(
                        new Runnable() {
                          @Override
                          public void run() {
                            try {
                              ShellUtils.Executer(
                                  "su -c rmdir /mnt/Windows; su -c mkdir /mnt/Windows "
                                      + "&& su -c mount.ntfs /dev/block/by-name/win /mnt/Windows "
                                      + "&& su -c mkdir /mnt/persist "
                                      + "&& su -c mount /dev/block/by-name/persist /mnt/persist "
                                      + "&& su -c mkdir -p /mnt/Windows/Windows/System32/drivers/DriverData/QUALCOMM/fastRPC/persist "
                                      + "&& su -c rm -rf /mnt/Windows/Windows/System32/drivers/DriverData/QUALCOMM/fastRPC/persist/sensors "
                                      + "&& su -c cp -r /mnt/persist/sensors /mnt/Windows/Windows/System32/drivers/DriverData/QUALCOMM/fastRPC/persist/ "
                                      + "&& su -c umount /mnt/persist "
                                      + "&& su -c umount /mnt/Windows"
                                      + "&& su -c rmdir /mnt/persist ");
                              messages.setText(getString(R.string.provision_sensors));
                              dismissButton.setVisibility(View.VISIBLE);
                            } catch (Exception error) {
                              error.printStackTrace();
                            }
                          }
                        },
                        2000);
                  }
                });

            dismissButton.setText(getString(R.string.dismiss));
            dismissButton.setOnClickListener(
                new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                    HideBlur();
                    dialog.dismiss();
                  }
                });
            dialog.setCancelable(false);
            dialog.show();
          }
        });

    x.mainbutton.cvDumpModem.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            yesButton.setVisibility(View.VISIBLE);
            ShowBlur();
            icons.setImageDrawable(modem);
            messages.setText(getString(R.string.dump_modem_question));
            yesButton.setText(getString(R.string.yes));
            yesButton.setOnClickListener(
                new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                    dismissButton.setVisibility(View.GONE);
                    yesButton.setVisibility(View.GONE);
                    messages.setText(getString(R.string.please_wait));
                    handler.postDelayed(
                        new Runnable() {
                          @Override
                          public void run() {
                            try {
                              ShellUtils.Executer(
                                  " su -c dd if=/dev/block/by-name/modemst1 of=/sdcard/bootmodem_fs1 "
                                      + "&& su -c dd if=/dev/block/by-name/modemst2 of=/sdcard/bootmodem_fs2 "
                                      + "&& su -c rm -r /mnt/Windows; su -c mkdir /mnt/Windows "
                                      + "&& su -c mount.ntfs /dev/block/by-name/win /mnt/Windows "
                                      + "&& su -c mv /sdcard/bootmodem_fs1 /mnt/Windows/Windows/System32/DriverStore/FileRepository/qcremotefs8150.inf_arm64_4271239f52792d6b/ "
                                      + "&& su -c mv /sdcard/bootmodem_fs2 /mnt/Windows/Windows/System32/DriverStore/FileRepository/qcremotefs8150.inf_arm64_4271239f52792d6b/ "
                                      + "&& su -c umount /mnt/Windows");
                              messages.setText(getString(R.string.provision_modem));
                              dismissButton.setVisibility(View.VISIBLE);
                            } catch (Exception error) {
                              error.printStackTrace();
                            }
                          }
                        },
                        2000);
                  }
                });
            dismissButton.setText(getString(R.string.dismiss));
            dismissButton.setOnClickListener(
                new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                    HideBlur();
                    dialog.dismiss();
                  }
                });
            dialog.setCancelable(false);
            dialog.show();
          }
        });

    x.mainbutton.cvFlashUefi.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            ShowBlur();
            icons.setImageDrawable(uefi);
            yesButton.setVisibility(View.VISIBLE);
            messages.setText(String.format(getString(R.string.flash_uefi_question), panel, ram));
            yesButton.setText(getString(R.string.yes));
            yesButton.setOnClickListener(
                new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                    dismissButton.setVisibility(View.GONE);
                    yesButton.setVisibility(View.GONE);
                    messages.setText(getString(R.string.please_wait));
                    handler.postDelayed(
                        new Runnable() {
                          @Override
                          public void run() {
                            try {
                              ShellUtils.Executer(
                                  "su -c dd if="
                                      + internalpath
                                      + uefi
                                      + " of=/dev/block/by-name/boot");
                              messages.setText(
                                  String.format(
                                      getString(R.string.flash_uefi_successfull), panel, ram));
                              dismissButton.setVisibility(View.VISIBLE);
                            } catch (Exception error) {
                              error.printStackTrace();
                            }
                          }
                        },
                        2000);
                  }
                });

            dismissButton.setText(getString(R.string.dismiss));
            dismissButton.setOnClickListener(
                new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                    HideBlur();
                    dialog.dismiss();
                  }
                });
            dialog.setCancelable(false);
            dialog.show();
          }
        });

    x.mainbutton.cvBackupBoot.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            ShowBlur();
            icons.setImageDrawable(boot);
            yesButton.setVisibility(View.VISIBLE);
            messages.setText(getString(R.string.backup_boot_question));
            yesButton.setText(getString(R.string.yes));
            yesButton.setOnClickListener(
                new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                    dismissButton.setVisibility(View.GONE);
                    yesButton.setVisibility(View.GONE);
                    messages.setText(getString(R.string.please_wait));
                    handler.postDelayed(
                        new Runnable() {
                          @Override
                          public void run() {
                            try {
                              String run =
                                  ShellUtils.Executer(
                                      "su -c dd if=/dev/block/by-name/boot of="
                                          + internalpath
                                          + "boot.img");
                              messages.setText(getString(R.string.backup_successfull));
                              dismissButton.setVisibility(View.VISIBLE);
                              checkdashboard();
                            } catch (Exception error) {
                              error.printStackTrace();
                            }
                          }
                        },
                        2000);
                  }
                });
            dismissButton.setText(getString(R.string.dismiss));
            dismissButton.setOnClickListener(
                new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                    HideBlur();
                    dialog.dismiss();
                  }
                });
            dialog.setCancelable(false);
            dialog.show();
          }
        });

    // HideBlur();
  }

  public void checkntfs() {
    String ntfspath = ShellUtils.Executer("su -c find /system/ -type f -name 'libntfs-3g.so'");
    if (ntfspath.isEmpty()) {
      x.dashboard.tvNtfsSupport.setText(
          String.format(getString(R.string.support_ntfs), getString(R.string.no)));
      x.mainbutton.cvDumpModem.setVisibility(View.GONE);
      x.mainbutton.cvDumpSensor.setVisibility(View.GONE);
      x.mainbutton.cvFlashUefi.setVisibility(View.GONE);
      x.dashboard.menubar.cvFlashNtfs.setVisibility(View.VISIBLE);
      new CopyAssets().copyAssets(getApplicationContext(), "ntfs-3g-magisk.zip");
    } else {
      x.dashboard.tvNtfsSupport.setText(
          String.format(getString(R.string.support_ntfs), getString(R.string.yes)));
      x.mainbutton.cvDumpModem.setVisibility(View.VISIBLE);
      x.mainbutton.cvDumpSensor.setVisibility(View.VISIBLE);
      x.mainbutton.cvFlashUefi.setVisibility(View.VISIBLE);
      x.dashboard.menubar.cvFlashNtfs.setVisibility(View.GONE);
    }
  }

  public void checkdashboard() {
    String uefiname =
        ShellUtils.Executer("su -c find " + internalpath + " -type f -name '" + uefi + "'");
    String findbackup =
        ShellUtils.Executer("su -c find " + internalpath + " -type f -name 'boot.img'");

    if (uefiname.isEmpty()) {
      uefiname = getString(R.string.not_found);
    }
    if (findbackup.isEmpty()) {
      findbackup = getString(R.string.not_found);
    }

    x.dashboard.tvUefiVersion.setText(
        String.format(getString(R.string.uefi_version), uefiname.replace(internalpath, "")));
    x.dashboard.tvBackupStatus.setText(
        String.format(getString(R.string.backup_status), getString(R.string.yes)));
    x.dashboard.tvRamvalue.setText(String.format(getString(R.string.ramvalue), ram));
    x.dashboard.tvPanel.setText(String.format(getString(R.string.paneltype), panel.toUpperCase()));
  }

  public void checkuefi() {
    String finduefi =
        ShellUtils.Executer("su -c find " + internalpath + " -type f -name '" + uefi + "'");
    if (finduefi.isEmpty()) {

      new CopyAssets().copyAssets(getApplicationContext(), uefi);
    } else {
      x.mainbutton.tvFlashUefi.setText(getString(R.string.flash_uefi_title));
      x.mainbutton.cvQuickboot.setVisibility(View.VISIBLE);
      x.mainbutton.cvFlashUefi.setEnabled(true);
      x.mainbutton.tvUefiSubtitle.setText(
          String.format(getString(R.string.flash_uefi_subtitle), panel, ram));
      x.mainbutton.tvUefiSubtitle.setVisibility(View.VISIBLE);
      checkdashboard();
    }
  }

  public void ShowBlur() {
    x.blur.setVisibility(View.VISIBLE);
  }

  public void HideBlur() {
    x.blur.setVisibility(View.GONE);
  }

  @Override
  public void onStart() {
    new Handler(Looper.getMainLooper())
        .post(
            new Runnable() {
              @Override
              public void run() {
                new Variant().getVariant(getApplicationContext());
                uefi = VernPreference.getVariant(getApplicationContext());
                panel = VernPreference.getPanel(getApplicationContext());
                ram = VernPreference.getRAM(getApplicationContext());
                checkntfs();
                checkuefi();
                new Security().Security(getApplicationContext());
                HideBlur();
              }
            });
    super.onStart();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    finishAffinity();
  }

  @Override
  public void onBackPressed() {
    ShowBlur();
    Drawable icon =
        ResourcesCompat.getDrawable(getResources(), R.drawable.ic_launcher_foreground, null);
    icon.setColorFilter(
        BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
            R.color.colorPrimary, BlendModeCompat.SRC_IN));
    new MaterialAlertDialogBuilder(
            MainActivity.this, R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog)
        .setMessage(getString(R.string.exit_app))
        .setNegativeButton(
            getString(R.string.cancel),
            new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int WhichButton) {
                dialog.cancel();
                dialog.dismiss();
                HideBlur();
              }
            })
        .setPositiveButton(
            getString(R.string.exit),
            new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
                HideBlur();
              }
            })
        .setIcon(icon)
        .setCancelable(false)
        .show();
  }

  private void unbindDrawables(View view) {
    if (view.getBackground() != null) {
      view.getBackground().setCallback(null);
    }
    if (view instanceof ViewGroup) {
      for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
        unbindDrawables(((ViewGroup) view).getChildAt(i));
      }
      ((ViewGroup) view).removeAllViews();
      view.setBackgroundResource(0);
    }
  }
}
