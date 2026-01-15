package com.itbaizhan.farm_system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sys_role")
public class Role {
    /** 角色ID */
    @TableId(value = "role_id",type = IdType.AUTO)
    private Long roleId;

    /** 角色名称 */
    private String roleName;

    /** 显示顺序 */
    private Integer roleSort;

    /** 角色状态（0正常 1停用） */
    private String status;

    /** 创建者 */
    private String createBy;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新者 */
    private String updateBy;

    /** 更新时间 */
    private LocalDateTime updateTime;

    /** 备注 */
    private String remark;

    /** 角色权限列表（非数据库字段） */
    @TableField(exist = false)
    private List<Permission> permissions;
}

