package com.atguigu.gmall.serviceImpl;

import com.atguigu.gmall.mapper.BaseAttrInfoMapper;
import com.atguigu.gmall.mapper.BaseAttrValueMapper;
import com.atguigu.gmall.model.prooduct.BaseAttrInfo;
import com.atguigu.gmall.model.prooduct.BaseAttrValue;
import com.atguigu.gmall.service.AttrApiService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class AttrApiServiceImpl implements AttrApiService {

    @Autowired
    private BaseAttrValueMapper baseAttrValueMapper;

    @Autowired
    private BaseAttrInfoMapper baseAttrInfoMapper;
    @Override
    public List<BaseAttrInfo> attrInfoList(Long category1Id, Long category2Id, Long category3Id) {
        QueryWrapper<BaseAttrInfo> baseAttrInfoQueryWrapper = new QueryWrapper<>();
        baseAttrInfoQueryWrapper.eq("category_id",category3Id);
        baseAttrInfoQueryWrapper.eq("category_level",3);
        List<BaseAttrInfo> baseAttrInfos = baseAttrInfoMapper.selectList(baseAttrInfoQueryWrapper);
        for (BaseAttrInfo baseAttrInfo : baseAttrInfos) {
            QueryWrapper<BaseAttrValue> baseAttrValueQueryWrapper=new QueryWrapper<>();
            baseAttrValueQueryWrapper.eq("attr_id",baseAttrInfo.getId());
            List<BaseAttrValue> baseAttrValues = baseAttrValueMapper.selectList(baseAttrValueQueryWrapper);
            baseAttrInfo.setAttrValueList(baseAttrValues);
        }
        return baseAttrInfos;
    }

    @Override
    public void saveAttrInfo(BaseAttrInfo baseAttrInfo) {
       //插入前先判断是否存在
        if(StringUtils.isEmpty(baseAttrInfo.getId())){
            baseAttrInfoMapper.insert(baseAttrInfo);
        }
        else {
            baseAttrInfoMapper.updateById(baseAttrInfo);
            QueryWrapper<BaseAttrValue> baseAttrValueQueryWrapper=new QueryWrapper<>();
            baseAttrValueQueryWrapper.eq("attr_id",baseAttrInfo.getId());
            baseAttrValueMapper.delete(baseAttrValueQueryWrapper);
        }
        List<BaseAttrValue> attrValueList = baseAttrInfo.getAttrValueList();
        for (BaseAttrValue baseAttrValue : attrValueList) {
            baseAttrValue.setAttrId(baseAttrInfo.getId());
            baseAttrValueMapper.insert(baseAttrValue);
        }
    }

    @Override
    public List<BaseAttrValue> getAttrValueList(Long attrId) {
        QueryWrapper<BaseAttrValue> baseAttrValueQueryWrapper=new QueryWrapper<>();
        baseAttrValueQueryWrapper.eq("attr_id",attrId);
        List<BaseAttrValue> baseAttrValues = baseAttrValueMapper.selectList(baseAttrValueQueryWrapper);
        return baseAttrValues;
    }
}
