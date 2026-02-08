package ru.ifmo.se.typer;

import ru.ifmo.se.io.input.dto.ParamRawData;
import ru.ifmo.se.service.ParamTypedData;

public class DataTyper {

    public ParamTypedData typifyParamRawData(ParamRawData paramRawData) {
        ParamTypedData paramTypedData = new ParamTypedData();

        if (paramRawData.getId() != null) {
            paramTypedData.setId(Long.parseLong(paramRawData.getId()));
        }
        return paramTypedData;
    }
}
