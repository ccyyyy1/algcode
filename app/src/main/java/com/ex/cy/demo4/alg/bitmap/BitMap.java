package com.ex.cy.demo4.alg.bitmap;

//位图
public class BitMap {
    char[] map;     //存储位图的容器
    int size;

    //size: 位的个数
    public BitMap(int size) {
        this.size = size;
        int arrLen = (size >> 3);
        arrLen += (size & 7) > 0 ? 1 : 0;
        map = new char[arrLen];  //  = size/8
    }

    //索引 [0,size)
    public void set(int index, boolean value) {
        if (!sizeCheck(index))
            throw new RuntimeException("out of size, size: " + size + " , index: " + index);

        int groupIndex = index >> 3;
        int groupOffset = index & 7; // index - groupIndex,  %8 = 偶数取模 = &(偶数-1)
        char setUp = (char) (1 << groupOffset);
        if (value)
            map[groupIndex] |= setUp;
        else
            map[groupIndex] &= ~setUp;
    }

    public boolean sizeCheck(int index) {
        if (index > -1 && index < size)
            return true;
        return false;
    }

    //索引 [0,size)
    public boolean get(int index) {
        if (!sizeCheck(index))
            throw new RuntimeException("out of size, size: " + size + " , index: " + index);

        int groupIndex = index >> 3;
        int groupOffset = index & 7; // index - groupIndex,  %8 = 偶数取模 = &(偶数-1)
        char setUp = (char) (1 << groupOffset);
        return (map[groupIndex] & setUp) > 0;
    }


    public static void main(String[] a) {
//        BitSet bs = new BitSet();
//        System.out.println(bs.get(2));
//        bs.set(2,true);
//        System.out.println(bs.get(2));

        System.out.println("========");
        BitMap bitMap = new BitMap(100);
        System.out.println("bitMap.get(0) " + bitMap.get(0));
        bitMap.set(0, true);
        System.out.println("bitMap.get(0) " + bitMap.get(0));
        System.out.println("bitMap.get(99) " + bitMap.get(99));
        bitMap.set(99, true);
        System.out.println("bitMap.get(99) " + bitMap.get(99));
        System.out.println("bitMap.get(100) " + bitMap.get(100));
        bitMap.set(100, true);
        System.out.println("bitMap.get(100) " + bitMap.get(100));
    }
}
