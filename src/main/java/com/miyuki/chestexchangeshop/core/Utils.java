package com.miyuki.chestexchangeshop.core;

public class Utils {
    /**
     * Generate random ID
     * @return string
     */
    public static String getlinkNo() {

        String linkNo = "";
        String model = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        char[] m = model.toCharArray();

        for (int j = 0; j < 6; j++) {

            char c = m[(int) (Math.random() * 36)];

            // Make sure there are no duplicate characters

            if (linkNo.contains(String.valueOf(c))) {

                j--;

                continue;

            }

            linkNo = linkNo + c;

        }

        return linkNo;

    }
}
