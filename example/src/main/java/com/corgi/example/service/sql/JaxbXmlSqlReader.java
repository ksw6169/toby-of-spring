package com.corgi.example.service.sql;

import com.corgi.example.service.sql.SqlReader;
import com.corgi.example.service.sql.SqlRegistry;
import com.corgi.example.xml.SqlType;
import com.corgi.example.xml.Sqlmap;
import lombok.Setter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.InputStream;

@Component
public class JaxbXmlSqlReader implements SqlReader {
    private static final String DEFAULT_SQLMAP_FILE = "sqlmap.xml";

    @Setter
    private String sqlmapFile = DEFAULT_SQLMAP_FILE;

    /**
     * JAXB API를 이용해 XML 파일에서 데이터를 읽은 다음. Map 형태로 저장
     */
    @Override
    public void read(SqlRegistry sqlRegistry) {
        try {
            JAXBContext context = JAXBContext.newInstance(Sqlmap.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            InputStream is = new ClassPathResource(sqlmapFile).getInputStream();
            Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(is);

            for (SqlType sql : sqlmap.getSql()) {
                // SQL 저장 로직 구현에 독립적인 인터페이스 메소드를 통해 읽어들인 SQL과 키를 전달한다.
                sqlRegistry.registerSql(sql.getKey(), sql.getValue());
            }
        } catch (JAXBException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
