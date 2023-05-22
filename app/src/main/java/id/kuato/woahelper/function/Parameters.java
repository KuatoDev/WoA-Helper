package id.kuato.woahelper.function;

import id.kuato.woahelper.util.Command;

public class Parameters {
  // find backup boot.img file
  private static final String find_boot_backup =
      "find /mnt/sdcard/woahelper/ -type f -name 'boot.img'";
  // find NTFS support
  private static final String find_ntfs = "find /system/ -type f -name 'libntfs-3g.so'";
  // mount windows
  private static final String mount_command =
      "mkdir /mnt/Windows; su -c mount.ntfs /dev/block/by-name/win /mnt/Windows";
  // unmount windows
  private static final String umount_command = "umount /mnt/Windows";
  // find system32
  private static final String find_system32 = "find /mnt/Windows/Windows/ -type d -name 'System32'";
  // boolean is windows installed
  boolean isInstalled = false;

  public boolean kernelHasBackup() {
    String checkbackup = Command.executeCommand(find_boot_backup);
    return checkbackup.contains("boot");
  }

  public boolean windowsIsMounted() {
    String mounted = Command.executeCommand(find_system32);
    return !mounted.isEmpty();
  }

  public boolean hasSupportNTFS() {
    String checkntfs = Command.executeCommand(find_ntfs);
    return checkntfs.contains("ntfs");
  }

  public boolean isWindowsInstalled() {
    try {
      if (windowsIsMounted()) {
        String result = Command.executeCommand(find_system32);
      } else {
        Command.executeCommand(mount_command);
        String result = Command.executeCommand(find_system32);
        //  isInstalled = !result.isEmpty();
        isInstalled = result.isEmpty();
      }
    } finally {
      Command.executeCommand(umount_command);
    }
    return isInstalled;
  }
}
