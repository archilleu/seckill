package com.hoya.service.server.impl;

import com.hoya.core.exception.ServerExceptionForbidden;
import com.hoya.core.exception.ServerExceptionNotFound;
import com.hoya.core.exception.ServerExceptionServerError;
import com.hoya.service.commons.redis.RedisClient;
import com.hoya.service.commons.redis.impl.MiaoShaUserKeyPrefix;
import com.hoya.service.dao.MiaoShaUserMapper;
import com.hoya.service.model.MiaoShaUser;
import com.hoya.service.server.MiaoShaUserService;
import com.hoya.service.util.UUIDUtil;
import com.hoya.service.vo.LoginVo;
import com.hoya.service.vo.MiaoShaUserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import static com.hoya.service.constant.CustomerConstant.COOKIE_NAME_TOKEN;

@Slf4j
@Service
public class MiaoShaUserServiceImpl implements MiaoShaUserService {

    @Autowired
    private MiaoShaUserMapper miaoShaUserMapper;

    @Autowired
    RedisClient redisClient;

    @Override
    public MiaoShaUserVo getById(Long id) {
        try {
            MiaoShaUser user = miaoShaUserMapper.selectByPrimaryKey(id);
            MiaoShaUserVo userVo = new MiaoShaUserVo();
            BeanUtils.copyProperties(user, userVo);
            return userVo;
        } catch (DataAccessException e) {
            log.error("***获取秒杀用户对象失败！getById*** error:{}", e);
            throw new ServerExceptionServerError("秒杀失败");
        }
    }

    @Override
    public void login(LoginVo loginVo, HttpServletResponse response) {
        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();
        MiaoShaUserVo userVo = getByMobile(mobile);
        if (null == userVo) {
            throw new ServerExceptionNotFound("用户不存在");
        }

        String dbPass = userVo.getPassword();
        if (!dbPass.equals(formPass)) {
            throw new ServerExceptionForbidden("密码不正确");
        }

        // 生成token
        String token = UUIDUtil.getUUid();
        addCookie(token, userVo, response);
        return;
    }


    @Override
    public MiaoShaUserVo getByMobile(String mobile) {
        try {
            MiaoShaUser user = miaoShaUserMapper.getByMobile(mobile);
            if (null == user) {
                return null;
            }
            MiaoShaUserVo userVo = new MiaoShaUserVo();
            BeanUtils.copyProperties(user, userVo);
            return userVo;
        } catch (DataAccessException e) {
            log.error("***获取秒杀用户对象失败！error:{}", e);
            throw new ServerExceptionServerError("获取对象失败");
        }
    }

    @Override
    public MiaoShaUser getByToken(String token) {
        MiaoShaUser user = (MiaoShaUser) redisClient.get(MiaoShaUserKeyPrefix.token, token, MiaoShaUser.class);
        return user;
    }

    public void addCookie(String token, MiaoShaUserVo user, HttpServletResponse response) {
        if (false == redisClient.set(MiaoShaUserKeyPrefix.token, token, user)) {
            throw new ServerExceptionServerError("登陆失败");
        }
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge((int) MiaoShaUserKeyPrefix.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }

//    // http://blog.csdn.net/tTU1EvLDeLFq5btqiK/article/details/78693323
//    public boolean updatePassword(String token, String nickName, String formPass) {
//        //取user
//        MiaoShaUser user = getByName(nickName);
//        if(user == null) {
//            throw new GlobleException(MOBILE_NOT_EXIST);
//        }
//        //更新数据库
//        MiaoShaUser toBeUpdate = new MiaoShaUser();
//        toBeUpdate.setNickname(nickName);
//        toBeUpdate.setPassword(MD5Util.formPassToDBPass(formPass, user.getSalt()));
//        miaoShaUserDao.updateByPrimaryKeySelective(toBeUpdate);
//        //处理缓存
//        redisClient.delete(MiaoShaUserKey.getByNickName, ""+nickName);
//        user.setPassword(toBeUpdate.getPassword());
//        redisClient.set(MiaoShaUserKey.token, token, user);
//        return true;
//    }

}
