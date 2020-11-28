package com.atguigu.gmall.serviceImpl;

import com.atguigu.gmall.mapper.TradeMarkMapper;
import com.atguigu.gmall.model.prooduct.BaseTrademark;
import com.atguigu.gmall.service.TradeMarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TradeMarkServiceImpl implements TradeMarkService {
   @Autowired
   private TradeMarkMapper tradeMarkMapper;
    @Override
    public List<BaseTrademark> getTrademarkList() {
        List<BaseTrademark> baseTrademarkList = tradeMarkMapper.selectList(null);
        return  baseTrademarkList;
    }
}
