package com.Draw;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class DrawGraph extends JPanel {
    private List<Node> nodes;
    private List<Edge> edges;
    private int[] prev;
    private int ifShowInShortest = 0;

    public DrawGraph() {
        nodes = new ArrayList<>();
        edges = new ArrayList<>();
    }

    public void addNode(String name, int index, List<Point2D> points) {
        if (index < nodes.size()) {
            throw new IllegalArgumentException("Node index must be unique");
        }
        nodes.add(new Node(name, index, points));  // 名字、索引、图中坐标
    }

    public void addEdge(int from_index, int to_index, int weight) {
        Node from = nodes.get(from_index);
        Node to = nodes.get(to_index);
        edges.add(new Edge(from, to, weight));  // 起始节点、终止节点、权重
    }

    public void setPrev(int[] prev) {
        this.prev = prev;
        ifShowInShortest=1;  // 用于标记是否显示最短路径
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setFont(new Font("Arial", Font.PLAIN, 15));

        // Draw edges
        for (Edge edge : edges) {
            int nodeIndex1 = edge.from.index;
            int nodeIndex2 = edge.to.index;
            if (ifShowInShortest==0)  // 控制最短路径是否突出显示
                g2d.setColor(Color.BLACK);
            else if(prev[nodeIndex2] == nodeIndex1)
                g2d.setColor(Color.BLUE);
            else
                g2d.setColor(Color.BLACK);

            int endx = edge.to.x;
            int endy = edge.to.y;
            double angleInRadians = Math.atan2(endy - edge.from.y, endx - edge.from.x);
            endx = (int) (endx - 15 * Math.cos(angleInRadians));
            endy = (int) (endy - 15 * Math.sin(angleInRadians));  // 箭头指向节点图像的边缘而不是圆心
            g2d.draw(new Line2D.Double(edge.from.x, edge.from.y, endx, endy)); // 画直线
            drawArrow(g2d, edge.from.x, edge.from.y, endx, endy);  // 画箭头
        }

        // Draw nodes
        for (Node node : nodes) {
            g2d.setColor(Color.RED);
            g2d.fillOval(node.x - 15, node.y - 15, 30, 30);  // 画圆，代表节点
            g2d.setColor(Color.BLACK);
            g2d.drawString("N" + node.index, node.x - 5, node.y + 5);  // 添加节点标号
            g2d.drawString("N" + node.index + ": " + node.name, 10, (1+node.index) * 18);  // 添加标号到节点名称映射
        }

        // Draw weights
        g2d.setColor(Color.GREEN);
        g2d.setFont(new Font("Arial", Font.PLAIN, 25));
        for (Edge edge : edges) {
            int midX = (edge.from.x + 2 * edge.to.x) / 3;  // 三分之一处标记权重
            int midY = (edge.from.y + 2 * edge.to.y) / 3;
            g2d.drawString(String.valueOf(edge.weight), midX, midY);
        }
    }

    private void drawArrow(Graphics2D g2d, int x1, int y1, int x2, int y2) {
        // 计算两个点的x和y坐标之差
        double dx = x2 - x1, dy = y2 - y1;
        // 计算线段与x轴之间的角度，单位是弧度
        double angle = Math.atan2(dy, dx);
        // 设置箭头的长度
        int len = 20;
        // 设置箭头的宽度
        int width = 10;
        // 定义一个数组来存储箭头头部的点的x坐标
        int[] xPoints = new int[3];
        // 定义一个数组来存储箭头头部的点的y坐标
        int[] yPoints = new int[3];
        // 箭头头部的第一个点是线段的终点 (x2, y2)
        xPoints[0] = x2;
        yPoints[0] = y2;
        // 计算箭头头部基线的第一个点的坐标
        xPoints[1] = (int) (x2 - len * Math.cos(angle - Math.PI / width));
        yPoints[1] = (int) (y2 - len * Math.sin(angle - Math.PI / width));
        // 计算箭头头部基线的第二个点的坐标
        xPoints[2] = (int) (x2 - len * Math.cos(angle + Math.PI / width));
        yPoints[2] = (int) (y2 - len * Math.sin(angle + Math.PI / width));
        // 使用当前颜色填充由这三个点定义的多边形的内部区域
        g2d.fillPolygon(xPoints, yPoints, 3);
    }

    // 用于防止图中边重叠，将节点坐标分布在一个圆上
    public static List<Point2D> layoutPointsOnCircle(double radius, int numPoints, double centerX, double centerY) {
        List<Point2D> points = new ArrayList<>();
        double angleIncrement = (2 * Math.PI) / numPoints;

        for (int i = 0; i < numPoints; i++) {  // 将一个圆周等分N份，返回划分的坐标
            double angle = i * angleIncrement;
            double x = centerX + radius * Math.cos(angle);
            double y = centerY + radius * Math.sin(angle);
            points.add(new Point2D.Double(x, y));
        }

        return points;
    }

//    public static void main(String[] args) {
//        JFrame frame = new JFrame("Directed Weighted Graph");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(400, 400);
//
//        double radius = 50.0; // 圆的半径
//        int numPoints = 5;    // 点的数量
//        double centerX = 100.0; // 圆心的x坐标
//        double centerY = 200.0; // 圆心的y坐标
//
//        List<Point2D> points = layoutPointsOnCircle(radius, numPoints, centerX, centerY);
//
//        DrawGraph graph = new DrawGraph();
//        Node nodeA = new Node("A", 1, points);
//        Node nodeB = new Node("B", 2, points);
//        Node nodeC = new Node("C", 3, points);
//
//        graph.addNode(nodeA);
//        graph.addNode(nodeB);
//        graph.addNode(nodeC);
//        graph.addEdge(new Edge(nodeA, nodeB, 5));
//        graph.addEdge(new Edge(nodeA, nodeC, 13));
//
//        frame.add(graph);
//        frame.setVisible(true);
//    }

    static class Node {
        String name;
        int x, y, index;

        public Node(String name, int index, List<Point2D> circle) {
            this.name = name;
            this.x = (int) circle.get(index).getX();
            this.y = (int) circle.get(index).getY();
            this.index = index;
        }
    }

    static class Edge {
        Node from, to;
        int weight;

        public Edge(Node from, Node to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }
    }
}