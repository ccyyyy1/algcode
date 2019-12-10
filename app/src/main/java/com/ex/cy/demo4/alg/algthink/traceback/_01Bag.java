package com.ex.cy.demo4.alg.algthink.traceback;

import java.util.Set;
import java.util.TreeSet;

//01背包问题
//给最大限重，求可能的最高重量（不可重复选物品，和可重复选物品）
//给最大限重和物品价值，求可能的最高总价值（不可重复选物品，和可重复选物品）
//方法：
//1. 回溯法 O(2^n) 空O(1)
//2. 动态规划法
//2.1 O(n*w) 空O(n*w)  n是物品个数，w是背包最大重量
//2.2 O(n*w) 空O(w)
public class _01Bag {
    //以回溯法，求给定物品goods[] ，goodsNum = n，最大背包重量 bagMax的情况下，最多可以放几个物品
    //O（2^n） ，空间 1
    public static void putGoodsGetBagMaxW(int[] goods, int goodsNum, int currentGoodsIndex, int bagMax, int currentKG, Set<Integer> chosed) {
        if (currentGoodsIndex >= goodsNum) {
            System.out.println("currentGoodsIndex >= goodsNum : " + currentKG + " chosed " + chosed);
            return;
        }
        if (currentKG + goods[currentGoodsIndex] > bagMax) {
            System.out.println("currentKG > bagMax : x " + (currentKG + goods[currentGoodsIndex]) + " : currentBagMaxWeight " + (currentKG) + " chosed " + chosed);
            return;
        }
        putGoodsGetBagMaxW(goods, goodsNum, currentGoodsIndex + 1, bagMax, currentKG, chosed);
        chosed.add(currentGoodsIndex);
        putGoodsGetBagMaxW(goods, goodsNum, currentGoodsIndex + 1, bagMax, currentKG + goods[currentGoodsIndex], chosed);
        chosed.remove(currentGoodsIndex);
    }

    //动态规划法
    public static int putGoodsGetBagMaxW(int[] weight, int n, int maxW) {
        boolean[] state = new boolean[maxW + 1];
        state[0] = true;                //不选第一个物品
        state[weight[0]] = true;        //选第一个物品
        int bagMaxW = 0;
        for (int i = 1; i < n; i++) {   //对每个物品决策
            for (int j = maxW - weight[i]; j >= 0; j--) {
                if (state[j])
                    state[j + weight[i]] = true;
                bagMaxW = bagMaxW > j + weight[i] ? bagMaxW : j + weight[i];
            }
        }
        return bagMaxW;
    }

    //以动态规划法 （基于状态表的决策机），求给出物品n个，和对应重量，单价，背包重量上限时，最高总价值是多少
    //每个物品最多只能选一次
    //参数：物品重量，物品个数，背包最大限重，物品价值，价值-物品集map(该价值由哪几个物品组成)
    public static int putGoodsGetMaxValue(int[] weight, int n, int maxW, int[] vals) {
        if (maxW <= 0 | n <= 0 | weight == null || vals == null || weight.length != vals.length)
            return -1;

        int[] w2vState = new int[maxW + 1];                    //下标=重量组合，值=在这种重量组合下，在背包内的物品总价值
        int maxV = -1;
        for (int i = 0; i < maxW + 1; i++)
            w2vState[i] = -1;                                  //表示不存在当前组合,以及该组合下的物品价值

        w2vState[0] = 0;                                       //不放任何东西（不放入第一个物品）
        w2vState[weight[0]] = vals[0];                         //放入第一个物品
        for (int i = 1; i < n; i++) {                          //对每个物品决策
            for (int j = maxW - weight[i]; j >= 0; j--) {
                //对当前第i个物品决策，那么在可以放下该物品的前提下，
                //对每个前继状态进行加入该物品重量后(A = 当前考察的前继状态下标（之前背包中的重量状态）+weight[i])
                //的总价值计算(state[A]=state[当前考察的前继状态下标] + value[i])
                //j从高重量遍历到低重量，避免重复计算物品i的重量和价值
                if (w2vState[j] != -1) {                       //考察放入物品i后，背包重量的增加，以及该重量下，物品总价值的增加
                    int tmpv = w2vState[j] + vals[i];          //对当前重量j时的价值，加上第i个物品价值后的价值
//                    tmpMaxV = tmpMaxV > w2vState[j] ? tmpMaxV : w2vState[j];
//                    System.out.println("j+i=" + j + "+" + weight[i] + "(" + i + ") " + w2vState[j] + "+" + vals[i] + "=" + tmpv);
                    w2vState[j + weight[i]] = w2vState[j + weight[i]] > tmpv ? w2vState[j + weight[i]] : tmpv;        //保留此重量时，最高价值的情况
//                    System.out.println("tmpv " + tmpv);
                    maxV = maxV > tmpv ? maxV : tmpv;
                }
            }
        }

        for (int i = 0; i < maxW + 1; i++)
            System.out.println("W:[" + i + "] V:" + w2vState[i]);
        return maxV;
    }

//    //如果物品可以重复选择呢
//    public static int getGoodsMaxVRepit(int[] weight, int n, int maxW, int[] vals) {
//        int[] w2vState = new int[maxW + 1];
//        for (int i = 0; i < w2vState.length; i++)
//            w2vState[i] = -1;
//        w2vState[0]=0;
//        w2vState[weight[0]]=vals[0];
//        //2级动态规划？，基于重复物品价值的 递归树？(动态规划套动态规划)
//        for(int i = 1; i < n;i++){
//            //2重循环？不够。。。并不是一个物品只能选2次，3重。。。。n重，直到背包被装满为止
//        }
//    }

