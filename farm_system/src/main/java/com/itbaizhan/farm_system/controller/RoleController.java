package com.itbaizhan.farm_system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itbaizhan.farm_common.result.BaseResult;
import com.itbaizhan.farm_system.entity.Permission;
import com.itbaizhan.farm_system.entity.Role;
import com.itbaizhan.farm_system.entity.User;
import com.itbaizhan.farm_system.mapper.RoleMapper;
import com.itbaizhan.farm_system.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    /**
     * 分页查询角色
     * @param pageNum 当前页码，默认1
     * @param pageSize 每页大小，默认10
     * @param roleName 角色名称，可选
     * @param status 角色状态，可选
     * @return 角色分页结果
     */
    @GetMapping("/list")
    public BaseResult<IPage<Role>> getRoleList(
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "roleName", required = false) String roleName,
            @RequestParam(value = "status", required = false) String status) {
        IPage<Role> result = roleService.findRolePage(pageNum, pageSize, roleName, status);
        return BaseResult.ok(result);
    }

    /**
     * 根据ID查询角色
     * @param id 角色ID
     * @return 角色信息
     */
    @GetMapping("/getRoleById")
    public BaseResult<Role> findById(@RequestParam("id") Long id) {
        Role role = roleService.findRoleById(id);
        return BaseResult.ok(role);
    }

    /**
     * 新增角色
     * @param role 角色信息
     * @return 操作结果
     */
    @PostMapping("/addRole")
    public BaseResult addRole(@RequestBody @Validated Role role) {
        roleService.addRole(role);
        return BaseResult.ok("新增角色成功");
    }

    /**
     * 修改角色
     * @param role 角色信息
     * @return 操作结果
     */
    @PutMapping("/updateRole")
    public BaseResult updateRole(@RequestBody @Validated Role role) {
        roleService.updateRole(role);
        return BaseResult.ok();
    }

    /**
     * 删除角色
     * @param ids 角色ID字符串，多个ID用逗号分隔
     * @return 操作结果
     */
    @DeleteMapping("/deleteRole")
    public BaseResult deleteRole(@RequestParam("ids") String ids) {
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        roleService.deleteRole(idList);
        return BaseResult.ok();
    }

    /**
     * 获取角色选择框列表
     * @return 角色列表
     */
    @GetMapping("/selectList")
    public BaseResult<List<Role>> getRoleSelectList() {
        List<Role> roles = roleService.getRoleSelectList();
        return BaseResult.ok(roles);
    }

    /**
     * 修改角色状态
     * @param roleId 角色ID
     * @param status 角色状态
     * @return 操作结果
     */
    @PutMapping("/changeStatus")
    public BaseResult changeStatus(@RequestParam("roleId") Long roleId,
                                   @RequestParam("status") String status) {
        roleService.changeRoleStatus(roleId, status);
        return BaseResult.ok();
    }

    /**
     * 查询已分配该角色的用户列表
     * @param roleId 角色ID
     * @param pageNum 当前页码，默认1
     * @param pageSize 每页大小，默认10
     * @param userName 用户名，可选
     * @return 已分配该角色的用户分页结果
     */
    @GetMapping("/getAssignUsers")
    public BaseResult<IPage<User>> getAssignedUsers(
            @RequestParam("roleId") Long roleId,
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "userName", required = false) String userName) {
        IPage<User> result = roleService.getAssignedUsers(roleId, pageNum, pageSize, userName);
        return BaseResult.ok(result);
    }

    /**
     * 查询未分配该角色的用户列表
     * @param roleId 角色ID
     * @param pageNum 当前页码，默认1
     * @param pageSize 每页大小，默认10
     * @param userName 用户名，可选
     * @return 未分配该角色的用户分页结果
     */
    @GetMapping("/getUnassignUsers")
    public BaseResult<IPage<User>> getUnassignedUsers(
            @RequestParam("roleId") Long roleId,
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "userName", required = false) String userName) {
        IPage<User> result = roleService.getUnassignedUsers(roleId, pageNum, pageSize, userName);
        return BaseResult.ok(result);
    }

    /**
     * 批量选择用户取消角色
     * @param roleId 角色ID
     * @param userIds 用户ID字符串，多个ID用逗号分隔
     * @return 操作结果
     */
    @DeleteMapping("/cancelRoleFromUsers")
    public BaseResult cancelRoleFromUsers(
            @RequestParam("roleId") Long roleId,
            @RequestParam("userIds") String userIds) {
        List<Long> userIdList = Arrays.stream(userIds.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        roleService.cancelRoleFromUsers(roleId, userIdList);
        return BaseResult.ok();
    }

    /**
     * 批量选择用户分配角色
     * @param roleId 角色ID
     * @param userIds 用户ID列表，多个ID用逗号分隔
     * @return 操作结果
     */
    @PostMapping("/assignRoleToUsers")
    public BaseResult assignRoleToUsers(
            @RequestParam("roleId") Long roleId,
            @RequestParam("userIds") String userIds) {
        List<Long> userIdList = Arrays.stream(userIds.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        roleService.assignRoleToUsers(roleId, userIdList);
        return BaseResult.ok();
    }

    /**
     * 给角色分配权限
     * @param roleId 角色ID
     * @param permissionIds 权限ID列表，多个ID用逗号分隔
     * @return 操作结果
     */
    @PutMapping("/assignPermissions")
    public BaseResult assignPermissions(
            @RequestParam("roleId") Long roleId,
            @RequestParam("permissionIds") String permissionIds) {
        List<Long> permissionIdList = Arrays.stream(permissionIds.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        roleService.assignPermissions(roleId, permissionIdList);
        return BaseResult.ok();
    }

    /**
     * 获取角色的权限列表
     * @param roleId 角色ID
     * @return 权限对象列表
     */
    @GetMapping("/getRolePermissions")
    public BaseResult<List<Permission>> getRolePermissions(@RequestParam("roleId") Long roleId) {
        List<Permission> permissions = roleService.getRolePermissions(roleId);
        return BaseResult.ok(permissions);
    }

}
