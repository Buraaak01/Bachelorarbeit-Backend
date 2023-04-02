package de.burak_dogan.bachelorarbeitbackend.repository;

import de.burak_dogan.bachelorarbeitbackend.domain.Student;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends CrudRepository<Student, Long> {
}
