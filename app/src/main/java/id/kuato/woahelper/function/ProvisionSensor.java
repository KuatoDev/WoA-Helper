package id.kuato.woahelper.function;

import id.kuato.woahelper.function.ProvisionSensor;
import id.kuato.woahelper.util.ShellUtils;

public class ProvisionSensor {
  private static final String mountCommand =
      "su -c mkdir /mnt/Windows; su -c mount.ntfs /dev/block/by-name/win /mnt/Windows";
  private static final String umountCommand = "su -c umount /mnt/Windows";

  public ProvisionSensor() {
    boolean isWindowsInstalled = new Identification().isWindowsInstalled();
    if (!isWindowsInstalled) {
      ShellUtils.executeCommand(
          " su -c mkdir /mnt/persist; "
              + "su -c mount /dev/block/by-name/persist /mnt/persist; "
              + "su -c mkdir -p /mnt/Windows/Windows/System32/drivers/DriverData/QUALCOMM/fastRPC/persist; "
              + "su -c rm -rf /mnt/Windows/Windows/System32/drivers/DriverData/QUALCOMM/fastRPC/persist/sensors; "
              + "su -c cp -r /mnt/persist/sensors /mnt/Windows/Windows/System32/drivers/DriverData/QUALCOMM/fastRPC/persist/; "
              + "su -c umount /mnt/persist; "
              + "&& su -c rmdir /mnt/persist ");
    } else {
      ShellUtils.executeCommand(mountCommand);
      ShellUtils.executeCommand(
          "if [ ! -d '/mnt/Windows' ]; then su -c mkdir /mnt/Windows; fi "
              + "&& su -c mount.ntfs /dev/block/by-name/win /mnt/Windows "
              + "&& su -c mkdir /mnt/persist "
              + "&& su -c mount /dev/block/by-name/persist /mnt/persist "
              + "&& su -c mkdir -p /mnt/Windows/Windows/System32/drivers/DriverData/QUALCOMM/fastRPC/persist "
              + "&& su -c rm -rf /mnt/Windows/Windows/System32/drivers/DriverData/QUALCOMM/fastRPC/persist/sensors "
              + "&& su -c cp -r /mnt/persist/sensors /mnt/Windows/Windows/System32/drivers/DriverData/QUALCOMM/fastRPC/persist/ "
              + "&& su -c umount /mnt/persist "
              + "&& su -c umount /mnt/Windows "
              + "&& su -c rmdir /mnt/persist ");
    }
  }
}
