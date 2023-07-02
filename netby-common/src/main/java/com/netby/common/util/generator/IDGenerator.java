package com.netby.common.util.generator;

import java.util.UUID;

/**
 * 根据类型生成唯一编码
 *
 * @author: byg
 */
@SuppressWarnings("unused")
public class IDGenerator {
    public static int getIdType(long id) {
        return SnowFlakeWrapper.getType(id);
    }

    public static long getMixId() {
        return SnowFlakeWrapper.getMixId();
    }

    public static long getLongId(int idType) {
        if (idType < IDTypeEnum.SYSTEM_CODE.getIdIndex() ||
                idType > IDTypeEnum.MAX_CODE.getIdIndex()) {
            return -1;
        }

        return SnowFlakeWrapper.getLongId(idType);
    }

    public static long getLongId(IDTypeIndex idType) {
        return getLongId(idType.index());
    }

    public static String getId(int codeType) {
        long longCode = IDGenerator.getLongId(codeType);
        if (longCode == -1) {
            return null;
        }

        return Long.toString(longCode);
    }

    public static String getUId() {
        return getId(IDTypeEnum.MAX_CODE);
    }

    public static String getId(IDTypeIndex idType) {
        return getId(idType.index());
    }

    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
