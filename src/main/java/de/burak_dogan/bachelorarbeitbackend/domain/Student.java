package de.burak_dogan.bachelorarbeitbackend.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.OffsetDateTime;


@Builder
@Data
public class Student {
        @Id
        Long id;

        String name;

        OffsetDateTime timestamp;



}
