package com.ex.cy.demo4.alg;

public class MathFormula {
    public static float dbsum(float a1, float q, int n) {
        return (float) (a1 * (1 - Math.pow(q, n)) / (1 - q));
    }

    public static void main(String[] args) {
//        System.out.println(dbsum(0.5f, 0.5f, 6));
//        long st = System.currentTimeMillis();
//        for (int i = 0; i < 10000000; i++) {
//            Math.sqrt(2);
//        }
//        long st2 = System.currentTimeMillis();
//        System.out.println("time1: " + (st2 - st));
//
//        st = System.currentTimeMillis();
//        for (int i = 0; i < 10000000; i++) {
//            sqrt(2);
//        }
//        st2 = System.currentTimeMillis();
//        System.out.println("time2: " + (st2 - st));
//
//        st = System.currentTimeMillis();
//        for (int i = 0; i < 10000000; i++) {
//            sqrt2(2);
//        }
//        st2 = System.currentTimeMillis();
//        System.out.println("time3: " + (st2 - st));


//        System.out.println(InvSqrt(2));
        System.out.println(Math.sqrt(2));
        System.out.println(sqrt(2));
        System.out.println(sqrt2(2));
        System.out.println(nSqrt(2, 2));
    }

    //二分
    public static double sqrt(double x) {
        double l = 0;
        double r = x;
        double m = (l + r) / 2;

//        int count = 0;
        while (Math.abs(m * m - x) > 0.000001) {
            m = (l + r) / 2;
            if (m * m > x)
                r = m;
            else
                l = m;
//            count++;
//            System.out.println(" "+ count + " : " + m + "^2 = " + m*m +" l:"+l + " r:"+r );
        }
//        System.out.println(m*m - x );
        return m;
    }

    //牛迭
    public static double sqrt2(double x) {
        double val = x;
        double last;
        do {
            last = val;
            val = (val + (x / val)) / 2;
//            System.out.println(val);
        } while (Math.abs(val - last) > 0.000001);
        return val;
    }

    //n次方根牛迭
    public static double nSqrt(int a, int n) {
        double v = a;
        double last = 0;
        do {
            last = v;
//            v = (Math.pow(v, next) * (next - 1) + a) / (next * Math.pow(v, next - 1));
            v = ((n - 1) * v + (a / Math.pow(v, n - 1))) / n;
        } while (Math.abs(v - last) > 0.000001);
        return v;
    }

//    public static double InvSqrt(double x) {
//        double xhalf = 0.5f * x;
//        int i = (int) x; // get bits for floating VALUE
//        i = 0x5f375a86 - i; // gives initial guess y0
//        x = i; // convert bits BACK to float
//        x = x * (1.5f - xhalf * x * x); // Newton step, repeating increases accuracy
//        x = x * (1.5f - xhalf * x * x); // Newton step, repeating increases accuracy
//        x = x * (1.5f - xhalf * x * x); // Newton step, repeating increases accuracy
//        return 1 / x;
//    }
}
