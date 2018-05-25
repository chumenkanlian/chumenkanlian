package com.chumenkanlian.controller;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.UUID;

/**
 * Created by HUANGJY on 2018/5/17.
 */
@RestController
public class HelloController {

    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }


    @GetMapping("/tests")
    String sessionTest(HttpSession session) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        UUID uid = (UUID) session.getAttribute("uid");
        if (uid == null) {
            uid = UUID.randomUUID();
        }
//        redisTemplate.
        session.setAttribute("uid", uid);
        return uid.toString();
    }

}
