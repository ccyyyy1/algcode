package com.ex.cy.demo4.alg.string;

public class BMStrCmp {

    private static int[] BMInitBadChar(char[] b) {
        int CHAR_SPACE = 256; //换成基于统计的,散列表吧,否则UNICODE的话，太多空白项，数组会变得稀疏，浪费太多空间
        int[] bc = new int[CHAR_SPACE];
        for (int i = 0; i < bc.length; i++)
            bc[i] = -1;
        for (int i = 0; i < b.length; i++)
            bc[b[i]] = i;
        return bc;
    }

    static boolean[] prefix;
    static int[] surffix;

    private static void BMInitGoodSuffix(char[] b) {
        int m = b.length;
        surffix = new int[m + 1];
        prefix = new boolean[m + 1];
        int k;
        int j;
        for (int i = 0; i < m - 1; i++) {//[0123]4
            j = i;
            k = 0;
            while (j <= 0 && b[m - 1 - k] == b[j]) {
                k++;
                surffix[k] = j;
                j--;
            }
            if (j < 0)
                prefix[k] = true;
        }
    }

    public static int BM(char[] a, char[] b) {
        int[] bc = BMInitBadChar(b);
        BMInitBadChar(b);
        int n = a.length;
        int m = b.length;

        int i = 0;
        int j;
        for (; i <= n - m; i++) {
            for (j = m - 1; j >= 0; j--) {
                if (b[j] != a[i + j])
                    break;
            }
            if (j < 0)
                return i;
            int badSeek = j - bc[a[i + j]];
            int goodSeek = 0;
            if (j < m - 1) {
                goodSeek = findGoodSeek(m, j);
            }
            i = i + Math.max(badSeek, goodSeek);
        }
        return -1;
    }

    //HARD
    private static int findGoodSeek(int m, int j) {
        int k = m - 1 - j;
        int goodSeek = m;
        if (surffix[k] != -1)
            goodSeek = j - surffix[k] + 1;
        else {
            for (int r = j + 2; r < m; r++) {
                if (prefix[m - r]) {
                    goodSeek = r;
                    break;
                }
            }
        }
        return goodSeek;
    }
}
