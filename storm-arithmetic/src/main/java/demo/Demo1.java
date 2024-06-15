package demo;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 *
 */
public class Demo1 {

    public static void main(String[] args) {

        String unti = "4";
        String[] split = unti.split(";");
        List<String> list = Arrays.asList(split);
        System.out.println(list);
    }
}
