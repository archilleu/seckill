package com.hoya.service.server.impl;

import com.hoya.core.exception.ServerExceptionForbidden;
import com.hoya.core.exception.ServerExceptionNotFound;
import com.hoya.core.exception.ServerExceptionServerError;
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

@Slf4j
@Service
public class MiaoShaUserServiceImpl implements MiaoShaUserService {

    @Autowired
    private MiaoShaUserMapper miaoShaUserMapper;

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
    public void login(LoginVo loginVo) {
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
