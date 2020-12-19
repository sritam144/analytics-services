package com.asiczen.analytics.repository;

import com.asiczen.analytics.model.EndOfDayMessage;
import com.asiczen.analytics.model.OrganizationView;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class RedisOrganizationMessageRepository {

    private static final String KEY = "ORGPARAMETERS";

    private HashOperations<String, String, OrganizationView> hashOperations;

    private RedisTemplate<String, OrganizationView> redisTemplate;

    public RedisOrganizationMessageRepository(RedisTemplate<String, OrganizationView> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = this.redisTemplate.opsForHash();
    }

    public OrganizationView get(String orgRefName) {
        log.trace("Looking for organization with reference name : {}", orgRefName);
        ObjectMapper objMapper = new ObjectMapper();
        return objMapper.convertValue(hashOperations.get(KEY, orgRefName), OrganizationView.class);
    }


}


