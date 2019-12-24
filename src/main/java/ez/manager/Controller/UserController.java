package ez.manager.Controller;

import ez.manager.Mapper.UserMapper;
import ez.manager.Model.ResObject;
import ez.manager.Model.User;
import ez.manager.Util.Constant;
import ez.manager.Util.RunnableThreadWebCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {

    @Autowired
    private HttpSession httpSession;
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/user/login")
    public String loginGet() {
        return "login";
    }

    @PostMapping("/user/login")
    public String loginPost(User user, Model model) {
        User userInfo = userMapper.selectByNameAndPwd(user);
        if (userInfo != null) {
            httpSession.setAttribute("user", userInfo);
//            model.addAttribute("header", userInfo);
            return "redirect:dashboard";
        } else {
            model.addAttribute("error", "用户名或密码错误，请重新登录！");
            return "login";
        }
    }

    @GetMapping("/user/UserManage")
    public String userManageGet(Model model) {
        User user = (User) httpSession.getAttribute("user");
        User user1 = userMapper.selectByNameAndPwd(user);
        model.addAttribute("user", user1);
        return "UserManage/UserManage";
    }

    @PostMapping("/user/UserManage")
    public String userManagePost(Model model, User user, HttpSession httpSession) {
        int i = userMapper.update(user);
        httpSession.setAttribute("user",user);
        return "redirect:UserManage";
    }

    @ResponseBody
    @PostMapping("/user/Logout")
    public ResObject<Object> itemCategoryEditState(){
        User user = (User) httpSession.getAttribute("user");
        httpSession.removeAttribute("user");
        RunnableThreadWebCount.delUser(user);
        ResObject<Object> object = new ResObject<Object>(Constant.Code01, Constant.Msg01, null, null);
        return object;
    }

}
