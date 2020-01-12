package core.strings

import android.os.Environment
import java.io.File
import java.io.File.separator

/** Common shared preferences file */
const val SHARED_PREFERENCES_COMMON = "SharedPrefsCommon"

/** Directory for pictures *Remailir/Pictures* */
val APP_PICTURES = "${separator}Remailir${separator}Pictures"

const val PROFILE_PICTURE = "ProfileImage"

val appPicturesPath
  get() = File(Environment.getExternalStorageDirectory(), APP_PICTURES)

val profilePictureFile
  get() = File(appPicturesPath, PROFILE_PICTURE)
