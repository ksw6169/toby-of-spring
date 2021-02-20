package com.corgi.example.learningtest;

import com.corgi.example.xml.SqlType;
import com.corgi.example.xml.Sqlmap;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.Unmarshaller;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class OxmTests {

    @Autowired
    private Unmarshaller unmarshaller;

    @Test
    void unmarshallSqlMap() throws IOException {
        Source xmlSource = new StreamSource(new ClassPathResource("sqlmap.xml").getInputStream());

        Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(xmlSource);

        List<SqlType> sqlList = sqlmap.getSql();

        assertEquals(6, sqlList.size());
        assertEquals("userAdd", sqlList.get(0).getKey());
        assertEquals("userGet", sqlList.get(1).getKey());
        assertEquals("userGetAll", sqlList.get(2).getKey());
        assertEquals("userGetCount", sqlList.get(3).getKey());
        assertEquals("userDeleteAll", sqlList.get(4).getKey());
        assertEquals("userUpdate", sqlList.get(5).getKey());
    }
}
