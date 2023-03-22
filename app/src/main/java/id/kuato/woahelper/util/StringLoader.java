package id.kuato.woahelper.util;

import android.content.Context;
import java.lang.String;

public class StringLoader {

  public static String getAppInternalPath(Context context) {
    return context.getFilesDir().getAbsolutePath();
  }
}
