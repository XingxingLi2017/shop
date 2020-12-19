package com.shop.util;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;

/***
 * snowflake algorithm for ID generation
 */
public class IdWorker {
    // start time of the id worker , the base for 41 bits millisecond bits
    private final static long twepoch = 1288834974657L;
    // worker id bits, the thread id bits
    private final static long workerIdBits = 5L;
    // id data center id , the machine's id bits
    private final static long datacenterIdBits = 5L;
    // max worker id , get 31
    private final static long maxWorkerId = -1L ^ (-1L << workerIdBits);
    // max data center id, get 31
    private final static long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);
    // sequence bits during one millisecond
    private final static long sequenceBits = 12L;
    // shifting bits for worker id
    private final static long workerIdShift = sequenceBits;
    // shifting bits for data center id
    private final static long datacenterIdShift = sequenceBits + workerIdBits;
    // shifting bits for millisecond
    private final static long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;
    // sequence mask used to get seqence bits quickly
    private final static long sequenceMask = -1L ^ (-1L << sequenceBits);
    // last timestamp that generates id
    private static long lastTimestamp = -1L;
    // 0 , control concurrency
    private long sequence = 0L;

    // start worker id and data center id
    private final long workerId;
    private final long datacenterId;

    public IdWorker(){
        // get data center id based on mac address of the network
        this.datacenterId = getDatacenterId(maxDatacenterId);

        // get worker id based on data center id and jvm pid string hash code
        this.workerId = getMaxWorkerId(datacenterId, maxWorkerId);
    }

    /**
     * generate id worker based on given worker id and data center id
     */
    public IdWorker(long workerId, long datacenterId) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    public synchronized long nextId() {
        // get current system millisec
        long timestamp = timeGen();
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(String.format("Clock moved backwards. Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        // get sequence if generate id in the same millisec
        if (lastTimestamp == timestamp) {
            // in current millisec , sequence + 1
            sequence = (sequence + 1) & sequenceMask;

            // sequence = 0 after increasing , the sequences was exhausted , wait for the next millisec
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }

        lastTimestamp = timestamp;

        // construct id and return
        long nextId = ((timestamp - twepoch) << timestampLeftShift)
                | (datacenterId << datacenterIdShift)
                | (workerId << workerIdShift) | sequence;

        return nextId;
    }

    private long tilNextMillis(final long lastTimestamp) {
        long timestamp = this.timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = this.timeGen();
        }
        return timestamp;
    }

    // get system timestamp by milliseconds
    private long timeGen() {
        return System.currentTimeMillis();
    }

    /**
     * get worker id based on data center id + jvm pid string hash code
     */
    protected static long getMaxWorkerId(long datacenterId, long maxWorkerId) {
        StringBuffer mpid = new StringBuffer();
        mpid.append(datacenterId);
        // getRuntimeMXBean() : Returns the managed bean for the runtime system of the Java virtual machine.
        // getName() : Returns the name representing the running Java virtual machine.
        String name = ManagementFactory.getRuntimeMXBean().getName();
        if (!name.isEmpty()) {
            /*
             * GET jvmPid
             */
            mpid.append(name.split("@")[0]);
        }
        /*
         * get the lower 16 bits of the hash code of data center id + PID
         */
        return (mpid.toString().hashCode() & 0xffff) % (maxWorkerId + 1);
    }

    /***
     * get data center id based on mac address of the network
     * @param maxDatacenterId
     * @return
     */
    protected static long getDatacenterId(long maxDatacenterId) {
        long id = 0L;
        try {
            InetAddress ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            if (network == null) {
                id = 1L;
            } else {
                byte[] mac = network.getHardwareAddress();
                id = ((0x000000FF & (long) mac[mac.length - 1])
                        | (0x0000FF00 & (((long) mac[mac.length - 2]) << 8))) >> 6;
                id = id % (maxDatacenterId + 1);
            }
        } catch (Exception e) {
            System.out.println(" getDatacenterId: " + e.getMessage());
        }
        return id;
    }


    public static void main(String[] args) {

        IdWorker idWorker=new IdWorker(0,0);

        for(int i=0;i<10000;i++){
            long nextId = idWorker.nextId();
            System.out.println(nextId);
        }
    }

}