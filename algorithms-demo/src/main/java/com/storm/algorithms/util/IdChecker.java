package com.storm.algorithms.util;

public class IdChecker {

    /**
     * 高效判断ID是否存在于逗号分隔的ID字符串中
     * 时间复杂度：O(n)，n为ID字符串长度
     * 空间复杂度：O(1)，仅使用常数级额外空间
     * 
     * @param idStr 逗号分隔的ID字符串（如"123,456,789"）
     * @param targetId 要检查的目标ID
     * @return 存在返回true，否则返回false
     */
    public static boolean isIdExists(String idStr, String targetId) {
        // 边界条件处理
        if (idStr == null || idStr.isEmpty()) {
            return false;
        }
        if (targetId == null || targetId.isEmpty()) {
            return false;
        }

        int targetLen = targetId.length();
        int strLen = idStr.length();
        
        // 如果目标ID长度大于整个字符串长度，直接返回false
        if (targetLen > strLen) {
            return false;
        }

        int i = 0;
        while (i <= strLen - targetLen) {
            // 检查当前位置是否匹配目标ID
            boolean match = true;
            for (int j = 0; j < targetLen; j++) {
                if (idStr.charAt(i + j) != targetId.charAt(j)) {
                    match = false;
                    break;
                }
            }

            if (match) {
                // 检查是否为独立ID（前后是逗号或字符串边界）
                boolean isStart = (i == 0);
                boolean isEnd = (i + targetLen == strLen);
                
                if (isStart && isEnd) {
                    // 整个字符串就是目标ID
                    return true;
                } else if (isStart) {
                    // 位于开头，后面必须是逗号
                    if (idStr.charAt(i + targetLen) == ',') {
                        return true;
                    }
                } else if (isEnd) {
                    // 位于结尾，前面必须是逗号
                    if (idStr.charAt(i - 1) == ',') {
                        return true;
                    }
                } else {
                    // 位于中间，前后都必须是逗号
                    if (idStr.charAt(i - 1) == ',' && idStr.charAt(i + targetLen) == ',') {
                        return true;
                    }
                }
            }

            // 移动到下一个可能的位置（跳过当前字符或逗号后的位置）
            // 优化：如果当前字符不是数字也不是逗号，可直接跳到下一个逗号
            i++;
            // 快速跳过逗号，减少无效检查
            while (i < strLen && idStr.charAt(i) == ',') {
                i++;
            }
        }

        return false;
    }


}
