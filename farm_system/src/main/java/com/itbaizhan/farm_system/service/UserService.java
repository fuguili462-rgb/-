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
import java.util.List;

@Service
//开启事务
@Transactional
public class UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private PermissionMapper permissionMapper;

    /**
     * 根据id查询用户
     * 注意：查询用户操作不仅要在用户表中查到该用户的信息，还要查到该用户的角色信息以及权限信息
     * sys_user_role表中根据user_id能查到role_id,这样就能查到用户对应的角色
     * sys_role_permission表中根据role_id能查到permission_id,这样能查到角色对应的权限
     * ！！！在接口中定义方法
     * @param id
     * @return
     */
    public User findUserById(Long id) {
        // 查询用户基本信息
        User user = userMapper.selectById(id);
        if (user != null) {
            // 查询用户的角色ID列表
            List<Long> roleIds = userMapper.selectRoleIdsByUserIds(id);
            if (roleIds != null && !roleIds.isEmpty()) {
                // 查询角色详细信息
                List<Role> roles = roleMapper.selectByIds(roleIds);
                // 为每个角色查询权限信息
                for (Role role : roles) {
                    List<Long> permissionIds = roleMapper.selectRolePermissionIds(role.getRoleId());
                    if (permissionIds != null && !permissionIds.isEmpty()) {
                        List<Permission> permissions = permissionMapper.selectByIds(permissionIds);
                        role.setPermissions(permissions);
                    }
                }
                user.setRoles(roles);
            }
        }
        return user;
    }

    /**
     * 添加用户
     * @param user
     * @return 返回一个布尔类型，添加成功返回true否则返回false
     */
    public boolean addUser(User user){
        //判断用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", user.getUserName());
        User userExist = userMapper.selectOne(queryWrapper);
        if(userExist != null){
            throw new BusException(CodeEnum.SYS_USER_EXIST);
        }
        //设置默认值
        user.setCreateTime(LocalDateTime.now());
        user.setStatus("0");//用户状态默认为0
        if(!StringUtils.hasText(user.getPassword())){
            user.setPassword("123456");
        }

        //向数据库添加用户
        return userMapper.insert(user) > 0;
    }

    /**
     * 修改用户
     * @param user
     * @return
     */
    public boolean updateUser(User user){
        //检查用户名是否重复（排除自己）
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", user.getUserName()).ne("user_id", user.getUserId());
        User userExist = userMapper.selectOne(queryWrapper);
        if(userExist != null){
            throw new BusException(CodeEnum.SYS_USER_EXIST);
        }
        //用户修改过后，更新修改的时间
        user.setUpdateTime(LocalDateTime.now());
        //如果用户修改用户信息的时候没有修改密码，则不修改密码
        if(!StringUtils.hasText(user.getPassword())){
            user.setPassword(null);
        }

        return userMapper.updateById(user) > 0;
    }

    /**
     * 重置用户密码，重置密码和修改密码不一样
     * 重置密码不需要输入旧密码，修改密码需要
     * 只有超级管理员可以重置密码
     * @param id
     * @return
     */
    public boolean resetPassword(Long id,String newPassword){
        User user = new User();
        user.setUserId(id);
        user.setPassword(newPassword);
        user.setUpdateTime(LocalDateTime.now());
        user.setPwdUpdateDate(LocalDateTime.now());
        return userMapper.updateById(user) > 0;
    }

    /**
     * 修改用户状态
     * @param userId 用户ID
     * @param status 新状态
     * @return 操作结果
     */
    public boolean updateStatus(Long userId,String status) {
        User user = new User();
        user.setUserId(userId);
        user.setStatus(status);
        user.setUpdateTime(LocalDateTime.now());

        return userMapper.updateById(user) > 0;
    }

    /**
     * 删除用户
     * 注意！删除用户不仅要把用户表中的数据删除，还要把用户关联的角色表里面的数据删除
     * @param ids
     * @return
     */
    public boolean deleteUser(List<Long> ids){
        //删除用户关联角色表
        userMapper.deleteUserRolesByUserIds(ids);
        //删除用户
        return  userMapper.deleteByIds(ids) > 0;
    }

    /**
     * 分页查询
     * @param page 当前页
     * @param size 每页大小
     * @param username 根据用户名查
     * @param status 根据用户状态查
     * @return
     */
    public IPage<User> findUserPage(int page, int size,String username,String status){
        Page<User> pageObj = new Page<>(page, size);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(StringUtils.hasText(username)){
            queryWrapper.like("user_name", username)
                    .or()
                    .like("nick_name", username);
        }
        if(StringUtils.hasText(status)){
            queryWrapper.eq("status", status);
        }
        queryWrapper.orderByDesc("create_time");
        return userMapper.selectPage(pageObj, queryWrapper);
    }

    /**
     * 修改用户状态
     * @param userId
     * @param roleIds
     * @return
     */
    public boolean assignRole(Long userId,List<Long> roleIds){
        //删除用户原来的角色
        List<Long> userIds = new ArrayList<>();
        userIds.add(userId);
        userMapper.deleteUserRolesByUserIds(userIds);
        //添加新角色
        if(roleIds != null && !roleIds.isEmpty()) {
            userMapper.insertUserRole(userId, roleIds);
        }

        return true;
    }
}
