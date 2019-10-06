package com.admin.demo.controller;

import com.admin.common.exception.BadRequestException;
import com.admin.common.utils.FileUtil;
import com.admin.common.utils.SecurityUtils;
import com.admin.common.utils.ServerResponse;
import com.admin.demo.entity.Picture;
import com.admin.demo.entity.User;
import com.admin.demo.service.PictureService;
import com.admin.demo.service.UserService;
import com.admin.log.myLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@RequestMapping("api/picture")
@RestController
public class PictureController {
    @Autowired
    PictureService pictureService;
    @Autowired
    UserService userService;

    public static final long maxSize = 1024*1024*5;
    public static final long userSize = 1024*500;
    public static final String path = "/home/pan/tomcat/webapps/temp/";

    @myLog("查询图片")
    @GetMapping(value = "/get")
    @PreAuthorize("hasAnyRole('ADMIN','PICTURE_ALL','PICTURE_SELECT')")
    public ResponseEntity getRoles(@RequestParam(defaultValue = "1") Integer page,
                                   @RequestParam(defaultValue = "10") Integer size,
                                   @RequestParam(defaultValue = "") String keywords){
        int start = (page-1)*size;
        Map<String, Object> map = new HashMap<>();
        List<Picture> pictures = pictureService.getPictures(start,size,keywords);
        Integer count = pictureService.getCount(keywords);
        map.put("pictures",pictures);
        map.put("count",count);
        return ResponseEntity.ok(map);
    }


    @myLog("上传图片")
    @PostMapping("/upload")
    @PreAuthorize("hasAnyRole('ADMIN','PICTURE_ALL','PICTURE_UPLOAD')")
    public ServerResponse upload(@RequestParam MultipartFile file){
                if (file.getSize()>maxSize){
         throw new BadRequestException("单张图片大小不能超过5M");
        }
        String userName = SecurityUtils.getUsername();
        Picture picture = pictureService.upload(file,userName);
        Map map = new HashMap(2);
        map.put("id",picture.getId());
        map.put("url",new String[]{picture.getUrl()});
        return ServerResponse.createBySuccess("上传成功",map);
    }

    @myLog("删除图片")
    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PICTURE_ALL','PICTURE_DELETE')")
    public ServerResponse delete(@PathVariable Long id) {
        pictureService.delete(pictureService.findById(id));
        return  ServerResponse.createBySuccessMessage("删除成功");
    }

    /**
     * 删除多张图片
     * @param ids
     * @return
     */
    @myLog("批量删除图片")
    @PreAuthorize("hasAnyRole('ADMIN','PICTURE_ALL','PICTURE_DELETE')")
    @DeleteMapping(value = "/deleteAll")
    public ServerResponse deleteAll( Long[] ids) {
        pictureService.deleteAll(ids);
        return  ServerResponse.createBySuccessMessage("删除成功");
    }


//上传头像
    @PostMapping("/user")
    public ServerResponse userPicture(@RequestParam MultipartFile file){

        if (file.getSize()>userSize){
            throw new BadRequestException("头像大小超过500k");
        }
        String name = UUID.randomUUID().toString().replaceAll("-", "");
        String fileName = file.getOriginalFilename();
        String ext = FileUtil.getExtensionName(fileName);

            if(ext.equalsIgnoreCase("jpg")||ext.equalsIgnoreCase("png")||ext.equalsIgnoreCase("jpeg")||ext.equalsIgnoreCase("pneg")) {
                try {

          //    file.transferTo(new File("E:\\a\\" + name + "." + ext));
                   file.transferTo(new File(path + name + "." + ext));//保存路径
                    User user = (User) userService.loadUserByUsername(SecurityUtils.getUsername());
                    String oldPicture = user.getPicture();
                    user.setPicture(name + "." + ext);
                    userService.updateUser(user);
                //    File img = new File("E:\\a\\"+oldPicture);
                    File img = new File(path+oldPicture);
                    //删除实体 文件
                    FileUtil.deleteFile(img);
                } catch (Exception e) {
                    e.printStackTrace();
                    return ServerResponse.createByErrorMessage("更新失败");
                }
            }else {
              throw new BadRequestException( "不支持该图片格式,请上传jpg,png,jpeg,pneg格式");
            }

        return ServerResponse.createBySuccessMessage("上传成功");
    }

}
