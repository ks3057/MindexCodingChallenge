package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {

    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    private int getNumberOfReports(Employee employee){
        LOG.debug("Fetching number of reports for [{}]", employee.getEmployeeId());

        Queue<Employee> queue = new LinkedList<>();
        queue.add(employee);
        int directReports = 0;

        while (!queue.isEmpty()) {
            int size = queue.size();
            directReports += size;
            for (int i = 0; i < size; i++) {
                Employee e = queue.poll();
                if (e != null) {
                    List<Employee> reports = e.getDirectReports();
                    if (reports != null) {
                        queue.addAll(reports);
                    }
                }
            }
        }

        return directReports > 0 ? directReports - 1 : 0;
    }

    @Override
    public ReportingStructure read(String id) {

        ReportingStructure reportingStructure = new ReportingStructure();

        Employee employee = employeeRepository.findByEmployeeId(id);
        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        reportingStructure.setNumberOfReports(getNumberOfReports(employee));
        reportingStructure.setEmployee(employee);
        return reportingStructure;
    }
}
