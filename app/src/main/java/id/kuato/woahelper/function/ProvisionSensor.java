package id.kuato.woahelper.function;

import id.kuato.woahelper.function.ProvisionSensor;
import id.kuato.woahelper.util.Command;

public class ProvisionSensor {
  // mount windows
  private static final String mount_command =
      "mkdir /mnt/Windows; mount.ntfs /dev/block/by-name/win /mnt/Windows";
  // umount windows
  private static final String umount_command = "umount /mnt/Windows";
  // Provision sensors
  private static final String provision_sensors =
      "mkdir /mnt/persist; "
          + "mount /dev/block/by-name/persist /mnt/persist; "
          + "mkdir -p /mnt/Windows/Windows/System32/drivers/DriverData/QUALCOMM/fastRPC/persist; "
          + "rm -rf /mnt/Windows/Windows/System32/drivers/DriverData/QUALCOMM/fastRPC/persist/sensors; "
          + "cp -r /mnt/persist/sensors /mnt/Windows/Windows/System32/drivers/DriverData/QUALCOMM/fastRPC/persist/; "
          + "umount /mnt/persist; "
          + "rm -r /mnt/persist ";

  public ProvisionSensor() {
    boolean windowsIsMounted = new Parameters().windowsIsMounted();
    if (!windowsIsMounted) {
      Command.executeCommand(provision_sensors);
    } else {
      Command.executeCommand(mount_command);
      Command.executeCommand(provision_sensors);
    }
    Command.executeCommand(umount_command);
  }
}
