/**
 * Created by ss14 on 2016/11/2.
 */
public class Action {
    private ActionType type;
    private int num;
    public Action(ActionType type , int num){
        this.type=type;
        this.num=num;
    }
    public ActionType getType(){
        return type;
    }

    public int getNum(){
        return num;
    }


}
