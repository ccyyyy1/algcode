package com.ex.cy.demo4.alg.string.cmp;

public class RKStrCmp {
    //在A串中 从一个起始位置i，选取 A[i]~A[i+m] 共m个字符，计算其hash值(h1)  i+m 不超过A的最大长度，也就是 i = [0,n-m] 范围
    //hash算法为 字符的取值个数，如只考虑a-z，共26个字母，算作R进制（26进制），那么一个3个字符的字符串的hash为 a1*R^2 + a2*R^1 + a3*R^0  (a1为高位，a3为低位)
    //随着 i自增1，向后移动一个起始位置后，还是做同样的hash计算(h2)
    //此时发现有规律 h2=（h1-a1*R^m-1）* R + a4*R^0
    //所以把A扫一遍计算 hash，在对每次算到的hash和 hashB 比较，一致说明有字符串匹配，如果考虑hash碰撞，则再次挨个比较字符即可（BF算法）
    //时间复杂度 O(n)
    public static int RK(char[] a, char[] b) {
        int m = b.length;
        RKinit(m);
        long hashB = RKHash(b, 0, m);
        long[] hashA = new long[a.length - b.length + 1];

        hashA[0] = RKHash(a, 0, m);
        if (hashA[0] == hashB)
            return 0;
        for (int i = 1; i < hashA.length; i++) {
            hashA[i] = (hashA[i - 1] - (a[i - 1] * RpowN[m - 1])) * R + a[i + m - 1];
            if (hashA[i] == hashB)
                return i;
        }
        return -1;
    }

    static final int R = 256; //256进制 ASCII码表范围
    static long[] RpowN = new long[4];
    static boolean inited;

    private static void RKinit(int blen) {  //初始化R的N次方的缓存表
        long[] tmp;
        int startI = 2;
        if (blen > RpowN.length) {
            tmp = new long[blen];
            startI = RpowN.length;
            RpowN = tmp;
            System.arraycopy(RpowN, 0, tmp, 0, RpowN.length);
            inited = false;
        }
        if (inited == false) {
            RpowN[0] = 1;
            RpowN[1] = R;
            for (int i = startI; i < RpowN.length; i++)
                RpowN[i] = (long) Math.pow(R, i);
            inited = true;
        }
    }

    public static long RKHash(char[] a, int off, int len) {
        long hashR = 0;
        for (int i = 0; i < len; i++) {        //从高位到低位 a1*R^len + a2*R^len-1 + ....alen*R^0
            hashR += RpowN[len - i - 1] * a[off + i];
        }
        return hashR;
    }

    public static void main(String[] args) {
        char[] a = "abcdefg".toCharArray();
        char[] b = "def".toCharArray();
        System.out.println("RK(a, b) " + RK(a, b));
    }
}
