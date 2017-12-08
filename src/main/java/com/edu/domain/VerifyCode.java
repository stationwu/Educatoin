package com.edu.domain;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "verify_code")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerifyCode {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "code",
        length = 6,
        nullable = true)
    private String code;

    @Column(name = "mobile",
        length = 11,
        nullable = true)
    private String mobile;

    @Column(name = "exceed_time")
    private Timestamp exceedTime;
    
    public VerifyCode(String code, String mobile) {
        this.code = code;
        this.mobile = mobile;
        this.exceedTime = new Timestamp(new Date().getTime() + 5 * 60 * 1000);
    }
}
