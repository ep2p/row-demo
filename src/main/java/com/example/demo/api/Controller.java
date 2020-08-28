package com.example.demo.api;

import com.example.demo.service.AlterService;
import com.example.demo.service.publisher.TestEventPublisher;
import lab.idioglossia.row.annotations.PreSubscribe;
import lab.idioglossia.row.annotations.RowController;
import lab.idioglossia.row.annotations.RowIgnore;
import lab.idioglossia.row.annotations.RowQuery;
import lab.idioglossia.row.context.RowContextHolder;
import lab.idioglossia.row.event.PublishStrategy;
import org.springframework.web.bind.annotation.*;

@RowController
public class Controller {
    private final AlterService alterService;
    private final TestEventPublisher testEventPublisher;

    public Controller(AlterService alterService, TestEventPublisher testEventPublisher) {
        this.alterService = alterService;
        this.testEventPublisher = testEventPublisher;
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

    @PreSubscribe(value = "test", strategy = PublishStrategy.Strategy.USER_SESSIONS)
    @GetMapping("/subs/t1")
    public @ResponseBody
    SampleDto subscribe(){
        return new SampleDto("done :)");
    }

    @PostMapping("/subs/publish/t1")
    public @ResponseBody
    SampleDto publish(@RequestBody SampleDto sampleDto){
        testEventPublisher.publish(sampleDto);
        return new SampleDto("finished publishing!");
    }
}
