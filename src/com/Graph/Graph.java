package com.Graph;

import java.awt.*;
import java.util.*;

import com.Draw.DrawGraph;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.geom.Point2D;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * The type Graph.
 */
public class Graph {
    /**
     * The Text map.
     */
// 成员 ////////////////////////////////////////////////////
    // 邻接表表示图，用HashMap<Integer,Integer>类表示<节点编号，权重>。
    ArrayList<HashMap<Integer, Integer>> textMap;
    /**
     * The Idx 2 text.
     */
// 建立一个从索引到对应单词的映射。
    HashMap<Integer, String> idx2Text;
    /**
     * The Text 2 idx.
     */
// 建立一个从单词到索引的映射。
    HashMap<String, Integer> text2Idx;
    /**
     * The Input words.
     */
    ArrayList<String> inputWords;

    // 工具方法 ////////////////////////////////////////////////
    /**
     * 建立单词<->索引的双向映射
     * @param text 欲建立索引的单词
     * @return 是否成功创建索引
    */
    private boolean constructTextMapping(String text) {
        if(text2Idx.containsKey(text)) {
            //System.out.println("Failed to insert text " + text + ".");
            return false;
        } else {
            text2Idx.put(text, text2Idx.size());
            idx2Text.put(text2Idx.get(text), text);
            return true;
        }
    }

    /**
     * HashMap的包装语法糖，用于获取单词对应的索引，和索引对应的单词。
     * @param word 输入的单词
     * @return 单词对应的索引值
     */
    private int getIdx(String word) {
        return text2Idx.get(word);
    }

    /**
     * HashMap的包装语法糖，用于获取单词对应的索引，和索引对应的单词。
     * @param idx 输入的索引
     * @return 索引对应的单词
     */
    private String getTag(int idx) {
        return idx2Text.get(idx);
    }

    /**
     * @param level 想要打印的层索引
     * @return 结构化的层结构字符串
     */
    private String printLevelInformation(int level) {
        HashMap<Integer, Integer> lvl = textMap.get(level);
        String result = "";
        for(int key : lvl.keySet()) {
            result += getTag(key) + "-(W: " + lvl.get(key).toString() + ") ";
        }
        return result;
    }

    /**
     * 向图中插入第一个词所使用的API
     * @param currWord 当前输入的词
     * @return 是否成功插入
     */
    private boolean insertFirstWord(String currWord) {
        if(!constructTextMapping(currWord)) {
            System.out.println("Error: Pure Stupidity");
            return false;
        }
        textMap.add(new HashMap<>());
        return true;
    }

    /**
     * 向图中插入非首个单词的API
     * @param prevWord 当前词的前一个词
     * @param currWord 当前词
     * @return 是否成功插入
     */
    private boolean insertPostWord(String prevWord, String currWord) {
        if(constructTextMapping(currWord)) {
            textMap.add(new HashMap<>());
        }
        if(!text2Idx.containsKey(prevWord)) {
            System.out.println("Error: PrevWord Not Mapped");
            return false;
        }
        // Typical Java. A mountain of stinky pooooop!
        textMap.get(getIdx(prevWord)).put(getIdx(currWord),
                textMap.get(getIdx(prevWord)).getOrDefault(getIdx(currWord), 0)+1);
        return true;
    }

    /**
     * 根据单词，判断这两个单词所代表的节点是否连接
     * @param source 源节点，边起始的节点
     * @param target 目的节点，边所连接的节点
     * @return 是否有对应的有向边
     */
    private boolean isConnected(String source, String target) {
        return textMap.get(getIdx(source)).containsKey(getIdx(target));
    }

    /**
     * 根据索引，判断这两个节点是否连接
     * @param srcIdx 源节点索引，边起始的节点
     * @param tgtIdx 目的节点索引，边所连接的节点
     * @return 是否有对应的有向边
     */
    private boolean isConnected(int srcIdx, int tgtIdx) {
        return textMap.get(srcIdx).containsKey(tgtIdx);
    }

    /**
     * 呃。
     * @return 随机值
     */
    private int getRandomNumber() {
        // Who cares?
        return 0;
    }

    /**
     * 结构化输出桥接词: ArrayList -> " apple banana"
     * @param a 桥接词表
     * @return 结构化的字符串
     */
    private String printBridgeList(ArrayList<String> a) {
        String result = "";
        for(String b : a) {
           result += (" " + b);
        }
        return result;
    }

