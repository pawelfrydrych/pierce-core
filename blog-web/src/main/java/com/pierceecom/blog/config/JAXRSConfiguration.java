package com.pierceecom.blog.config;

import com.pierceecom.blog.controllers.HelloPierceResource;
import com.pierceecom.blog.controllers.PostController;
import com.pierceecom.blog.services.PostService;

import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/")
public class JAXRSConfiguration  extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        HashSet<Class<?>> classes = new HashSet<>();
        classes.add(HelloPierceResource.class);
		classes.add(PostController.class);
        return classes;
    }
}
