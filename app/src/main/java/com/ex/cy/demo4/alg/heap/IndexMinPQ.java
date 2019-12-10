package com.ex.cy.demo4.alg.heap;

import java.util.Arrays;
import java.util.Random;

//索引优先队列 ,类似 带有键值对  key:index value:obj
//二叉堆实现
public class IndexMinPQ<T extends Comparable<T>> {
    int[] pqueue;       //索引优先队列  N->k , 从1开始 ,1为堆顶（最小)
    int[] k2n;          //k->N  ,从1开始，0是哨兵
    T items[];          //k->Item   ，从1开始，0是哨兵
    final int maxN;     //最大索引数
    int N;              //当前数据个数

    public IndexMinPQ(int maxN) {
        this.maxN = maxN;
        pqueue = new int[maxN + 1];
        k2n = new int[maxN + 1];
        items = (T[]) new Comparable[maxN + 1];

        for (int i = 0; i < maxN + 1; i++) {
            k2n[i] = -1;
        }
    }

    public int getMaxN() {
        return maxN;
    }

    public boolean contain(int k) {
        return k2n[k] != -1;
    }

    //k 自定义索引， k = [1 ~ maxN] ,下标从1开始
    //item 数据对象
    public void insert(int k, T item) {
        if (k < 1 || k > maxN)
            throw new RuntimeException("k=[1,maxN] K:" + k);
        if (contain(k)) {
            throw new RuntimeException("already contain K:" + k + " item:" + items[pqueue[k]]);
//            return;
        }
        N++;
        k2n[k] = N;         //k->N
        pqueue[N] = k;      //N->k
        items[k] = item;
        swimUp(N);
    }

    public void change(int k, T item) {
        items[k] = item;
        swimUp(k2n[k]);
        sinkDown(k2n[k]);
    }

    public T top() {
        if (N > 0)
            return items[pqueue[1]];
        return null;
    }

    public T delTop() {
        if (N < 1)
            return null;
        T top = items[pqueue[1]];   //保留删除的最顶
        exch(1, N);                 //最低的换到最顶，做sink
        items[pqueue[N]] = null;    //删掉换到最低的最顶
        k2n[pqueue[N]] = -1;
        pqueue[N] = 0;
        N--;
        sinkDown(1);
        return top;
    }

    public T del(int k) {
        if (k2n[k] == -1)
            return null;
        T tDel = items[k];
        pqueue[k2n[k]] = pqueue[N]; //最低的换到当前
        N--;
        swimUp(k2n[k]);             //将换过来的做 swim，sink ，堆有序化
        sinkDown(k2n[k]);
        k2n[k] = -1;                //删除目标k对应的n
        items[k] = null;
        return tDel;
    }

    public int topIndex() {
        if (N > 0)
            return pqueue[1];
        return -1;
    }

    public boolean isEmpty() {
        return N < 1;
    }

    public int size() {
        return N;
    }

    private void swimUp(int n) {
        int child = n;
        int parent = child / 2;
        while (parent > 0) {
            T pt = items[pqueue[parent]];
            T ct = items[pqueue[child]];
            if (pt.compareTo(ct) < 0)
                break;
            exch(parent, child);
            child = parent;
            parent = child / 2;
        }
    }

    private void sinkDown(int n) {
        int parent = n;
        int lchild = parent * 2;
        int rchild = lchild + 1;
        int targetChild;
        while (lchild <= N || rchild <= N) {
            T pt = items[pqueue[parent]];
            T tc = null;
            targetChild = lchild;       //默认用left child，当 lchild == rchild value 的时候,或不存在rchild的时候
            if (rchild < N) {           //targetChild ，找到2个child中更小的那个
                int rcmpl = items[pqueue[rchild]].compareTo(items[pqueue[lchild]]);
                if (rcmpl < 0)          //若 rchild 更小
                    targetChild = rchild;
            }
            tc = items[pqueue[targetChild]];
            if (pt.compareTo(tc) < 0)
                break;
            exch(parent, targetChild);
            parent = targetChild;
            lchild = parent * 2;
            rchild = lchild + 1;
        }
    }

    private void exch(int n1, int n2) {
        int tmp = pqueue[n1];       //[n]->k
        pqueue[n1] = pqueue[n2];
        pqueue[n2] = tmp;
        tmp = k2n[pqueue[n1]];      //[k]->n
        k2n[pqueue[n1]] = k2n[pqueue[n2]];
        k2n[pqueue[n2]] = tmp;
    }

    @Override
    public String toString() {
        return "IndexMinPQ{" +
                "pqueue[N]=k =" + Arrays.toString(pqueue) +
                ", \nk2n[k]=N =" + Arrays.toString(k2n) +
                ", \nitems[k]=O =" + Arrays.toString(items) +
                ", \nmaxN=" + maxN +
                ", \nN=" + N +
                '}';
    }

    public static void main(String[] args) {
        IndexMinPQ<Integer> ipq = new IndexMinPQ<>(10);
        ipq.insert(2, 22);
        ipq.insert(6, 66);
        ipq.insert(1, 11);
        ipq.insert(4, 44);
        System.out.println(ipq);

        ipq.change(1, 111);
        System.out.println(ipq);

        System.out.println("ipq.contain(2) :" + ipq.contain(2));

        System.out.println("ipq.top() :" + ipq.top()); //2
        System.out.println("ipq.topIndex() :" + ipq.topIndex()); //2
        System.out.println("ipq.delTop() :" + ipq.delTop());
        System.out.println(ipq);

        ipq.insert(10, 1);
//        ipq.insert(0, 1);
        System.out.println(ipq);

        while (!ipq.isEmpty()) {
            System.out.println("k: " + ipq.topIndex() + " v:" + ipq.delTop());
        }

        System.out.println("====");
        Random r = new Random(123);
        while (ipq.size() < ipq.getMaxN()) {
            ipq.insert(ipq.size() + 1, r.nextInt(100));
        }
        while (!ipq.isEmpty()) {
            System.out.println("k: " + ipq.topIndex() + " v:" + ipq.delTop());
        }
    }
}
