package com.buit.config.mvc;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
/**
* @ClassName: MySqlTimeDeSerializer
* @Description: 日期处理
* @author 神算子
* @date 2020年4月26日 下午3:42:28
 */
public class MySqlTimeDeSerializer extends DateDeserializers.TimestampDeserializer {

    private static final long serialVersionUID = 1L;

    @Override
    public Timestamp deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        if (p != null) {
            String calendatStr = p.getText();
            if (StringUtils.isNotBlank(calendatStr)) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                dateFormat.setLenient(false);
                try {
                    return new Timestamp(dateFormat.parse(calendatStr).getTime());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.deserialize(p, ctxt);
    }
}
