package com.kiki.SpringBatchCsvtoCsv.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    private Long empId;
    private String firstName;
    private String LastName;
    private Integer age;


    public static String[] fields(){
        return new String[]{"empId","firstName","LastName","Age"};
    }
}
