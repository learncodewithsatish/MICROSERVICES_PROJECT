package com.address.client;

import com.address.model.dto.EmployeeDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//@FeignClient(name = "employeeClient", url = "${employee.service.url}")
@FeignClient(name = "EMPLOYEE")
public interface EmployeeClient {

    @GetMapping("/employees/{id}")
    EmployeeDto getSingleEmployee(@PathVariable Long id);

}
