import com.Graph.Graph;
import com.InputFileReader.InputFileReader;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        // 初始化
        Scanner inScanner = new Scanner(System.in);
        System.out.println("Please input the path of the file");
        String fileName = inScanner.nextLine();
        InputFileReader ifr = new InputFileReader(fileName);
        ArrayList<String> words = ifr.getFileContent();
        Graph g = new Graph(words);


        // 展示图
        g.showDirectedGraph();

        // 查询桥接词
        while(true) {
            System.out.println("Query Bridge Words?(y or n):");
            String isQueryBridgeWord = inScanner.nextLine();
            if(Objects.equals(isQueryBridgeWord, "y")) {
                System.out.println("Please input word1:");
                String word1 = inScanner.nextLine();
                System.out.println("Please input word2:");
                String word2 = inScanner.nextLine();
                g.queryBridgeWords(word1, word2);
            } else if(Objects.equals(isQueryBridgeWord, "n")) {
                break;
            } else {
                System.out.println("Please input y or n!");
                continue;
            }
        }

        // 生成新文本
        while(true) {
            System.out.println("Generate new text according to bridge words list?(y or n):");
            String isGenerateNewText = inScanner.nextLine();
            if(Objects.equals(isGenerateNewText, "y")) {
                System.out.println("Please input your original text:");
                String newText = inScanner.nextLine();
                System.out.println(g.generateNewText(newText));
            } else if(Objects.equals(isGenerateNewText, "n")) {
                break;
            } else {
                System.out.println("Please input y or n!");
                continue;
            }
        }

        // 计算最短路径
        while(true) {
            System.out.println("Calculate Shortest Path?(y or n)");
            String isCalcShortestPath = inScanner.nextLine();
            if(Objects.equals(isCalcShortestPath, "y")) {
                System.out.println("Please input word1:");
                String word1 = inScanner.nextLine();
                System.out.println("Plaese input word2(If you don't have word2, just press ENTER):");
                String word2 = inScanner.nextLine();
                if(Objects.equals(word2, "")) {
                    System.out.println("Shortest path from \"" + word1 + "\" is:");
                    System.out.println(g.calcShortestPath(word1));
                } else {
                    System.out.println("Shortest path between \"" + word1 + "\" and \"" + word2 + " \""
                            + " is: " + g.calcShortestPath(word1, word2)
                    );
                }
            } else if(Objects.equals(isCalcShortestPath, "n")) {
                break;
            } else {
                System.out.println("Please input y or n!");
                continue;
            }
        }

        // 随机游走
        while(true) {
            System.out.println("Random Walk?(y or n)");
            String isRandomWalk = inScanner.nextLine();
            if(Objects.equals(isRandomWalk, "y")) {
                String randomWalk = g.randomWalk();
                System.out.println(randomWalk);
                g.saveString(randomWalk);
            } else if(Objects.equals(isRandomWalk, "n")) {
                break;
            } else {
                System.out.println("Please input y or n!");
                continue;
            }
        }
    }
}