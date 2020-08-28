package com.example.demo.service.publisher;

import com.example.demo.api.SampleDto;
import lab.idioglossia.row.annotations.Publish;

@Publish(async = true, value = "test")
public interface TestEventPublisher {
    void publish(SampleDto sampleDto);
}
