package com.Draw;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class DrawGraph extends JPanel {
    /** 节点.
     */
    private List<Node> nodes;
    /** 边.
     */
    private List<Edge> edges;
    /** 用于记录最短路径.
     */
    private int[] prev;
    /** 用于标记画图时是否显示最短路径.
     */
    private int ifShowInShortest = 0;
    /** 构造器.
     */
    public DrawGraph() {
        nodes = new ArrayList<>();
        edges = new ArrayList<>();
    }
    /** 添加节点.
     * @param name 节点名称
     * @param index 节点索引
     * @param points 节点坐标
     */
    public void addNode(
            final String name, final int index, final List<Point2D> points) {
        if (index < nodes.size()) {
            throw new IllegalArgumentException("Node index must be unique");
        }
        nodes.add(new Node(name, index, points));  // 名字、索引、图中坐标
    }
    /** 添加边.
     * @param fromIndex 起始节点索引
     * @param toIndex 终止节点索引
     * @param weight 权重
     */
    public void addEdge(
            final int fromIndex, final int toIndex, final int weight) {
        Node from = nodes.get(fromIndex);
        Node to = nodes.get(toIndex);
        edges.add(new Edge(from, to, weight));  // 起始节点、终止节点、权重
    }
    /** 设置最短路径.
     * @param prevpath 最短路径
     */
    public void setPrev(final int[] prevpath) {
        this.prev = prevpath;
        ifShowInShortest = 1;  // 用于标记是否显示最短路径
    }
    /** 画图.
     * @param g 画笔
     */
    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setFont(new Font("Arial", Font.PLAIN, 15));

        // Draw edges
        for (Edge edge : edges) {
            int nodeIndex1 = edge.from.index;
            int nodeIndex2 = edge.to.index;

            if (nodeIndex1 == nodeIndex2) {  // 自环
                // 画一个圆
                g2d.setColor(Color.BLUE);
                g2d.drawOval(nodes.get(nodeIndex1).x - 12,
                        nodes.get(nodeIndex1).y - 12, 30, 30);
                continue;
            }
            if (ifShowInShortest == 0) {  // 控制最短路径是否突出显示
                g2d.setColor(Color.BLACK);
            } else if (prev[nodeIndex2] == nodeIndex1) {
                g2d.setColor(Color.BLUE);
            } else {
                g2d.setColor(Color.BLACK);
            }
            int endx = edge.to.x;
            int endy = edge.to.y;
            double angleInRadians = Math.atan2(
                    endy - edge.from.y, endx - edge.from.x);
            endx = (int) (endx - 12 * Math.cos(angleInRadians));
            endy = (int) (
                    endy - 12 * Math.sin(angleInRadians));  // 箭头指向节点图像的边缘而不是圆心
            g2d.draw(new Line2D.Double(
                    edge.from.x, edge.from.y, endx, endy)); // 画直线
            drawArrow(g2d, edge.from.x, edge.from.y, endx, endy);  // 画箭头
        }

        // Draw nodes
        for (Node node : nodes) {
            g2d.setColor(Color.RED);
            g2d.fillOval(node.x - 12, node.y - 12, 24, 24);  // 画圆，代表节点
            g2d.setColor(Color.BLACK);
            g2d.drawString("N" + node.index, node.x - 5, node.y + 5);  // 添加节点标号
            g2d.drawString("N" + node.index + ": " + node.name, 10,
                    (1 + node.index) * 18);  // 添加标号到节点名称映射
        }

        // Draw weights
        g2d.setColor(Color.GREEN);
        g2d.setFont(new Font("Arial", Font.PLAIN, 25));
        for (Edge edge : edges) {
            int midX = (edge.from.x + 2 * edge.to.x) / 3;  // 三分之一处标记权重
            int midY = (edge.from.y + 2 * edge.to.y) / 3;
            if (edge.from.index == edge.to.index) {
                g2d.setColor(Color.ORANGE);
                g2d.drawString(String.valueOf(edge.weight), midX + 10, midY);
                g2d.setColor(Color.GREEN);
            } else {
                g2d.drawString(String.valueOf(edge.weight), midX, midY);
            }
        }
    }

    private void drawArrow(final Graphics2D g2d, final int x1,
                           final int y1, final int x2, final int y2) {
        // 计算两个点的x和y坐标之差
        double dx = x2 - x1;
        double dy = y2 - y1;
        // 计算线段与x轴之间的角度，单位是弧度
        double angle = Math.atan2(dy, dx);
        // 设置箭头的长度
        int len = 15;
        // 设置箭头的宽度
        int width = 7;
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
    /** 将节点坐标分布在一个圆上.
     * @param radius 半径
     * @param numPoints 节点数
     * @param centerX 圆心x坐标
     * @param centerY 圆心y坐标
     * @return 节点坐标
     */
    public static List<Point2D> layoutPointsOnCircle(
            final double radius, final int numPoints,
            final double centerX, final double centerY) {
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



    static class Node {
        /** 节点名称.
         */
        private String name;
        /** 节点x坐标.
         */
        private int x;
        /** 节点y坐标.
         */
        private int y;
        /** 节点索引.
         */
        private int index;

        Node(final String nAME, final int i, final List<Point2D> circle) {
            this.name = nAME;
            this.x = (int) circle.get(i).getX();
            this.y = (int) circle.get(i).getY();
            this.index = i;
        }
    }

    static class Edge {
        /** 起始节点.
         */
        private Node from;
        /** 终止节点.
         */
        private Node to;
        /** 权重.
         */
        private int weight;

        Edge(final Node froM, final Node tO, final int weighT) {
            this.from = froM;
            this.to = tO;
            this.weight = weighT;
        }
    }
}
