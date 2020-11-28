package com.atguigu.gmall.mapper;

import com.atguigu.gmall.model.list.SearchAttr;
import com.atguigu.gmall.model.prooduct.BaseAttrInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BaseAttrInfoMapper extends BaseMapper<BaseAttrInfo> {
    List<SearchAttr> selectBaseAttrInfoListBySkuId(Long skuId);
}
