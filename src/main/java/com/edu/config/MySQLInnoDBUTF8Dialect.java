package com.edu.config;

import org.hibernate.dialect.MySQL57InnoDBDialect;

public class MySQLInnoDBUTF8Dialect extends MySQL57InnoDBDialect {
    @Override
    public String getTableTypeString() {
        return " ENGINE=InnoDB DEFAULT CHARSET=utf8";
    }
}
