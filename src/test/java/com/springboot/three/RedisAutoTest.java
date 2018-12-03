package com.springboot.three;

import org.apache.logging.log4j.util.Strings;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class RedisAutoTest {
	@Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void save(){
    	if(Strings.isNotBlank(stringRedisTemplate.opsForValue().get("zzp"))) {
    		stringRedisTemplate.delete("zzp");
    		stringRedisTemplate.opsForValue().set("zzp","test1");    		
    	}
//        Assert.assertEquals("big z",stringRedisTemplate.opsForValue().get("zzp"));
        
        stringRedisTemplate.opsForValue().set("user1", "felix");
        Assert.assertEquals("felix", stringRedisTemplate.opsForValue().get("user1"));
    }

}