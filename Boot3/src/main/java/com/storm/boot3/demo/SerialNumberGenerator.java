package com.storm.boot3.demo;

import cn.hutool.core.util.StrUtil;

import java.util.ArrayList;
import java.util.List;

public class SerialNumberGenerator {

    public static String incrementUntilUnique(String input, List<String> existingValues) {
        if (input == null || input.length() != 6) {
            throw new IllegalArgumentException("Input must be a six-character string composed of digits and uppercase letters.");
        }
        if (existingValues == null) {
            throw new IllegalArgumentException("Existing values set must not be null.");
        }

        char[] chars = input.toCharArray();
        boolean uniqueFound = false;
        String newString = "";
        while (!uniqueFound) {
            int i = 5;
            boolean incremented = false;

            while (i >= 0) {
                char currentChar = chars[i];

                if (currentChar >= '0' && currentChar <= '9') {
                    if (currentChar < '9') {
                        chars[i] = (char) (currentChar + 1);
                        incremented = true;
                    } else {
                        chars[i] = 'A'; // 9变为A
                        incremented = true; // 关键修改：设置incremented为true以停止进位
                    }
                } else if (currentChar >= 'A' && currentChar <= 'Z') {
                    if (currentChar < 'Z') {
                        chars[i] = (char) (currentChar + 1);
                        incremented = true;
                    } else {
                        if (i == 0) {
                            chars[i] = 'A'; // 首位Z重置为A
                        } else {
                            chars[i] = '0'; // 非首位Z重置为0
                        }
                    }
                } else {
                    throw new IllegalStateException("Invalid character in input string: " + currentChar);
                }

                if (incremented) {
                    break;
                }

                i--;
            }

            // 处理所有位都进位的情况（例如ZZZZZZ -> A00000）
            if (!incremented && i == -1) {
                for (int j = 1; j < 6; j++) {
                    chars[j] = '0';
                }
                chars[0] = 'A';
            }

            newString = new String(chars);
            if (!existingValues.contains(newString)) {
                uniqueFound = true;
            }
        }

        return newString;
    }

    // Example usage and test
    public static void main(String[] args) {
        String newSerialNumber = "AAAAAA";
        for (int i = 0; i < 10; i++) {
            List<String> existingValues = new ArrayList<>();
            newSerialNumber = incrementUntilUnique(newSerialNumber, existingValues);
            System.out.println("New Serial Number: " + newSerialNumber);
        }
    }
}