package com.ex.cy.demo4.alg.heap;

public class CenterNumber {
    //          odd round         even round
    //                     \   /
    //                      \ /
    // (small) - ------------o------------>  + (big) stream numbers
    //             max heap /^\ min heap
    //    small numbers    / | \    big numbers
    //                     center number

    /**
     * 1.2个堆数量差 最大相差1
     * 2.最后结束时，maxHeap中所有数 都小于 minHeap中所有数, 此时maxHeap的堆顶，或minHeap的堆顶为中位数
     * 数据量为偶数个时（先从右边开始放入， minHeap),下一个数据时，放左边，依次交替
     *
     * @param a
     */
    BinHeap maxHeap;
    BinHeap minHeap;

    public void insertNum(long num) {
        //1 先满足数值大小，放入正确的堆
        //2 再满足堆数量平衡
        //2者不可弄反，否则堆数量平衡了，但最后求不到中位数
        if (((minHeap.getCount() + maxHeap.getCount()) & 1) > 0) {  //奇数,默认加到 maxHeap ，左边 , 用 > 或 < 比 == 判断往往所需cpu机器周期数更少，速度更快
            if (!minHeap.isEmpty() && minHeap.top() < num) {        //先判断，如果这个数比 右边 minHeap 中的最小还要大，就放到minHeap (右边）
                minHeap.add(num);
                num = minHeap.pop();                                //平衡数量
            }
            maxHeap.add(num);                                       //左边
        } else {                                                    //偶数,默认加到 minHeap ，右边
            if (!maxHeap.isEmpty() && maxHeap.top() > num) {
                maxHeap.add(num);
                num = maxHeap.pop();                                //平衡数量
            }
            minHeap.add(num);                                       //右边
        }
    }

    public float getMidNum() {
        long dataCount = minHeap.getCount() + maxHeap.getCount();
        if (dataCount > 0) {
            if ((dataCount & 1) > 0)                                 //奇数个数据量，从minHeap中取得
                return minHeap.top();
            return (maxHeap.top() + minHeap.top()) / 2f;              //偶数个数据量，2个中位数的平均数
        }
        return 0;
    }

    //从数据流中获取中位数
    public float centerNum(int[] datas) {
        maxHeap = new BinHeap(datas.length / 2 + 2, true);
        minHeap = new BinHeap(datas.length / 2 + 2, false);
        int c = 0;
        while (c < datas.length) {
            insertNum(datas[c++]);
        }
        return getMidNum();
    }

    public static void main(String[] args) {
        CenterNumber cn = new CenterNumber();
        int[] a = new int[]{
                1, 3, 4, 6, 5, 2
        };
        float mid = cn.centerNum(a);
        System.out.println(" mid " + mid); //3.5

        a = new int[]{
                1, 3, 4, 6, 5, 2, 7
        };
        mid = cn.centerNum(a);
        System.out.println(" mid " + mid); //4
    }
}
