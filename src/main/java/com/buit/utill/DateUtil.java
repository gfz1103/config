package com.buit.utill;

import java.sql.Timestamp;
import java.time.Instant;


/**
 * @author All
 */
public class DateUtil {


    /**
     * 获取当前时间
     * @return Timestamp
     */
    public static Timestamp getCurrentTimestamp(){
        Timestamp timestamp = Timestamp.from(Instant.now());
        return timestamp;
    }
}
