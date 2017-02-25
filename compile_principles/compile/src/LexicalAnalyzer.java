import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ss14 on 2016/10/22.
 */
public class LexicalAnalyzer {

    // keyword
    private static  String [] reservedWords = { "class","public","protected","private", "void",
            "int","short", "char", "float", "double","long","boolean","assert", "string",
            "if", "else", "do","for", "while", "try", "catch", "finally","switch",
            "break","return","continue","final", "case", "default","throw","true","false",
            "enum","super","abstract","interface","implements","extends",
            "const","static","import","package","instanceof","new","this",
            "synchronized","transient","volatile"
    };

    private static String [] delimiter={
        ";" , "," , "\'","\"" ,"[","]","(",")","{","}","?",":"
    };

    private static String [][] operatorTable={
            {"+","+","="}  , {"-","-","="} , {"*","="}, {"/","="}, {"%","="},
            {">","=",">"}, {"<","=","<"}, {"!","="}, {"=","="},
            {"|","|","="}, {"&","&","="},
            {"~"}, {"^","="}

    };


    private static ArrayList reservedList = new ArrayList(Arrays.asList(reservedWords));
    private static ArrayList delimiterList = new ArrayList(Arrays.asList(delimiter));

    private List<TokenWrapper> result = new LinkedList<>();
    private static String input="";


    public void analyse(String inputText){
        input =inputText;
        int len=inputText.length();
        int i=0;

        while(i<len){

            i=skipWhiteSpace(i);
            if(i>=len){
                break;
            }
            //如果是字母开头
            if(isLetter(input.charAt(i))){
                int temp=findKeyWord(i);

                //找到了一个keyword
                if(temp!=-1){
                    i=temp;
                //没有找到就作为id处理
                }else{
                    i=findID(i);
                }
            //数字开头
            }else if(isDigit(input.charAt(i))){
                i=findNumber(i);
            }else{

                if(input.charAt(i)=='/'){
                    int temp=skipComment(i);
                    if(temp!=-1){
                        i= temp;
                    }else{
                        i=findSymbol(i);
                    }
                }else{
                    i=findSymbol(i);
                }
            }
        }

        for(TokenWrapper wrapper : result){
            if(wrapper.token!=Token.Comment)
            System.out.println(wrapper.token+" "+wrapper.word);
        }
    }

    private int findKeyWord(int i){
        StringBuilder stringBuilder = new StringBuilder();
        while(i<input.length()){
            if(isLetter(input.charAt(i))){
                stringBuilder.append(input.charAt(i));
                i++;
            }else{
                break;
            }
        }
        String temp = stringBuilder.toString();

        if(reservedList.contains(temp)){

            TokenWrapper tokenWrapper = new TokenWrapper(Token.Keyword,temp);
            result.add(tokenWrapper);
            //此时i移动到找到的关键字的下一个字符处
            return i;
        }
        //没有匹配的字符
        return -1;
    }

    private int findID(int i){
        StringBuilder stringBuilder = new StringBuilder();
        while(i<input.length()){
            if(isLetterOrDigit(input.charAt(i))){
                stringBuilder.append(input.charAt(i));
                i++;
            }else{
                break;
            }

        }
        String temp = stringBuilder.toString();
        TokenWrapper tokenWrapper = new TokenWrapper(Token.Identifier,temp);
        result.add(tokenWrapper);
        //此时i移动到找到的关键字的下一个字符处
        return i;
    }

    private int findNumber(int i){

        int state=0;
        StringBuilder stringBuilder = new StringBuilder();
        while(i<input.length()){
            char temp=input.charAt(i);
            switch (state){
                case 0:
                        if(isDigit(temp)){
                            stringBuilder.append(temp);
                            state=1;
                        }
                        break;
                case 1:
                        if(temp=='.'){
                            stringBuilder.append(temp);
                            state=2;
                        }else if(isDigit(temp)){
                            stringBuilder.append(temp);
                            state=1;
                        }else{
                            String word=stringBuilder.toString();
                            TokenWrapper tokenWrapper=new TokenWrapper(Token.Integer,word);
                            result.add(tokenWrapper);
                            //此时指向整数后一个字符
                            return i;
                        }
                        break;

                case 2:
                        if(isDigit(temp)){
                            stringBuilder.append(temp);
                            state=3;
                        }else if(Character.isWhitespace(temp)){
                            String word=stringBuilder.toString();
                            TokenWrapper tokenWrapper=new TokenWrapper(Token.Float,word);
                            result.add(tokenWrapper);
                            //此时指向float后一个字符
                            return i;
                        }
                        break;
                case 3:
                        if(isDigit(temp)){
                            stringBuilder.append(temp);
                            state=3;
                        }else{
                            String word=stringBuilder.toString();
                            TokenWrapper tokenWrapper=new TokenWrapper(Token.Float,word);
                            result.add(tokenWrapper);
                            //此时指向float后一个字符
                            return i;
                        }
                        break;
            }
            i++;
        }

        if(state==1||state==2||state==3){
            String word=stringBuilder.toString();
            TokenWrapper tokenWrapper=new TokenWrapper(Token.Integer,word);
            result.add(tokenWrapper);
            //此时指向整数后一个字符
            return i;
        }
        return -1;
    }

    private int findSymbol(int i){

      if(delimiterList.contains(input.charAt(i)+"")){
            String word=input.charAt(i)+"";
            TokenWrapper tokenWrapper=new TokenWrapper(Token.Delimiter,word);
            result.add(tokenWrapper);
            return ++i;
        }else{
            return findOperator(i);
        }

    }

