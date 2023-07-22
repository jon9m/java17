package com.mmk.courseinfo.server;

import com.mmk.courseinfo.repository.CourseRepository;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Properties;
import java.util.logging.LogManager;

public class CoreServer {
    static {
        LogManager.getLogManager().reset();
        SLF4JBridgeHandler.install();
    }

    private static final Logger LOG = LoggerFactory.getLogger(CoreServer.class);
    private static final String BASE_URI = "http://localhost:8080/";

    public static void main(String[] args) {
        String databaseFile = loadDatabaseFile();

        LOG.info("Starting HTTP server with database file " + databaseFile);

        CourseRepository courseRepository = CourseRepository.openCourseRepository(databaseFile);
        ResourceConfig resourceConfig = new ResourceConfig().register(new CourseResource(courseRepository));
        GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), resourceConfig);
    }

    private static String loadDatabaseFile() {
        try (InputStream inputStream = CoreServer.class.getResourceAsStream("/server.properties")) {
            Properties properties = new Properties();
            properties.load(inputStream);
            return (String) properties.get("course-info.database");
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
