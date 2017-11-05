package com.edu.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

@Entity(name = "tester")
@Table(name = "tester")
public class Tester implements Identifiable<String> {

    @Id
    @GenericGenerator(
            name = "assigned-sequence",
            strategy = "com.edu.domain.StudentIdentifierGenerator",
            parameters = {
                    @Parameter(name = "sequence_prefix", value = "M"),
            }
    )
    @GeneratedValue(generator = "assigned-sequence", strategy = GenerationType.TABLE)
    private String id;

    @Version
    private Integer version;

    public Tester() {
    }

    public Tester(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }
}