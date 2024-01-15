package fpt.gymmanagement.processor;

import fpt.gymmanagement.dto.BlogTypeDTO;
import fpt.gymmanagement.service.BlogTypeService;
import fpt.gymmanagement.transform.BlogTypeTransform;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BlogTypeProcessorTest {
    @Autowired
    BlogTypeProcessor processor;

    @Autowired
    BlogTypeTransform transform;

    @Autowired
    BlogTypeService service;

    @BeforeEach
    void setup() {
        processor = Mockito.mock(BlogTypeProcessor.class);
        transform = Mockito.mock(BlogTypeTransform.class);
        service = Mockito.mock(BlogTypeService.class);
    }

    static BlogTypeDTO initialSuccess() {
        return BlogTypeDTO.builder().id("1").name("Ăn uống").build();
    }
    @Test
    void testCreateSuccess() {
        BlogTypeDTO dto = initialSuccess();
        BlogTypeDTO response = BlogTypeDTO.builder().id("1").name("Ăn uống").build();
        Mockito.when(processor.saveOrUpdate(dto, null)).thenReturn(response);
        var result = processor.saveOrUpdate(dto, null);
        Assertions.assertEquals(result, response);
    }
}
