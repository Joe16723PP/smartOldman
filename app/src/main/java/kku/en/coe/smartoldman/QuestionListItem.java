package kku.en.coe.smartoldman;

public class QuestionListItem {
    private String question , question_number;

    public QuestionListItem(String question, String question_number) {
        this.question = question;
        this.question_number = question_number;
    }

    public String getQuestion() {
        return question;
    }

    public String getQuestion_number() {
        return question_number;
    }
}
