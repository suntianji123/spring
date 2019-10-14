package org.springframework.core.io;

import org.apache.logging.log4j.core.util.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.util.FileCopyUtils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;


/**
 * 测试Resource
 */
class ResourceTests {

    /**
     * 测试ByteArrayResource
     * @throws IOException
     */
    @Test
    void byteArrayResource() throws IOException{
        Resource resource = new ByteArrayResource("testString".getBytes());
        assertThat(resource.exists()).isTrue();
        assertThat(resource.isOpen()).isFalse();
        String content = FileCopyUtils.copyToString(new InputStreamReader(resource.getInputStream()));
        assertThat(content).isEqualTo("testString");
        assertThat(new ByteArrayResource("testString".getBytes())).isEqualTo(resource);
    }

    /**
     * 测试带描述的byteArrayResource
     * @throws IOException
     */
    @Test
    void byteArrayResourceWithDescription() throws IOException{
        Resource resource = new ByteArrayResource("my description","testString".getBytes());
        assertThat(resource.exists()).isTrue();
        assertThat(resource.isOpen()).isFalse();
        String content = FileCopyUtils.copyToString(new InputStreamReader(resource.getInputStream()));
        assertThat(content).isEqualTo("testString");
        assertThat(resource.getDescription().contains("my description")).isTrue();
        assertThat(new ByteArrayResource("testString".getBytes())).isEqualTo(resource);
    }

    @Test
    void inputStreamResource() throws IOException{
        InputStream is = new ByteArrayInputStream("testString".getBytes());
        Resource resource = new InputStreamResource(is);
        assertThat(resource.exists()).isTrue();
        assertThat(resource.isOpen()).isTrue();
        String content = FileCopyUtils.copyToString(new InputStreamReader(resource.getInputStream()));
        assertThat(content).isEqualTo("testString");
        assertThat(new InputStreamResource(is)).isEqualTo(resource);
    }

    @Test
    void classPathResource() throws IOException{
        Resource resource = new ClassPathResource("org/springframework/core/io/Resource.class");
        doTestResource(resource);
    }

    private void doTestResource(Resource resource) throws IOException{
        assertThat(resource.getFilename()).isEqualTo("Resource.class");
        assertThat(resource.getURL().getFile().endsWith("Resource.class")).isTrue();
        assertThat(resource.exists()).isTrue();
        assertThat(resource.isReadable()).isTrue();
        assertThat(resource.contentLength() >0).isTrue();
        assertThat(resource.lastModified() >0).isTrue();

        Resource resource1 = resource.createRelative("ClassPathResource.class");
        assertThat(resource1.getFilename()).isEqualTo("ClassPathResource.class");
        assertThat(resource1.getURL().getFile().endsWith("ClassPathResource.class")).isTrue();
        assertThat(resource1.exists()).isTrue();
        assertThat(resource1.isReadable()).isTrue();
        assertThat(resource1.contentLength()>0).isTrue();
        assertThat(resource1.lastModified()>0).isTrue();

        Resource resource3 =  resource.createRelative("../NestedIOException.class");
        assertThat(resource3.getURL().getFile().endsWith("NestedIOException.class")).isTrue();
        assertThat(resource3.exists()).isTrue();
        assertThat(resource3.isReadable()).isTrue();
        assertThat(resource3.contentLength()>0).isTrue();
        assertThat(resource3.lastModified()>0).isTrue();

        Resource resource4 = resource.createRelative("X.class");
        assertThat(resource4.exists()).isFalse();
        assertThat(resource4.isReadable()).isFalse();
        assertThatExceptionOfType(FileNotFoundException.class).isThrownBy(resource4::contentLength);
        assertThatExceptionOfType(FileNotFoundException.class).isThrownBy(resource4::lastModified);
    }

    @Test
    void classPathResourceWithRelativePath() throws IOException{
        Resource resource = new ClassPathResource("dir/");
        Resource relative = resource.createRelative("subdir");
        assertThat(relative).isEqualTo(new ClassPathResource("dir/subdir"));
    }

    @Test
    void fileSystemResourceWithRelativePath() throws IOException{
        Resource resource = new FileSystemResource("dir/");
        Resource relative = resource.createRelative("subdir");
        assertThat(relative).isEqualTo(new FileSystemResource("dir/subdir"));
    }

    @Test
    void urlResourceWithRelativePath() throws IOException{
        Resource resource = new UrlResource("file:dir/");
        Resource relative = resource.createRelative("subdir");
        assertThat(relative).isEqualTo(new UrlResource("file:dir/subdir"));
    }

    @Test
    void testNonFileResourceExists() throws IOException{
        Resource resource = new UrlResource("https://spring.io/");
        assertThat(resource.exists()).isTrue();
    }

    @Test
    void abstractResourceExceptions(){
        final String name = "test-resource";

        Resource resource = new AbstractResource() {
            @Override
            public String getDescription() {
                return name;
            }

            @Override
            public InputStream getInputStream() throws IOException {
                throw new FileNotFoundException();
            }
        };

        assertThatExceptionOfType(FileNotFoundException.class).isThrownBy(resource::getURL).withMessageContaining(name);
        assertThatExceptionOfType(FileNotFoundException.class).isThrownBy(resource::getFile).withMessageContaining(name);
        assertThatExceptionOfType(FileNotFoundException.class).isThrownBy(()->resource.createRelative("/test")).withMessageContaining(name);
        assertThat(resource.getFilename()).isNull();
    }

    @Test
    void contentLength() throws IOException{
        Resource resource = new AbstractResource() {
            @Override
            public String getDescription() {
                return "";
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return new ByteArrayInputStream(new byte[]{'a','b','c'});
            }
        };

        assertThat(resource.contentLength()).isEqualTo(3l);
    }

    @Test
    void readableChannel() throws IOException{
        Resource resource = new FileSystemResource(getClass().getResource("Resource.class").getFile());
        ReadableByteChannel channel = null;
        try{
            channel = resource.readableByteChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate((int)resource.contentLength());
            channel.read(byteBuffer);

            //将写入到的bytebuffer的指针指向0
            byteBuffer.rewind();
            assertThat(byteBuffer.limit()>0).isTrue();
        }finally {
            if(channel != null){
                channel.close();
            }
        }
    }

    @Test
    void inputStreamNotFoundOnFileSystemResource(){
        assertThatExceptionOfType(FileNotFoundException.class).
                isThrownBy(()->new FileSystemResource(getClass().getResource("Resource.class").
                        getFile()).createRelative("X").getInputStream());
    }

    @Test
    void readableByteChannelNotFoundOnFileSystemResource(){
        assertThatExceptionOfType(FileNotFoundException.class).isThrownBy(()->new FileSystemResource(getClass().
                getResource("Resource.class").getFile()).createRelative("X").readableByteChannel());
    }

    @Test
    void inputStreamOnNotFoundClassPathResource(){
        assertThatExceptionOfType(FileNotFoundException.class).isThrownBy(()->
                new ClassPathResource("Resource.class",getClass()).createRelative("X").getInputStream());
    }

    @Test
    void readableByteChannelNotFoundOnClassPathResource(){
        assertThatExceptionOfType(FileNotFoundException.class).isThrownBy(()->
                new ClassPathResource("Resource.class",getClass()).createRelative("X").readableByteChannel());
    }

}
