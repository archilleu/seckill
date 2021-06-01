package com.hoya.service.vo;

import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MiaoShaUserVo {

    private Long id;

    private String nickname;

    private String password;

    private String salt;

    private String head;

    private Date registerDate;

    private Date lastLoginDate;

    private Integer loginCount;

}