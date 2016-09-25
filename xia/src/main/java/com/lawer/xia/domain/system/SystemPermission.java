package com.lawer.xia.domain.system;

import org.springframework.web.bind.annotation.CrossOrigin;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by lindeng on 9/21/2016.
 * 菜单或者权限
 */
@Entity
@Table(name="t_sys_permission")
public class SystemPermission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="pid",nullable = false)
    private Long pId;

    @Column(name="url",length = 250,nullable = false)
    private String url;

    @Column(name="name",length = 40,nullable = false)
    private String name;

    @Column(name="resource_type",nullable = false,columnDefinition = "enum('menu','button')")
    private String resourceType;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "t_sys_role_permission", joinColumns = { @JoinColumn(name = "permission_id") }, inverseJoinColumns = {
            @JoinColumn(name = "role_id") })
    private Set<SystemRole> roles;

    @Column(name="permission",length = 45,nullable = false)
    private String permission;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getpId() {
        return pId;
    }

    public void setpId(Long pId) {
        this.pId = pId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public Set<SystemRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<SystemRole> roles) {
        this.roles = roles;
    }
}
