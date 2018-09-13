package technology.nine.mobile_tracker.model;

public class SmsLog {
    private String phoneNumber,messageBody,time,date, messageType;

    public SmsLog(String phoneNumber, String messageBody, String time, String date, String messageType) {
        this.phoneNumber = phoneNumber;
        this.messageBody = messageBody;
        this.time = time;
        this.date = date;
        this.messageType = messageType;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public String getMessageType() {
        return messageType;
    }
}
