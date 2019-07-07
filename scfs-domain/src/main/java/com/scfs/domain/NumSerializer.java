package com.scfs.domain;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.scfs.common.utils.DecimalUtil;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Created by Administrator on 2016/12/14.
 */
public class NumSerializer extends JsonSerializer<BigDecimal> {
    @Override
    public void serialize(BigDecimal value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
            JsonProcessingException {
        jgen.writeString(DecimalUtil.toQuantityString(value));
    }
}