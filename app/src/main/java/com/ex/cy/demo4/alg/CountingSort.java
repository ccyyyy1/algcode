package com.ex.cy.demo4.alg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

//计数排序 O(n)
public class CountingSort {
    static class Node {
        int k;
        int v;

        public Node(int k, int v) {
            this.k = k;
            this.v = v;
        }
    }

    static int seed = 123;
    static Random r;

    static List<Node> randInit(int size) {
        List<Node> l = new ArrayList<>();
        r = new Random(seed);

        List<Integer> sourcePool = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            sourcePool.add(i);
        }
        for (int i = 0; i < size; i++) {
            int k = sourcePool.remove(r.nextInt(sourcePool.size()));
            l.add(new Node(k, k));
        }
        return l;
    }

    static List<Node> randInitRepit(int size, int maxk) {
        List<Node> l = new ArrayList<>();
        r = new Random(seed);

        for (int i = 0; i < size; i++) {
            int k = r.nextInt(maxk);
            l.add(new Node(k, i));
        }
        return l;
    }

    //time: O(next)
    //space: O(next)
    //stabilize: Y*   ,can modify, default: reversed position
    static Node[] countingSort(Node[] a, int maxk) {
        int[] c = new int[maxk];
        //1. scan a[] (count key's value num)
        for (int i = 0; i < a.length; i++) {
            c[a[i].k]++;
        }

        //2.add c[] (get insert index in r[])   //计算该类数值在r[]中的，属于这个数值区段的最高位索引号
        int lastV = 0;                          //先在a[]中扫到，则先插入到r[]中同数值区域中高索引号位置
        for (int i = 0; i < c.length; i++) {    //r[] 中 从右向左插入
            lastV = lastV + c[i];
            c[i] = lastV;
        }

        //3. scan a[] to r[] (sort)
        Node[] r = new Node[a.length];              //1. i=len->0 稳定
        for (int i = a.length - 1; i >= 0; i--) {   //2. i=0->len 逆稳定序（不稳定）(换成： for (int i = 0; i < a.length; i++) 的话)
            int index = c[a[i].k] - 1;
            c[a[i].k]--;                        //插入后，将对应数值索引号-1，下一次扫到直接按此位置插入r[]
            r[index] = a[i];
        }
        return r;
    }

    public static void main(String[] args) {
        CountingSort cs = new CountingSort();
        int maxk = 5;
        int size = 9;

        long initst = System.currentTimeMillis();
        List<Node> list = cs.randInitRepit(size, maxk);
        Node[] a = new Node[list.size()];
        list.toArray(a);
        for (Node n : a) {
            System.out.print(n.k + "(" + n.v + ") ");
        }
        long initet = System.currentTimeMillis();
        System.out.println("init End , time:" + (initet - initst));

        long st = System.currentTimeMillis();
        Node[] r = countingSort(a, maxk);
        long se = System.currentTimeMillis();
        for (Node n : r) {
            System.out.print(n.k + "(" + n.v + ") ");
        }
        System.out.println("sort End , time:" + (se - st) + " size:" + size);

//        Collections.sort();
        Arrays.sort(new int[2]);
        //DualPivotQuicksort
        //ComparableTimSort
        //TimSort
    }
}
