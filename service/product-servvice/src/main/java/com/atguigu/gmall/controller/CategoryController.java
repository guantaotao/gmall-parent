package com.atguigu.gmall.controller;

import com.atguigu.gmall.common.result.Result;

import com.atguigu.gmall.model.prooduct.BaseCategory1;
import com.atguigu.gmall.model.prooduct.BaseCategory2;
import com.atguigu.gmall.model.prooduct.BaseCategory3;
import com.atguigu.gmall.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("admin/product")
@CrossOrigin
@RestController
public class CategoryController {
    @Autowired
    private CategoryService productService;


    @RequestMapping("getCategory1")
    public Result getCategory1(){
        List<BaseCategory1> list=productService.getCategory1();
        return Result.ok(list);
    }
    @RequestMapping("getCategory2/{category1Id}")
    public Result getCategory2(@PathVariable("category1Id") Long category1Id){
        List<BaseCategory2> list=productService.getCategory2(category1Id);
        return Result.ok(list);
    }
    @RequestMapping("getCategory3/{category2Id}")
    public Result getCategory3(@PathVariable("category2Id") Long category2Id){
        List<BaseCategory3> list=productService.getCategory3(category2Id);
        return Result.ok(list);
    }

}
