package id.kuato.woahelper.vayu;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.BlendModeColorFilterCompat;
import androidx.core.graphics.BlendModeCompat;
import com.google.android.material.button.MaterialButton;
import id.kuato.woahelper.R;
import id.kuato.woahelper.databinding.ActivityMainBinding;
import id.kuato.woahelper.util.MemoryUtils;
import id.kuato.woahelper.util.RAM;
import id.kuato.woahelper.util.ShellUtils;

public class MainActivity extends AppCompatActivity {

  private ActivityMainBinding x;
  private Context context;
  int ram;
  int ramvalue;
  String panel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Inflate and get instance of binding
    x = ActivityMainBinding.inflate(getLayoutInflater());
    // set content view to binding's root
    setContentView(x.getRoot());
    Drawable iconToolbar =
        ResourcesCompat.getDrawable(getResources(), R.drawable.ic_launcher_foreground, null);
    setSupportActionBar(x.toolbarlayout.toolbar);
    x.toolbarlayout.toolbar.setTitle(getString(R.string.app_title));
    x.toolbarlayout.toolbar.setSubtitle(getString(R.string.app_subtitle));
    x.toolbarlayout.toolbar.setNavigationIcon(iconToolbar);
    checkdevice();
    checkuefi();
    x.tvDumpSensor.setText(getString(R.string.dump_sensors_title));
    x.tvDumpModem.setText(getString(R.string.dump_modem_title));
    x.tvAppCreator.setText("Vern Kuato @2022");
    x.tvBackupBoot.setText(getString(R.string.backup_boot_title));
    x.tvBackupSubtitle.setText(getString(R.string.backup_boot_subtitle));
    x.tvSensorSubtitle.setText(getString(R.string.dump_sensors_subtitle));
    x.tvModemSubtitle.setText(getString(R.string.dump_modem_subtitle));
    x.tvQuickBoot.setText(getString(R.string.quickboot_title));
    x.tvBootSubtitle.setText(getString(R.string.quickboot_subtitle));

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

