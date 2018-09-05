package technology.nine.mobile_tracker.model;

public class SmsLogs {
    private  String phoneNumber;
    private  String messageBody;
    private  String time;
    private  String messageType;

    public SmsLogs(String phoneNumber, String messageBody, String time, String messageType) {
        this.phoneNumber = phoneNumber;
        this.messageBody = messageBody;
        this.time = time;
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

    public String getMessageType() {
        return messageType;
    }
}
