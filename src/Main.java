import com.Graph.Graph;
import com.InputFileReader.InputFileReader;
import java.util.ArrayList;


public class Main {
    public static void main(String[] args) {
        // 填写绝对路径，给出了一个例子
        InputFileReader ifr = new InputFileReader("test.txt");
        ArrayList<String> words = ifr.getFileContent();
        Graph g = new Graph(words);
//        g.queryBridgeWords("seek", "to");
//        g.queryBridgeWords("to", "explore");
//        g.queryBridgeWords("explore", "new");
//        g.queryBridgeWords("new", "and");
//        g.queryBridgeWords("and", "exciting");
//        g.queryBridgeWords("exciting", "synergies");

//        g.generateNewText("Seek to explore new and exciting synergies.");
//        g.printTextGraph();

        g.showDirectedGraph();

        String shortestPath = g.calcShortestPath("to","new");
        System.out.println("Find path: " + shortestPath);
        shortestPath = g.calcShortestPath("new");
        System.out.println("Find path: " + shortestPath);

        String re = g.randomWalk();
        System.out.println(re);
        g.saveString(re);
    }
}