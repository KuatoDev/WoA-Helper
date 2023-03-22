package id.kuato.verncopyright;
/*
 * Copyright (C) 2013 Vern Kuato
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.View;

public class BlurKit {
  private static BlurKit instance;
  private final RenderScript rs;

  private BlurKit(Context context) {
    rs =RenderScript.create(context);
  }

  public static BlurKit getInstance(Context context) {
    if (instance == null) {
      instance = new BlurKit(context.getApplicationContext());
    }
    return instance;
  }

  public Bitmap blur(Bitmap bitmap, int radius) {
    Bitmap output =
        Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
    ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
    Allocation input = Allocation.createFromBitmap(rs, bitmap);
    Allocation outputAllocation = Allocation.createFromBitmap(rs, output);
    blurScript.setRadius(radius);
    blurScript.setInput(input);
    blurScript.forEach(outputAllocation);
    outputAllocation.copyTo(output);
    return output;
  }

  public void destroy() {
    rs.destroy();
  }

  public Bitmap blur(View src, int radius) {
    Bitmap bitmap = getBitmapForView(src, 1f);
    return blur(bitmap, radius);
  }

  public Bitmap fastBlur(View src, int radius, float downscaleFactor) {
    Bitmap bitmap = getBitmapForView(src, downscaleFactor);
    return blur(bitmap, radius);
  }

  private Bitmap getBitmapForView(View view, float downscaleFactor) {
    Bitmap bitmap =
        Bitmap.createBitmap(
            (int) (view.getWidth() * downscaleFactor),
            (int) (view.getHeight() * downscaleFactor),
            Bitmap.Config.ARGB_8888);

    Canvas canvas = new Canvas(bitmap);
    canvas.scale(downscaleFactor, downscaleFactor);
    view.draw(canvas);

    return bitmap;
  }

  public static BlurKit getInstance() {
    if (instance == null) {
      throw new RuntimeException("BlurKit not initialized!");
    }

    return instance;
  }
}
