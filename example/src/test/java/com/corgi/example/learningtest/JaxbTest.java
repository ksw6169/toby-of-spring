package com.corgi.example.learningtest;

import com.corgi.example.xml.SqlType;
import com.corgi.example.xml.Sqlmap;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JaxbTest {

    @Test
    void readSqlmap() throws JAXBException, IOException {
        // 바인딩용 클래스를 이용해 JAXB 컨텍스트를 만든다.
        JAXBContext context = JAXBContext.newInstance(Sqlmap.class);

        // 언마샬러 생성
        Unmarshaller unmarshaller = context.createUnmarshaller();

        // 생성한 언마샬러를 이용해 지정한 경로의 XML 파일 읽어와서 언마샬링 수행(XML to Java Object)
        Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(new ClassPathResource("sqlmap.xml").getInputStream());

        // List에 담겨 있는 Sql 오브젝트를 가져와 XML 문서와 같은 정보를 갖고 있는지 확인한다.
        List<SqlType> sqlList = sqlmap.getSql();

        assertEquals(3, sqlList.size());
        assertEquals("add", sqlList.get(0).getKey());
        assertEquals("insert", sqlList.get(0).getValue());
        assertEquals("get", sqlList.get(1).getKey());
        assertEquals("select", sqlList.get(1).getValue());
        assertEquals("delete", sqlList.get(2).getKey());
        assertEquals("delete", sqlList.get(2).getValue());
    }
}
