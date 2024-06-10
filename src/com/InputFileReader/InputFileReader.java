package com.InputFileReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class InputFileReader {
    /**
     * 该类用于读取文件内容
     * 1. 读取文件内容
     * 2. 将文件内容转换为单词列表
     * 3. 返回单词列表
     */
    private String filePath = "";

    /** 构造函数.
     * @param inputFilePath 文件路径
     */
    public InputFileReader(final String inputFilePath) {
        filePath = inputFilePath;
    }

    // 工具方法 /////////////////////////////////
    private ArrayList<String> tokenizeString(final String input) {
        String processedLine = input.replaceAll("[,?.;!]", " ");
        processedLine = processedLine.replaceAll("[^a-zA-Z\\s]", "");
        String[] words = processedLine.split("\\s+");
        ArrayList<String> result = new ArrayList<>();
        for (String word : words) {
            String t = word.replace(" ", "");
            if (Objects.equals(t, "")) {
                continue;
            } else {
                result.add(word.replace(" ", ""));
            }
        }

        return result;
    }

    /** 读取文件内容.
     * @return 文件内容
     */
    public ArrayList<String> getFileContent() {
        ArrayList<String> result = new ArrayList<>();
        try {
            BufferedReader inStream = new BufferedReader(
                    new FileReader(filePath));
            String line;
            while ((line = inStream.readLine()) != null) {
                result.addAll(tokenizeString(line.toLowerCase()));
            }
        } catch (IOException e) {
            System.out.println("Failed to read the file.");
            return null;
        }
        System.out.println(result);
        return result;
    }
}
