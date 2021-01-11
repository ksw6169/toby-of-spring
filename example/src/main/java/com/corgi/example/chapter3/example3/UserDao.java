package com.corgi.example.chapter3.example3;

import com.corgi.example.chapter3.example3.entity.User;
import com.corgi.example.chapter3.example3.statement.AddStatement;
import com.corgi.example.chapter3.example3.statement.DeleteAllStatement;
import com.corgi.example.chapter3.example3.statement.StatementStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Slf4j
@RequiredArgsConstructor
@Repository(value = "chapter3UserDao3")
public class UserDao {

    private final DataSource dataSource;

    /**
     * 컨텍스트 메소드를 호출하는 클라이언트 코드
     */
    public void deleteAll() throws SQLException {
        StatementStrategy st = new DeleteAllStatement();    // 구체적인 전략 오브젝트 생성
        jdbcContextWithStatementStrategy(st);                // context 호출. 전략 오브젝트 전달
    }

    public void add(User user) throws SQLException {
        StatementStrategy st = new AddStatement(user);
        jdbcContextWithStatementStrategy(st);
    }

    /**
     * 컨텍스트 메소드
     *
     * @param   st    클라이언트가 컨텍스트를 호출할 때 넘겨주는 전략 파라미터
     * @throws  SQLException
     */
    public void jdbcContextWithStatementStrategy(StatementStrategy st) throws SQLException {

        Connection c = null;
        PreparedStatement ps = null;

        try {
            c = dataSource.getConnection();
            ps = st.makePreparedStatement(c);
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw e;
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    log.error(e.getMessage());
                }
            }

            if (c != null) {
                try {
                    c.close();
                } catch (SQLException e) {
                    log.error(e.getMessage());
                }
            }
        }
    }
}
