package kku.en.coe.smartoldman;

public class ArrayAnswer {
    private static ArrayAnswer instance;
    private int array_answer[];

    public int[] getArray_answer() {
        return array_answer;
    }

    public void setArray_answer(int[] array_answer) {
        this.array_answer = array_answer;
    }

    public ArrayAnswer() {
        
    }

    public static synchronized ArrayAnswer getInstance(){
        if( instance == null ){
            instance = new ArrayAnswer();
        }
        return instance;
    }
}
