package technology.nine.mobile_tracker.model;

public class NotificationLogs {
    private String packageName, title, text, date,appName;


    public NotificationLogs(String packageName, String title, String text, String date,String appName) {
        this.packageName = packageName;
        this.title = title;
        this.text = text;
        this.date = date;
        this.appName = appName;


    }

    public String getPackageName() {
        return packageName;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getDate() {
        return date;
    }

    public String getAppName() {
        return appName;
    }

}
