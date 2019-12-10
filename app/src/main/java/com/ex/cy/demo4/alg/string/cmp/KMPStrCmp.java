package com.ex.cy.demo4.alg.string.cmp;

import java.util.Arrays;

//只有好前缀规则
//next[i]=k   i是前缀子串长度，k是与当前前缀子串能够匹配的字符数最多的 前缀后缀子串 的 最高位下标值
public class KMPStrCmp {
    private static int[] KMPInitNextArray(char[] b) {
        int m = b.length;
        int[] next = new int[m];
        next[0] = -1;
        int k = -1;

        for (int i = 1; i < m; i++) {
            while (k != -1 && b[i] != b[k + 1])
                k = next[k];    //右移（缩短）用于匹配当前前缀子串的前缀子串（从最位开始向左切短）
            if (b[i] == b[k + 1])
                k++;
            next[i] = k;
        }
        return next;
    }

    public static int KMP(char[] a, char[] b) {
        int[] next = KMPInitNextArray(b);
        int j = 0;
        for (int i = 0; i < a.length; i++) {
            while(j>0 && a[i] != b[j])
                j = next[j-1]+1;    //? 当前第j个不匹配时， 将当前位置j，回溯到 前一个匹配者j-1 的最大匹配下标的下一个字符作为j的指向，再次比对
            if (a[i] == b[j])
                j++;
            if (j == b.length)
                return i - b.length + 1;
        }
        return -1;
    }


    public static void main(String[] args) {
        char[] a= "ababababacabacab".toCharArray();
        char[] b = "ababacab".toCharArray();
        int[] next = KMPInitNextArray(b);
        System.out.println(Arrays.toString(next));
        next = KMPInitNextArray("ababacababacabababababab".toCharArray());
        System.out.println(Arrays.toString(next));
        System.out.println(KMP(a,b));
    }
}
