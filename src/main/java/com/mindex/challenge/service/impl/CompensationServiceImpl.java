package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompensationServiceImpl implements CompensationService {

    private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);

    @Autowired
    private CompensationRepository compensationRepository;

    @Autowired
    private EmployeeService employeeService;

    @Override
    public Compensation create(Compensation compensation) {
        String employeeId = compensation.getEmployee().getEmployeeId();
        Employee employee = employeeService.read(employeeId);
        //get the updated employee from database
        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + employeeId);
        }

        compensation.setEmployee(employee);
        compensationRepository.insert(compensation);
        LOG.info("Compensation created for employeeId: {}", compensationRepository.findCompensationByEmployeeEmployeeId(employeeId));
        return compensation;
    }

    @Override
    public Compensation read(String id) {
        Compensation compensation = compensationRepository.findCompensationByEmployeeEmployeeId(id);

        if (compensation == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        return compensation;
    }
}
