package com.example.demo;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class BasicAlterService implements AlterService {
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void alter(SampleDto sampleDto) {
        sampleDto.setField(sampleDto.getField() + " -- Altered");
    }
}
