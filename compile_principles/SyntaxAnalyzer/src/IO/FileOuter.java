package IO; /**
 * Created by ss14 on 2016/10/26.
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

public class FileOuter {


    public void OutputFile(String filePath, List<String> content) {

        try {
            String encoding = "GBK";
            File file = new File(filePath);
            if (!(file.isFile() && file.exists())) { //判断文件是否存在
                System.out.println("找不到指定的文件,创建新文件");
                file.createNewFile();
            }

            OutputStreamWriter wt = new OutputStreamWriter(
                    new FileOutputStream(file), encoding);//考虑到编码格式
            BufferedWriter bwt = new BufferedWriter(wt);
            String temp = null;
            for (String line : content) {
                bwt.write(line+'\n');
            }
            bwt.close();
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }

    }

}




