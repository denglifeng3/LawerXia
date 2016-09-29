package com.lawyer.xia.domain.system;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by lindeng on 9/21/2016.
 * 用户表
 */
@Entity
@Table(name="t_sys_user")
public class SystemUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long userId;

    @Column(name="user_name",length = 40,nullable = false)
    private String userName;

    @Column(name="pass_word",length = 250,nullable = false)
    private String passWord;

    @ManyToOne
    @JoinColumn(name = "dep_id")
    private SysDepartment sysDepartment;

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name = "t_sys_user_role", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = {
            @JoinColumn(name = "role_id") })
    private Set<SystemRole> roles;

    private String salt;

    private Integer state;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public SysDepartment getSysDepartment() {
        return sysDepartment;
    }

    public void setSysDepartment(SysDepartment sysDepartment) {
        this.sysDepartment = sysDepartment;
    }

    public Set<SystemRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<SystemRole> roles) {
        this.roles = roles;
    }

    public String getSalt() {
        return this.userName+ this.salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "SystemUser{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", passWord='" + passWord + '\'' +
                ", sysDepartment=" + sysDepartment +
                ", roles=" + roles +
                ", salt='" + salt + '\'' +
                ", state=" + state +
                '}';
    }

    @Transient
    public Set<String> getRolesName() {
        Set<SystemRole> roles = getRoles();
        Set<String> userRoles = new HashSet<String>();
        for (SystemRole role : roles) {
            userRoles.add(role.getRoleName());
        }
        roles=getSysDepartment().getRoles();
        for (SystemRole role : roles) {
            userRoles.add(role.getRoleName());
        }
        return userRoles;
    }

    @Transient
    public List<SystemRole> getRolesList() {
        Set<SystemRole> roles = getRoles();
        List<SystemRole> userRoles = new ArrayList<SystemRole>();
        for (SystemRole role : roles) {
            userRoles.add(role);
        }
        roles=getSysDepartment().getRoles();
        for (SystemRole role : roles) {
            userRoles.add(role);
        }
        return userRoles;
    }
}
