package com.admin.demo.mapper;

import com.admin.demo.entity.EmailConfig;
import org.apache.ibatis.annotations.Param;




public interface EmailToolMapper{

    void save(EmailConfig emailConfig);

    EmailConfig findById(@Param("id") Long id);
}
