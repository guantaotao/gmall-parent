package com.atguigu.gmall.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.gmall.common.cacheAspect.GmallCache;
import com.atguigu.gmall.mapper.Category1Mapper;
import com.atguigu.gmall.mapper.Category2Mapper;


import com.atguigu.gmall.mapper.Category3Mapper;
import com.atguigu.gmall.mapper.CategoryViewMappper;
import com.atguigu.gmall.model.prooduct.BaseCategory1;
import com.atguigu.gmall.model.prooduct.BaseCategory2;
import com.atguigu.gmall.model.prooduct.BaseCategory3;
import com.atguigu.gmall.model.prooduct.BaseCategoryView;
import com.atguigu.gmall.service.CategoryService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private Category1Mapper category1Mapper;
    @Autowired
    private Category2Mapper category2Mapper;
    @Autowired
    private Category3Mapper category3Mapper;
    @Autowired
    private CategoryViewMappper categoryViewMappper;


    @Override
    public List<BaseCategory1> getCategory1() {
        List<BaseCategory1> list = category1Mapper.selectList(null);
        return list;
    }
    @Override
    public List<BaseCategory2> getCategory2(Long category1Id) {
        QueryWrapper<BaseCategory2> Wrapper = new QueryWrapper<>();
         Wrapper.eq("category1_id",category1Id);
        List<BaseCategory2> list = category2Mapper.selectList(Wrapper);
        return list;
    }

    @Override
    public List<BaseCategory3> getCategory3(Long category2Id) {
        QueryWrapper<BaseCategory3> Wrapper = new QueryWrapper<>();
        Wrapper.eq("category2_id",category2Id);
        List<BaseCategory3> list = category3Mapper.selectList(Wrapper);
        return list;
    }

    @GmallCache
    @Override
    public BaseCategoryView getCategoryView(Long category3Id) {
        QueryWrapper<BaseCategoryView>categoryViewQueryWrapper=new QueryWrapper<>();
        categoryViewQueryWrapper.eq("category3_id",category3Id);
        BaseCategoryView baseCategoryView = categoryViewMappper.selectOne(categoryViewQueryWrapper);
        return baseCategoryView;
    }

    @Override
    public List<JSONObject> getCategoryList() {

        // 将分类数据查询出来
        List<BaseCategoryView> baseCategoryViews = categoryViewMappper.selectList(null);

        // 封装成多级json集合
        List<JSONObject> category1List = new ArrayList<>();

        Map<Long, List<BaseCategoryView>> category1Map = baseCategoryViews.stream().collect(Collectors.groupingBy(BaseCategoryView::getCategory1Id));

        for (Map.Entry<Long, List<BaseCategoryView>> category1group : category1Map.entrySet()) {

            // 1级分类的id和名称
            Long category1Id = category1group.getKey();
            String category1Name = category1group.getValue().get(0).getCategory1Name();
            // 封装一级分类
            JSONObject category1jsonObject = new JSONObject();
            category1jsonObject.put("categoryId",category1Id);
            category1jsonObject.put("categoryName",category1Name);

            // 封装二级分类
            List<JSONObject> category2List = new ArrayList<>();
            Map<Long, List<BaseCategoryView>> category2Map = category1group.getValue().stream().collect(Collectors.groupingBy(BaseCategoryView::getCategory2Id));
            for (Map.Entry<Long, List<BaseCategoryView>> category2group : category2Map.entrySet()) {
                JSONObject category2jsonObject = new JSONObject();
                Long category2Id = category2group.getKey();
                String category2Name = category2group.getValue().get(0).getCategory2Name();
                category2jsonObject.put("categoryId",category2Id);
                category2jsonObject.put("categoryName",category2Name);

                // 封装三级分类
                List<JSONObject> category3List = new ArrayList<>();
                Map<Long, List<BaseCategoryView>> category3Map = category2group.getValue().stream().collect(Collectors.groupingBy(BaseCategoryView::getCategory3Id));
                for (Map.Entry<Long, List<BaseCategoryView>> category3group : category3Map.entrySet()) {
                    JSONObject category3jsonObject = new JSONObject();
                    Long category3Id = category3group.getKey();
                    String category3Name = category3group.getValue().get(0).getCategory3Name();
                    category3jsonObject.put("categoryId",category3Id);
                    category3jsonObject.put("categoryName",category3Name);
                    category3List.add(category3jsonObject);
                }

                // 2级
                category2jsonObject.put("categoryChild",category3List);
                category2List.add(category2jsonObject);
            }

            // 1级
            category1jsonObject.put("categoryChild",category2List);
            category1List.add(category1jsonObject);


        }

        return category1List;
        /*//查询分类列表
        List<BaseCategoryView> baseCategoryViews = categoryViewMappper.selectList(null);
        //流式编程 根据category1Id分类
        Map<Long, List<BaseCategoryView>> collect = baseCategoryViews.stream().collect(Collectors.groupingBy(BaseCategoryView::getCategory1Id));
        //获取map集合的键值对映射关系
        Set<Map.Entry<Long, List<BaseCategoryView>>> entries = collect.entrySet();
        List<JSONObject> category1List=new ArrayList<>();
        JSONObject jsonObject1 = new JSONObject();
        //循环
        for (Map.Entry<Long, List<BaseCategoryView>> entry : entries) {
            //获取一级分类的id一级名称
            Long category1Id = entry.getKey();
            String category1Name = entry.getValue().get(0).getCategory1Name();
            //封装一级分类
            jsonObject1.put("categoryId",category1Id);
            jsonObject1.put("categoryName",category1Name);
            List<BaseCategoryView> baseCategoryViewList = entry.getValue();
            //根据category2Id分类
            Map<Long, List<BaseCategoryView>> collect1 = baseCategoryViewList.stream().collect(Collectors.groupingBy(BaseCategoryView::getCategory2Id));
            Set<Map.Entry<Long, List<BaseCategoryView>>> entries1 = collect1.entrySet();
            List<JSONObject> category2List=new ArrayList<>();
            JSONObject jsonObject2 = new JSONObject();
            for (Map.Entry<Long, List<BaseCategoryView>> longListEntry : entries1) {
                Long category2Id = longListEntry.getKey();
                List<BaseCategoryView> value = longListEntry.getValue();
                //封装二级分类
                jsonObject2.put("categoryId",category2Id);
                jsonObject2.put("categoryName",value.get(0).getCategory2Name());
                Map<Long, List<BaseCategoryView>> collect2 = value.stream().collect(Collectors.groupingBy(BaseCategoryView::getCategory3Id));
                List<JSONObject> category3List=new ArrayList<>();
                JSONObject jsonObject3 = new JSONObject();
                Set<Map.Entry<Long, List<BaseCategoryView>>> entries2 = collect2.entrySet();
                for (Map.Entry<Long, List<BaseCategoryView>> listEntry : entries2) {
                    Long category3Id = listEntry.getKey();
                    List<BaseCategoryView> value1 = listEntry.getValue();
                    //封装三级分类
                    jsonObject3.put("categoryId",category3Id);
                    jsonObject3.put("categoryName",value1.get(0).getCategory2Name());
                    category3List.add(jsonObject3);
                }
                jsonObject2.put("categoryChild",category3List);
                category2List.add(jsonObject3);
            }
            jsonObject1.put("categoryChild",category2List);
            category1List.add(jsonObject2);
        }

        return category1List;
    }*/
    }
}
