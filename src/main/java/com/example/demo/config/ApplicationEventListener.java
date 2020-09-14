package com.example.demo.config;

import com.example.demo.api.NoAnnotationController;
import lab.idioglossia.row.config.RowScanner;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationEventListener implements ApplicationListener<ApplicationStartedEvent> {
    private final RowScanner rowScanner;
    private final NoAnnotationController noAnnotationController;

    public ApplicationEventListener(RowScanner rowScanner, NoAnnotationController noAnnotationController) {
        this.rowScanner = rowScanner;
        this.noAnnotationController = noAnnotationController;
    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {
        rowScanner.processBean(noAnnotationController, NoAnnotationController.class);
    }
}
