package org.springframework.core.io;

import org.junit.jupiter.api.Test;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;

import static org.assertj.core.api.Assertions.assertThat;


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



}
