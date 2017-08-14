package com.mrerror.proautocue_androidteleprompter.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by ahmed on 09/08/17.
 */

public class ScriptsContract {
    public static final String CONTENT_AUTHORITY = "com.mrerror.proautocue_androidteleprompter";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_SCRIPT= "script";

    public static final class ScriptEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_SCRIPT)
                .build();


        public static final String TABLE_NAME = "script";
        public static final String SCRIPT_PATH = "script_path";

        public static Uri buildScriptUriWithId(long id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(id))
                    .build();
        }

    }
}
