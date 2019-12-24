package com.ex.cy.demo4.alg.bitmap;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

//它实际上是一个很长的二进制向量和一系列随机映射函数
//布隆过滤器可以用于检索一个元素是否在一个集合中(用多个hash函数对元素求多个hash)。它的优点是空间效率和查询时间都远远超过一般的算法，缺点是有一定的误识别率和删除困难。
//删除困难：Counting Bloom Filter ， 按位计数（置true一次，+1，删一次 -1, =0 表示false)

//1.估数据量n 以及 期望的误判率fpp
//2.hash函数的选取 以及 bit数组的大小 (影响误判率，hash函数离散效果越好，bit数组越大，误判率越低，根据误判率动态扩容bit数组大小，类似散列表的装载因子，动态扩容，降低散列冲突（若是开放定址法，则会减少键簇））

//BloomFilter有以下参数:
//
//        m 位数组的长度
//        n 加入其中元素的数量
//        k 哈希函数的个数
//        f False Positive

//使用Hash函数的个数，位数组的大小来降低失误率
// false is aways false，true is maybe true

public class BoolmFilter {
    int dataRange;
    int storageSize;
    BitMap bitmap;

    public BoolmFilter(int storageSize) {
        bitmap = new BitMap(storageSize);
        this.storageSize = storageSize;
    }

    public void set(int key, boolean value) {
//        int h1 = hash1(key);
        int h2 = hash2(key);
        int h3 = hash3(key);
        System.out.println("h1 "  + ", h2 " + h2 + ", h3 " + h3);
        //bitmap.set(h1, value);
        bitmap.set(h2, value);
        bitmap.set(h3, value);
    }

    //存在会有误判，不存在不会有误判
    //存在-实际上存在
    //存在-实际上不存在（误判）
    //
    //由hash1，hash2....hashK ，共同存在，判定为该数存在
    //hash函数越多，产生冲突的概率越低
    public boolean get(int data) {
//        int h1 = hash1(data);
        int h2 = hash2(data);
        int h3 = hash3(data);
        return bitmap.get(h2) && bitmap.get(h3);
    }

    private int hash1(int data) {
        return data % storageSize;
    }

    private int hash2(int data) {
        //低16位 ，异或 高16位
        return ((data << 16) ^ data) & 0x7fffffff % storageSize;
    }

    private int hash3(int data) {
        byte[] databytes = new byte[4];
        databytes[0] = (byte) (data & 0xff);
        databytes[1] = (byte) (data & 0xff00);
        databytes[2] = (byte) (data & 0xff0000);
        databytes[3] = (byte) (data & 0xff000000);
        return (int) (murmurHash3(databytes) & 0x7fffffffffffffffl % storageSize);
    }

    //MurmurHash3
    //对于规律性较强的key，MurmurHash的随机分布特征表现更良好
    //能够产生出32-bit或128-bit哈希值
    private long murmurHash3(byte[] key) {

        ByteBuffer buf = ByteBuffer.wrap(key);
        int seed = 0x1234ABCD;

        ByteOrder byteOrder = buf.order();
        buf.order(ByteOrder.LITTLE_ENDIAN);

        long m = 0xc6a4a7935bd1e995L;
        int r = 47;

        long h = seed ^ (buf.remaining() * m);

        long k;
        while (buf.remaining() >= 8) {
            k = buf.getLong();

            k *= m;
            k ^= k >>> r;
            k *= m;

            h ^= k;
            h *= m;
        }

        if (buf.remaining() > 0) {
            ByteBuffer finish = ByteBuffer.allocate(8).order(
                    ByteOrder.LITTLE_ENDIAN);
            // for big-endian version, do this first:
            // finish.position(8-buf.remaining());
            finish.put(buf).rewind();
            h ^= finish.getLong();
            h *= m;
        }

        h ^= h >>> r;
        h *= m;
        h ^= h >>> r;

        buf.order(byteOrder);
        return h;
    }

    public static void main(String[] a) {
        BoolmFilter bf = new BoolmFilter(100);
        //数据范围 1 ~ 1000
        //数据个数 10 个
        //位图大小 100

        //TODO 加动态扩容， 误判太高
        Random r = new Random(0xdeadface);
        Set<Integer> datas = new HashSet<>();
        int i = 0;
        for (; i < 10; i++) {
            int data = r.nextInt(1000);
            datas.add(data);
            System.out.println(bf.get(data));
            bf.set(data, true);
            System.out.println(bf.get(data));
        }

        for (i = 0; i < 1000; i++) {
            if (bf.get(i)) {
                System.out.print(i + " true ");
                if (!datas.contains(i))
                    System.out.print(" - 误判");
                System.out.println();
            }
        }
    }

    static long numOfBits(long n, double p) {
        if (p == 0) {
            p = Double.MIN_VALUE;
        }
        return (long) (-n * Math.log(p) / (Math.log(2) * Math.log(2)));
    }

    // k = (m/n) * ln(2)
    static int numOfHashFunctions(long n, long m) {
        return Math.max(1, (int) Math.round((double) m / n * Math.log(2)));
    }
}
