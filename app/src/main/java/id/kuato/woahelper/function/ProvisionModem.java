package id.kuato.woahelper.function;

import id.kuato.woahelper.util.Command;

public class ProvisionModem {
  // mount windows
  private static final String mount_command =
      "mkdir /mnt/Windows; mount.ntfs /dev/block/by-name/win /mnt/Windows";
  // unmount windows
  private static final String umount_command = "umount /mnt/Windows";
  // Provisioning modem
  private static final String provision_command =
      "mv /sdcard/bootmodem_fs1 /mnt/Windows/Windows/System32/DriverStore/FileRepository/qcremotefs8150.inf_arm64_4271239f52792d6b/ && mv /sdcard/bootmodem_fs2 /mnt/Windows/Windows/System32/DriverStore/FileRepository/qcremotefs8150.inf_arm64_4271239f52792d6b/";

  public ProvisionModem() {
    boolean windowsIsMounted = new Parameters().windowsIsMounted();
    if (!windowsIsMounted) {
      Command.executeCommand(mount_command);
      Command.executeCommand(provision_command);
      Command.executeCommand(umount_command);
    } else {
      Command.executeCommand(provision_command);
    }
    Command.executeCommand(umount_command);
  }
}