    /**
     * 计算两个单词的桥接词，具体的算法
     * @param srcWord 源词
     * @param tgtWord 目标词
     * @return 桥接词表
     */
    private ArrayList<String> queryBridgeWordsSolver(String srcWord, String tgtWord) {
        ArrayList<String> result = new ArrayList<>();
        if(!text2Idx.containsKey(srcWord) && !text2Idx.containsKey(tgtWord)) {
            return result;
        } else if(!text2Idx.containsKey(srcWord)) {
            return result;
        } else if(!text2Idx.containsKey(tgtWord)) {
            return result;
        }

        for(int srcWordConnectIdx : textMap.get(getIdx(srcWord)).keySet()) {
            if(isConnected(srcWordConnectIdx, getIdx(tgtWord))) {
                result.add(getTag(srcWordConnectIdx));
            }
        }


        return result;
    }
    // 对外接口 ///////////////////////////////////////////////

    /**
     * 构图的包装方法。
     *
     * @return 是否成功建图 boolean
     */
    public boolean constructGraph() {
        return constructGraph(inputWords);
    }

    /**
     * 构图的具体算法
     *
     * @param words 切割好的词表
     * @return 是否成功构图 boolean
     */
    public boolean constructGraph(ArrayList<String> words) {
        if(words.isEmpty()) {
            System.out.println("Warning: Empty File.");
            return false;
        }
        insertFirstWord(words.getFirst());
        for(int idx = 1; idx < words.size(); ++idx) {
            if(!insertPostWord(words.get(idx-1), words.get(idx))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 用于给用户查询桥接词的API，输出预先设置好的语句。
     *
     * @param word1 源词
     * @param word2 目标词
     */
    public void queryBridgeWords(String word1, String word2) {
        ArrayList<String> result = queryBridgeWordsSolver(word1, word2);
        if(!text2Idx.containsKey(word1) && !text2Idx.containsKey(word2)) {
            System.out.println("No \"" + word1 + "\" " + "and \"" + word2 + "\" in the graph!");
            return;
        } else if(!text2Idx.containsKey(word1)) {
            System.out.println("No \"" + word1 + "\" in the graph!");
            return;
        } else if(!text2Idx.containsKey(word2)) {
            System.out.println("No \"" + word2 + "\" in the graph!");
            return;
        }
        if(!result.isEmpty()) {
            System.out.println("The bridge words from \"" + word1 + "\" to \"" + word2 + "\" " +
                    (result.size() == 1 ? "is" : "are") +
                    printBridgeList(result)
            );
            return;
        }
        System.out.println("No bridge words from \"" + word1 + "\" to \"" + word2 + "\"!");
    }

    /**
     * 根据输入，计算桥接词，生成新的文本
     *
     * @param inputText 用户输入文本
     * @return 填充桥接词的文本 string
     */
    public String generateNewText(String inputText) {
        String processedLine = inputText.replaceAll("[^A-Za-z]", " ");
        String[] words = processedLine.split("\\s+");
        String result = "" + words[0];
        String prevWord = words[0];
        for(int idx = 1; idx < words.length; ++idx) {
            ArrayList<String> bridgeWords = queryBridgeWordsSolver(prevWord, words[idx]);
            if(bridgeWords.isEmpty()) {
                result += " " + words[idx];
            } else if(bridgeWords.size() == 1) {
                result += " " + bridgeWords.getFirst() + " " + words[idx];
            } else {
                result += " " + bridgeWords.get(getRandomNumber()) + " " + words[idx];
            }
            prevWord = words[idx];
        }
        return result;
    }

    public void showDirectedGraph(String savename){
        int[] prev = null;
        showDirectedGraph(savename, 800,800,200.0,400,400,  prev);
    }
    public void showDirectedGraph(String savename, int[] prev){
        showDirectedGraph(savename, 800,800,200.0,400,400,  prev);
    }
    public void showDirectedGraph(){
        // 获取当前时间
        LocalDateTime currentTime = LocalDateTime.now();

        // 定义时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

        // 将当前时间格式化为字符串
        String formattedTime = currentTime.format(formatter);

        // 使用格式化后的时间作为文件名
        String fileName = formattedTime + "_output.png";

        int[] prev = null;

        showDirectedGraph(fileName,800,800,200.0,400,400, prev);
    }
    /**
     *  绘制图的API
     *
     *  @param savename 保存的文件名
     *  @param frame_w: 窗口宽度
     *  @param frame_h: 窗口高度
     *  @param radius: 圆的半径
     *  @param centerX: 圆心的x坐标
     *  @param centerY: 圆心的y坐标
     *  @param prev: 可能的最短路径，用于突出最短路径
     */
    public void showDirectedGraph(String savename, int frame_w, int frame_h, Double radius, double centerX, double centerY, int[] prev) {
        int numPoints = textMap.size();    // 点的数量

        double scale = numPoints * 1.0 / 10;
        if (scale < 1){
            scale = 1;
        }
        else {
            int temp_frame_h = frame_h;
            frame_w = (int) (frame_w * scale);
            frame_h = (int) (frame_h * scale);
            if (frame_w > 1800) {
                frame_w = 1800;
                frame_h = 900;
            }
            centerX = (double) frame_w / 2;
            centerY = (double) frame_h / 2;
            radius = frame_h - centerY - 70;
        }
        JFrame frame = new JFrame(savename);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(frame_w, frame_h);

        List<Point2D> points = DrawGraph.layoutPointsOnCircle(radius, numPoints, centerX, centerY);

        DrawGraph graph = new DrawGraph();
        if (prev != null){
            graph.setPrev(prev);
        }
        for(int idx = 0; idx < textMap.size(); ++idx) {
            graph.addNode(getTag(idx), idx, points);
        }
        for(int idx = 0; idx < textMap.size(); ++idx) {
            for(int key : textMap.get(idx).keySet()) {
                graph.addEdge(idx, key, textMap.get(idx).get(key));
            }
        }

        frame.add(graph);
        frame.setVisible(true);
        savePic(frame, savename);
    }

    public String calcShortestPath(String word1){
        // 检查图中是否包含这两个单词，如果不包含，打印消息并返回没有找到路径的字符串

        if(!text2Idx.containsKey(word1)) {
            System.out.println("No \"" + word1 + "\" in the graph!");
            return "No path found between " + word1 + " and " + "*";
        }

        // 获取单词对应的索引
        int index1 = getIdx(word1);
        int[] prev = dijkstra(index1);

        // 其他点到word1的最短路径
        String re_text = "";
        for(int i = 0; i < textMap.size(); i++){
            if(i != index1){
                int curr = i;
                // 如果终点的前驱节点为-1且终点不是起点，则表示没有找到路径
                if (prev[curr] == -1) {
                    re_text = re_text + "No path found between " + word1 + " and " + getTag(curr) + "\n";
                }else{
                    // 创建一个新的前驱数组用于输出图形
                    int[] prev2 = new int[textMap.size()];
                    Arrays.fill(prev2, -1);

                    String result = "";
                    int weight = 0;
                    // 从终点开始，逆向追溯到起点，构建最短路径字符串，并计算权重的和
                    while(curr != -1) {
                        if (prev[curr] != -1){
                            weight += textMap.get(prev[curr]).get(curr);
                        }
                        prev2[curr] = prev[curr];
                        result = getTag(curr) + " " + result;
                        curr = prev[curr];
                    }
                    result = result + " Weight: " + weight;
                    // 显示有向图，使用新的前驱数组
                    showDirectedGraph(word1 + "_" + getTag(i) + "_output.png", prev2);
                    re_text = re_text + result + "\n";
                }
            }
        }
        return re_text;
    }

    /**
     * 计算最短路径的API
     *
     * @param word1 源词
     * @param word2 目标词
     * @return 最短路径
     */
    public String calcShortestPath(String word1, String word2) {
        // 检查图中是否包含这两个单词，如果不包含，打印消息并返回没有找到路径的字符串
        if(!text2Idx.containsKey(word1) && !text2Idx.containsKey(word2)) {
            System.out.println("No \"" + word1 + "\" " + "and \"" + word2 + "\" in the graph!");
            return "No path found between " + word1 + " and " + word2;
        } else if(!text2Idx.containsKey(word1)) {
            System.out.println("No \"" + word1 + "\" in the graph!");
            return "No path found between " + word1 + " and " + word2;
        } else if(!text2Idx.containsKey(word2)) {
            System.out.println("No \"" + word2 + "\" in the graph!");
            return "No path found between " + word1 + " and " + word2;
        }

        // 获取单词对应的索引
        int index1 = getIdx(word1);
        int index2 = getIdx(word2);

        // dijkstra求解
        int[] prev = dijkstra(index1);

        // 输出最短路径
        int nodeSize = textMap.size();
        int curr = index2;
        // 如果终点的前驱节点为-1且终点不是起点，则表示没有找到路径
        if (prev[curr] == -1 && curr != index1) {
            return "No path found between " + word1 + " and " + word2;
        }

        // 创建一个新的前驱数组用于输出图形
        int[] prev2 = new int[nodeSize];
        Arrays.fill(prev2, -1);

        String result = "";
        int weight = 0;
        // 从终点开始，逆向追溯到起点，构建最短路径字符串，并计算权重的和
        while(curr != -1) {
            if (prev[curr] != -1){
                weight += textMap.get(prev[curr]).get(curr);
            }
            prev2[curr] = prev[curr];
            result = getTag(curr) + " " + result;
            curr = prev[curr];
        }

        // 显示有向图，使用新的前驱数组
        showDirectedGraph(word1 + "_" + word2 + "_output.png", prev2);

        // 返回最短路径字符串
        return result + " Weight: " + weight;
    }

    /**
     * 随机游走
     * @return 随机游走的路径
     */
    public String randomWalk(){
        System.out.println("Start random walk");
        if(textMap.isEmpty()){
            System.out.println("No nodes in the graph");
            return "";
        }
        System.out.println("input 'c' to continue; input 'b' to break; input 'a' to auto.");
        Random random = new Random();
        Scanner scanner = new Scanner(System.in);
        // keyboard input c go next; input b break
        int curr = random.nextInt(textMap.size());  // 随机获得出发节点
        String result = getTag(curr) + " ";

        Set<Tuple> visited = new HashSet<>();  // 记录经历过的边，元素为(index1,index2)
        int visited_flag = 0;  // 跳出标记
        int auto_flag = 0;  // 自动游走标记

        while(true){
            // get keyboard input
            String input = "";

            if(auto_flag==0){
                System.out.println("please input: ");
                input = scanner.next();
                assert input.length() == 1;
            }
            else{
                input = "c";
            }

            if(Objects.equals(input, "a")){
                auto_flag = 1;
                input = "c";
            }

            if(Objects.equals(input, "b")){
                break;
            }
            else if(Objects.equals(input, "c")){
                HashMap<Integer, Integer> currMap = textMap.get(curr);  // 当前节点的邻接表
                int size = currMap.size();  // 当前节点的邻接表大小，随机数范围
                if (size == 0){
                    break;  // 当前节点无邻接节点，跳出
                }

                int old_curr = curr;
                int next = random.nextInt(size);  // 随机获得下一个节点

                int i = 0;
                for(int key : currMap.keySet()){
                    if(i == next){  // 随机获得的下一个节点
                        curr = key;
                        Tuple newtuple = new Tuple(old_curr, curr);  // 新的边
                        for(Tuple t : visited){
                            if (t.isequal(newtuple)){  // 判断是否经历过
                                visited_flag = 1;
                                break;
                            }
                        }
                        break;
                    }
                    i++;
                }
                if(visited_flag==0) {
                    visited.add(new Tuple(old_curr, curr));
                    result = result + getTag(curr) + " ";
                }
            }
            if(visited_flag==1){
                break;
            }
        }
        System.out.println("end random walk");
        return result;
    }

    // 元组，用于随机游走
    static class Tuple{
        int element1;
        int element2;
        Tuple(int input1, int input2){
            this.element1 = input1;
            this.element2 = input2;
        }

        public boolean isequal(Tuple t){
            return this.element1 == t.element1 && this.element2 == t.element2;
        }
    }

    /**
     * Dijkstra算法
     * @param index1: 起始词的索引
     * @return 前驱数组
     */
    public int[] dijkstra(int index1){
        // dijkstra 最短路
        int nodeSize = textMap.size();
        int[] dist = new int[nodeSize];  // 距离数组，存储起点到每个节点的最短距离
        int[] prev = new int[nodeSize];  // 前驱数组
        boolean[] visited = new boolean[nodeSize];  // 访问标记数组

        // 初始化距离数组、前驱数组和访问标记数组
        for(int i = 0; i < nodeSize; ++i) {
            dist[i] = Integer.MAX_VALUE;
            prev[i] = -1;
            visited[i] = false;
        }

        // 将起点距离设置为0
        dist[index1] = 0;

        // Dijkstra算法核心部分
        for(int i = 0; i < nodeSize; ++i) {
            int minDist = Integer.MAX_VALUE;
            int minIndex = -1;

            // 找到未访问的距离最小的节点
            for(int j = 0; j < nodeSize; ++j) {
                if(!visited[j] && dist[j] < minDist) {
                    minDist = dist[j];
                    minIndex = j;
                }
            }

            // 如果没有找到未访问的节点，则退出循环
            if(minIndex == -1) {
                break;
            }

            // 标记当前节点为已访问
            visited[minIndex] = true;

            // 更新与当前节点相邻的未访问节点的距离和前驱节点
            for(int j = 0; j < nodeSize; ++j) {
                if(!visited[j] && isConnected(minIndex, j) && dist[minIndex] + textMap.get(minIndex).get(j) < dist[j]) {
                    dist[j] = dist[minIndex] + textMap.get(minIndex).get(j);
                    prev[j] = minIndex;
                }
            }
        }
        return prev;
    }

    /**
     * 保存文本到文件
     * @param texToSave 欲保存的文本
     */
    public void saveString(String texToSave){
         // 获取当前时间
        LocalDateTime currentTime = LocalDateTime.now();

        // 定义时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

        // 将当前时间格式化为字符串
        String formattedTime = currentTime.format(formatter);

        // 使用格式化后的时间作为文件名
        String fileName = formattedTime + "_output.txt";

        saveString(texToSave, fileName);
    }
    public void saveString(String textToSave, String savename){
        try {
            // 创建一个FileWriter对象，用于写入文件
            // 第二个参数为true表示追加模式，false表示覆盖模式
            FileWriter writer = new FileWriter(savename, true);

            // 写入文本
            writer.write(textToSave);

            // 关闭FileWriter对象
            writer.close();

            System.out.println("文本已成功保存到文件: " + savename);
        } catch (IOException e) {
            System.out.println("保存文本到文件时发生错误: " + e.getMessage());
        }
    }

    /**
     * 保存图到文件
     *
     * @param jf 欲保存的图
     * @param savename 欲保存的文件名
     */
    public void savePic(JFrame jf, String savename){
		//得到窗口内容面板
		Container content=jf.getContentPane();
		//创建缓冲图片对象
		BufferedImage img=new BufferedImage(
                content.getWidth(),content.getHeight(),BufferedImage.TYPE_INT_RGB);
		//得到图形对象
		Graphics2D g2d = img.createGraphics();
        // 设置背景颜色为白色
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, jf.getWidth(), jf.getHeight());
		//将窗口内容面板输出到图形对象中
		content.printAll(g2d);
		//保存为图片
		File f=new File(savename);
		try {
			ImageIO.write(img, "jpg", f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//释放图形对象
		g2d.dispose();
	}


    // Debug接口 //////////////////////////////////////////////////

    /**
     * 打印图，但是只打印节点的索引值
     */
    public void printGraph() {
        for(int idx = 0; idx < textMap.size(); ++idx) {
            System.out.print(idx);
            System.out.print(": ");
            System.out.println(textMap.get(idx));
        }
    }

    /**
     * 打印图，也打印节点的标签文本单词
     */
    public void printTextGraph() {
        for(int idx = 0; idx < textMap.size(); ++idx) {
            System.out.println(getTag(idx) + ": " + printLevelInformation(idx));
        }
    }

    /**
     * Instantiates a new Graph.
     */
// 构造器 ///////////////////////////////////////////////////
    public Graph() {
        textMap = new ArrayList<>();
        idx2Text = new HashMap<>();
        text2Idx = new HashMap<>();
    }

    /**
     * Instantiates a new Graph.
     *
     * @param words the words
     */
    public Graph(ArrayList<String> words) {
        textMap = new ArrayList<>();
        idx2Text = new HashMap<>();
        text2Idx = new HashMap<>();
        inputWords = words;
        constructGraph();
    }

    // Getter和Setter方法 /////////////////////////////////////////

    /**
     * Gets text map.
     *
     * @return the text map
     */
    public ArrayList<HashMap<Integer, Integer>> getTextMap() {
        return textMap;
    }

    /**
     * Sets text map.
     *
     * @param textMap the text map
     */
    public void setTextMap(ArrayList<HashMap<Integer, Integer>> textMap) {
        this.textMap = textMap;
    }

    /**
     * Gets idx 2 text.
     *
     * @return the idx 2 text
     */
    public HashMap<Integer, String> getIdx2Text() {
        return idx2Text;
    }

    /**
     * Sets idx 2 text.
     *
     * @param idx2Text the idx 2 text
     */
    public void setIdx2Text(HashMap<Integer, String> idx2Text) {
        this.idx2Text = idx2Text;
    }

    /**
     * Gets text 2 idx.
     *
     * @return the text 2 idx
     */
    public HashMap<String, Integer> getText2Idx() {
        return text2Idx;
    }

    /**
     * Sets text 2 idx.
     *
     * @param text2Idx the text 2 idx
     */
    public void setText2Idx(HashMap<String, Integer> text2Idx) {
        this.text2Idx = text2Idx;
    }
}
