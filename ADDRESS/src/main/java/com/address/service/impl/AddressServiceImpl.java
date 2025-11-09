package com.address.service.impl;

import com.address.client.EmployeeClient;
import com.address.model.dto.AddressDto;
import com.address.model.dto.AddressRequest;
import com.address.model.dto.AddressRequestDto;
import com.address.model.entity.Address;
import com.address.repository.AddressRepository;
import com.address.service.AddressService;
import com.commomlib.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class AddressServiceImpl implements AddressService {

    Logger log = LoggerFactory.getLogger(AddressServiceImpl.class);

    private final AddressRepository addressRepository;
    private final ModelMapper modelMapper;
    private final EmployeeClient employeeClient;

    @Value("${greeting}")
    private String greeting;

    @Value("${common.greeting}")
    private String commonGreeting;

    public AddressServiceImpl(AddressRepository addressRepository,
                              ModelMapper modelMapper,
                              EmployeeClient employeeClient) {
        this.addressRepository = addressRepository;
        this.modelMapper = modelMapper;
        this.employeeClient = employeeClient;
    }


    @Override
    public List<AddressDto> saveAddress(AddressRequest addressRequest) {
        employeeClient.getSingleEmployee(addressRequest.getEmpId());
        List<Address> listToSave = this.saveOrUpdateAddressRequest(addressRequest);
        listToSave.forEach(address -> address.setCreatedAt(LocalDateTime.now()));
        List<Address> savedAddress = addressRepository.saveAll(listToSave);
        return savedAddress.stream().map(address -> modelMapper.map(address, AddressDto.class)).toList();
    }

    @Override
    public List<AddressDto> updateAddress(AddressRequest addressRequest) {
        employeeClient.getSingleEmployee(addressRequest.getEmpId());
        List<Address> addressByEmpId = addressRepository.findAllByEmpId(addressRequest.getEmpId());
        if(addressByEmpId.isEmpty()){
            log.info("No address found for employee id {}", addressRequest.getEmpId());
            log.info("Creating new address for employee id {}", addressRequest.getEmpId());
        }

        List<Address> listToUpdate = this.saveOrUpdateAddressRequest(addressRequest);

        List<Long> upcomingNonNullIds = listToUpdate.stream().map(Address::getId).filter(Objects::nonNull).toList();
        List<Long> existingIds = addressByEmpId.stream().map(Address::getId).toList();

        List<Long> idsToDelete = existingIds.stream().filter(id -> !upcomingNonNullIds.contains(id)).toList();
        if(!idsToDelete.isEmpty()){
            addressRepository.deleteAllById(idsToDelete);
        }
        listToUpdate.forEach(address -> address.setUpdatedAt(LocalDateTime.now()));

        List<Address> updatedAddress = addressRepository.saveAll(listToUpdate);
        return updatedAddress.stream().map(address -> modelMapper.map(address, AddressDto.class)).toList();
    }

    @Override
    public AddressDto getSingleAddress(Long id) {
        Address address = addressRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + id));
        return modelMapper.map(address, AddressDto.class);
    }

    @Override
    public List<AddressDto> getAllAddress() {
//        try {
//            Thread.sleep(6000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        List<Address> all = addressRepository.findAll();
        if(all.isEmpty()){
            throw new ResourceNotFoundException("No address found");
        }
        return all.stream().map(address -> modelMapper.map(address, AddressDto.class)).toList();
    }

    @Override
    public void deleteAddress(Long id) {
        Address address = addressRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + id));
        addressRepository.delete(address);
    }

    @Override
    public List<AddressDto> getAddressByEmpId(Long empId) {
        System.out.println("Greeting: " + greeting);
        System.out.println("Common Greeting: " + commonGreeting);
        List<Address> addressByEmpId = addressRepository.findAllByEmpId(empId);
        if(addressByEmpId.isEmpty()){
            throw new ResourceNotFoundException("No address found for employee id: " + empId);
        }
        return addressByEmpId.stream().map(address -> modelMapper.map(address, AddressDto.class)).toList();
    }

    private List<Address> saveOrUpdateAddressRequest(AddressRequest addressRequest){
        List<Address> listToSave = new ArrayList<>();
        for(AddressRequestDto addressRequestDto : addressRequest.getAddressRequestDtoList()) {
            Address address = new Address();
            address.setId(addressRequestDto.getId() != null ? addressRequestDto.getId() : null);
            address.setStreet(addressRequestDto.getStreet());
            address.setCity(addressRequestDto.getCity());
            address.setCountry(addressRequestDto.getCountry());
            address.setPinCode(addressRequestDto.getPinCode());
            address.setAddressType(addressRequestDto.getAddressType());
            address.setEmpId(addressRequest.getEmpId());
            listToSave.add(address);
        }
        return listToSave;
    }
}
