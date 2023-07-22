package com.mmk.courseinfo.cli.service;

import com.mmk.courseinfo.domain.Course;
import com.mmk.courseinfo.repository.CourseRepository;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CourseStorageServiceTest {

    @Test
    void storePluralsightCourses() {
        CourseRepository courseRepository = new InMemoryRepository();
        CourseStorageService courseStorageService = new CourseStorageService(courseRepository);

        PluralsightCourse ps1 = new PluralsightCourse("1", "Title 1", "01:40:00.123", "/url-1", false);
        courseStorageService.storePluralsightCourses(List.of(ps1));

        Course expected = new Course("1", "Title 1", 100, "https://app.pluralsight.com/url-1", Optional.empty());

        assertEquals(List.of(expected), courseRepository.getAllCourses());
    }

    static class InMemoryRepository implements CourseRepository {

        private final List<Course> courses = new ArrayList();

        @Override
        public void saveCourse(Course course) {
            courses.add(course);
        }

        @Override
        public List<Course> getAllCourses() {
            return courses;
        }

        @Override
        public void addNotes(String id, String notes) {
            throw new UnsupportedOperationException("Adding is not supported");
        }
    }
}