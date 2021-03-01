package com.corgi.example.service.sql;

import com.corgi.example.exception.SqlNotFoundException;
import com.corgi.example.exception.SqlRetrievalFailureException;
import com.corgi.example.service.sql.SqlReader;
import com.corgi.example.service.sql.SqlRegistry;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseSqlService {
    private SqlReader sqlReader;
    private SqlRegistry sqlRegistry;

    protected void loadSql() {
        this.sqlReader.read(this.sqlRegistry);
    }

    protected String getSql(String key) throws SqlRetrievalFailureException {
        try {
            return this.sqlRegistry.findSql(key);
        } catch (SqlNotFoundException e) {
            throw new SqlRetrievalFailureException(e);
        }
    }

    protected void read() {
        this.sqlReader.read(this.sqlRegistry);
    }
}
