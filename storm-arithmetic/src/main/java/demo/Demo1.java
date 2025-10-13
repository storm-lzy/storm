package demo;


import java.util.*;

/**
 *
 */
public class Demo1 {

    public static void main(String[] args) {

        List<OrderPO> list = new ArrayList<OrderPO>();

        OrderPO  orderPO = new OrderPO();
        orderPO.setId(1L);
        list.add(orderPO);

        OrderPO  orderPO1 = new OrderPO();
        orderPO1.setId(1L);
        list.add(orderPO1);


        OrderPO canMergeOrder = list.stream().min(Comparator.comparing(OrderPO::getId)).get();

        System.out.println(canMergeOrder);

    }


    /**
     * 将int类型数字转换为32位二进制字符串，每8位用空格分隔
     * @param number 要转换的整数
     * @return 格式化后的二进制字符串
     */
    public static String formatIntToBinary(int number) {
        StringBuilder binaryStr = new StringBuilder();

        // 从最高位(第31位)开始处理到最低位(第0位)
        for (int i = 0; i < 32; i++) {
            // 获取当前位的值(0或1)
            // 右移(31 - i)位，再与1进行与运算
            int bit = (number >> (31 - i)) & 1;
            binaryStr.append(bit);

            // 每8位添加一个空格(最后一组除外)
            if ((i + 1) % 8 == 0 && i != 31) {
                binaryStr.append(" ");
            }
        }

        return binaryStr.toString();
    }


}
