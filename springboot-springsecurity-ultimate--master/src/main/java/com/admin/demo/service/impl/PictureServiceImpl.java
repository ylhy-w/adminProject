package com.admin.demo.service.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.admin.common.exception.BadRequestException;
import com.admin.common.utils.FileUtil;
import com.admin.demo.entity.Picture;
import com.admin.demo.mapper.PictureMapper;
import com.admin.demo.service.PictureService;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class PictureServiceImpl implements PictureService {

    @Autowired
    PictureMapper pictureMapper;

    public static final String SUCCESS = "success";

    public static final String CODE = "code";

    public static final String MSG = "message";

    public static final String URL = "https://sm.ms/api/upload";


    @Override
    @Transactional(rollbackFor = Throwable.class)
    public Picture upload(MultipartFile multipartFile, String username) {
        File file = FileUtil.toFile(multipartFile);
        HashMap<String, Object> paramMap = new HashMap<>(1);

        paramMap.put("smfile", file);
        String result= HttpUtil.post(URL, paramMap);
        JSONObject jsonObject = JSONUtil.parseObj(result);
        Picture picture = null;
        System.out.println(jsonObject.toString());
        if(!jsonObject.get(CODE).toString().equals(SUCCESS)){
            throw new BadRequestException(jsonObject.get(MSG).toString());
        }
        //转成实体类
        picture = JSON.parseObject(jsonObject.get("data").toString(), Picture.class);
        picture.setSize(FileUtil.getSize(Integer.valueOf(picture.getSize())));
        picture.setUsername(username);
        picture.setFilename(FileUtil.getFileNameNoEx(multipartFile.getOriginalFilename())+"."+FileUtil.getExtensionName(multipartFile.getOriginalFilename()));
        pictureMapper.save(picture);
        //删除临时文件
        FileUtil.del(file);
        return picture;

    }

    @Override
    public Picture findById(Long id) {
        Picture picture = pictureMapper.findById(id);
        return picture;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    @Async
    public void delete(Picture picture) {

        try {
            String result= HttpUtil.get(picture.getDelete());
            pictureMapper.delete(picture);
        } catch(Exception e){
            pictureMapper.delete(picture);
        }

    }

    @Override
    public List<Picture> getPictures(int start, Integer size, String keywords) {
        return pictureMapper.getPictures(start,size,keywords);
    }

    @Override
    public Integer getCount(String keywords) {
        return pictureMapper.getCount(keywords);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            delete(findById(id));
        }
    }
}
