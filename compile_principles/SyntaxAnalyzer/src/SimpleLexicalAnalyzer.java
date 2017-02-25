import java.util.ArrayList;
import java.util.List;

/**
 * Created by ss14 on 2016/11/2.
 */
public class SimpleLexicalAnalyzer {

    //i,+,*,(,),$
    public List<Token> LexAnalyse(String input){
        List<Token> result = new ArrayList<>();

        for(int i=0;i<input.length();i++){

            char temp=input.charAt(i);
            switch (temp){
                case 'i':  result.add(Token.ID); break;
                case '+':  result.add(Token.PLUS);break;
                case '*':  result.add(Token.MULTIPLY);break;
                case '(':  result.add(Token.LEFT_Par);break;
                case ')':  result.add(Token.RIGHT_Par);break;
                case '$':  result.add(Token.END);
            }
        }

        result.add(Token.END);
        return result;
    }
}
