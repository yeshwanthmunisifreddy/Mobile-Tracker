package technology.nine.mobile_tracker.data;

import android.provider.BaseColumns;

class CallLogsContract {
    private CallLogsContract() {
    }

    //CallLogs constant fields
    static class CallLogsEntry implements BaseColumns {
        static final String SMS_TABLE_NAME = "SMSLogs";
        static final String CALL_LOG_TABLE_NAME = "CallLog";
        static final String NAME = "name";
        static final String NUMBER = "number";
        static final String CALL_TYPE = "callType";
        static final String DATE = "date";
        static final String TIME = "time";
        static final String DURATION = "duration";
        static final String BODY = "body";
        static final String SMS_TYPE = "SmsType";

    }
}

