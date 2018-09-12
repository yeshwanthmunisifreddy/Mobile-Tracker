package technology.nine.mobile_tracker.model;

public class NotificationLogs {
    private String packageName, title, text, date;
    private byte[] image;

    public NotificationLogs(String packageName, String title, String text, String date, byte[] image) {
        this.packageName = packageName;
        this.title = title;
        this.text = text;
        this.date = date;
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

    public byte[] getImage() {
        return image;
    }
}
