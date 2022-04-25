package jpabook.jpashop.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

// 항상 Controller -> View로 랜더링한다.
@Controller
@Slf4j
public class HomeController {

    // Logger logger = LoggerFactory.getLogger(getClass()); ==> lombok의 slf4j 어노테이션 쓰면 됨.

    @RequestMapping("/")
    public String home(){
        log.info("home Controller");
        return "home"; // resource - templates
    }
}
