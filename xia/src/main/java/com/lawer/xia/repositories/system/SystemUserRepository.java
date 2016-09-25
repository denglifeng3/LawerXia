package com.lawer.xia.repositories.system;

import com.lawer.xia.domain.system.SystemUser;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by lindeng on 9/21/2016.
 */
public interface SystemUserRepository extends CrudRepository<SystemUser,Long> {
    SystemUser findByUserName(String userName);
}
