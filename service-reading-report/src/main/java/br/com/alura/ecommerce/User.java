package br.com.alura.ecommerce;

public class User {
    private final String uuid;

    public User(String uuid) {
        this.uuid = uuid;
    }

    public String getReportPath() {
        return "C:\\Users\\jonatas\\IdeaProjects\\ecommerce\\service-users\\target\\" + uuid + "-report.txt";
    }

    public String getUuid() {
        return uuid;
    }
}
