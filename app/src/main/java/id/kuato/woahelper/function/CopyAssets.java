package id.kuato.woahelper.function;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CopyAssets {
  public static void copyAssetFile(Context context, String fileName, String destinationPath)
      throws IOException {
    if (fileName == null || fileName.trim().isEmpty()) {
      throw new IllegalArgumentException("filename cannot be null or empty");
    }
    InputStream in = context.getAssets().open(fileName);
    File outFile = new File(destinationPath, fileName);
    OutputStream out = new FileOutputStream(outFile);
    copyFile(in, out);
    in.close();
    out.flush();
    out.close();
  }

  public static void copyFile(InputStream in, OutputStream out) throws IOException {
    byte[] buffer = new byte[512];
    int read;
    while ((read = in.read(buffer)) != -1) {
      out.write(buffer, 0, read);
    }
  }
}
