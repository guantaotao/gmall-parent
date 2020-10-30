package com.atguigu.controller;

import com.atguigu.gmall.common.result.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestController {

    @RequestMapping("edu")
    public Result add(){
        Result<Object> objectResult = new Result<>();
        objectResult.setData("1");
        return Result.ok("1");
    }
}
