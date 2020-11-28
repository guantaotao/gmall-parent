package com.atguigu.gmall.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.prooduct.BaseTrademark;
import com.atguigu.gmall.service.TradeMarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("admin/product")
public class TradeMarkController {
    @Autowired
    private TradeMarkService tradeMarkService;

    @RequestMapping("baseTrademark/getTrademarkList")
    public Result getTrademarkList(){
       List<BaseTrademark> baseTrademarkList= tradeMarkService.getTrademarkList();
        return Result.ok(baseTrademarkList);
    }
}
