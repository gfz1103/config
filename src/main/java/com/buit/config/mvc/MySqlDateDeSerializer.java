package com.buit.config.mvc;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
/**
* @ClassName: MySqlDateDeSerializer
* @Description: 日期处理
* @author 神算子
* @date 2020年4月26日 下午3:41:56
*
 */
public class MySqlDateDeSerializer extends DateDeserializers.SqlDateDeserializer {

    private static final long serialVersionUID = 1L;

    @Override
    public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        if (p != null) {
            String calendatStr = p.getText();
            if (StringUtils.isNotBlank(calendatStr)) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                dateFormat.setLenient(false);
                try {
                    return new Date(dateFormat.parse(calendatStr).getTime());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.deserialize(p, ctxt);
    }
}
