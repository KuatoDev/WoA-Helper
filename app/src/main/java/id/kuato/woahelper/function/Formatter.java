package id.kuato.woahelper.function;

/*
 *  * Copyright (C) 2020 Vern Kuato
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  */

import android.app.ActivityManager;
import android.content.Context;

public class Formatter {
  private static String ERROR;

  public static String extractNumberFromString(String source) {
    StringBuilder result = new StringBuilder(100);
    for (char ch : source.toCharArray()) {
      if (ch >= '0' && ch <= '9') {
        result.append(ch);
      }
    }
    return result.toString();
  }

  public String floatForm(double d) {
    return String.format(java.util.Locale.US, "%.2f", d);
  }

  public String bytesToHuman(long size) {
    long Kb = 1024;
    long Mb = Kb * 1024;
    long Gb = Mb * 1024;
    long Tb = Gb * 1024;
    long Pb = Tb * 1024;
    long Eb = Pb * 1024;

    if (size < Kb) return floatForm(size);
    if (size >= Kb && size < Mb) return floatForm((double) size / Kb);
    if (size >= Mb && size < Gb) return floatForm((double) size / Mb);
    if (size >= Gb && size < Tb) return floatForm((double) size / Gb);
    if (size >= Tb && size < Pb) return floatForm((double) size / Tb);
    if (size >= Pb && size < Eb) return floatForm((double) size / Pb);
    if (size >= Eb) return floatForm((double) size / Eb);

    return "0";
  }
}
