package technology.nine.mobile_tracker.model;

public class UserData {

    private String number;
    private String callType;
    private String date;
    private String time;
    private String duration;

    public UserData(String number, String callType, String date, String time, String duration) {

        this.number = number;
        this.callType = callType;
        this.date = date;
        this.time = time;
        this.duration = duration;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

