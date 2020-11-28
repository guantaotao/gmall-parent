package com.atguigu.gmall.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.prooduct.BaseAttrInfo;
import com.atguigu.gmall.model.prooduct.BaseAttrValue;
import com.atguigu.gmall.service.AttrApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("admin/product")
public class AttrApiController {
    @Autowired
    private AttrApiService attrApiService;

    @RequestMapping("attrInfoList/{category1Id}/{category2Id}/{category3Id}")
    public Result attrInfoList(@PathVariable("category1Id")Long category1Id, @PathVariable("category2Id")Long category2Id, @PathVariable("category3Id")Long category3Id){

        List<BaseAttrInfo> list= attrApiService.attrInfoList(category1Id,category2Id,category3Id);

        return Result.ok(list);
    }
    @RequestMapping("saveAttrInfo")
    public Result saveAttrInfo(@RequestBody BaseAttrInfo baseAttrInfo){
        attrApiService.saveAttrInfo(baseAttrInfo);
        return Result.ok();
    }

    //根据平台属行id获取平台属性
    @RequestMapping("getAttrValueList/{attrId}")
    public Result getAttrValueList(@PathVariable("attrId") Long attrId){
        List<BaseAttrValue> baseAttrValues=attrApiService.getAttrValueList(attrId);
        return Result.ok(baseAttrValues);
    }

}
