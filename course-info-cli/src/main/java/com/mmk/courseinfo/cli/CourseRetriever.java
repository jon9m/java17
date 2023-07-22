package com.mmk.courseinfo.cli;

import com.mmk.courseinfo.cli.service.CourseRetrievalService;
import com.mmk.courseinfo.cli.service.CourseStorageService;
import com.mmk.courseinfo.cli.service.PluralsightCourse;
import com.mmk.courseinfo.repository.CourseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static java.util.function.Predicate.not;

public class CourseRetriever {

    private static final Logger LOG = LoggerFactory.getLogger(CourseRetriever.class);

    public static void main(String[] args) {
        LOG.info("Courser Retriever starting...");
        if (args.length == 0) {
            LOG.warn("Please provide author name");
            return;
        }

        try {
            retrieveCourses(args[0]);
        } catch (Exception e) {
            LOG.error("Unexpected error");
            e.printStackTrace();
        }
    }

    private static void retrieveCourses(String authorId) {
        LOG.info("Retrieving courses for the author '{}'", authorId);
        CourseRetrievalService courserRetrievalService = new CourseRetrievalService();

        List<PluralsightCourse> coursesFor = courserRetrievalService.getCoursesFor(authorId);
        List<PluralsightCourse> pluralsightCourses = coursesFor
                .stream()
                .filter(not(PluralsightCourse::isRetired))
                .toList();

        CourseRepository courseRepository = CourseRepository.openCourseRepository("./courses.db");
        CourseStorageService courseStorageService = new CourseStorageService(courseRepository);
        courseStorageService.storePluralsightCourses(coursesFor);
        LOG.info("Retrieved following {} courses {} for {}", pluralsightCourses.size(), pluralsightCourses, authorId);
    }

}
