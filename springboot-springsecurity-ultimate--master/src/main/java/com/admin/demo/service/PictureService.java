package com.admin.demo.service;

import com.admin.demo.entity.Picture;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PictureService {
    Picture upload(MultipartFile file, String userName);

    Picture findById(Long id);

    void delete(Picture picture);

    List<Picture> getPictures(int start, Integer size, String keywords);

    Integer getCount(String keywords);
}
