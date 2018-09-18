package technology.nine.mobile_tracker.model;

public class Notifications {
    private String packageName;
    private String appName;
    private String title;
    private String bigTitle;
    private String text;
    private String bigText;
    private String summary;
    private String date;
    private byte[] smallIcon;
    private byte[] bigIcon;
    private byte[] extraPicture;

    public Notifications(String packageName, String appName, String title,
                         String bigTitle, String text, String bigText,
                         String summary, String date, byte[] smallIcon,
                         byte[] bigIcon, byte[] extraPicture) {
        this.packageName = packageName;
        this.appName = appName;
        this.title = title;
        this.bigTitle = bigTitle;
        this.text = text;
        this.bigText = bigText;
        this.summary = summary;
        this.date = date;
        this.smallIcon = smallIcon;
        this.bigIcon = bigIcon;
        this.extraPicture = extraPicture;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getAppName() {
        return appName;
    }

    public String getTitle() {
        return title;
    }

    public String getBigTitle() {
        return bigTitle;
    }

    public String getText() {
        return text;
    }

    public String getBigText() {
        return bigText;
    }

    public String getSummary() {
        return summary;
    }

    public String getDate() {
        return date;
    }

    public byte[] getSmallIcon() {
        return smallIcon;
    }

    public byte[] getBigIcon() {
        return bigIcon;
    }

    public byte[] getExtraPicture() {
        return extraPicture;
    }
}
