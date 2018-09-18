package technology.nine.mobile_tracker.model;

public class NotificationLogs {
    private String packageName, title, text, date,appName;
    private byte[] image;

    public NotificationLogs(String packageName, String title, String text, String date,String appName, byte[] image) {
        this.packageName = packageName;
        this.title = title;
        this.text = text;
        this.date = date;
        this.appName = appName;
        this.image = image;

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

    public byte[] getImage() {
        return image;
    }
}
