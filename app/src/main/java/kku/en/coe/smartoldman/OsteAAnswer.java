package kku.en.coe.smartoldman;

public class OsteAAnswer {
    private static OsteAAnswer oste_inst;
    private int oste_answer[];

    public int[] getOste_answer() {
        return oste_answer;
    }

    public void setOste_answer(int[] oste_answer) {
        this.oste_answer = oste_answer;
    }

    public OsteAAnswer() {
    }

    public static synchronized OsteAAnswer getOsteInst(){
        if( oste_inst == null ){
            oste_inst = new OsteAAnswer();
        }
        return oste_inst;
    }
}
