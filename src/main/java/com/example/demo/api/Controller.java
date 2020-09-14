package com.example.demo.api;

import com.example.demo.service.AlterService;
import com.example.demo.service.publisher.TestEventPublisher;
import lab.idioglossia.row.annotations.PreSubscribe;
import lab.idioglossia.row.annotations.RowController;
import lab.idioglossia.row.annotations.RowIgnore;
import lab.idioglossia.row.annotations.RowQuery;
import lab.idioglossia.row.context.RowContext;
import lab.idioglossia.row.context.RowContextHolder;
import lab.idioglossia.row.context.RowUser;
import lab.idioglossia.row.domain.protocol.RequestDto;
import lab.idioglossia.row.domain.protocol.ResponseDto;
import lab.idioglossia.row.event.PublishStrategy;
import lab.idioglossia.row.event.Subscription;
import lab.idioglossia.row.service.PublisherService;
import lab.idioglossia.row.service.SubscriberService;
import lab.idioglossia.row.utl.RequestResponseUtil;
import org.springframework.web.bind.annotation.*;

@RowController
public class Controller {
    private final AlterService alterService;
    private final TestEventPublisher testEventPublisher;
    private final PublisherService publisherService;
    private final SubscriberService subscriberService;

    public Controller(AlterService alterService, TestEventPublisher testEventPublisher, PublisherService publisherService, SubscriberService subscriberService) {
        this.alterService = alterService;
        this.testEventPublisher = testEventPublisher;
        this.publisherService = publisherService;
        this.subscriberService = subscriberService;
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


    //manual subscription
    @PreSubscribe(value = "test", strategy = PublishStrategy.Strategy.USER_SESSIONS)
    @GetMapping("/subs/t2")
    public @ResponseBody
    SampleDto subscribe2(RequestDto requestDto, ResponseDto responseDto){
        RowContext rowContext = RowContextHolder.getContext();
        if(rowContext.isRowRequest()){
            if(!RequestResponseUtil.isUnSubscribing(requestDto)){
                System.out.println("Subscribing");
                RowUser rowUser = rowContext.getRowUser();
                subscriberService.subscribe(requestDto.getHeaders().get("CUSTOM_HEADER_SUBS"), PublishStrategy.Strategy.USER_SESSIONS.getPublishStrategy(), rowUser, responseDto);
                return new SampleDto("Finished subscribing");
            }else{
                System.out.println("Un-Subscribing");
                subscriberService.unsubscribe(requestDto.getHeaders().get("CUSTOM_HEADER_SUBS"), requestDto);
                return new SampleDto("Finished un-subscribing");
            }
        }
        return new SampleDto("Nah! you need to be using websocket!");
    }

    //manual publish
    @PostMapping("/subs/publish/t2")
    public @ResponseBody
    SampleDto publish2(RequestDto requestDto, @RequestBody SampleDto sampleDto){
        RowContext rowContext = RowContextHolder.getContext();
        if(rowContext.isRowRequest()) {
            publisherService.publish(requestDto.getHeaders().get("CUSTOM_HEADER_SUBS"), sampleDto);
            return new SampleDto("finished publishing!");
        }
        return new SampleDto("Nah! you need to be using websocket!");
    }
}
