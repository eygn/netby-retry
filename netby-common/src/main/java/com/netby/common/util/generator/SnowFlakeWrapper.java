package com.netby.common.util.generator;


import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

import com.netby.common.util.OsUtil;
import lombok.extern.slf4j.Slf4j;

import static com.netby.common.util.generator.SnowFlakeConstants.*;

/**
 * @author: byg
 */
@Slf4j
public final class SnowFlakeWrapper {

    private static final long DIRECT = 1452649500L;

    private static final long PID_SHIFT = CODER_SEQUENCE_FIELD_BITS;
    private static final long IP_SHIFT = CODER_PID_FIELD_BITS + PID_SHIFT;
    private static final long TYPE_SHIFT = CODER_IP_FIELD_BITS + IP_SHIFT;
    private static final long TIMESTAMP_SHIFT = CODER_IP_FIELD_BITS + TYPE_SHIFT;
    private static final long SEQUENCE_MASK = ~(-1L << CODER_SEQUENCE_FIELD_BITS);
    private static long workId = -1;
    private static final AtomicLong LAST_TIME = new AtomicLong(-1L);
    private static AtomicLong sequence = new AtomicLong(0L);

    private static long workIdV2 = -1;
    private static final long SEQUENCE_MASK_V2 = ~(-1L << CODER_SEQUENCE_FIELD_BITS_V2);
    private static final long TIMESTAMP_SHIFT_V2 = CODER_SEQUENCE_FIELD_BITS_V2 + CODER_WORK_FIELD_BITS_V2;
    private static final AtomicLong LAST_TIME_V2 = new AtomicLong(-1L);
    private static final LongAdder SEQUENCE_V2 = new LongAdder();

    /**
     * 根据编码获取编码类型
     *
     * @param code 编码
     * @return 编码类型int值
     */
    public static int getType(long code) {
        return (int) ((code >> TYPE_SHIFT) & 0XFF);
    }

    /**
     * 根据类型生成编码
     *
     * @param type 编码类型int值
     * @return 成功返回非负长整形的字符串, 失败返回null
     */
    @SuppressWarnings("unused")
    public static String getId(int type) {
        long longId = SnowFlakeWrapper.getLongId(type);

        return (longId == -1) ? null : Long.toString(longId);
    }

    /**
     * 根据类型生成编码
     *
     * @param type 编码类型int值
     * @return 成功返回非负长整形, 失败返回-1
     */
    public static long getLongId(int type) {

        long code = -1;
        int loop = 0;
        do {
            try {
                code = nextId(type);
                break;
            } catch (Exception e) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e1) {
                    log.error("", e1);
                    Thread.currentThread().interrupt();
                }
            }
        } while (loop++ < SnowFlakeConstants.GENERATE_MAX_TRIES);

        return code;
    }

    /**
     * 真正生成唯一编码的方法
     *
     * @param type 编码类型int值
     * @return 成功返回非负长整形
     */
    private synchronized static long nextId(long type) {

        long timestamp = timeGen();
        final long lastTimestamp = LAST_TIME.get();
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        if (timestamp == lastTimestamp) {
            sequence.set(sequence.incrementAndGet() & SEQUENCE_MASK);
            if (sequence.get() == 0L) {
                timestamp = tillNextMillis(lastTimestamp);
            }
        } else {
            sequence = new AtomicLong(0L);
        }

        LAST_TIME.set(timestamp);
        return ((timestamp - DIRECT) << TIMESTAMP_SHIFT) | type << TYPE_SHIFT | getWorkId() | (sequence.get());
    }

    private static long getWorkId() {
        if (workId < 0) {
            try {
                long ip = OsUtil.getLongIp();
                ip = ip & 0XFF;
                long pid = OsUtil.getCurrentProcessId();
                pid = pid & 0X1F;
                workId = (ip << IP_SHIFT) | (pid << PID_SHIFT);
            } catch (Exception e) {
                throw new RuntimeException("init workId error with reason:" + e.getMessage());
            }
        }
        return workId;
    }

    private static long getWorkIdV2() {
        if (workIdV2 < 0) {
            try {
                long ip = OsUtil.getLongIp();
                ip = ip & 0XFFFF;
                workIdV2 = ip << CODER_SEQUENCE_FIELD_BITS_V2;
            } catch (Exception e) {
                throw new RuntimeException("init workId error with reason:" + e.getMessage());
            }
        }
        return workIdV2;
    }

    private static long tillNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    private static long timeGen() {
        return System.currentTimeMillis() / 1000;
    }

    public synchronized static long getMixId() {
        return getInnerMixId();
    }


    public static long getInnerMixId() {
        long now = System.currentTimeMillis();
        long timestamp = now / 1000;
        final long lastTimestamp = LAST_TIME_V2.get();
        if (timestamp < lastTimestamp) {
            timestamp = lastTimestamp;
        }

        long s;
        if (timestamp == lastTimestamp) {
            SEQUENCE_V2.increment();
            s = SEQUENCE_V2.longValue();
            if ((s & SEQUENCE_MASK_V2) == 0) {
                long hold = (timestamp + 1) * 1000 - System.currentTimeMillis();
                try {
                    if (hold > 0) {
                        TimeUnit.MILLISECONDS.sleep(hold);
                    }
                } catch (InterruptedException ignored) {
                }
                return getMixId();
            }
        } else {
            s = 0;
            LAST_TIME_V2.set(timestamp);
            SEQUENCE_V2.reset();
        }
        return ((timestamp - DIRECT) << TIMESTAMP_SHIFT_V2) | getWorkIdV2() | s;
    }

}
