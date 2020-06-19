package com.example.demo.api;

import com.example.demo.service.AlterService;
import labs.psychogen.row.annotations.RowController;
import labs.psychogen.row.annotations.RowIgnore;
import labs.psychogen.row.annotations.RowQuery;
import labs.psychogen.row.context.RowContextHolder;
import org.springframework.web.bind.annotation.*;

@RowController
public class Controller {
    private final AlterService alterService;

    public Controller(AlterService alterService) {
        this.alterService = alterService;
    }

    @GetMapping("/t1")
    public @ResponseBody
    SampleDto sayHi(){
        return new SampleDto("hi");
    }

    @PostMapping("/t2")
    public @ResponseBody
    SampleDto alter(@RequestBody SampleDto sampleDto){
        System.out.println(RowContextHolder.getContext().getRowUser().getUserId());
        System.out.println(sampleDto.getField());
        sampleDto.setField(sampleDto.getField() + " -- Altered");
        return sampleDto;
    }

    @PostMapping("/t3")
    public @ResponseBody
    SampleDto alter2(@RowQuery SampleDto query, @RequestBody SampleDto sampleDto){
        System.out.println(query.getField());
        System.out.println(sampleDto.getField());
        alterService.alter(sampleDto);
        return sampleDto;
    }

    @GetMapping("/t4/{var}")
    public @ResponseBody
    SampleDto varTest(@PathVariable("var") String var){
        return new SampleDto(var);
    }

    @RowIgnore
    @GetMapping("/t5")
    public @ResponseBody
    SampleDto ignored(){
        return new SampleDto("hi");
    }

}
