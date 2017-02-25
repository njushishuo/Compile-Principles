public class Main {

    public static void main(String[] args) {
        String input="\n /* asdas***/ public static \n void main(){ a=3+2.098; a++; b+=a;//hhhh \n b=a*4; c*=5;/* asda\ns***/a" ;
        LexicalAnalyzer analyzer=new LexicalAnalyzer();
        analyzer.analyse(input);
    }
}
