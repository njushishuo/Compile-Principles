import IO.FileOuter;
import IO.FileReader;

import java.util.List;

public class Main {

    private static String grammarPath="test/cfg.txt";
    private static String tablePath="test/table.txt";
    private static String inputPath="test/input.txt";
    private static String outputPath="test/output.txt";

    public static void main(String[] args) {
        FileReader fileReader = new FileReader();
        FileOuter fileOuter = new FileOuter();
        SimpleLexicalAnalyzer simpleLex = new SimpleLexicalAnalyzer();
        SyntaxAnalyser syntaxAnalyzer = new SyntaxAnalyser();
        //CFG
        String grammar=fileReader.readFile(grammarPath);
        String tableContent=fileReader.readFile(tablePath);
        String input = fileReader.readFile(inputPath);


        List<Token> tokenList = simpleLex.LexAnalyse(input);
        syntaxAnalyzer.preHandle(grammar,tableContent);
        List<String>result= syntaxAnalyzer.syntaxAnalyse(tokenList);
        fileOuter.OutputFile(outputPath,result);

    }

}
