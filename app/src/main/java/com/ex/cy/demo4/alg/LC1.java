package com.ex.cy.demo4.alg;

import java.util.ArrayList;
import java.util.List;

//letcode 题目
public class LC1 {
    static class Solution {
        public List<String> letterCombinations(String digits) {
            List<String> bottomList;
            List<String> returnList = new ArrayList<String>();
            if (digits.length() == 1) {
                return d2l(digits);
            } else {
                char curChar = digits.charAt(digits.length() - 1);
                bottomList = letterCombinations(digits.substring(0, digits.length() - 1));
                List<String> curl = d2l(curChar + "");
                for (int i = 0; i < bottomList.size(); i++) {
                    for (int j = 0; j < curl.size(); j++) {
                        returnList.add(bottomList.get(i) + curl.get(j));
                    }
                }
            }
            return returnList;
        }

        public List<String> d2l(String digits) { //TODO static List , don't wanna write idiot "abc" ...
            List<String> l = new ArrayList<String>();
            int n = Integer.parseInt(digits);
            l.add((char) (97 + (n - 2) * 3) + "");
            l.add((char) (97 + (n - 2) * 3 + 1) + "");
            l.add((char) (97 + (n - 2) * 3 + 2) + "");
            if (n == 9) {
                l.add((char) (97 + (n - 2) * 3 + 3) + "");
            }
            return l;
        }
    }

    public static void main(String[] s) {
        Solution so = new Solution();
        System.out.println(so.letterCombinations("234"));

        int a = -3;
        int b = 3;
        System.out.println(a/b);
        System.out.println(a%b);
    }
}
