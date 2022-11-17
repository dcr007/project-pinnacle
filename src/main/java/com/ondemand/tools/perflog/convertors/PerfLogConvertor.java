package com.ondemand.tools.perflog.convertors;

import com.ondemand.tools.perflog.models.PerfLog;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * @author Chandu D - i861116
 * @created 17/11/2022 - 11:40 AM
 * @description
 */
@Slf4j
@Converter(autoApply = true)
public class PerfLogConvertor implements AttributeConverter<PerfLog,String> {

    @Override
    public String convertToDatabaseColumn(PerfLog perfLog) {

        return null;
    }

    @Override
    public PerfLog convertToEntityAttribute(String s) {
        PerfLog perfLog = new PerfLog();
        String[] strSplit = s.split(" ");

        return null;
    }

    private static  String afterEquals(String input) {
        return input.substring(input.indexOf('=') + 1);
    }
}
