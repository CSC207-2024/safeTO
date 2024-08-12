package com.demo.mapper;

import com.demo.pojo.RegisterUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface UserMapper {
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertUser(RegisterUser user);

    RegisterUser findByEmail(String email);
}
