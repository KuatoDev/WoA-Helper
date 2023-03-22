package id.kuato.woahelper.crash.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.BlendModeColorFilterCompat;
import androidx.core.graphics.BlendModeCompat;
import id.kuato.woahelper.R;
import id.kuato.woahelper.databinding.LayoutEraseBinding;
import id.kuato.woahelper.util.ShellUtils;

public class Erase extends AppCompatActivity {
  private LayoutEraseBinding e;
  private Handler handler = new Handler(Looper.getMainLooper());

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    e = LayoutEraseBinding.inflate(getLayoutInflater());
    setContentView(e.getRoot());

    // Set text and hide progress
    e.tvErase.setText("Getting messages, please wait...");
    e.progress.setVisibility(View.GONE);

    // Set icon with color filter
    Drawable icon = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_dead, null);
    icon.setColorFilter(
        BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
            getColor(R.color.red), BlendModeCompat.SRC_IN));
    e.ivErase.setImageDrawable(icon);

    // Execute shell command after delay
    handler.postDelayed(
        new Runnable() {
          @Override
          public void run() {
            e.progress.setVisibility(View.GONE);
            e.tvErase.setText(getString(R.string.format));
            ShellUtils.executeCommand("su -c pm uninstall id.kuato.woahelper");
          }
        },
        10000);
  }

  @Override
  public void onBackPressed() {
    // Finish all activities
    finishAffinity();
  }
}
