package com.mmk.courseinfo.server;

import com.mmk.courseinfo.domain.Course;
import com.mmk.courseinfo.repository.CourseRepository;
import com.mmk.courseinfo.repository.RepositoryException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Path("/courses")
public class CourseResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(CourseResource.class);

    private final CourseRepository courseRepository;

    public CourseResource(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
//    public List<Course> getCourses() {
    public Stream<Course> getCourses() {
        try {
            return courseRepository.getAllCourses().stream().sorted(Comparator.comparing(Course::id));
//                    .toList();
        } catch (RepositoryException e) {
            LOGGER.error("Could not retrieve courses from the database ", e);
            throw new NotFoundException();
        }
    }

    @POST
    @Path("/{id}/notes")
    @Consumes(MediaType.TEXT_PLAIN)
    public void addNotes(@PathParam("id") String id, String notes) {
        try {
            courseRepository.addNotes(id, notes);
        } catch (RepositoryException e) {
            LOGGER.error("Could not add notes to the record ", e);
        }
    }

}