    //用递归，回溯法做一次
    public static int getGoodsMaxWRepit(int[] weight, int n, int maxW, int[] vals, int cw, int ci) {
        if (cw + weight[ci] > maxW)
            return cw;
        else if (cw + weight[ci] == maxW)
            return cw + weight[ci];

        int noww = cw + weight[ci];
        int cmaxw = 0;
        for (int i = 0; i < n; i++) //对下一个物品做左右可选项的组合
        {
            int tmpcmaxw = getGoodsMaxWRepit(weight, n, maxW, vals, noww, i);
            if (tmpcmaxw > cmaxw)//子问题，子组合返回的重量组合
                cmaxw = tmpcmaxw;
        }
        int resultw = cmaxw > noww ? cmaxw : noww;
        return resultw;     // 子问题中最大重量 = 当前问题的所有可能的组合中最大的重量
    }

    //用递归，回溯法做一次
    //如果物品可以重复选择，求最高装满背包的情况下，最高总价值
    public static int getGoodsMaxVRepit2(int[] weight, int n, int maxW, int[] vals, int cw, int cv) {
        int maxV = 0;
        int tmpV = 0;
        for (int i = 0; i < n; i++) {
            tmpV = getGoodsMaxVRepit_2(weight, weight.length, maxW, vals, cw, i, cv);
            if (tmpV > maxV)
                maxV = tmpV;
        }
        return maxV;
    }

    public static int getGoodsMaxVRepit_2(int[] weight, int n, int maxW, int[] vals, int cw, int ci, int cv) {
        int noww = cw + weight[ci];  //放入当前物品后的背包重量
        if (noww > maxW) //超重则返回超重前价值
            return cv;
        else if (noww == maxW) //刚好放满，则返回加入该物品的价值后的总价值
            return cv + vals[ci];

        int nowv = cv + vals[ci];

        int cmaxv = 0;  //当前子问题中返回的所有价值中的最大价值
        for (int i = 0; i < n; i++) //对下一个物品做左右可选项的组合
        {
            int tmpcmaxv = getGoodsMaxVRepit_2(weight, n, maxW, vals, noww, i, nowv);
            if (tmpcmaxv > cmaxv)//子问题，子组合返回的价值
                cmaxv = tmpcmaxv;
        }
        int resultv = cv > cmaxv ? cv : cmaxv;
        return resultv;     //子问题中最大价值 = 当前问题的所有可能的组合中最大的价值
    }

    public static void main(String[] ar) {
        //demo1
        int[] weight = new int[]{7, 4, 8, 3, 5, 9, 6};
        int[] vals = new int[]{17, 16, 15, 14, 13, 12, 11};
        Set<Integer> chosed = new TreeSet<>();
        putGoodsGetBagMaxW(weight, weight.length, 0, 10, 0, chosed);
        System.out.println("putGoodsGetBagMaxW(weight, weight.length, 10); " + putGoodsGetBagMaxW(weight, weight.length, 10));

        //demo2
        int maxV = putGoodsGetMaxValue(weight, weight.length, 10, vals);
        System.out.println(maxV);

        int maxW3 = getGoodsMaxWRepit(weight, weight.length, 10, null, 0, 0);
        System.out.println("maxW3 " + maxW3);

        int maxV3 = getGoodsMaxVRepit2(weight, weight.length, 10, vals, 0, 0);
        System.out.println(maxV3);
        System.out.println("maxV3 " + maxV3);
    }

}
