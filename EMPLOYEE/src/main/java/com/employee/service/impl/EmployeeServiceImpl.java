package com.employee.service.impl;

import com.employee.client.AddressClient;
import com.employee.exception.BadRequestException;
import com.employee.exception.ResourceNotFoundException;
import com.employee.model.dto.AddressDto;
import com.employee.model.dto.EmployeeDto;
import com.employee.model.entity.Employee;
import com.employee.respository.EmployeeRepository;
import com.employee.service.EmployeeService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);
    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;
    private final AddressClient addressClient;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
                               ModelMapper modelMapper,
                               AddressClient addressClient) {
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
        this.addressClient = addressClient;
    }


    @Override
    public EmployeeDto saveEmployee(EmployeeDto employeeDto) {
        if(employeeDto.getId() != null){
            throw new RuntimeException("Employee already exists");
        }
        Employee entity = modelMapper.map(employeeDto, Employee.class);
        Employee savedEntity = employeeRepository.save(entity);
        return modelMapper.map(savedEntity, EmployeeDto.class);
    }

    @Override
    public EmployeeDto updateEmployee(Long id, EmployeeDto employeeDto) {
        if(id == null || employeeDto.getId() == null){
            throw new BadRequestException("Please provide employee id");
        }
        if(!Objects.equals(id, employeeDto.getId())) {
            throw new BadRequestException("Id mismatch");
        }
        employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        Employee entity = modelMapper.map(employeeDto, Employee.class);
        Employee updatedEmployee = employeeRepository.save(entity);
        return modelMapper.map(updatedEmployee, EmployeeDto.class);
    }

    @Override
    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        employeeRepository.delete(employee);
    }

    @Override
    public EmployeeDto getSingleEmployee(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        List<AddressDto> addresses = new ArrayList<>();
        EmployeeDto dto = modelMapper.map(employee, EmployeeDto.class);
        try{
            addresses = addressClient.getAddressByEmpId(employee.getId());
            dto.setAddress(addresses);
        } catch (Exception e) {
            log.error("Address not found with employee id: {}", employee.getId());
        }
        return dto;
    }

    @Override
    public List<EmployeeDto> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        if(employees.isEmpty()){
            throw new ResourceNotFoundException("No employees found");
        }
        List<EmployeeDto> employeeDtoList = employees.stream().map(emp -> modelMapper.map(emp, EmployeeDto.class)).toList();
        List<EmployeeDto> response = new ArrayList<>();
        for(EmployeeDto employee : employeeDtoList){
            List<AddressDto> addresses = new ArrayList<>();
            try{
                addresses = addressClient.getAddressByEmpId(employee.getId());
                employee.setAddress(addresses);
            } catch (Exception e) {
                log.error("Address not found with employee id: {}", employee.getId());
            }
            response.add(employee);
        }
        return response;
    }

    @Override
    public EmployeeDto getEmployeeByEmpCodeAndCompanyName(String empCode, String companyName) {
        Employee employee = employeeRepository.findByEmpCodeAndCompanyName(empCode, companyName).orElseThrow(() -> new ResourceNotFoundException("Employee not found with empCode: " + empCode + " and companyName: " + companyName));
        return modelMapper.map(employee, EmployeeDto.class);
    }
}
