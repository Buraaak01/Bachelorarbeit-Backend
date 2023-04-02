package de.burak_dogan.bachelorarbeitbackend.controller;

import de.burak_dogan.bachelorarbeitbackend.domain.Student;
import de.burak_dogan.bachelorarbeitbackend.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;

@RestController
@Slf4j
public class StudentController {

    private final StudentService studentService;


    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/createStudent")
    @ResponseBody
    void create(){
        Student student = Student.builder().name("Zekiye").timestamp(OffsetDateTime.now()).build();
        studentService.create(student);
    }

}
