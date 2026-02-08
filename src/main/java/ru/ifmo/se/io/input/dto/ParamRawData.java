package ru.ifmo.se.io.input.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ParamRawData {

    private String id;

    public List<String> getEmptyFields() {
        List<String> listOfEmptyFields = new ArrayList<>();
        if (id != null && id.isEmpty()) {
            listOfEmptyFields.add("id");
        }
        return listOfEmptyFields;
    }
}
