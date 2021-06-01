package com.hoya.service.server;

import com.hoya.service.vo.LoginVo;
import com.hoya.service.vo.MiaoShaUserVo;

public interface MiaoShaUserService {

    MiaoShaUserVo getById(Long id);

    void login(LoginVo loginVo);

    MiaoShaUserVo getByMobile(String name);
}
