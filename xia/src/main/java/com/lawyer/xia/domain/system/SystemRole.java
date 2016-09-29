package com.lawyer.xia.domain.system;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by lindeng on 9/21/2016.
 * 权限表
 */
@Entity
@Table(name="t_sys_role")
public class SystemRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="role_name",length = 40,nullable = false)
    private String roleName;

    @ManyToMany
    @JoinTable(name = "t_sys_user_role", joinColumns = { @JoinColumn(name = "role_id") }, inverseJoinColumns = {
            @JoinColumn(name = "user_id") })
    private Set<SystemUser> users;

    @ManyToMany
    @JoinTable(name = "t_sys_dep_role", joinColumns = { @JoinColumn(name = "role_id") }, inverseJoinColumns = {
            @JoinColumn(name = "dep_id") })
    private Set<SysDepartment> departments;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "t_sys_role_permission", joinColumns = { @JoinColumn(name = "role_id") }, inverseJoinColumns = {
            @JoinColumn(name = "permission_id") })
    private Set<SystemPermission> menus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Set<SystemUser> getUsers() {
        return users;
    }

    public void setUsers(Set<SystemUser> users) {
        this.users = users;
    }

    public Set<SysDepartment> getDepartments() {
        return departments;
    }

    public void setDepartments(Set<SysDepartment> departments) {
        this.departments = departments;
    }

    public Set<SystemPermission> getMenus() {
        return menus;
    }

    public void setMenus(Set<SystemPermission> menus) {
        this.menus = menus;
    }

    @Transient
    public List<String> getPermissionsName() {
        List<String> list = new ArrayList<String>();
        Set<SystemPermission> perlist = getMenus();
        for (SystemPermission per : perlist) {
            list.add(per.getPermission());
        }
        return list;
    }
}
