package id.kuato.woahelper.function;

import id.kuato.woahelper.util.ShellUtils;

public class Identification {
  private static final String findbackup =
      "su -c find /mnt/sdcard/woahelper/ -type f -name 'boot.img'";
  private static final String findntfs = "su -c find /system/ -type f -name 'libntfs-3g.so'";
  private static final String mountCommand =
      "su -c mkdir /mnt/Windows; su -c mount.ntfs /dev/block/by-name/win /mnt/Windows";
  private static final String umountCommand = "su -c umount /mnt/Windows";
  private static final String findCommand =
      "su -c find /mnt/Windows/Windows/ -type d -name 'System32'";
  boolean isInstalled = false;

  public boolean kernelHasBackup() {
    String checkbackup = ShellUtils.executeCommand(findbackup);
    return checkbackup.contains("boot");
  }

  public boolean hasSupportNTFS() {
    String checkntfs = ShellUtils.executeCommand(findntfs);
    return checkntfs.contains("ntfs");
  }

  public boolean isWindowsInstalled() {
    try {
      ShellUtils.executeCommand(mountCommand);
      String result = ShellUtils.executeCommand(findCommand);
      isInstalled = !result.isEmpty();
    } finally {
      ShellUtils.executeCommand(umountCommand);
    }
    return isInstalled;
  }
}
