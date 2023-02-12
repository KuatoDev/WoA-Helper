package id.kuato.woahelper.util;
/*
 *  * Copyright (C) 2020= Vern Kuato
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

import id.kuato.woahelper.util.MemoryUtils;

public class RAM {

	public long getTotalMemory(Context context) {
		ActivityManager actManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
		assert actManager != null;
		actManager.getMemoryInfo(memInfo);
		long totalMemory = memInfo.totalMem;
		return totalMemory;
	}

	public String getMemory(Context context) {
		String mem;
		mem = new MemoryUtils().bytesToHuman(getTotalMemory(context));
		return mem;
	}
}
