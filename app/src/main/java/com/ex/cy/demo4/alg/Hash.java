package com.ex.cy.demo4.alg;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Hash {
    public static int hash(String s) {
        int h = 0;
        char c = 0;
        int R = 31; //31 进制，质数，方便用位移和+- 1 变换为接近 32，64这类 2^n 次方的数据
        for (int i = 0; i < s.length(); i++) {
            c = s.charAt(i);
            h = h * R + c;
        }
        return h;
    }

    public static int hash(byte[] b) {
        int h = 0;
        byte c = 0;
        int R = 31; //31 进制，质数，方便用位移和+- 1 变换为接近 32，64这类 2^n 次方的数据
        for (int i = 0; i < b.length; i++) {
            c = b[i];
            h += h * R + c;
        }
        return h;
    }

    public static int hash(int n) {
        return ((n << 16) ^ n) & 0x7fffffff;
    }

    //碰撞：鸽巢原理，定长的散列值的空间有限，而进行散列的数据的空间超过了被映射到的散列值的空间范围

    //把数据散列后，对n取莫，将任务可以负载到n个机器中的其中1个（均衡性取决于散列函数的分布性和数据本身的分布性)
    //比如做数据分片
    public static int hashTo(byte[] data, int n) {
        return hash(data) % n;
    }

    //一致性hash     //均匀一致性hash
    //散列值范围是 [0,MAX], 有k台机器， 将 散列值分成m个区间，m远大于k，这样每台机器处理 m/k 个小区间
    //一致性hash，在服务器减少时，不会造成大部分key取莫后不命中，造成压力转移到一台服务器上
    //只会造成一小部分key不命中
    public static String hashTo(byte[] data, List<String> serverIpList) {
        //1.对数据求hash
        int dataHash = hash(data);
//        List<Integer> serverHashList = new ArrayList<>();//hash环 1.线性，查找时,时间复杂度 O(n)
        TreeMap<Integer, String> serverHashList = new TreeMap<>();

        //2.将服务器ip 求hash
        for (String serverIp : serverIpList) {
//            serverHashList.add(hash(serverIp));
            int shash = hash(serverIp);
            serverHashList.put(shash, serverIp);
            System.out.println("serverIp " + serverIp + " Hash " + shash + " Shash " + serverIp.hashCode());
        }

        //3.看数据hash落在哪个服务器hash范围内
//        int i = 0;
//        for (int serverHash : serverHashList) {
//            if (dataHash > serverHash)
//                i++;
//            else
//                break;
//        }
//        return serverIpList.get(i);
        System.out.println("data hash " + dataHash);

        //TODO 可以添加虚拟节点，平衡负载,避免真实机器的节点在hash环中过于集中，造成负载不均衡的情况,虚拟节点的添加
        //TODO 的hash可以手动拼一个，要点是能够在hash环上均匀平摊出现的负载(微调的方式可以考虑加权hash，根据机器实际负载情况
        //TODO 把这个虚拟节点放在一个更容易接收到负载的，空旷的一段弧线上
        Map.Entry<Integer, String> e = serverHashList.ceilingEntry(dataHash);   //向上取
        if (e != null)
            return e.getValue();
        e = serverHashList.floorEntry(dataHash);                                //向下取
        if (e != null)
            return e.getValue();
        return null;
    }

    //负载均衡，数据分片，分布存储
    public static void main(String[] args) {
        List<String> serverList = new ArrayList<>();
        serverList.add("192.168.1.100:8080");
        serverList.add("192.168.1.150:8080");
        serverList.add("192.168.1.180:8080");
        serverList.add("192.168.1.190:8080");
        byte[] data = "/home/index123".getBytes();      // 多个数据 对比2种方式 容易看出结果不同,去掉一台服务器后
        String server = hashTo(data, serverList);
        System.out.println(server);
        //而如果用%n的方式，当服务器数量变化时，可以看到效果（会话粘滞型的的服务会造成单机压力过大，宕机的情况)
        int dataHash = hash(data);
        int serN = (dataHash % serverList.size());
        System.out.println("dataHash%n = " + serN + " " + serverList.get(serN));
    }
}
