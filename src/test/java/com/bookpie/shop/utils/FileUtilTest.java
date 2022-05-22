package com.bookpie.shop.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class FileUtilTest {

    final StringBuffer filename = new StringBuffer("test.png");
    final byte[] content = "test image".getBytes();

    @Test
    public void fileUploadTest() throws Exception{
        //given
        MockMultipartFile file = new MockMultipartFile("content",filename.toString(),"multipart/mixec",content);
        //when
        String save = FileUtil.save("./tmp", file);
        //then
        assertEquals(save,"./tmp/"+filename);
    }

}
