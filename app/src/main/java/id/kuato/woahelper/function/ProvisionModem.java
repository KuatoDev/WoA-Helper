package id.kuato.woahelper.function;

import id.kuato.woahelper.util.ShellUtils;

public class ProvisionModem {

  private static final String mountCommand =
      "su -c mkdir /mnt/Windows; su -c mount.ntfs /dev/block/by-name/win /mnt/Windows";
  private static final String umountCommand = "su -c umount /mnt/Windows";

  public ProvisionModem() {
    boolean isWindowsInstalled = new Identification().isWindowsInstalled();
    if (!isWindowsInstalled) {
      ShellUtils.executeCommand(
          "su -c mv /sdcard/bootmodem_fs1 /mnt/Windows/Windows/System32/DriverStore/FileRepository/qcremotefs8150.inf_arm64_4271239f52792d6b/;"
              + "su -c mv /sdcard/bootmodem_fs2 /mnt/Windows/Windows/System32/DriverStore/FileRepository/qcremotefs8150.inf_arm64_4271239f52792d6b/ ");
    } else {
      ShellUtils.executeCommand(mountCommand);
      ShellUtils.executeCommand(
          "if [ -d \"/mnt/Windows\" ]; then su -c umount /mnt/Windows; su -c rm -r /mnt/Windows; fi "
              + "&& su -c mkdir /mnt/Windows "
              + "&& su -c mount.ntfs /dev/block/by-name/win /mnt/Windows "
              + "&& su -c mv /sdcard/bootmodem_fs1 /mnt/Windows/Windows/System32/DriverStore/FileRepository/qcremotefs8150.inf_arm64_4271239f52792d6b/ "
              + "&& su -c mv /sdcard/bootmodem_fs2 /mnt/Windows/Windows/System32/DriverStore/FileRepository/qcremotefs8150.inf_arm64_4271239f52792d6b/ "
              + "&& su -c umount /mnt/Windows");
    }
  }
}
