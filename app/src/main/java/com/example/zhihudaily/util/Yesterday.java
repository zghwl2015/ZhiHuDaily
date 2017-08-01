package com.example.zhihudaily.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by hwl on 2017/8/1.
 */

public class Yesterday {

    public static String findYesterday(String today){
        if (today != ""){
            int year = Integer.parseInt(today.substring(0, 4));
            int month = Integer.parseInt(today.substring(4, 6));
            int day = Integer.parseInt(today.substring(6, 8));

            boolean monthFlag = false;
            boolean yearFlag = false;

//            int[] bigMonths = {1, 3, 5, 7, 8, 10, 12};
            List<Integer> bigMonList = new ArrayList<>();
            Collections.addAll(bigMonList, 1, 3, 5, 7, 8, 10, 12);
            if (day == 1){
                monthFlag = true;
                if (bigMonList.contains((month - 1) % 12)){
                    day = 31;
                }else {
                    day = 30;
                }
            }else {
                day--;
            }

            if (monthFlag){
                if (month == 1){
                    month = 12;
                    yearFlag = true;
                }else {
                    month--;
                }
            }

            if (yearFlag){
                year--;
            }

            return year + asString(month) + asString(day);
        }else {
            return "";
        }
    }

    static String asString(int a){

        return a < 10 ? "0" + a : "" + a;
    }



}
