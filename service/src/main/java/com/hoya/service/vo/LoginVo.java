package com.hoya.service.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginVo {

    @NotNull
    private String mobile;

    @NotNull
    @Length(min = 1)
    private String password;

}
