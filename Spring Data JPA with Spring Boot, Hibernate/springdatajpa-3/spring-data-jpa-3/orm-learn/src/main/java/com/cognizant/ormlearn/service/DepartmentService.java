package com.cognizant.ormlearn.service;

import com.cognizant.ormlearn.model.Department;
import com.cognizant.ormlearn.repository.DepartmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service layer for Department operations.
 * Used in Hands-on 4, 5.
 */
@Service
public class DepartmentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentService.class);

    @Autowired
    private DepartmentRepository departmentRepository;

    /**
     * Fetch department by id.
     * Because @OneToMany(fetch=EAGER) is set, employeeList is loaded too.
     */
    @Transactional
    public Department get(int id) {
        LOGGER.info("Start get: id={}", id);
        Department department = departmentRepository.findById(id).get();
        LOGGER.info("End get");
        return department;
    }

    /** Persist (insert or update) a department. */
    @Transactional
    public void save(Department department) {
        LOGGER.info("Start save");
        departmentRepository.save(department);
        LOGGER.info("End save: department={}", department);
    }
}
