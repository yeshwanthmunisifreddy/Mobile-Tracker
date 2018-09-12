package technology.nine.mobile_tracker.model;

public class CallLogs {
    private String name, phoneNumber,callType,callDate,callTime,callDuration;

    public CallLogs(String name, String phoneNumber, String callType, String callDate, String callTime, String callDuration) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.callType = callType;
        this.callDate = callDate;
        this.callTime = callTime;
        this.callDuration = callDuration;
    }


    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getCallType() {
        return callType;
    }

    public String getCallDate() {
        return callDate;
    }

    public String getCallTime() {
        return callTime;
    }

    public String getCallDuration() {
        return callDuration;
    }
}
