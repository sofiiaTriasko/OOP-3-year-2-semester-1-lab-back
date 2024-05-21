package com.example.semester_6_oop_lab_1_back.Util;

import java.util.Random;

public class Utils {
    private final static Random random = new Random();

    public static Integer randomId() {
        return random.nextInt(Integer.MAX_VALUE);
    }
}
