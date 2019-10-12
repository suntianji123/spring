package org.springframework.core.io;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * ClassPath相对路径资源加载器
 */
public class ClassRelativeResourceLoader extends DefaultResouceLoader {

    private final Class<?> clazz;

    public ClassRelativeResourceLoader(Class<?> clazz){
        Assert.notNull(clazz,"Class must not be null");
        this.clazz = clazz;
        setClassLoader(clazz.getClassLoader());
    }

    @Override
    protected Resource getResourceByPath(String path) {
        return new ClassRelativeContextResource(path,clazz);
    }

    private static class ClassRelativeContextResource extends ClassPathResource implements ContextResource {

        private final Class<?> clazz;

        /**
         *
         * @param path
         * @param clazz
         */
        public ClassRelativeContextResource(String path, Class<?> clazz) {
            super(path, clazz);
            this.clazz = clazz;
        }

        @Override
        public String getPathWithinContext() {
            return getPath();
        }

        @Override
        public Resource createRelative(String relativePath) throws IOException {
            String pathToUse = StringUtils.applyRelativePath(getPath(),relativePath);
            return new ClassRelativeContextResource(pathToUse,this.clazz);
        }
    }
}
