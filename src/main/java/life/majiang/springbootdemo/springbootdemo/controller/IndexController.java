package life.majiang.springbootdemo.springbootdemo.controller;

import life.majiang.springbootdemo.springbootdemo.dto.PaginationDTO;
import life.majiang.springbootdemo.springbootdemo.dto.QuestionDTO;
import life.majiang.springbootdemo.springbootdemo.mapper.UserMapper;
import life.majiang.springbootdemo.springbootdemo.model.User;
import life.majiang.springbootdemo.springbootdemo.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionService questionService;

    //   一个斜杠  表示根目录  就是默认访问页面
    @GetMapping("/")
    public String index(HttpServletRequest request,
                        Model model,
                        @RequestParam(name = "page" ,defaultValue = "1")Integer page) {
        Cookie[] cookies = request.getCookies();
        if(cookies != null && cookies.length!=0)
            for (Cookie cookie:cookies){
                if(cookie.getName().equals("token")){
                    String token = cookie.getValue();
                    User user = userMapper.findByToken(token);
                    if(null != user){
                        request.getSession().setAttribute("user",user);
                    }
                    break;
                }
            }

        Integer size = 2 ;
        PaginationDTO pagination = questionService.list(page,size);
        model.addAttribute("pagination",pagination);
        if(pagination.getQuestions().size() > 0 ){
            return "index";
        }
        else {
            return "index";
        }



    }

}
