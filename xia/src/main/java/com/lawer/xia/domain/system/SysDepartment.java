package com.lawer.xia.domain.system;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by lindeng on 9/21/2016.
 * 部门表
 */
@Entity
@Table(name="t_sys_department")
public class SysDepartment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="dep_name",length = 40,nullable = false)
    private String depName;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "sysDepartment")
    private Set<SystemUser> users;

    @ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(name = "t_sys_dep_role", joinColumns = { @JoinColumn(name = "dep_id") }, inverseJoinColumns = {
            @JoinColumn(name = "role_id") })
    private Set<SystemRole> roles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDepName() {
        return depName;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }

    public Set<SystemUser> getUsers() {
        return users;
    }

    public void setUsers(Set<SystemUser> users) {
        this.users = users;
    }

    public Set<SystemRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<SystemRole> roles) {
        this.roles = roles;
    }
}
