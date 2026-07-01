package com.cognizant.ormlearn.service;

import com.cognizant.ormlearn.model.Employee;
import com.cognizant.ormlearn.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service layer for Employee operations.
 * Used in Hands-on 4, 5, 6.
 */
@Service
public class EmployeeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeService.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * Fetch employee by id.
     * Because @ManyToOne is EAGER by default, department is also fetched.
     * Because @ManyToMany on skillList is set to EAGER, skills are also fetched.
     */
    @Transactional
    public Employee get(int id) {
        LOGGER.info("Start get: id={}", id);
        Employee employee = employeeRepository.findById(id).get();
        LOGGER.info("End get");
        return employee;
    }

    /**
     * Persist (insert or update) an employee.
     * Hibernate determines INSERT vs UPDATE based on whether the id is 0.
     */
    @Transactional
    public void save(Employee employee) {
        LOGGER.info("Start save");
        employeeRepository.save(employee);
        LOGGER.info("End save: employee={}", employee);
    }
}
