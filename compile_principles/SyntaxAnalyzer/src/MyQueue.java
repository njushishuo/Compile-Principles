import java.util.List;

/**
 * Created by ss14 on 2016/11/2.
 */
public class MyQueue {

    public int index;
    public List<Token> tokenList;

    public MyQueue(List<Token> tokenList){
        this.tokenList=tokenList;
        this.index=0;
    }

    public Token peek(){
        if(index>=tokenList.size()){
            return tokenList.get(tokenList.size()-1);
        }

        return tokenList.get(index);
    }

    public void remove(){
        if(index<tokenList.size()){
            index++;
        }
    }

    public void print(){
        for(Token token:tokenList){
            System.out.print(token+" ");
        }
    }
}

