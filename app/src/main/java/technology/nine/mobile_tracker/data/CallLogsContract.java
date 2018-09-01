package technology.nine.mobile_tracker.data;

import android.provider.BaseColumns;

class CallLogsContract {
    private CallLogsContract() {
    }

    static class CallLogsEntry implements BaseColumns {
        static final String TABLE_NAME = "callLogs";
        static final String CALL_LOG_TABLE_NAME = "CallLog";
        static  final String NAME = "name";
        static final String NUMBER = "number";
        static final String CALL_TYPE = "callType";
        static final String DATE = "date";
        static final String TIME = "time";
        static final String DURATION = "duration";

    }
}

