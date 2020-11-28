package com.atguigu.gmall.service;


import com.atguigu.gmall.model.prooduct.BaseSaleAttr;
import com.atguigu.gmall.model.prooduct.SpuImage;
import com.atguigu.gmall.model.prooduct.SpuInfo;
import com.atguigu.gmall.model.prooduct.SpuSaleAttr;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Map;

public interface SpuService {
    IPage<SpuInfo> spuList(IPage<SpuInfo> spuInfoPage, Long category3Id);

    List<BaseSaleAttr> baseSaleAttrList();

    void saveSpuInfo(SpuInfo spuInfo);

    List<SpuImage> spuImageList(Long spuId);

    List<SpuSaleAttr> spuSaleAttrList(Long spuId);

    List<SpuSaleAttr> getSpuSaleAttrs(Long spuId,Long skuId);



}
