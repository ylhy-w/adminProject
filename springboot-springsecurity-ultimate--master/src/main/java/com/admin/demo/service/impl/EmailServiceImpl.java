package com.admin.demo.service.impl;

import com.admin.common.exception.BadRequestException;
import com.admin.common.utils.*;
import com.admin.demo.entity.MailRetrieve;
import com.admin.demo.entity.User;
import com.admin.demo.mapper.MailRetrieveMapper;
import com.admin.demo.mapper.UserMapper;
import com.admin.demo.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@Transactional
@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private MailRetrieveMapper mailRetrieveMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    UserMapper userMapper;

    //忘记密码
    @Async
    public void findPassowrd(String basePath, User user) {
        String result = null;
            //生成邮件url唯一地址
            String key = RandomUtils.getRandom(6) + "";
            Timestamp outDate = new Timestamp(System.currentTimeMillis() + (long) (30 * 60 * 1000));
            //30分钟后过期    忽略毫秒数
            long outtimes = outDate.getTime();
            String sid = user.getUsername() + "&" + key + "&" + outtimes;
            MailRetrieve mailRetrieve = new MailRetrieve(user.getUsername(), Md5Encrypt.EncoderByMd5(sid), outtimes);
            MailRetrieve findMailRetrieve = mailRetrieveMapper.findByAccount(user.getUsername());
            if (findMailRetrieve != null) {
                mailRetrieveMapper.delete(findMailRetrieve.getAccount());
            }
            mailRetrieveMapper.save(mailRetrieve);
            result = basePath + "reset_password?account=" + user.getUsername() + "&sid=" + Md5Encrypt.EncoderByMd5(sid);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("1184071961@qq.com");//发送者.
            message.setTo(user.getEmail());//接收者.
            message.setSubject("邮箱重置密码");//邮件主题.
            message.setText("重置密码链接：" + result + "  该链接30分钟内有效且只能使用一次，请尽快处理，务必牢记新密码！！！");//邮件内容.
            mailSender.send(message);//发送邮件

    }

    @Override
    public void changeEmail(String password, String email, String code) {

        Object activeCode =redisUtil.get(email+"_code");
        if (activeCode==null){
            throw new BadRequestException("邮箱不一致或激活码已过期");
        }
        if (!code.equals(activeCode)){
            throw new BadRequestException("激活码错误");
        }

        String pass = SecurityUtils.getUserDetails().getPassword();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        boolean b=encoder.matches(password,pass);
        if (b){
            User user = userMapper.getUser( SecurityUtils.getUsername());
            user.setEmail(email);
            userMapper.updateEmail(user);
            redisUtil.del(email+"_code");
        }else {
            throw new BadRequestException("密码错误");
        }

    }

    //重置密码
    public ServerResponse verifyMail(String sid, String account) {
        String info= null;
        //    boolean result=false;
        MailRetrieve mailRetrieve=mailRetrieveMapper.findByAccount(account);
        long outTime=mailRetrieve.getOutTime();
        Timestamp outDate = new Timestamp(System.currentTimeMillis());
        long nowTime=outDate.getTime();
        if(outTime==0){
            info="重置链接已失效，请重新发送";
            throw  new BadRequestException(info);
        }else if(outTime<=nowTime){
            info="重置链接已过期，请重新发送";
            throw  new BadRequestException(info);
        }else if("".equals(sid)){
            info="无效链接";
            throw  new BadRequestException(info);
        }else if(!sid.equals(mailRetrieve.getSid())){
            info="重置链接错误，请重新发送";
            throw  new BadRequestException(info);
        }else{
            User user = new User();
            String newpassword = RandomUtils.getRandom(6)+"";
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String hashPass = encoder.encode(newpassword);
            user.setUsername(account);
            user.setPassword(hashPass);
            userMapper.updatePassword(user);
            info="重置成功,新密码为："+newpassword;
            mailRetrieveMapper.update(account);
            return ServerResponse.createBySuccessMessage(info);
        }

    }

    //发送邮箱激活码
    @Async
    public void emailActive(String email) {
        String code = RandomUtils.getRandom(6)+"";
        //放入redis缓存
        redisUtil.set(email+"_code",code,300);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("1184071961@qq.com");//发送者.
        message.setTo(email);//接收者.
        message.setSubject("邮箱激活码");//邮件主题.
        message.setText("您正在进行绑定邮箱操作，激活码为：" + code + "  该激活码5分钟内有效，请尽快处理");//邮件内容.
        mailSender.send(message);//发送邮件
    }

    //注册
    public void register(String username, String password, String email, String code) {
        if (userMapper.checkUsername(username)>0){
            throw new BadRequestException("用户名已存在");
        }
        Object activeCode =redisUtil.get(email+"_code");
        if (activeCode==null){
            throw new BadRequestException("邮箱不一致或该激活码已过期");
        }
        if (!code.equals(activeCode)){
            throw new BadRequestException("激活码错误");
        }
        //密码加密 必须与security加密规则一致
        //使用BCryptPasswordEncoder，即使相同的明文，
        // 生成的新的加密字符串都是不一样的，
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encode = encoder.encode(password);
        User user = new User();
        user.setUsername(username);
        user.setPassword(encode);
        user.setEmail(email);
        int i= userMapper.register(user);
        if (i==0){
            throw new UsernameNotFoundException("注册失败");
        }
        //将新注册数据添加到错误次数表
        userMapper.regAttempts(user.getId());
        redisUtil.del(email+"_code");
    }
}
