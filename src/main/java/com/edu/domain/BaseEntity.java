package com.edu.domain;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

@MappedSuperclass
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    protected long id;

	@Column(insertable = true, updatable = false)
	private Timestamp createdAt;

	@Column(insertable = true, updatable = true)
	private Timestamp updatedAt;

	@Version
	private long version;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

}
