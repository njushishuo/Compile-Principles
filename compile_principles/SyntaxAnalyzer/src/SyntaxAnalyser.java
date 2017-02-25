import java.util.*;

/**
 * Created by ss14 on 2016/11/2.
 */
public class SyntaxAnalyser {
    private  String  [] Grammar;
    private  Map<Token,Action>[] ActionTable ;
    private  Map<Character,Integer> [] GotoTable;
    private  Token [] ActionHeader = {Token.ID , Token.PLUS,Token.MULTIPLY,Token.LEFT_Par,
                                        Token.RIGHT_Par,Token.END};
    private  String [] GotoHeader = {"E","T","F"};

    /**
     * 该方法用于创建分析表中的Action部分和Goto部分
     * 结果是，Action部分只保留含有S*,R*,或Accept状态的Action
     *        Goto部分保留"E","T","F"下出现数字的部分
     * @param tableString
     */
      public void preHandle(String grammar,String tableString){

          Grammar=grammar.split(",");
          String lines [] = tableString.split(",");
          int StatesCount=lines.length;

          //Parsing Table
           ActionTable  = new Map[StatesCount];
           GotoTable =new Map[StatesCount];

          for(int i=0;i<StatesCount;i++){
              ActionTable[i]= new HashMap<>();
              GotoTable[i]=new HashMap<>();
              String [] parts = lines[i].split(" ");

              //对于每一行
              for(int j=0;j<parts.length;j++){
                  //对于Action部分
                  if(j<=ActionHeader.length-1){
                      //如果不是空
                      if(!parts[j].equals("-1")&&!parts[j].equals("acc")){
                          //先获取数字（移进或规约）
                          //  System.out.println(parts[j]);
                          String temp = parts[j].substring(1);
                          int num=Integer.parseInt(temp);
                          //再判断S,R
                          if(parts[j].startsWith("s")){
                              ActionTable[i].put(ActionHeader[j],new Action(ActionType.SHIFT,num));
                          }else if(parts[j].startsWith("r")){
                              ActionTable[i].put(ActionHeader[j],new Action(ActionType.REDUCE,num));
                          }else{
                              System.out.print("输入文件格式错误");
                          }
                      }else if(parts[j].equals("acc")){
                          ActionTable[i].put(ActionHeader[j],new Action(ActionType.ACCEPT,-1));
                      }
                  }else{
                      //如果不为空
                      if(!parts[j].equals("-1")){
                          //获取数字（移进或规约）
                          int num=Integer.parseInt(parts[j]);
                          GotoTable[i].put(GotoHeader[j-ActionHeader.length].charAt(0),num);
                      }

                  }
              }
          }


//          for(int i=0;i<StatesCount;i++){
//              for(Map.Entry<Token,Action> entry : ActionTable[i].entrySet()){
//                  System.out.print(entry.getKey()+":"+entry.getValue().getType()+entry.getValue().getNum()+"  ");
//              }
//
//              for(Map.Entry<Character,Integer> entry : GotoTable[i].entrySet()){
//                  System.out.print(entry.getKey()+":"+entry.getValue()+" ");
//              }
//              System.out.print('\n');
//          }
      }


      public List<String> syntaxAnalyse(List<Token> tokenList){
          List<String> result= new ArrayList<>();
          Stack<Integer> stateStack = new Stack<>();
          Stack<Object> symbolStack= new Stack<>();
          MyQueue tokenQueue = new MyQueue(tokenList);
          stateStack.push(0);
          while(true){
              //当前输入token
              Token curToken=tokenQueue.peek();
              //当前Token压入栈中
              symbolStack.push(curToken);
              //栈顶状态
              int curState=stateStack.peek();
              //栈顶符号
              Object curSymbol=symbolStack.peek();
              if(ActionTable[curState].containsKey(curSymbol)){
                  Action curAction = ActionTable[curState].get(curSymbol);
                  if(curAction.getType()==ActionType.SHIFT){
                      stateStack.push(curAction.getNum());
                      tokenQueue.remove();
                  }else if(curAction.getType()==ActionType.REDUCE){
                      //使用几号产生式规约
                      int reduceNum=curAction.getNum();
                      //该产生式右部的长度
                      int productionLen =getLeftLength(reduceNum);
                      for(int i=0;i<productionLen;i++){
                          symbolStack.pop();
                          stateStack.pop();
                      }
                      curState=stateStack.peek();
                      symbolStack.push(Grammar[reduceNum].charAt(0));
                      curSymbol=symbolStack.peek();
                      //查询GOTO表
                      if(GotoTable[curState].containsKey(curSymbol)){
                          int gotoState=GotoTable[curState].get(curSymbol);
                          stateStack.push(gotoState);
                          result.add(Grammar[curAction.getNum()]);
                      }

                  }else if(curAction.getType()==ActionType.ACCEPT){
                      System.out.println("Success");
                      break;
                  }
              }else{
                  System.out.println("error");
              }

          }
          return result;
      }

      private int getLeftLength(int garmmarNum){
          String temp= Grammar[garmmarNum];
          return temp.length()-1-temp.indexOf('>');
      }

}
