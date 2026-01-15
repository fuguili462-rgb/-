package com.itbaizhan.farm_system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itbaizhan.farm_system.entity.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMapper extends BaseMapper<Role> {
    /**
     * 在sys_role_permission表中,根据role_id能查到角色对应的permission_id也就是权限
     *
     * @param roleId
     * @return
     */
    List<Long> selectRolePermissionIds(@Param("roleId") Long roleId);

    /**
     * 根据角色id删除角色与权限关联
     */
    void deleteRolePermissionByRoleIds(@Param("roleIds") List<Long> roleIds);

    /**
     * 根据角色id删除角色与用户关联
     */
    void deleteUserRolesByRoleIds(@Param("roleIds") List<Long> roleIds);

    /**
     * 在用户角色中间表中根据角色id查询用户id
     *
     * @param roleId
     * @return
     */
    List<Long> selectUserRoleByRoleId(@Param("roleId") Long roleId);

    /**
     * service方法中的批量选择用户取消角色
     * 具体做法：在sys_user_role这张中间表中查询到要取消角色的role_id,然后根据role_id来取消相应的用户
     * @param roleId
     * @param userIds
     */
    void deleteUserRolesByRoleIdAndUserIds(@Param("roleId") Long roleId, @Param("userIds") List<Long> userIds);

    /**
     * 检查分配的角色是否已存在
     * @param roleId 角色id
     * @param userId 用户id
     * @return 数量
     */
    int countUserRoleExists(@Param("roleId") Long roleId,@Param("userId")  Long userId);

    /**
     * 插入角色
     * @param roleId 角色id
     * @param userId 用户id
     */
    void insertUserRole(@Param("roleId") Long roleId,@Param("userId")  Long userId);

    /**
     * 给角色分配权限
     * @param roleId 角色id
     * @param permissionIds 权限id列表
     */
    void insertRolePermission(@Param("roleId")Long roleId,@Param("permissionIds") List<Long> permissionIds);



}
