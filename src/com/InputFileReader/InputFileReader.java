package com.InputFileReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class InputFileReader {
    // 成员   ///////////////////////////////////
    String filePath = "";

    // 构造器 ///////////////////////////////////
    public InputFileReader(String inputFilePath) {
        filePath = inputFilePath;
    }

    // 工具方法 /////////////////////////////////
    private ArrayList<String> tokenizeString(String input) {
        String processedLine = input.replaceAll("[^A-Za-z]", " ");
        String[] words = processedLine.split("\\s+");
        ArrayList<String> result = new ArrayList<>();
        for(String word : words){
            String t = word.replace(" ", "");
            if(Objects.equals(t, "")) {
                continue;
            } else {
                result.add(word.replace(" ", ""));
            }
        }

        return result;
    }

    // 对外API /////////////////////////////////
    public ArrayList<String> getFileContent() {
        ArrayList<String> result = new ArrayList<>();
        try {
            BufferedReader inStream = new BufferedReader(new FileReader(filePath));
            String line;
            while((line = inStream.readLine()) != null) {
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
