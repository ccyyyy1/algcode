package com.ex.cy.demo4.alg.heap;

public class BinHeap {
    long[] a;
    int size = 16;          //数组大小
    int count;              //数据个数
    boolean isMax;          //是否是大根堆

    public BinHeap() {
        a = new long[size];
    }

    public BinHeap(int size, boolean isMax) {
        this.size = size;
        this.isMax = isMax;
        a = new long[size];
    }

    public void add(long k) {
        reSize();
        a[count + 1] = k;
        count++;
        swimUp(count);
    }

    //动态扩容
    private void reSize() {
        if (count < size - 1)
            return;
        long[] a2 = new long[size << 1];
        System.arraycopy(a, 0, a2, 0, a.length);
        a = a2;
        size <<= 1;
    }

    public long pop() {
        if (count > 0) {
            long n = a[1];
            a[1] = a[count];
            count--;
            sinkDown(1);
            return n;
        }
        throw new RuntimeException("null heap");
    }

    public boolean isEmpty() {
        return count == 0;
    }

    public long top() {
        if (count > 0)
            return a[1];
        throw new RuntimeException("null heap");
    }

    public int getCount() {
        return count;
    }

    private void swimUp(int i) {
        long tmp = a[i];
        int c = i;
        int p = i;
        for (; (c >> 1) > 0 && isMax ? (a[c >> 1] < tmp) : (a[c >> 1] > tmp); c = p) {
            p = c >> 1;
            a[c] = a[p];    //空节点上移, child = parent
        }
        a[p] = tmp;         //填充空节点
    }

    private void sinkDown(int i) {
        long tmp = a[i];
        int p = i;
        int lc, rc, tc = i; //left child, right child, target child

        //TODO SUCK CODE!
        for (; p < (count / 2 + 1); p = tc) {
            lc = p << 1;
            rc = lc + 1;
            int tmptc;
            if (isMax) {
                if (rc > count) {
                    if (a[lc] < tmp) {
                        a[p] = a[lc];
                        tc = lc;
                    }
                } else {
                    tmptc = a[lc] < a[rc] ? rc : lc;
                    if (a[tmptc] > tmp) {
                        a[p] = a[tmptc];
                        tc = tmptc;
                    }
                }
            } else {
                if (rc > count) {
                    if (a[lc] < tmp) {
                        a[p] = a[lc];
                        tc = lc;
                    }
                } else {
                    tmptc = a[lc] > a[rc] ? rc : lc;
                    if (a[tmptc] < tmp) {
                        a[p] = a[tmptc];
                        tc = tmptc;
                    }
                }
            }
            if(p == tc)
                break;
        }
        a[tc] = tmp;
    }
}
