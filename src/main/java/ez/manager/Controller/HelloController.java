package ez.manager.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public class HelloController {
    @RequestMapping("/jfjafjjf")
    public String index(){
        return "Hello Spring Boot,Index!";
    }
    @RequestMapping(value = "/test",method = RequestMethod.GET)
    public String test(){
        return "Spring Boot Test Demo!";
    }
}
