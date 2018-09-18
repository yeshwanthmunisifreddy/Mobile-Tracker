package technology.nine.mobile_tracker.data;

import android.provider.BaseColumns;

import java.util.Date;

class LogsContract {
    private LogsContract() {
    }

    //CallLogs constant fields
    static class LogsEntry implements BaseColumns {
        //DataBase Table names
        static final String NOTIFICATION_TABLE_NAME = "Notifications",
                SMS_TABLE_NAME = "SMSLogs",
                CALL_LOG_TABLE_NAME = "CallLog";

        //Table column names
        static final String NAME = "name", NUMBER = "number", CALL_TYPE = "callType", DATE = "date", TIME = "time",
                DURATION = "duration", BODY = "body", SMS_TYPE = "SmsType",
                TEXT = "text", PACKAGE_NAME = "packageName",   TITLE = "title", SMALL_ICON= "smallIcon",
                APP_NAME = "appName", BIG_TITLE = "bigTitle",BIG_TEXT ="bigText",
                SUMMARY="summary",BIG_ICON = "bigIcon",PICTURE = "picture";

    }
}

