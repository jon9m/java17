package com.mmk.courseinfo.repository;

import com.mmk.courseinfo.domain.Course;

import java.util.List;

public interface CourseRepository {
    void saveCourse(Course course);

    List<Course> getAllCourses();

    static CourseRepository openCourseRepository(String databaseFile) {
        return new CourseJdbcRepository(databaseFile);
    }

    void addNotes(String id, String notes);
}