    x.cvQuickboot.setOnClickListener(
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
                    new Handler()
                        .postDelayed(
                            new Runnable() {
                              @Override
                              public void run() {
                                try {
                                  String run =
                                      ShellUtils.Executer(
                                          " su -c dd if=/dev/block/by-name/modemst1 of=/sdcard/bootmodem_fs1 "
                                              + "&& su -c dd if=/dev/block/by-name/modemst2 of=/sdcard/bootmodem_fs2 "
                                              + "&& su -c rm -r /mnt/Windows; su -c mkdir /mnt/Windows "
                                              + "&& su -c mount.ntfs /dev/block/by-name/win /mnt/Windows "
                                              + "&& su -c mv -v /sdcard/bootmodem_fs1 /mnt/Windows/Windows/System32/DriverStore/FileRepository/qcremotefs8150.inf_arm64_4271239f52792d6b/ "
                                              + "&& su -c mv -v /sdcard/bootmodem_fs2 /mnt/Windows/Windows/System32/DriverStore/FileRepository/qcremotefs8150.inf_arm64_4271239f52792d6b/ "
                                              + "&& su -c umount /mnt/Windows && su -c dd if=/sdcard/vayu-uefi-v2.1.0-release/"
                                              + panel
                                              + "-"
                                              + ramvalue
                                              + "gb-v2.1.0.img of=/dev/block/by-name/boot && su -c reboot");
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

    x.cvDumpSensor.setVisibility(View.GONE);
    
    /*
        x.cvDumpSensor.setOnClickListener(
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
                        new Handler()
                            .postDelayed(
                                new Runnable() {
                                  @Override
                                  public void run() {
                                    try {
                                      String r =
                                          ShellUtils.Executer(
                                              "su -c mkdir /mnt/Windows "
                                                  + "&& su -c mount.ntfs /dev/block/by-name/win /mnt/Windows "
                                                  + "&& su -c mkdir /mnt/persist "
                                                  + "&& su -c mount /dev/block/by-name/persist "
                                                  + "&& su -c mv /mnt/persist/sensor/ /mnt/Windows/Windows/System32/Drivers/DriverData/QUALCOMM/fastRPC/persist/ "
                                                  + "&& su -c umount /mnt/persist "
                                                  + "&& su -c umount /mnt/Windows"
                                                  + "&& su -c rm -d /mnt/persist "
                                                  + "&& su -c rm -d /mnt/Windows");
                                      messages.setText("Provisioning Sensors finished...");
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
    */
    
    x.cvDumpModem.setOnClickListener(
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
                    new Handler()
                        .postDelayed(
                            new Runnable() {
                              @Override
                              public void run() {
                                try {
                                  String run =
                                      ShellUtils.Executer(
                                          " su -c dd if=/dev/block/by-name/modemst1 of=/sdcard/bootmodem_fs1 "
                                              + "&& su -c dd if=/dev/block/by-name/modemst2 of=/sdcard/bootmodem_fs2 "
                                              + "&& su -c rm -r /mnt/Windows; su -c mkdir /mnt/Windows "
                                              + "&& su -c mount.ntfs /dev/block/by-name/win /mnt/Windows "
                                              + "&& su -c mv /sdcard/bootmodem_fs1 /mnt/Windows/Windows/System32/DriverStore/FileRepository/qcremotefs8150.inf_arm64_4271239f52792d6b/ "
                                              + "&& su -c mv /sdcard/bootmodem_fs2 /mnt/Windows/Windows/System32/DriverStore/FileRepository/qcremotefs8150.inf_arm64_4271239f52792d6b/ "
                                              + "&& su -c umount /mnt/Windows");
                                  messages.setText(run);
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

    x.cvFlashUefi.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            ShowBlur();
            icons.setImageDrawable(uefi);
            yesButton.setVisibility(View.VISIBLE);
            messages.setText(
                String.format(getString(R.string.flash_uefi_question), panel, ramvalue));
            yesButton.setText(getString(R.string.yes));
            yesButton.setOnClickListener(
                new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                    dismissButton.setVisibility(View.GONE);
                    yesButton.setVisibility(View.GONE);
                    messages.setText(getString(R.string.please_wait));
                    new Handler()
                        .postDelayed(
                            new Runnable() {
                              @Override
                              public void run() {
                                try {
                                  String run =
                                      ShellUtils.Executer(
                                          "su -c dd if=/sdcard/vayu-uefi-v2.1.0-release/"
                                              + panel
                                              + "-"
                                              + ramvalue
                                              + "gb-v2.1.0.img of=/dev/block/by-name/boot");
                                  messages.setText(run);
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

    x.cvBackupBoot.setOnClickListener(
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
                    new Handler()
                        .postDelayed(
                            new Runnable() {
                              @Override
                              public void run() {
                                try {
                                  String run =
                                      ShellUtils.Executer(
                                          "su -c dd if=/dev/block/by-name/boot of=/sdcard/boot.img");
                                  messages.setText("Backup boot image successful...");
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

    HideBlur();
  }

  public void checkdevice() {
    try {
      String stram =
          MemoryUtils.extractNumberFromString(new RAM().getMemory(getApplicationContext()));
      ram = Integer.parseInt(stram);
      if (ram > 600) {
        ramvalue = 8;
      } else {
        ramvalue = 6;
      }
    } catch (NumberFormatException n) {
      System.out.println(n);
    }

    String run = ShellUtils.Executer("su -c cat /proc/cmdline");
    if (run.isEmpty()) {
    } else if (run.contains("j20s_42")) {
      panel = "huaxing";
    } else if (run.contains("j20s_36")) {
      panel = "tianma";
    } else {
      panel = "unknown";
    }
  }

  public void checkuefi() {
    String finduefi =
        ShellUtils.Executer(
            "su -c find /mnt/sdcard/vayu-uefi-v2.1.0-release/ -type f -name '*"
                + panel
                + "-"
                + ramvalue
                + "*'");
    if (finduefi.isEmpty()) {
      x.cvFlashUefi.setEnabled(false);
      x.tvFlashUefi.setText(getString(R.string.uefi_not_found));
      x.tvUefiSubtitle.setVisibility(View.VISIBLE);
      x.tvUefiSubtitle.setText(getString(R.string.uefi_not_found_subtitle));
    } else {
      x.tvFlashUefi.setText(getString(R.string.flash_uefi_title));
      x.cvQuickboot.setVisibility(View.VISIBLE);
      x.cvFlashUefi.setEnabled(true);
      x.tvUefiSubtitle.setText(
          String.format(getString(R.string.flash_uefi_subtitle), panel, ramvalue));
      x.tvUefiSubtitle.setVisibility(View.VISIBLE);
    }
  }

  public void ShowBlur() {
    x.blur.setVisibility(View.VISIBLE);
  }

  public void HideBlur() {
    x.blur.setVisibility(View.GONE);
  }
}
