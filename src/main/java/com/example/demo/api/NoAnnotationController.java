package com.example.demo.api;

import lab.idioglossia.row.context.RowContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NoAnnotationController {
    @GetMapping("/n1")
    public @ResponseBody
    SampleDto sayHi(){
        return new SampleDto("hi");
    }

    @GetMapping("/n2")
    public @ResponseBody
    SampleDto checkRow(){
        return new SampleDto("Is this a row request? " + RowContextHolder.getContext().isRowRequest());
    }
}
