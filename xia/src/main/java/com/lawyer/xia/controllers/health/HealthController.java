package com.lawyer.xia.controllers.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by lindeng on 9/27/2016.
 */
@RestController
public class HealthController implements HealthIndicator{
    @Override
    @RequestMapping("/health")
    public Health health() {
        return  Health.up().build();
    }
}
