package com.atguigu.gmall.service;

import com.atguigu.gmall.model.prooduct.BaseAttrInfo;
import com.atguigu.gmall.model.prooduct.BaseAttrValue;

import java.util.List;

public interface AttrApiService {

    List<BaseAttrInfo> attrInfoList(Long category1Id, Long category2Id, Long category3Id);

    void saveAttrInfo(BaseAttrInfo baseAttrInfo);

    List<BaseAttrValue> getAttrValueList(Long attrId);
}
