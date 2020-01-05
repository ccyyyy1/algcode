package com.ex.cy.demo4.alg.algthink.dynamic;

import java.util.Arrays;

public class Coins {
    //动态规划特点：
    //1个模型，3个特性，2个方法
    //1个模型，3个特性
    //满足了就可以用动态规划求解
    //
    //1模型：阶段决策最优解模型
    //
    //3特性：
    //1.重复子问题 (不同的决策序列，到某个阶段后，会遇到相同的状态）
    //2.无后效性（问题之间是离散的，表示无依赖性）
    //2.1.只关心解决当前阶段子问题，不需要知道之前的状态是怎么推导出来的
    //2.2.前面解决的问题，不受到后面解决的子问题影响其状态
    //3.最优子结构（一个问题的最优解，包含子问题的最优解，要求这个问题的最优解，就化简成了，求这个问题所有子问题的最优解，如果都求解到了，那么这个问题的最优解也就求解到了）
    //
    //2个方法
    //1.状态表
    //回溯法暴力求解（可以配合’子问题状态的记忆‘来修枝）-> 确定状态（子问题由哪几个主要参数决定） - 画递归树 - 找重复子问题 - 画状态转移表(一般是2维表) - 填状态表 - 按照填写过程的逻辑code (- 在此基础上优化空间复杂度 - 状态方程)
    //2.状态方程（类似递归时候写的 递推公式）
    //找最优子结构 -> 写动态方程 - code

    //1.可以分解成子问题
    //2.不可重复选中（如果可以重复，就用贪心法）
    //3.子问题之间要离散，无关联（不可有依赖性）
    //4.有限制的上限值（如背包重量）
    //
    //对于NPC问题，要能够识别，识别后，确定无动态规划解法，往往会使用贪心算法求一个近似最优解
    //如：集合（集合覆盖），序列（TSP（旅行商））
    //1.要同时考虑所有组合
    //2.无法拆分成子问题（无法分治）

    //KNN  多维向量之间的
    // 分类（编组），预测（回归）
    // 常用的分类方法：
    // 1.欧几里得距离
    // 2.归一化向量后，只考察cos值（角度）
    // 在推荐系统中K值往往取sqrt(n) ,n为用户总数
    // NBC 朴素贝叶斯

    //回溯，动态规划，贪心，分治
    //分类：
    // 1.回溯，动态规划，贪心 （求最优解）
    // 2.分治
    // 最初分析问题的时候他们都可以用递归的编程思想（方法）来求出时间复杂度相对较高的初期解决方案
    //能解决的问题 :回溯>动态规划>贪心
    //时间复杂度   :回溯>动态规划>贪心
    //            2^n n*m


    //1.暴力回溯法求解 O(n^m)
    public static int getCoinCount1(int money, int[] coins, int n) {
//        if (check(money, coins, n))
//            return -1;

        //TODO BUG dead loop
        if (money <= 0)  //0元找0个
            return 0;
        int count = 0;
        for (int i = 0; i < n; i++) {
            if (money < coins[i]) //找不开
                continue;
            int tmpCount = getCoinCount1(money - coins[i], coins, n);
            if (tmpCount != 0 && (count > tmpCount || count == 0)) {
                count = tmpCount + 1;
            }
        }
        return count;
    }

    //2.用动态规划找给定面值的硬币（最少硬币枚数）
    //返回-1 表示找不开
    public static int getCoinCount(int money, int[] coins, int n) {
        if (check(money, coins, n))
            return -1;

        int[] state = new int[money + 1];       //下标：[i]=剩余需要找i元, 值：需要找的硬币枚数
        int[] coinValues = new int[money + 1];  //下标：i剩余需要找i元，值：需要找的每个硬币的币值
        for (int i = 0; i < money + 1; i++)
            state[i] = -1;
        state[money] = 0;
        for (int i = money; i >= 0; i--) {
            if (state[i] == -1)
                continue; //当前状态无效(没有任何一种找法，可以得到剩余i元的情况)
            for (int j = 0; j < n; j++) {
                int toMoney = i - coins[j];
                if (toMoney < 0)
                    continue; //找过头了
                int coinCount = state[i] + 1;
                if (state[toMoney] == -1 || state[toMoney] > coinCount) {
                    state[toMoney] = coinCount;
                    coinValues[toMoney] = coins[j];
                    if (toMoney == 0) {
                        System.out.println("toMoney==0  " + coinCount);
                        //其实，可以break出最外层循环了，以最少的硬币枚次找到了合适的钱
                    }
                }
            }
        }
        if (coinValues[0] != 0) {
            int i = 0;
            while (coinValues[i] != 0) {
                System.out.println("coinValue(面值): " + coinValues[i]);
                i += coinValues[i];
                if (i > money - 1)
                    break;
            }
        }
        return state[0];
    }

    private static boolean check(int money, int[] coins, int n) {
        if (money < 0)
            return true;
        if (coins == null)
            return true;
        if (n <= 0)
            return true;
        for (int i = 0; i < n; i++) {
            if (coins[i] <= 0)
                return true;
        }
        return false;
    }

    public static void main(String[] ar) {
        int money = 11;
        int[] coins = new int[]{2, 5, 10, 50};
        System.out.println("币值有 " + Arrays.toString(coins));

        int co = 0;
        //TODO 暴力回溯法有bug
//        co = getCoinCount1(money, coins, coins.length);
//        System.out.println("getCoinCount1(): " + co);
//        System.out.println("=======");

        System.out.println("\n======= 找钱 " + money);
        co = getCoinCount(money, coins, coins.length);
        System.out.println("硬币枚数: " + co);

        money = 167;
        System.out.println("\n======= 找钱 " + money);
        co = getCoinCount(money, coins, coins.length);
        System.out.println("硬币枚数: " + co);

        money = 3;
        System.out.println("\n======= 找钱 " + money);
        co = getCoinCount(money, coins, coins.length);
        System.out.println("硬币枚数: " + co);
    }
}
