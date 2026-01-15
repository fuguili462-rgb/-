package com.itbaizhan.farm_system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itbaizhan.farm_common.execption.BusException;
import com.itbaizhan.farm_common.result.BaseResult;
import com.itbaizhan.farm_common.result.CodeEnum;
import com.itbaizhan.farm_system.entity.User;
import com.itbaizhan.farm_system.mapper.UserMapper;
import com.itbaizhan.farm_system.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;


    /**
     * 根据id查询用户
     */
    @GetMapping("/getUserById")
    public BaseResult<User> findUserById(Long id){
        User user = userService.findUserById(id);
        return BaseResult.ok(user);
    }

    /**
     *
     * @param pageNum
     * @param pageSize
     * @param username
     * @param status
     * @return
     */
    @GetMapping("/list")
    public BaseResult<IPage<User>> findUserPage(
            @RequestParam(value = "pageNum" ,defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize" ,defaultValue = "10") int pageSize,
            @RequestParam(value ="username" , required = false) String username,
            @RequestParam(value ="status" , required = false) String status){

        IPage<User> userPage = userService.findUserPage(pageNum, pageSize, username, status);
        return BaseResult.ok(userPage);
    }

    /**
     * 添加用户
     * @param user
     * @return
     * 不要忘记加@RequestBody注解！！！！！！
     * @RequestBody会告诉 Spring：这是一个 JSON 请求体，需要用 Jackson 反序列化成 User 对象。
     */
    @PostMapping("/addUser")
    public BaseResult addUser(@RequestBody User user){
        log.info("接收到的用户对象: {}", user); // ← 查看 userName 是否为 null
        userService.addUser(user);
        return BaseResult.ok();
    }


    /**
     * 修改用户一般用put请求
     * @param user
     * @return
     */
    @PutMapping("/updateUser")
    public BaseResult updateUser(@RequestBody User user){
        userService.updateUser(user);
        return BaseResult.ok();
    }

    /**
     * 重置密码
     * @param id
     * @param newPassword
     * @return
     */
    @PutMapping("/resetPassword")
    public BaseResult resetPwd(Long id,String newPassword){
        userService.resetPassword(id, newPassword);
        return BaseResult.ok();
    }

    /**
     * 修改用户状态
     * @param id
     * @return
     */
    @PutMapping("/changeStatus")
    public BaseResult changeStatus(Long id,String status){
        userService.updateStatus(id,status);
        return BaseResult.ok();
    }

    /**
     * 前端传过来的id是字符串类型
     * @param ids
     * @return
     */
    @DeleteMapping("/deleteUser")
    public BaseResult deleteUser(String ids){
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        userService.deleteUser(idList);
        return BaseResult.ok();
    }

    /**
     * 修改用户角色
     * @param userId
     * @param roleIds
     * @return
     */
    @PutMapping("/assignRoles")
    public BaseResult assignRole(@RequestParam(value = "userId") Long userId,
                                 @RequestParam(value = "roleIds",required = false) List<Long> roleIds){
        userService.assignRole(userId,roleIds);
        return BaseResult.ok();
    }
}
