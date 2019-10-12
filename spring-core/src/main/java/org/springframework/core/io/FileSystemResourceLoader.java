package org.springframework.core.io;

/**
 * 文件系统资源加载器
 */
public class FileSystemResourceLoader extends DefaultResouceLoader {

    /**
     * 根据路径获取资源
     * @param path 路径
     * @return
     */
    @Override
    protected Resource getResourceByPath(String path) {
        if(path.startsWith("/")){
            path  = path.substring(1);
        }

        return new FileSystemContextResource(path);
    }

    private  static class FileSystemContextResource extends FileSystemResource implements ContextResource {

        public FileSystemContextResource(String path){
            super(path);
        }

        @Override
        public String getPathWithinContext() {
            return getPath();
        }
    }
}
