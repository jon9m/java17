package com.mmk.courseinfo.repository;

import com.mmk.courseinfo.domain.Course;
import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

class CourseJdbcRepository implements CourseRepository {

    private static final String H2_DATABASE_URL = "jdbc:h2:file:%s;AUTO_SERVER=TRUE;INIT=RUNSCRIPT FROM './course-info-repository/db_init.sql'";
    private static final String INSERT_COURSE = """
                MERGE INTO COURSES(ID, NAME, LENGTH, URL) VALUES(?, ?, ?,?)
            """;
    private static final String ADD_NOTES = """
            UPDATE COURSES SET NOTES = ? WHERE ID = ?
            """;

    private final DataSource dataSource;

    public CourseJdbcRepository(String databaseFile) {
        JdbcDataSource jdbcDataSource = new JdbcDataSource();
        jdbcDataSource.setURL(H2_DATABASE_URL.formatted(databaseFile));
        this.dataSource = jdbcDataSource;
    }

    @Override
    public void saveCourse(Course course) {

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_COURSE)) {

            preparedStatement.setString(1, course.id());
            preparedStatement.setString(2, course.name());
            preparedStatement.setLong(3, course.length());
            preparedStatement.setString(4, course.url());

            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RepositoryException("Failed to save the record", e);
        }
    }

    @Override
    public List<Course> getAllCourses() {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM COURSES");

            List<Course> courses = new ArrayList<>();
            while (resultSet.next()) {
                Course course = new Course(resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getLong(3),
                        resultSet.getString(4),
                        Optional.ofNullable(resultSet.getString(5)));
                courses.add(course);
            }
            return Collections.unmodifiableList(courses);
        } catch (SQLException e) {
            throw new RepositoryException("Failed to save the record", e);
        }
    }

    @Override
    public void addNotes(String id, String notes) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(ADD_NOTES)) {

            preparedStatement.setString(1, notes);
            preparedStatement.setString(2, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RepositoryException("Failed to add notes to the record " + id, e);
        }
    }
}
