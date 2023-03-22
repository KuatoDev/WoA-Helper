package id.kuato.woahelper.function;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import androidx.transition.TransitionManager;

public class AnimateTransition {
  public static void animate(View view) {
    TransitionManager.beginDelayedTransition((ViewGroup) view.getParent());
  }
  public static void blink(View v) {
    Animation anim = new AlphaAnimation(0.0f, 1.0f);
    anim.setDuration(500);
    anim.setStartOffset(20);
    anim.setRepeatMode(Animation.REVERSE);
    anim.setRepeatCount(Animation.INFINITE);
    v.startAnimation(anim);
  }
}
