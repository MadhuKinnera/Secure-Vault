package com.madhu.utility;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

@Service
public class ResourceService {

    private final ResourceLoader resourceLoader;

    public ResourceService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public String getResourcesPath() {
        try {
            Resource resource = resourceLoader.getResource("classpath:");
            return resource.getFile().getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null; 
        }
    }
}
