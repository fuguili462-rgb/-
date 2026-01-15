package com.itbaizhan.farm_system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itbaizhan.farm_system.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface UserMapper extends BaseMapper<User> {

    /**
     * 删除用户关联角色表
     * 因为这个sql语句要自己写，所以要定义一个方法
     * @Param 注解里面的参数要和collection里面填的参数一致
     */
    void deleteUserRolesByUserIds(@Param("userIds") List<Long> userIds);

    /**
     * 在sys_user_role表中,根据user_id能查到role_id,这样就能查到用户对应的角色
     * @param userId
     * @return
     */
    List<Long> selectRoleIdsByUserIds(@Param("userId") Long userId);

    void insertUserRole(@Param("userId") Long userId, @Param("roleIds") List<Long> roleIds);
}
