package tr.xip.rireki.ext

import android.util.Log
import tr.xip.rireki.BuildConfig

inline fun log(lambda: () -> String) {
    if (BuildConfig.DEBUG) Log.d("Rireki", lambda())
}