    private int findOperator(int i){
        char aChar = input.charAt(i);
        for(int k=0;k<operatorTable.length;k++) {
            if (operatorTable[k][0].charAt(0) == aChar) {
                if (operatorTable[k].length == 2) {
                    return findOpByChar(i, operatorTable[k][0].charAt(0), operatorTable[k][1].charAt(0));
                } else if (operatorTable[k].length == 3) {
                    return findOpByChar(i, operatorTable[k][0].charAt(0), operatorTable[k][1].charAt(0),
                            operatorTable[k][2].charAt(0));
                } else {
                    TokenWrapper tokenWrapper = new TokenWrapper(Token.Operator, operatorTable[k][0]);
                    result.add(tokenWrapper);
                    return ++i;
                }

            }
        }
        System.out.println("can't find the operator: "+aChar);
        return -1;
    }

    private int findOpByChar(int i,char firstChar,char secChar ){
        int state=0;
        String operator = "";
        while(i<input.length()){
            char temp=input.charAt(i);
            switch (state){
                case 0:
                    if(temp==firstChar){
                        operator+=temp;
                        state=1;
                    }
                    break;
                case 1:
                    if(temp==secChar){
                        operator+=temp;
                        state=2;
                    }else{
                        TokenWrapper tokenWrapper=new TokenWrapper(Token.Operator,firstChar+"");
                        result.add(tokenWrapper);
                        return i;
                    }
                    break;
                case 2:
                    String word=operator;
                    TokenWrapper tokenWrapper=new TokenWrapper(Token.Operator,word);
                    result.add(tokenWrapper);
                    //此时指向+=或++后一个字符
                    return i;
            }
            i++;
        }
        return -1;
    }

    private int findOpByChar(int i,char firstChar,char secChar,char thirdChar ){
        int state=0;
        String operator = "";
        while(i<input.length()){
            char temp=input.charAt(i);
            switch (state){
                case 0:
                    if(temp==firstChar){
                        operator+=temp;
                        state=1;
                    }
                    break;
                case 1:
                    if(temp==secChar){
                        operator+=temp;
                        state=2;
                    }else if(temp==thirdChar){
                        operator+=temp;
                        state=3;
                    }else{
                        TokenWrapper tokenWrapper=new TokenWrapper(Token.Operator,firstChar+"");
                        result.add(tokenWrapper);
                        return i;
                    }
                    break;
                default:
                    String word=operator;
                    TokenWrapper tokenWrapper=new TokenWrapper(Token.Operator,word);
                    result.add(tokenWrapper);
                    //此时指向+=或++后一个字符
                    return i;
            }
            i++;
        }
        return -1;
    }

    private int skipComment(int i){
        if(i>input.length()-2){
            System.out.print("语法错误：程序的末尾出现了:"+'/');
            return -1;
        }else{
            if(input.charAt(i+1)=='/'){
                return skipSingleLineCommt(i);
            }else if(input.charAt(i+1)=='*'){
                return  skipMultipleLinesCommt(i);
            }else {
                //不是注释开头
                return -1;
            }
        }

    }

    private int skipWhiteSpace(int i){
        while(i<input.length()){
            if(Character.isWhitespace(input.charAt(i))){
                i++;
            }else{
                return i;
            }
        }
        return i;
    }

    private int skipSingleLineCommt(int i){
        int state=0;
        String operator = "";
        while(i<input.length()){
            char temp=input.charAt(i);
            switch (state){
                case 0:
                    if(temp=='/'){
                        operator+=temp;
                        state=1;
                    }
                    break;
                case 1:
                    if(temp=='/'){
                        operator+=temp;
                        state=2;
                    }
                    break;
                case 2:
                    if(temp=='\n'){
                        state=3;
                    }else{
                        operator+=temp;
                        state=2;
                    }
                    break;
                case 3:
                    String word=operator;
                    TokenWrapper tokenWrapper=new TokenWrapper(Token.Comment,word);
                    result.add(tokenWrapper);
                    //此时指向+=或++后一个字符
                    return i;
            }
            i++;
        }
        //如果没有'\n'作为结束
        String word=operator;
        TokenWrapper tokenWrapper=new TokenWrapper(Token.Comment,word);
        result.add(tokenWrapper);
        return i;

    }

    private int skipMultipleLinesCommt(int i){
        int state=0;
        String operator = "";
        while(i<input.length()){
            char temp=input.charAt(i);
            switch (state){
                case 0:
                    if(temp=='/'){
                        operator+=temp;
                        state=1;
                    }
                    break;
                case 1:
                    if(temp=='*'){
                        operator+=temp;
                        state=2;
                    }
                    break;
                case 2:
                    if(temp=='*'){
                        state=3;
                    }else{
                        state=2;
                    }
                    operator+=temp;
                    break;
                case 3:
                    if(temp=='*'){
                        state=3;
                    }else if(temp=='/'){
                        state=4;
                    }else{
                        state=2;
                    }
                    operator+=temp;
                    break;
                case 4:
                    //下面代码执行的前提是*/后面还有其他字符
                    String word=operator;
                    TokenWrapper tokenWrapper=new TokenWrapper(Token.Comment,word);
                    result.add(tokenWrapper);
                    //此时指向*/后一个字符
                    return i;
            }
            i++;
        }
        //若*/后没有任何字符了
        String word=operator;
        TokenWrapper tokenWrapper=new TokenWrapper(Token.Comment,word);
        result.add(tokenWrapper);
        //此时指向*/后一个字符
        return i;
    }

    private boolean isLetter(char aChar){
        if('a'<=aChar&&aChar<='z' ||'A'<=aChar&&aChar<='Z'){
            return true;
        }
        return false;
    }

    private boolean isDigit(char aChar){
        if('0'<=aChar&&aChar<='9'){
            return true;
        }
        return false;
    }

    private boolean isLetterOrDigit(char aChar){
        return isLetter(aChar)||isDigit(aChar);
    }


}
