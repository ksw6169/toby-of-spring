package com.corgi.example.chapter3.example9;

import com.corgi.example.chapter3.example9.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository(value = "chapter3UserDao9")
public class UserDao {

    @Autowired
    @Qualifier(value = "chapter3JdbcTemplate9")
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RowMapper<User> userMapper;

    public int deleteAll() {
        /**
         * [콜백을 직접 만들어 전달한 경우]
         */
//        return jdbcTemplate.update(new PreparedStatementCreator() {
//            @Override
//            public PreparedStatement createPreparedStatement(Connection c) throws SQLException {
//                return c.prepareStatement("delete from users");
//            }
//        });

        /**
         * [JdbcTemplate의 내장 콜백을 사용한 경우]
         * update()에 SQL문을 전달하면 JdbcTemplate의 내장 콜백을 사용하여 내부 실행 메소드를 호출함
         * JdbcTemplate에서 쿼리 실행 시 Statement, ResultSet, Connection을 반환하기 때문에 간편하게 사용 가능
         */
        return jdbcTemplate.update("delete from users");
    }

    public int add(User user) {
        /**
         * [콜백을 직접 만들어 전달한 경우]
         */
//        return jdbcTemplate.update(new PreparedStatementCreator() {
//            @Override
//            public PreparedStatement createPreparedStatement(Connection c) throws SQLException {
//                PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values(?,?,?)");
//                ps.setString(1, user.getId());
//                ps.setString(2, user.getName());
//                ps.setString(3, user.getPassword());
//                return ps;
//            }
//        });


        /**
         * [JdbcTemplate의 내장 콜백을 사용한 경우]
         * 바인딩할 파라미터는 순서대로 넣어주면 된다.
         */
        return jdbcTemplate.update("insert into users(id, name, password) values(?,?,?)",
                user.getId(), user.getName(), user.getPassword());
    }

    public int getCount() {
        /**
         * [콜백을 직접 만들어 전달한 경우]
         */
//        return jdbcTemplate.query(
//            // 첫번째 콜백 - PreparedStatement 생성
//            new PreparedStatementCreator() {
//                @Override
//                public PreparedStatement createPreparedStatement(Connection c) throws SQLException {
//                    return c.prepareStatement("select count(*) from users");
//                }
//            },
//
//            // 두번째 콜백 - PreparedStatement 실행 결과로 받은 ResultSet에서 데이터 알맞게 가공
//            new ResultSetExtractor<Integer>() {
//                @Override
//                public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
//                    rs.next();
//                    return rs.getInt(1);
//                }
//            }
//        );

        /**
         * [JdbcTemplate의 내장 콜백을 사용한 경우]
         * jdbcTemplate.queryForInt()는 @Deprecated 되어 다른 방법을 사용해야 한다.
         */
        return jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
    }

    public User get(String id) {
        /**
         * ResultSetExtractor는 ResultSet을 한 번만 전달해주기 때문에 수동으로 반복문을 돌려서 원하는 타입에 맞게 매핑해줘야 되지만,
         * RowMapper는 ResultSet의 각 행을 반복적으로 리턴해주기 때문에 반복문 없이 각 행을 원하는 타입에 맞게 매핑할 수 있음
         */
        return jdbcTemplate.queryForObject("select * from users where id = ?", new Object[]{id}, userMapper);
//                new RowMapper<User>() {
//                    @Override
//                    public User mapRow(ResultSet rs, int i) throws SQLException {
//                        return User.builder()
//                                .id(rs.getString("id"))
//                                .name(rs.getString("name"))
//                                .password(rs.getString("password"))
//                                .build();
//                    }
//                }
    }

    public List<User> getAll() {
        return jdbcTemplate.query("select * from users order by id", userMapper);
//                new RowMapper<User>() {
//                    @Override
//                    public User mapRow(ResultSet rs, int i) throws SQLException {
//                        return User.builder()
//                                .id(rs.getString("id"))
//                                .name(rs.getString("name"))
//                                .password(rs.getString("password"))
//                                .build();
//                    }
//                });
    }
}
