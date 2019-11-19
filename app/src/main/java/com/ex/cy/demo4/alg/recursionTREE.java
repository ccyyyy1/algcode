package com.ex.cy.demo4.alg;

//递归树可用于算法的时间复杂度分析，如带有递推公式的
//是一种算法的时间复杂度分析方式,思路
public class recursionTREE {

    public static void pp(int[] data, int n, int k) {
        if (k == 1) {
            for (int i = 0; i < n; i++) {
                System.out.print(data[i] + " ");
            }
            System.out.println();
        } else {
            for (int i = 0; i < k; i++) {
                int tmp = data[i];
                data[i] = data[k - 1];
                data[k - 1] = tmp;

                pp(data, n, k - 1);

                tmp = data[i];
                data[i] = data[k - 1];
                data[k - 1] = tmp;
            }
        }
    }


    //一开始有1个细胞，每小时分裂1次，3小时后，最后分裂一次，然后死亡
    //多少小时后有多少细胞?
    //时间复杂度是 : O(2^(n/3)) ~ O(2^n)  ,根据递归树分析时间复杂度 最低值 和 最高值
    //f(n) = f(n-1) + f(n-1) - f(n-3)
    //                \-------------/
    //    上一轮分裂的   上一轮死后剩下的
    public static int cell(int hour) {
        if (hour == -2) //1-3
            return 0;
        if (hour == -1)//2-3
            return 0;
        if (hour == 0)
            return 1;
        int lastR = cell(hour - 1);
        int lastRDead = cell(hour - 3);
        System.out.println("lastR : " + lastR);
        System.out.println("lastRDead : " + lastRDead);
        return (2 * lastR) - lastRDead;
        //3 hour : 1.839287
        //4 hour : 1.83928
        //note dead ： 2
    }

    public static void main(String[] args) {
        int[] data = new int[]{1, 2, 3, 4};
        pp(data, 4, 4);

        System.out.println(cell(4)); // 有点不准确   4 hour = 13才对
//        for (int i = 0; i < 24; i++) {
//            int cellN = cell(i);
//            System.out.println(cellN);
//        }
    }
}
