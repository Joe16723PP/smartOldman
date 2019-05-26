package kku.en.coe.smartoldman;

public class DepQListitem {
    private String id,question;

    public String getDepId() {
        return id;
    }

    public String getDepQuestion() {
        return question;
    }

    public DepQListitem(String id, String question) {
        this.id = id;
        this.question = question;
    }
}
