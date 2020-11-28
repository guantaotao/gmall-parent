package com.atguigu.gmall.serviceImpl;

import com.atguigu.gmall.mapper.*;
import com.atguigu.gmall.model.prooduct.*;
import com.atguigu.gmall.service.SpuService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Service
public class SpuServiceImpl implements SpuService {
    @Autowired
    private SpuInfoMapper spuInfoMapper;
    @Autowired
    private SpuSaleAttrMapper spuSaleAttrMapper;
    @Autowired
    private BaseSaleAttrMapper baseSaleAttrMapper;
    @Autowired
    private SpuImgMapper spuImgMapper;
    @Autowired
    private SpuSaleAttrValueMapper spuSaleAttrValueMapper;
    @Override
    public IPage<SpuInfo> spuList(IPage<SpuInfo> spuInfoPage, Long category3Id) {
        QueryWrapper<SpuInfo> baseCategory3QueryWrapper=new QueryWrapper<>();
        baseCategory3QueryWrapper.eq("category3_id",category3Id);
        IPage<SpuInfo> spuInfoIPage = spuInfoMapper.selectPage(spuInfoPage, baseCategory3QueryWrapper);

        return spuInfoIPage;
    }
    //获取销售属性
    @Override
    public List<BaseSaleAttr> baseSaleAttrList() {
        List<BaseSaleAttr> spuSaleAttrs = baseSaleAttrMapper.selectList(null);
        return spuSaleAttrs;
    }

    @Override
    public void saveSpuInfo(SpuInfo spuInfo) {
      //保存主表
        if(StringUtils.isEmpty(spuInfo.getId())){
            spuInfoMapper.insert(spuInfo);
        }else{
            spuInfoMapper.updateById(spuInfo);
        }
        //保存图片表
        Long id = spuInfo.getId();
        List<SpuImage> spuImageList = spuInfo.getSpuImageList();
        for (SpuImage spuImage : spuImageList) {
            spuImgMapper.insert(spuImage);
            spuImage.setSpuId(id);
        }
        //保存销售属性
        List<SpuSaleAttr> spuSaleAttrList = spuInfo.getSpuSaleAttrList();
        for (SpuSaleAttr spuSaleAttr : spuSaleAttrList) {
            spuSaleAttr.setSpuId(id);
            spuSaleAttrMapper.insert(spuSaleAttr);

            List<SpuSaleAttrValue> spuSaleAttrValueList = spuSaleAttr.getSpuSaleAttrValueList();
            for (SpuSaleAttrValue spuSaleAttrValue : spuSaleAttrValueList) {
                //联合外键
                spuSaleAttrValue.setSpuId(id);
                spuSaleAttrValue.setBaseSaleAttrId(spuSaleAttr.getBaseSaleAttrId());
                spuSaleAttrValue.setSaleAttrName(spuSaleAttr.getSaleAttrName());
                spuSaleAttrValueMapper.insert(spuSaleAttrValue);
            }
        }
    }

    @Override
    public List<SpuImage> spuImageList(Long spuId) {
        QueryWrapper<SpuImage> spuImageQueryWrapper=new QueryWrapper<>();
        spuImageQueryWrapper.eq("spu_id",spuId);
        List<SpuImage> spuImageList = spuImgMapper.selectList(spuImageQueryWrapper);
        return spuImageList;
    }

    @Override
    public List<SpuSaleAttr> spuSaleAttrList(Long spuId) {
        QueryWrapper<SpuSaleAttr>spuSaleAttrQueryWrapper=new QueryWrapper<>();
        spuSaleAttrQueryWrapper.eq("spu_id",spuId);
        List<SpuSaleAttr> saleAttrList = spuSaleAttrMapper.selectList(spuSaleAttrQueryWrapper);
        for (SpuSaleAttr spuSaleAttr : saleAttrList) {
            QueryWrapper<SpuSaleAttrValue>wrapper=new QueryWrapper<>();
            wrapper.eq("spu_id",spuId);
            wrapper.eq("base_sale_attr_id",spuSaleAttr.getBaseSaleAttrId());
            //设置销售属性值
            List<SpuSaleAttrValue> spuSaleAttrValues = spuSaleAttrValueMapper.selectList(wrapper);
            spuSaleAttr.setSpuSaleAttrValueList(spuSaleAttrValues);
        }
        return saleAttrList;
    }

    @Override
    public List<SpuSaleAttr> getSpuSaleAttrs(Long spuId,Long skuId) {
        // 实现一个三个表(spu_sale_attr,spu_sale_attr_value,sku_sale_attr_value)联合查询
        List<SpuSaleAttr> spuSaleAttrs = spuSaleAttrMapper.selectSpuSaleAttrListCheckBySku(spuId,skuId);

        return spuSaleAttrs;



     /*   QueryWrapper<SpuSaleAttr>wrapper=new QueryWrapper<>();
        wrapper.eq("spu_id",spuId);
        List<SpuSaleAttr> spuSaleAttrs = spuSaleAttrMapper.selectList(wrapper);
        for (SpuSaleAttr spuSaleAttr : spuSaleAttrs) {
            QueryWrapper<SpuSaleAttrValue>spuSaleAttrValueQueryWrapper=new QueryWrapper<>();
            //联合外键
            spuSaleAttrValueQueryWrapper.eq("spu_id",spuId);
            spuSaleAttrValueQueryWrapper.eq("base_sale_attr_id",spuSaleAttr.getBaseSaleAttrId());
            List<SpuSaleAttrValue> spuSaleAttrValues = spuSaleAttrValueMapper.selectList(spuSaleAttrValueQueryWrapper);
            spuSaleAttr.setSpuSaleAttrValueList(spuSaleAttrValues);
        }
      */
    }


}
