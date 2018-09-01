package technology.nine.mobile_tracker.model;

public class CallLogs {
    private  String name;
    private String phoneNumber;
    private String calltype;
    private String callDate;
    private String callTime;
    private String callDuration;

    public CallLogs(String name, String phoneNumber, String calltype, String callDate, String callTime, String callDuration) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.calltype = calltype;
        this.callDate = callDate;
        this.callTime = callTime;
        this.callDuration = callDuration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCalltype() {
        return calltype;
    }

    public void setCalltype(String calltype) {
        this.calltype = calltype;
    }

    public String getCallDate() {
        return callDate;
    }

    public void setCallDate(String callDate) {
        this.callDate = callDate;
    }

    public String getCallTime() {
        return callTime;
    }

    public void setCallTime(String callTime) {
        this.callTime = callTime;
    }

    public String getCallDuration() {
        return callDuration;
    }

    public void setCallDuration(String callDuration) {
        this.callDuration = callDuration;
    }
}
