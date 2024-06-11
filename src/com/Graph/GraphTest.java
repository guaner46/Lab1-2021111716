package com.Graph;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GraphTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private Graph g;

    @BeforeEach
    void setUp() {
        // 重定向标准输出到 ByteArrayOutputStream
        System.setOut(new PrintStream(outContent));
        ArrayList<String> words = new ArrayList<>();
        words.add("to");
        words.add("explore");
        words.add("strange");
        words.add("new");
        words.add("worlds");
        words.add("to");
        words.add("seek");
        words.add("out");
        words.add("new");
        words.add("life");
        words.add("and");
        words.add("new");
        words.add("civilizations");
        g = new Graph(words);
    }

    @AfterEach
    void tearDown() {
        // 恢复标准输出
        System.setOut(originalOut);
    }

    @Test
    void calcShortestPathTest1() {
        // testcase1
        String goldRe = "to explore strange new worlds  Weight: 4";
        String goldPr = "";
        String re = g.calcShortestPath("to", "worlds");
        assertEquals(goldPr, outContent.toString().trim());  // 检查屏幕打印,trim删除字符串两端的空白字符
        assertEquals(goldRe, re);
    }
    @Test
    void calcShortestPathTest2() {
        // testcase2
        String goldRe = "No path found between civilizations and new";
        String goldPr = "";
        String re  = g.calcShortestPath("civilizations", "new");
        assertEquals(goldPr, outContent.toString().trim());
        assertEquals(goldRe, re);
    }
    @Test
    void calcShortestPathTest3() {
        // testcase3
        String goldRe = "No path found between to and happy";
        String goldPr = "No \"happy\" in the graph!";
        String re  = g.calcShortestPath("to", "happy");
        assertEquals(goldPr, outContent.toString().trim());
        assertEquals(goldRe, re);
    }
    @Test
    void calcShortestPathTest4() {
        // testcase4
        String goldRe = "No path found between happy and hit";
        String goldPr = "No \"happy\" and \"hit\" in the graph!";
        String re  = g.calcShortestPath("happy", "hit");
        assertEquals(goldPr, outContent.toString().trim());
        assertEquals(goldRe, re);
    }
    @Test
    void calcShortestPathTest5() {
        // testcase5
        String goldRe = "No path found between to and ";
        String goldPr = "No \"\" in the graph!";
        String re  = g.calcShortestPath("to", "");
        assertEquals(goldPr, outContent.toString().trim());
        assertEquals(goldRe, re);
    }
    @Test
    void calcShortestPathTest6() {
        // testcase6
        String goldRe = "No path found between  and ";
        String goldPr = "No \"\" and \"\" in the graph!";
        String re  = g.calcShortestPath("", "");
        assertEquals(goldPr, outContent.toString().trim());
        assertEquals(goldRe, re);
    }
    @Test
    void calcShortestPathTest7() {
        // testcase7
        String goldRe = "to  Weight: 0";
        String goldPr = "";
        String re  = g.calcShortestPath("to", "to");
        assertEquals(goldPr, outContent.toString().trim());
        assertEquals(goldRe, re);
    }
}