package Lesson_6;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Homework6 {
//    public static void main(String[] args) {
//        //int[] myArr = {1, 4, 5, 1, 1};
//        //System.out.println(Arrays.toString(arrayCutAfterLastFour(myArr)));
//        //System.out.println(arrayOnlyFourAndOne(myArr));
//
//    }

    public int[] arrayCutAfterLastFour(int[] inArr) {
        boolean statusFour = false;
        int indexLastFour = 0;
        if (inArr.length > 0) {
            for (int i = inArr.length - 1; i >= 0; i--) {
                if (inArr[i] == 4) {
                    indexLastFour = i;
                    statusFour = true;
                    break;
                }
            }
            if (statusFour && (indexLastFour != inArr.length - 1)) {
                return Arrays.copyOfRange(inArr, indexLastFour + 1, inArr.length);
            } else if (statusFour) {
                return new int[0];
            } else {
                throw new RuntimeException("Во входящем массиве нет 4!");
            }
        }
        return new int[0];
    }

    public boolean arrayOnlyFourAndOne(int[] inArr) {
        boolean statusFour = false;
        boolean statusOne = false;
        for (int arg : inArr) {
            switch (arg) {
                case 1 -> statusOne = true;
                case 4 -> statusFour = true;
                default -> {
                    return false;
                }
            }
        }
        return (statusFour && statusOne);
    }

}
