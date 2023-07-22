package com.mmk.courseinfo.cli.service;

import com.mmk.courseinfo.domain.Course;
import com.mmk.courseinfo.repository.CourseRepository;

import java.util.List;
import java.util.Optional;

public class CourseStorageService {
    private static final String PS_BASE_URL = "https://app.pluralsight.com";
    private final CourseRepository courseRepository;

    public CourseStorageService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public void storePluralsightCourses(List<PluralsightCourse> courses) {
        courses.forEach(plCourse -> {
            Course course =
                    new Course(plCourse.id(), plCourse.title(), plCourse.durationInMinutes(), PS_BASE_URL + plCourse.contentUrl(), Optional.empty());
            courseRepository.saveCourse(course);
        });
    }
}
