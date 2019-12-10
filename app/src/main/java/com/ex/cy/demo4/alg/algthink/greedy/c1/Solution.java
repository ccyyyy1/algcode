package com.ex.cy.demo4.alg.algthink.greedy.c1;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class Solution {

    /**
     * 范围区间
     */
    private PriorityQueue<ScopeBusi> scopeList =
            new PriorityQueue<ScopeBusi>(10, new Comparator<ScopeBusi>() {
                @Override
                public int compare(ScopeBusi lhs, ScopeBusi rhs) {
                    return Solution.this.compare(lhs.getEnd(), rhs.getEnd());
                }
            });

    /**
     * 比较的算法
     *
     * @param val1
     * @param val2
     * @return
     */
    public int compare(int val1, int val2) {
        if (val1 > val2) {
            return 1;
        } else if (val1 < val2) {
            return -1;
        }
        return 0;
    }

    public void add(int start, int end) {
        scopeList.offer(new ScopeBusi(start, end));
    }

    /**
     * 使用贪心算法，求最多能选出多少个区间呢？
     *
     * <p>算法实现，即按结束结点构建小顶堆，
     *
     * <p>然后在剩余的区间中查找，开始时间大小小顶堆的结束时间，这样即可求得最大不相交区间
     *
     * @return
     */
    public List<ScopeBusi> countScope() {

        ScopeBusi scopeStartTmp = scopeList.poll();

        List<ScopeBusi> result = new ArrayList<>();
        result.add(scopeStartTmp);

        while (!scopeList.isEmpty()) {

            ScopeBusi endBusi = scopeList.peek();
            System.out.println("endBusi " + endBusi);

            if (endBusi.getStart() >= scopeStartTmp.getEnd()) {
                result.add(endBusi);
                scopeStartTmp = endBusi;
                scopeList.poll();
            } else {
                scopeList.poll();
            }
        }
        return result;
    }

    public static void main(String[] a){
        Solution s = new Solution();
        s.add(8,9);
        s.add(3,5);
        s.add(1,5);
        s.add(6,8);
        s.add(1,4);
        s.add(2,4);
        s.add(8,10);
        s.add(5,9);
        System.out.println(s.countScope());
    }

}
