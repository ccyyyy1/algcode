package com.ex.cy.demo4.alg.algthink.greedy;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Alg {
    //贪心算法思想
    public static class Range {
        int l; //left
        int r; //right

        //区间范围
        public Range(int l, int r) {
            this.l = l;
            this.r = r;
        }

        //是否和另一个区间有重叠
        public boolean isHaveSameRange(Range other) {
            //2-1 =1  3-5 = -2
            if (r <= other.l)
                return false;
            else if (l >= other.r)
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "{" +
                    l + "-" + r +
                    '}';
        }
    }

    //问题1:有一些区间数据，求他们里面的一个区间对象组成的集合，集合中的区间之间不发生重叠（端点接触不算重叠），且该集合中的区间个数最多
    //办法：先排序，按照lmin升序，在不重叠的情况下，选r小的和l小的
    public void noAndRanges(Range[] ranges) {
        ranges = sortByLmin(ranges);
        //先找l小的，在选r小的, 若发生重叠，那么在重叠区域中，1.选短的。 2.选r小的
        List<Range> select = new LinkedList<>();

        int lmin = ranges[0].l;
        int rmax = ranges[ranges.length - 1].r;
        Range rminRange = null;
        //解法1.BF 多次遍历，每次选一个 r 最min的，然后删除和这个重叠的，再来一次，直到没有任何Range剩下
        for (int i = 0; i < ranges.length; i++) {
            if(rminRange == null) {
                rminRange = ranges[i];
                continue;
            }else if(rminRange.r > ranges[i].r)
                rminRange = ranges[i];
        }

//        Range first = null;
//        Range target = null;
//        Range last = null;
//        int skip = 0;
//        for (int i = 0; i < ranges.length; i++) {
//            if (first == null) {
//                first = ranges[i];
//                target = first;
//                skip = 0;
//            } else {
//                if (first.isHaveSameRange(ranges[i])) {
//                    if (target.r > ranges[i].r) {
//                        target = ranges[i];
//                        skip = 0;
//                    }else{
//                        skip++;
//                    }
//                } else {
//                    last = ranges[i];           //出现第一个和first不重叠的区间
//                    select.add(target);
//                    first = null;
//                    i = i - skip;//TODO BUG..
//                }
//            }
//        }
        System.out.println("select " + select);
    }

    //按照 l端点 从小到大排列这些区间,l相同的情况下，r小的排在前面
    //第一顺序：l值从小到大，第二顺序：l值相同时，r值从小到大
    public Range[] sortByLmin(Range[] ranges) {
        //先用不稳定排序算法，时间复杂度低的，按照r从低到高排列，然后再用稳定排序算法按l从低高排序
        //1.quick sort
        //2.归并（若有足够空间），或插入排序(原地）
        ranges = quickSortRmin(ranges, 0, ranges.length - 1);
        System.out.println(Arrays.toString(ranges));
        ranges = meargeSortLmin(ranges, 0, ranges.length - 1);
        System.out.println(Arrays.toString(ranges));
        return ranges;
    }

    public Range[] meargeSortLmin(Range[] ranges, int s, int e) {
        if (e - s <= 0) {
            Range[] thislv = new Range[1];
            thislv[0] = ranges[s];
            return thislv;
        }
        int m = (s + e) >> 1;
        Range[] ls = meargeSortLmin(ranges, s, m);
//        System.out.println("ls: " + Arrays.toString(ls));
        Range[] rs = meargeSortLmin(ranges, m + 1, e);
//        System.out.println("rs: " + Arrays.toString(rs));
        Range[] thislv = new Range[e - s + 1];
        int li = 0;
        int ri = 0;
        int i = 0;
        Range lo = null;
        Range ro = null;
        while (i <= e - s) {
            if (li <= m - s && lo == null)
                lo = ls[li++];
            if (ri <= e - m - 1 && ro == null)
                ro = rs[ri++];
            if (lo != null && ro != null)
                if (lo.l <= ro.l) {
                    thislv[i++] = lo;
                    lo = null;
                } else {
                    thislv[i++] = ro;
                    ro = null;
                }
            else if (ro == null) {
                thislv[i++] = lo;
                lo = null;
            } else {
                thislv[i++] = ro;
                ro = null;
            }
        }
        return thislv;
    }

    //以 r 从小到大排列
    public Range[] quickSortRmin(Range[] ranges, int s, int e) {
        if (s >= e)
            return ranges;
        int l, r, p;
        l = s;
        r = e - 1;
        p = e;
        if (ranges[r].r < ranges[l].r)//三值取中作为p
            swap(ranges, r, l);
        if (ranges[p].r > ranges[r].r)
            swap(ranges, p, r);
        while (l < p) {
            while (ranges[l].r <= ranges[p].r && l < p)
                l++;
            while (ranges[r].r >= ranges[p].r && r > l)
                r--;
            if (l < r)
                swap(ranges, l, r);
            else {
                swap(ranges, l, p);
                break;
            }
        }
        quickSortRmin(ranges, s, l - 1);
        quickSortRmin(ranges, l + 1, e);
        return ranges;
    }

    private void swap(Range[] ranges, int a, int b) {
        Range tmp = ranges[a];
        ranges[a] = ranges[b];
        ranges[b] = tmp;
    }

    public static void main(String[] a) {
        Random r = new Random(1234);
        Range[] ranges = new Range[8];
        for (int i = 0; i < ranges.length; i++) {
            int lv = r.nextInt(9);
            int rv = 1 + lv + r.nextInt(4);
            ranges[i] = new Range(lv, rv);
            System.out.println(ranges[i]);
        }
        Alg alg = new Alg();
        alg.noAndRanges(ranges);
    }

}
