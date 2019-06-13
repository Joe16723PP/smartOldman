package kku.en.coe.smartoldman;

public class GlobalAnswerQuestion {
    private static GlobalAnswerQuestion instance;
    private String array_answer[];

    public String[] getArray_answer() {
        return array_answer;
    }

    public void setArray_answer(String[] array_answer) {
        this.array_answer = array_answer;
    }

    public GlobalAnswerQuestion() {
    }

    public static synchronized GlobalAnswerQuestion getInstance(){
        if( instance == null ){
            instance = new GlobalAnswerQuestion();
        }
        return instance;
    }
}
