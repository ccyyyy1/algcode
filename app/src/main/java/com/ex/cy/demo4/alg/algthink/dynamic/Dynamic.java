package com.ex.cy.demo4.alg.algthink.dynamic;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Dynamic {
    //购物凑单，满200减50，给定n个商品，单价v[]，如何凑出最多的200元的单子?

    //一个订单
    public static class Sheet {
        public List<Integer> goods;

        public Sheet(Integer... goods) {
            this.goods = new LinkedList<>();
            for (int g : goods)
                this.goods.add(g);
        }

        public Sheet(Iterable<Integer> integers) {
            this.goods = new LinkedList<>();
            for (int g : integers)
                this.goods.add(g);
        }

        public int totalPrice() {
            int sum = 0;
            for (int i : goods)
                sum += i;
            return sum;
        }

        @Override
        public String toString() {
            return "Sheet{" +
                    "goods=" + goods +
                    ", totalPrice= " + totalPrice() +
                    '}';
        }
    }

    public static List<Sheet> buy200sub50(List<Integer> goods) {
        int bagMaxV = 200;
        List<Sheet> sheets = new LinkedList<>();
        if (goods == null)
            return sheets;

        //去掉大于等于200的，单独成为一个订单
        for (int i = 0; i < goods.size(); ) {
            if (goods.get(i) >= bagMaxV) {
                sheets.add(new Sheet(goods.get(i)));
                goods.remove(i);
                continue;
            }
            i++;
        }
        Collections.sort(goods, new Comparator<Integer>() {
            @Override
            public int compare(Integer lhs, Integer rhs) {
                return rhs - lhs;
            }
        });

        while (goods.size() > 0) {
            List<Integer> indexs = b2(goods, bagMaxV);
            List<Integer> chosedGoods = new LinkedList<Integer>();
            for (int i = 0; i < indexs.size(); i++) {       //从goods的高下标向低下标删除,不用做下标删除后的偏移
                chosedGoods.add(goods.get(indexs.get(i)));  //找到物品单价
                goods.remove((int) indexs.get(i));
            }
            sheets.add(new Sheet(chosedGoods));
        }

        return sheets;
    }

    /**
     * 输入的单个物品价值 x < maxW (不包含maxW)
     * 时间复杂度 O(n*w)
     *
     * @param goods price of goods
     * @return list of index
     */

    //应该有2级动态规划
    //上层 单数层  （多种凑出订单数量w的方案（一个方案包含多个200元单独的订单方案））state[w] w是单数，有很多种凑出1单>=200的情况，后面的凑单方案要基于前面的（减去那些已被凑单的商品，在此基础上再凑单），做一个穷举，记录每一种凑单到剩余0个商品时，凑到的订单数量，全部穷举完后，找到订单数量最多的那种，查看记录或倒推，得到该种凑单数量最多的方案下，每个单独的200元订单的每个商品的选购状况
    //底层 凑200层 (多种凑出200元单的方案)把凑单价格>= 200的都输出给上层（list<sheet>),上层根据其中一种方案，从待选商品中，去掉，把剩下的商品再次用b2凑单
    //这个过程好像是一颗3维的递归树，一次返回一堆可能的200元订单，每次深入的时候，基于其中一种情况作为基础
    private static List<Integer> b2(List<Integer> goods, int maxW) { //200个1元200个199元； 先选一个大的，再选一个小的，应该有这种思想的会是比较好的吧？，可能还要吧订单数量的多少给规划进去.
        List<Integer> indexs = new LinkedList<>();
        //199*2 = 398 （因为输入的商品最大的单价是199，所以考虑超出200元时，最多2个199的商品，=398元，
        int doubleMaxW = ((maxW - 1) << 1);
        int[] p2i = new int[doubleMaxW]; //[price] to goods index
        for (int i = 0; i < doubleMaxW; i++)
            p2i[i] = -1;

        p2i[goods.get(0)] = 0; //记录在此价格下，的物品下标
        for (int i = 1; i < goods.size(); i++) {
            for (int j = p2i.length - 1; j >= 0; j--) { //从高价格找向低价格
                if (p2i[j] != -1 || j == 0) { //j==0 表示0元，但是物品下标是-1,表示没有选购任何物品的情况(j==0用于处理任何物品i都可以在此基础上单独选购)
                    int toPrice = goods.get(i) + j;
                    if (toPrice >= doubleMaxW) //超出398元的订单总价，物品个数一定>3个（199为最大物品单价的情况下，那么一定会形成一个至少2个物品，且订单价格低于等于398元的），那么不处理
                        continue;
                    p2i[toPrice] = i;
                }
            }
        }

        //从=200元开始往高价格找，找到第一个最低价的订单为止,如果都为-1，说明输入的物品总价值没有超过200
        int sheetPrice = maxW;
        int goodsIndex = -1;
        for (; sheetPrice < doubleMaxW; sheetPrice++) {
            if (p2i[sheetPrice] != -1) {
                goodsIndex = p2i[sheetPrice];
                break;
            }
        }

        if (goodsIndex == -1) {
            System.out.println("总价值未超过 " + maxW);
            for (int i = goods.size() - 1; i >= 0; i--) {
                indexs.add(i);
            }
        } else {
            //顺着形成>=200元的最低价订单的最后一个物品index，反向找到形成该订单的所有物品Index，并加入indexs集合
            System.out.println("总价值超过 " + maxW + ", =" + sheetPrice);
            while (goodsIndex != -1 && sheetPrice > 0) {
                indexs.add(goodsIndex);
                int prvSheetPrice = sheetPrice - goods.get(goodsIndex); //当前订单价格 - 形成当前价格的最后一个商品价格 = 上一个商品价格
                goodsIndex = p2i[prvSheetPrice];
                sheetPrice = prvSheetPrice;
                System.out.println("清点商品 " + sheetPrice);
            }
        }
        return indexs;
    }

    public static void main(String[] ar) {
        Random r = new Random(1234);
        List<Integer> goods = new LinkedList<>();
        for (int i = 0; i < 20; i++) {
            goods.add(r.nextInt(300));
        }
        System.out.println("goods " + goods);


        //TODO 有bug，觉得有问题。。可能不是最优解,单数最多的最优解，不一定全部都是单个单子里最近接200元的组成的
        List<Sheet> sheets = buy200sub50(goods);
        for (Sheet s : sheets) {
            System.out.println(s);
        }
    }
}
