package de.burak_dogan.bachelorarbeitbackend.service;

import de.burak_dogan.bachelorarbeitbackend.domain.Student;
import de.burak_dogan.bachelorarbeitbackend.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }


    public void create(Student entity){
        studentRepository.save(entity);
    }
}
