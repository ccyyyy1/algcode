package com.ex.cy.demo4.alg.heap;

//和中位数很类似
//中位数为 PersentNumber = 50%
//现在可以自定义 左边（大根堆） 和 右边（小根堆） 所占整个数据的比例，以求的整个数据流中 第 x% 大的数据是多少  (x 0% ~ 100%)
public class PersentNumber {

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
    float persent = 0.5f;

    public PersentNumber() {
    }

    public PersentNumber(float persent) {
        //TODO 支持 0% 100%
        if(persent <= 0f || persent >= 1f)
            throw new RuntimeException("unsupport 0 100, only (0,100)");
        this.persent = persent;
    }

    public void insertNum(long num) {
        //((minHeap.getCount() + maxHeap.getCount()) & 1) > 0
        long total = minHeap.getCount() + maxHeap.getCount();
        float pc = (total * (1-persent));

        if (pc < minHeap.getCount()) {
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

    public float getPersentNum() {
        long dataCount = minHeap.getCount() + maxHeap.getCount();
        float p = dataCount * persent;
        if (dataCount > 0) {
            if (p <= maxHeap.getCount())
                return minHeap.top();
            else if (p > maxHeap.getCount()) {
                return maxHeap.top();
            } else { //==
                if (maxHeap.getCount() > 0 && minHeap.getCount() > 0)
                    return (minHeap.top() + maxHeap.top()) / 2f;
                else if (maxHeap.getCount() > 0)
                    return maxHeap.top();
                else if (minHeap.getCount() > 0)
                    return minHeap.top();
                else
                    return -1;
            }
        }
        return 0;
    }

    //从数据流中获取中位数
    public float percentNum(int[] datas) {
        maxHeap = new BinHeap(datas.length / 2 + 2, true);
        minHeap = new BinHeap(datas.length / 2 + 2, false);
        int c = 0;
        while (c < datas.length) {
            insertNum(datas[c++]);
        }
        return getPersentNum();
    }

    public static void main(String[] args) {
        PersentNumber cn = new PersentNumber(0.99f);
        int[] a = new int[]{
//                1, 3, 4, 6, 5, 2, 9, 8, 10, 7
                1,2,3,4,5,6,7,8,9,10
        };
        float mid = cn.percentNum(a);
        System.out.println(" mid " + mid); //8
    }
}
