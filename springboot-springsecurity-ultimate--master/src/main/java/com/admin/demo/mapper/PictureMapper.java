package com.admin.demo.mapper;

import com.admin.demo.entity.Picture;

import java.util.List;
import java.util.Optional;

public interface PictureMapper {
    void save(Picture picture);

    Picture findById(Long id);

    void delete(Picture picture);

    List<Picture> getPictures(int start, Integer size, String keywords);

    Integer getCount(String keywords);
}
