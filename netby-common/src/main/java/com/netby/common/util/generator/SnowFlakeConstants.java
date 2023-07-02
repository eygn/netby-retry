package com.netby.common.util.generator;

/**
 * @author: byg
 */
@SuppressWarnings("unused")
public class SnowFlakeConstants {
    // ||<----------------------------------code:64---------------------------------->||
    // ||<-reserved:1->||<-timestamp:31->||<-type:8->||<----workid:13---->||<-seq:11->||
    // ||<-reserved:1->||<-timestamp:31->||<-type:8->||<-ip:8->||<-pid:5->||<-seq:11->||
    /**
     * bits of TIMESTAMP field
     */
    public static final int CODER_RESERVED_FIELD_BITS = 1;
    /**
     * bits of TIMESTAMP field
     */
    public static final int CODER_TIMESTAMP_FIELD_BITS = 31;
    /**
     * bits of TYPE field
     */
    public static final int CODER_TYPE_FIELD_BITS = 8;
    /**
     * bits of IP field
     */
    public static final int CODER_IP_FIELD_BITS = 8;
    /**
     * bits of PID field
     */
    public static final int CODER_PID_FIELD_BITS = 5;
    /**
     * bits of SEQUENCE field
     */
    public static final int CODER_SEQUENCE_FIELD_BITS = 11;
    /**
     * max times to try
     */
    public static final int GENERATE_MAX_TRIES = 3;

    // ||<------------------------code:64-------------------------->||
    // ||<-reserved:1->||<-timestamp:31->||<-workId:16->||<-seq:16->||
    // 注意：最多到2084年
    /**
     * bits of TIMESTAMP field
     */
    public static final int CODER_TIMESTAMP_FIELD_BITS_V2 = 31;
    public static final int CODER_WORK_FIELD_BITS_V2 = 16;
    public static final int CODER_SEQUENCE_FIELD_BITS_V2 = 16;

}
