package com.atguigu.gmall.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.prooduct.BaseSaleAttr;
import com.atguigu.gmall.model.prooduct.SpuImage;
import com.atguigu.gmall.model.prooduct.SpuInfo;
import com.atguigu.gmall.model.prooduct.SpuSaleAttr;
import com.atguigu.gmall.service.SpuService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("admin/product")
public class SpuController {
    @Autowired
    private SpuService spuService;
    //获取spu列表
    @RequestMapping("{pageNum}/{pageSize}")
    public Result list(Long category3Id,@PathVariable("pageNum")Long pageNum,@PathVariable("pageSize")Long pageSize){
        IPage<SpuInfo>spuInfoPage = new Page<>();
        spuInfoPage.setSize(pageSize);
        spuInfoPage.setCurrent(pageNum);
        IPage<SpuInfo> spuInfoIPage = spuService.spuList(spuInfoPage, category3Id);
        return Result.ok(spuInfoIPage);
    }

    //获取销售属性
    @RequestMapping("baseSaleAttrList")
    public Result baseSaleAttrList(){
        List<BaseSaleAttr>saleAttrList=spuService.baseSaleAttrList();
        return Result.ok(saleAttrList);
    }
    //添加spu
    @RequestMapping("saveSpuInfo")
    public Result saveSpuInfo(@RequestBody SpuInfo spuInfo){
        spuService.saveSpuInfo(spuInfo);
        return Result.ok();
    }
    //上传图片
    @RequestMapping("fileUpload")
    public Result fileUpload(@RequestParam("file") MultipartFile multipartFile)  {
        String path = SpuController.class.getClassLoader().getResource("tracker.conf").getPath();
        try {
            ClientGlobal.init(path);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
        //得到tracker
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer=null;
        try {
            trackerServer = trackerClient.getConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //根据tracker获取storage
        StorageClient storageClient = new StorageClient(trackerServer, null);
        //上传文件
        String originalFilename = multipartFile.getOriginalFilename();
        int i = originalFilename.lastIndexOf(".");
        String substring = originalFilename.substring(i + 1);
        //根据工具类获取后缀名
        //StringUtils.getFilenameExtension(originalFilename);
        String[] strings=new String[0];
        try {
            //接收返回的id
             strings = storageClient.upload_file(multipartFile.getBytes(), substring, null);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }

        StringBuffer url = new StringBuffer("htttp://192.168.200.128:8080");
        for (String string : strings) {
            url.append("/"+string);
        }
        System.out.println(url);
        return Result.ok(url);
    }

    //根据spuid查询spu图片列表
    @RequestMapping("spuImageList/{spuId}")
    public Result spuImageList(@PathVariable("spuId")Long spuId){
        List<SpuImage> spuImageList=spuService.spuImageList(spuId);
        return Result.ok(spuImageList);
    }
    //根据spuid获取销售属性
    @RequestMapping("spuSaleAttrList/{spuId}")
    public Result spuSaleAttrList(@PathVariable("spuId")Long spuId){
       List<SpuSaleAttr>saleAttrList= spuService.spuSaleAttrList(spuId);
        return Result.ok(saleAttrList);
    }

}
