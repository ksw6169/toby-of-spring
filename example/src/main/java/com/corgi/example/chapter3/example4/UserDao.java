package com.corgi.example.chapter3.example4;

import com.corgi.example.chapter3.example4.entity.User;
import com.corgi.example.chapter3.example4.statement.StatementStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * [Legacy 4]
 * 템플릿 메소드 패턴, 전략 패턴 사용 시 클래스 파일이 많아지는 단점을 해결하기 위해
 * 전략 클래스를 독립적으로 만들지 말고 UserDao 클래스 안에 내부 클래스(=로컬 클래스)로 정의해서 사용
 * (AddStatement 같은 클래스는 UserDao의 특정 메소드에서만 사용하기 때문에
 * 재활용이 불가능하므로 내부 클래스로 정의해서 사용하는 것을 고려해볼 수 있음)
 */
@Slf4j
@RequiredArgsConstructor
@Repository(value = "chapter3UserDao4")
public class UserDao {

    private final DataSource dataSource;

    public void deleteAll() throws SQLException {

        class DeleteAllStatement implements StatementStrategy {
            @Override
            public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                return c.prepareStatement("delete from users");
            }
        }

        StatementStrategy st = new DeleteAllStatement();
        jdbcContextWithStatementStrategy(st);
    }

    public void add(final User user) throws SQLException {

        class AddStatement implements StatementStrategy {
            @Override
            public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                PreparedStatement ps = c.prepareStatement("insert into users(id, name,password) values(?,?,?)");
                ps.setString(1, user.getId());
                ps.setString(2, user.getName());
                ps.setString(3, user.getPassword());
                return ps;
            }
        }

        StatementStrategy st = new AddStatement();
        jdbcContextWithStatementStrategy(st);
    }

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
