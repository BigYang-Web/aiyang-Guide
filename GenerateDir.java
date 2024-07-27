
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class GenerateDir {

    static String basePath = "/Users/zhaoyangyang/Projects/myPage/docs";
    static String filePath = "/Users/zhaoyangyang/Projects/myPage/docs/_sidebar.md";
    static List<String> result = new LinkedList<>();
    static String commond1 = "- [%s](%s)";  //[当前目录](/当前文件名)
    static String commond2 = "  - [%s](%s)";  //[当前目录](/当前文件名)

    public static void main(String[] args) throws IOException {
        File rootDir = new File(basePath); // 替换为你的根目录路径

        traverseDirectory(rootDir, result);

        result.add(0, "<!-- docs/_sidebar.md -->");

        // 最终打印所有文件和目录路径
        for (String path : result) {
            System.out.println(path);
        }


        Files.write(Paths.get(filePath), result);
    }

    public static void traverseDirectory(File dir, List<String> result) throws UnsupportedEncodingException {
        if (dir.isDirectory()) {

            File[] files = dir.listFiles();
            if (files != null) {

                List<File> fileList = new ArrayList<>();
                Collections.addAll(fileList, files);


                // 将与目录同名的文件放在第一个
                fileList.sort(new Comparator<File>() {
                    @Override
                    public int compare(File f1, File f2) {
                        if (f1.isDirectory() || f2.isDirectory()) {
                            return 0; // 不改变目录的顺序
                        }
                        String dirName = dir.getName();
                        String f1Name = removeExtension(f1.getName());
                        String f2Name = removeExtension(f2.getName());
                        if (f1Name.equalsIgnoreCase(dirName) && !f2Name.equalsIgnoreCase(dirName)) {
                            return -1;
                        }
                        if (!f1Name.equalsIgnoreCase(dirName) && f2Name.equalsIgnoreCase(dirName)) {
                            return 1;
                        }
                        return 0;
                    }
                });


                for (File file : fileList) {
                    traverseDirectory(file, result); // 递归调用
                }
            }
        } else {
            extracted(dir.toPath(), basePath, result);
        }
    }

    private static void extracted(Path file, String basePath, List<String> fileNameS) throws UnsupportedEncodingException {
        String filePah1 = file.toString();
        if (!filePah1.endsWith(".md") || filePah1.contains("_") || filePah1.contains("README")) {
            return;
        }
        String filePath = file.toString().replace(basePath, "");
        String fileName = removeExtension(file.getFileName().toString());

        String pathName = file.getParent().toString().replace(basePath, "").replace("/", "");

        String encodedString = filePath.replace(" ","%20");

        if (pathName.equalsIgnoreCase(fileName)) {
            String str1 = String.format(commond1, fileName, encodedString);
            fileNameS.add(str1);
        } else {
            String str1 = String.format(commond2, fileName, encodedString);
            fileNameS.add(str1);
        }


    }

    public static String removeExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex == -1) {
            return fileName; // 文件名没有扩展名
        }
        return fileName.substring(0, dotIndex);
    }

}
