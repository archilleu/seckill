package com.hoya.service.server;

import com.hoya.service.model.MiaoShaUser;
import com.hoya.service.vo.LoginVo;
import com.hoya.service.vo.MiaoShaUserVo;

import javax.servlet.http.HttpServletResponse;

public interface MiaoShaUserService {

    MiaoShaUserVo getById(Long id);

    void login(LoginVo loginVo, HttpServletResponse response);

    MiaoShaUserVo getByMobile(String name);

    MiaoShaUser getByToken(String token);
}
