package com.corgi.example.service;

import com.corgi.example.exception.SqlNotFoundException;
import com.corgi.example.exception.SqlRetrievalFailureException;
import com.corgi.example.xml.SqlType;
import com.corgi.example.xml.Sqlmap;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;

@RequiredArgsConstructor
@Service
public class OxmSqlService implements SqlService {

    private final Unmarshaller unmarshaller;
    private final String sqlmapFile;
    private final SqlRegistry sqlRegistry;
    private final OxmSqlReader oxmSqlReader = new OxmSqlReader();

    @PostConstruct
    public void loadSql() {
        this.oxmSqlReader.read(this.sqlRegistry);
    }

    @Override
    public String getSql(String key) throws SqlRetrievalFailureException {
        try {
            return this.sqlRegistry.findSql(key);
        } catch (SqlNotFoundException e) {
            throw new SqlRetrievalFailureException(e);
        }
    }

    private class OxmSqlReader implements SqlReader {
        @Override
        public void read(SqlRegistry sqlRegistry) {
            try {
                Source source = new StreamSource(new ClassPathResource(sqlmapFile).getInputStream());

                Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(source);

                for (SqlType sqlType : sqlmap.getSql()) {
                    sqlRegistry.registerSql(sqlType.getKey(), sqlType.getValue());
                }
            } catch (IOException e) {
                throw new IllegalArgumentException(sqlmapFile + "을 가져올 수 없습니다.", e);
            }
        }
    }
}
