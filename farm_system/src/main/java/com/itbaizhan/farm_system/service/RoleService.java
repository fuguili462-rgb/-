package com.itbaizhan.farm_system.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itbaizhan.farm_common.execption.BusException;
import com.itbaizhan.farm_common.result.CodeEnum;
import com.itbaizhan.farm_system.entity.Permission;
import com.itbaizhan.farm_system.entity.Role;
import com.itbaizhan.farm_system.entity.User;
import com.itbaizhan.farm_system.mapper.PermissionMapper;
import com.itbaizhan.farm_system.mapper.RoleMapper;
import com.itbaizhan.farm_system.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class RoleService {
    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private UserMapper userMapper;

    /**
     * 分页查询角色
     *
     * @param page     当前页
     * @param size     每页大小
     * @param roleName 角色名称
     * @param status   角色状态
     * @return 角色详细信息
     */
    public IPage<Role> findRolePage(int page, int size, String roleName, String status) {
        Page<Role> roleObj = new Page<>(page, size);
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        if (roleName != null && !roleName.isEmpty()) {
            queryWrapper.eq("role_name", roleName);
        }
        if (status != null && !status.isEmpty()) {
            queryWrapper.eq("status", status);
        }
        queryWrapper.orderByDesc("role_sort");
        return roleMapper.selectPage(roleObj, queryWrapper);
    }

    /**
     * 根据Id查询角色,包含角色的权限信息
     *
     * @param id 角色id
     * @return 角色详细信息包括权限
     */
    public Role findRoleById(Long id) {
        Role role = roleMapper.selectById(id);
        //如果角色不为空，则查询角色的权限
        if (role != null) {
            List<Long> permissionIds = roleMapper.selectRolePermissionIds(id);
            if (permissionIds != null && !permissionIds.isEmpty()) {
                List<Permission> permissions = permissionMapper.selectByIds(permissionIds);
                role.setPermissions(permissions);
            }
        }
        return role;
    }

    /**
     * 添加角色
     *
     * @param role 角色信息
     * @return 成功放回true 失败返回false
     */
    public boolean addRole(Role role) {
        // 检查角色名称是否存在
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_name", role.getRoleName());
        Role existRole = roleMapper.selectOne(queryWrapper);
        if (existRole != null) {
            throw new BusException(CodeEnum.SYS_ROLE_EXIST);
        }

        role.setCreateTime(LocalDateTime.now());
        role.setStatus("0"); // 默认正常状态

        return roleMapper.insert(role) > 0;
    }

    /**
     * 修改角色
     *
     * @param role 用户信息
     * @return 成功放回true 失败返回false
     */
    public boolean updateRole(Role role) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_name", role.getRoleName())
                .ne("role_id", role.getRoleId());
        Role existRole = roleMapper.selectOne(queryWrapper);
        if (existRole != null) {
            throw new BusException(CodeEnum.SYS_ROLE_EXIST);
        }
        role.setUpdateTime(LocalDateTime.now());
        return roleMapper.updateById(role) > 0;
    }

    /**
     * 修改角色状态
     *
     * @param status id
     * @return 成功放回true 失败返回false
     */
    public boolean changeRoleStatus(Long id, String status) {
        Role role = new Role();
        role.setRoleId(id);
        role.setStatus(status);
        role.setUpdateTime(LocalDateTime.now());
        return roleMapper.updateById(role) > 0;
    }

    /**
     * 删除角色
     *
     * @param ids 角色id列表
     * @return 成功放回true 失败返回false
     * 删除角色不仅要删除sys_role里面的数据还要删除中间sys_user_role里面的数据
     * 还要删除角色关联的权限
     */
    public boolean deleteRole(List<Long> ids) {
        //删除角色与权限关联
        roleMapper.deleteRolePermissionByRoleIds(ids);
        //删除角色与用户关联
        roleMapper.deleteUserRolesByRoleIds(ids);
        //删除角色
        return roleMapper.deleteByIds(ids) > 0;
    }

    /**
     * 角色分配功能
     * 获取角色选择框下拉列表，就是把角色表里面的角色全部查询出来，
     *
     * @return 正常状态的角色列表
     */
    public List<Role> getRoleSelectList() {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", "0");
        queryWrapper.orderByDesc("role_sort");
        queryWrapper.select("role_id", "role_name");
        return roleMapper.selectList(queryWrapper);
    }


    /**
     * 查询已经分配角色的用户列表
     *
     * @param roleId   角色id
     * @param page     当前页
     * @param size     每页大小
     * @param userName 用户名称
     * @return 已分配该角色的用户分页数据
     */
    public IPage<User> getAssignedUsers(Long roleId, int page, int size, String userName) {
        Page<User> pageObj = new Page<>(page, size);
        // 查询已分配该角色的用户id
        List<Long> userIds = roleMapper.selectUserRoleByRoleId(roleId);
        if (userIds.isEmpty()) {
            return pageObj; // 返回空页面
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("user_id", userIds);
        if (StringUtils.hasText(userName)) {
            queryWrapper.like("user_name", userName)
                    .or()
                    .like("nick_name",userName);
        }
        queryWrapper.eq("status", "0"); // 只查询正常状态的用户
        queryWrapper.orderByDesc("create_time");
        return userMapper.selectPage(pageObj, queryWrapper);
    }

    /**
     * 查询未分配角色的用户列表
     *
     * @param roleId   角色id
     * @param page     当前页
     * @param size     每页大小
     * @param userName 用户名称
     * @return 未分配该角色的用户分页数据
     */
    public IPage<User> getUnassignedUsers(Long roleId, int page, int size, String userName) {
        Page<User> pageObj = new Page<>(page, size);
        //查询到userIds后就能在用户表中查询用户
        List<Long> userIds = roleMapper.selectUserRoleByRoleId(roleId);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (!userIds.isEmpty()) {
            queryWrapper.notIn("user_id", userIds);
        }
        if (StringUtils.hasText(userName)) {
            queryWrapper.like("user_name", userName)
                    .or()
                    .like("role_name", userName);
        }
        //只查询正常状态的用户
        queryWrapper.eq("status", "0");
        queryWrapper.orderByDesc("create_time");
        return userMapper.selectPage(pageObj, queryWrapper);
    }

    /**
     * 批量选择用户取消角色
     * 具体做法：在sys_user_role这张中间表中查询到要取消角色的role_id,然后根据role_id来取消相应的用户
     *
     * @param roleId  角色id
     * @param userIds 用户id列表
     * @return 成功返回true 失败返回false
     */
    public boolean cancelRoleFromUsers(Long roleId, List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return false;
        }
        roleMapper.deleteUserRolesByRoleIdAndUserIds(roleId, userIds);
        return true;
    }

    /**
     * 批量选择用户分配角色
     * 给用户分配角色时，要检查分配的当前角色是否存在，如果不存在则插入数据，否则不插入
     *
     * @param roleId  角色id
     * @param userIds 用户id列表
     * @return 成功返回true 失败返回false
     */
    public boolean assignRoleToUsers(Long roleId, List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return false;
        }
        for (Long userId : userIds) {
            int count = roleMapper.countUserRoleExists(roleId, userId);
            if (count == 0) {
                roleMapper.insertUserRole(roleId, userId);
            }
        }
        return true;
    }


    /**
     * 给角色分配权限
     *
     * @param roleId 角色id
     * @param permissionIds 权限id列表
     * @return true成功,false失败
     **/
    public boolean assignPermissions(Long roleId, List<Long> permissionIds) {
        //先删除角色已有的权限
        roleMapper.deleteRolePermissionByRoleIds(List.of(roleId));
        //再分配新的权限
        if (permissionIds != null && !permissionIds.isEmpty()) {
            roleMapper.insertRolePermission(roleId, permissionIds);
        }
        return true;
    }


    /**
     * 获取角色权限对象列表
     *  大白话就是查询一个角色的所有权限信息
     * @param roleId 角色ID
     * @return 权限列表
     * no usages
     *
     */
    public List<Permission> getRolePermissions(Long roleId) {
        //根据角色Id查询角色对应的权限id列表
        List<Long> permissionIds = roleMapper.selectRolePermissionIds(roleId);
        if (permissionIds != null && !permissionIds.isEmpty()) {
            return permissionMapper.selectByIds(permissionIds);
        }
        return new ArrayList<>();
    }


}



