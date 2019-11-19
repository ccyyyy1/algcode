package com.ex.cy.demo4.alg.heap;

import java.util.ArrayList;
import java.util.List;

public class TopK {
    BinHeap heap;
    int k;

    public TopK(int k) {
        this.k = k;
        heap = new BinHeap(k + 3, false);
    }

    public void add(long data) {
        if (heap.getCount() == k) {
            if (heap.top() < data) {        //保存的大数中，最小的大数都比新数据小，那么将堆顶弹出，换入新数据
                heap.pop();
                heap.add(data);
            }
        } else {
            heap.add(data);
        }
    }

    public List<Long> finish() {
        List<Long> l = new ArrayList<>();
        for (; heap.getCount() > 0; ) {
            l.add(heap.pop());
        }
        return l;
    }

    public static void main(String[] args) {
        TopK topMax3 = new TopK(3);
        long[] list = new long[]{8, 4, 5, 7, 9, 1, 6, 2, 3};
        for (long l : list) {
            topMax3.add(l);
        }
        System.out.println(topMax3.finish());
    }
}
