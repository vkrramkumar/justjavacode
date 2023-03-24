package com.kiki.SpringBatchCsvtoCsv.processor;

import com.kiki.SpringBatchCsvtoCsv.model.Employee;
import org.springframework.batch.item.ItemProcessor;


public class EmployeeProcessor implements ItemProcessor<Employee,Employee> {

    @Override
    public Employee process(Employee employee) throws Exception {
        Employee employee1=new Employee(employee.getEmpId(),employee.getFirstName(),employee.getLastName(),employee.getAge());
        return employee1;
    }
}
