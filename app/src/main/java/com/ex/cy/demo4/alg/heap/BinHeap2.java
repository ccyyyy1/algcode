package com.ex.cy.demo4.alg.heap;

public class BinHeap2<T extends Comparable<T>> {
    T[] a;
    int size = 16;          //数组大小
    int count;              //数据个数
    boolean isMax;          //是否是大根堆

    public BinHeap2() {
        this(16, false);
    }

    public BinHeap2(int size, boolean isMax) {
        this.size = size;
        this.isMax = isMax;
        a = (T[]) new Comparable[size];
    }

    public void add(T k) {
        reSize();
        a[++count] = k;
        swimUp(count);
    }

    //动态扩容
    private void reSize() {
        if (count < size - 1)
            return;
        T[] a2 = (T[]) new Comparable[size << 1];
        System.arraycopy(a, 0, a2, 0, a.length);
        a = a2;
        size <<= 1;
    }

    public T pop() {
        if (count > 0) {
            T n = a[1];
            a[1] = a[count];
            count--;
            sinkDown(1);
            return n;
        }
        throw new RuntimeException("null heap");
    }

    public T top() {
        if (count > 0)
            return a[1];
//        throw new RuntimeException("null heap");
        return null;
    }

    public T remove(T t) {
        T reObj = null;
        for (int i = 0; i < count; i++) {
            if (a[i].equals(t)) {
                reObj = a[i];
            }
        }
        return reObj;
    }

    public T removeAt(int index) {
        T reObj = a[index];
        a[index] = a[count--];
        sinkDown(index);
        return reObj;
    }

    public boolean isEmpty() {
        return count < 1;
    }


    public int getCount() {
        return count;
    }


    private void swimUp(int i) {
        T tmp = a[i];
        int c = i;
        int p = i;
        for (; (c >> 1) > 0 && (isMax ? (((Comparable) a[c >> 1]).compareTo(tmp) < 0) : (((Comparable) a[c >> 1]).compareTo(tmp) > 0)); c = p) {
            p = c >> 1;
            a[c] = a[p];    //空节点上移, child = parent
        }
        a[p] = tmp;         //填充空节点
    }

    private void sinkDown(int i) {
        T tmp = a[i];
        int p = i;
        int lc, rc, tc = i; //left child, right child, target child

        //TODO SUCK CODE!
        for (; p < (count / 2 + 1); p = tc) {
            lc = p << 1;
            rc = lc + 1;
            int tmptc;
            if (isMax) {
                if (rc > count) {
                    if (((Comparable) a[lc]).compareTo(tmp) < 0) {
                        a[p] = a[lc];
                        tc = lc;
                    }
                } else {
                    tmptc = ((Comparable) a[lc]).compareTo(a[rc]) < 0 ? rc : lc;
                    if (((Comparable) a[tmptc]).compareTo(tmp) > 0) {
                        a[p] = a[tmptc];
                        tc = tmptc;
                    }
                }
            } else {
                if (rc > count) {
                    if (((Comparable) a[lc]).compareTo(tmp) < 0) {
                        a[p] = a[lc];
                        tc = lc;
                    }
                } else {
                    tmptc = ((Comparable) a[lc]).compareTo(a[rc]) > 0 ? rc : lc;
                    if (((Comparable) a[tmptc]).compareTo(tmp) < 0) {
                        a[p] = a[tmptc];
                        tc = tmptc;
                    }
                }
            }
            if (p == tc)
                break;
        }
        a[tc] = tmp;
    }
}
