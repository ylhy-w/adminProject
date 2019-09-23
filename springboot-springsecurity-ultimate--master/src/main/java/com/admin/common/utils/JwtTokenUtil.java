package com.admin.common.utils;

import com.admin.demo.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = -3301605591108950415L;
    private Clock clock = DefaultClock.INSTANCE;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.header}")
    private String tokenHeader;

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getIssuedAtDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getIssuedAt);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }



    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(clock.now());
    }

    private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }

    private Boolean ignoreTokenExpiration(String token) {
        // here you specify tokens, for that the expiration is ignored
        return false;
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    //生成token
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        final Date createdDate = clock.now();
        final Date expirationDate = calculateExpirationDate(createdDate);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }


    public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
        final Date created = getIssuedAtDateFromToken(token);
        return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
                && (!isTokenExpired(token) || ignoreTokenExpiration(token));
    }

    //token刷新
    public String refreshToken(String token) {
        final Date createdDate = clock.now();
        final Date expirationDate = calculateExpirationDate(createdDate);

        final Claims claims = getAllClaimsFromToken(token);
        claims.setIssuedAt(createdDate);
        claims.setExpiration(expirationDate);

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        User user = (User) userDetails;
       final Date created = getIssuedAtDateFromToken(token);
//       final Date expiration = getExpirationDateFromToken(token);
//        如果token存在，且token创建日期 > 最后修改密码的日期 则代表token有效
        return (!isTokenExpired(token)
            && !isCreatedBeforeLastPasswordReset(created, user.getLast_password_reset_time())
        );
    }

    private Date calculateExpirationDate(Date createdDate) {
        return new Date(createdDate.getTime() + expiration);
    }
}



/**
 * @author: zzx
 * @date: 2018/10/16 9:06
 * @description: jwt生成token
 */
//public class JwtTokenUtil {

//    @Autowired
//    private static RedisUtil redisUtil;
//
//    // 寻找证书文件
//    private static InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("jwt.jks"); // 寻找证书文件
//    private static PrivateKey privateKey = null;
//    private static PublicKey publicKey = null;
//
//    static { // 将证书文件里边的私钥公钥拿出来
//        try {
//            KeyStore keyStore = KeyStore.getInstance("JKS"); // java key store 固定常量
//            keyStore.load(inputStream, "123456".toCharArray());
//            privateKey = (PrivateKey) keyStore.getKey("jwt", "123456".toCharArray()); // jwt 为 命令生成整数文件时的别名
//            publicKey = keyStore.getCertificate("jwt").getPublicKey();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 生成token
//     * @param subject （主体信息）
//     * @param expirationSeconds 过期时间（秒）
//     * @param claims 自定义身份信息
//     * @return
//     */
//    public static String generateToken(String subject, int expirationSeconds, Map<String,Object> claims) {
//        return Jwts.builder()
//                .setClaims(claims)
//                .setSubject(subject)
//                .setExpiration(new Date(System.currentTimeMillis() + expirationSeconds * 1000))
////                .signWith(SignatureAlgorithm.HS512, salt) // 不使用公钥私钥
//                .signWith(SignatureAlgorithm.RS256, privateKey)
//                .compact();
//    }
//
//    /**
//     * @author: zzx
//     * @date: 2018-10-19 09:10
//     * @deprecation: 解析token,获得subject中的信息
//     */
//    public static String parseToken(String token, String salt) {
//        String subject = null;
//        try {
//            /*Claims claims = Jwts.parser()
////                    .setSigningKey(salt) // 不使用公钥私钥
//                    .setSigningKey(publicKey)
//                    .parseClaimsJws(token).getBody();*/
//            subject = getTokenBody(token).getSubject();
//        } catch (Exception e) {
//        }
//        return subject;
//    }
//
//    //获取token自定义属性
//    public static Map<String,Object> getClaims(String token){
//        Map<String,Object> claims = null;
//        try {
//            claims = getTokenBody(token);
//        }catch (Exception e) {
//        }
//
//        return claims;
//    }
//
//    // 是否已过期
//    public static boolean isExpiration(String expirationTime){
//        /*return getTokenBody(token).getExpiration().before(new Date());*/
//
//        //通过redis中的失效时间进行判断
//        String currentTime = DateUtil.getTime();
//        if(DateUtil.compareDate(currentTime,expirationTime)){
//            //当前时间比过期时间小，失效
//            return true;
//        }else{
//            return false;
//        }
//    }
//
//    private static Claims getTokenBody(String token){
//        return Jwts.parser()
//                .setSigningKey(publicKey)
//                .parseClaimsJws(token)
//                .getBody();
//    }
//}
