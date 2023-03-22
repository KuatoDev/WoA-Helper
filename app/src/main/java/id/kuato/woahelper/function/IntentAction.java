package id.kuato.woahelper.function;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class IntentAction {
  public static void intent(Context context,String url) {
    Intent i = new Intent(Intent.ACTION_VIEW);
    i.setData(Uri.parse(url));
    context.startActivity(i);
  }
}
