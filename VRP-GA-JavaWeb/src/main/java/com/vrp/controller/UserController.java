package com.vrp.controller;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.vrp.controller.utils.Res;
import com.vrp.pojo.*;
import com.vrp.service.IDeliveryService;
import com.vrp.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

/**
 * @Author jinjun99
 * @Date Created in 2022/4/8 19:14
 * @Description 处理前端对用户表的相关请求
 * @Since version-1.0
 */
@RestController
@RequestMapping("/user")
public class UserController {
    /**
     * 注入用户表服务对象
     */
    @Autowired
    private IUserService userService;
    /**
     * 注入配送数据表的服务对象
     */
    @Autowired
    private IDeliveryService deliveryService;

    /**
     * 处理前端用户登录请求
     * @param req 客户端浏览器发出的请求被封装成为一个HttpServletRequest对象
     * @param user 前端页面用户登录输入的数据
     * @return 标准的返回值格式
     */
    @PostMapping("/login")
    public Res login(HttpServletRequest req, @RequestBody User user) {

        /*根据前端页面提交的用户名查询数据库*/
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        lqw.eq(User::getUserName, user.getUserName());
        User user1 = userService.getOne(lqw);
        /*如果没有查询到则返回登录失败信息*/
        if (user1 == null) {
            return new Res(false, "用户名不存在〒▽〒");
        }
        /*如果获取到了再对比前端输入的用户密码和类型与数据库查出来的是否一致*/
        boolean b = Objects.equals(user.getPassword(), user1.getPassword()) &&
                Objects.equals(user.getUserType(), user1.getUserType());
        /*任意一项不一致就返回密码错误信息*/
        if (!b) {
            return new Res(false, "密码错误〒▽〒");
        }
        /*登录成功，把id存入Session并返回登录成功的结果*/
        req.getSession().setAttribute("user",user1.getId());
        return new Res(true, user1, "登录成功(*^▽^*)");

    }


    /**
     * 处理前端用户退出登录请求
     * @param req 客户端浏览器发出的请求被封装成为一个HttpServletRequest对象
     * @return
     */
    @PostMapping("/signOut")
    private Res signOut(HttpServletRequest req){
        /*清理Session中的用户id*/
        req.getSession().removeAttribute("user");
        return new Res(true,"退出成功");
    }

    /**
     * 处理前端用户注册请求
     * @param register 前端页面用户注册输入的数据
     * @return
     */
    @PostMapping
    public Res register(@RequestBody Register register) {
        /*根据前端页面提交的用户名查询数据库*/
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        lqw.eq(User::getUserName, register.getUserName());
        User user1 = userService.getOne(lqw);
        /*如果查到数据则返回注册失败信息*/
        if (user1 != null) {
            return new Res(false, "该用户名已存在(இωஇ )");
        }
        /*如果没查到,再查看注册的用户类型是否为管理员*/
        if (Objects.equals(register.getUserType(), "管理员")) {
            /*查询配送数据表获得管理员注册码*/
            List<Delivery> deliveries = deliveryService.list();
            /*判断用户输入的注册码是否与数据库中的相符,不符则返回失败信息*/
            if (!Objects.equals(register.getRegistrationCode(), deliveries.get(0).getRegistrationCode())) {
                return new Res(false, "管理员注册码不正确(இωஇ )");
            }
        }
        /*所有条件都符合以后,把前端用户输入的注册信息封装到一个用户对象中*/
        User user = new User();
        user.setUserName(register.getUserName());
        user.setPassword(register.getPassword());
        user.setUserType(register.getUserType());
        user.setNetName(register.getNetName());
        user.setPhoneNumb(register.getPhoneNumb());
        /*调用用户表服务对象的添加方法添加一条用户数据*/
        Boolean flag = userService.save(user);
        return new Res(flag, flag ? "注册成功٩(๑^o^๑)۶" : "注册失败(இωஇ )");
    }

    /** 处理前端用户修改信息请求
     * @param modify 前端页面用户修改信息输入的数据
     * @return
     */
    @PutMapping
    private Res upDate(@RequestBody Modify modify) {
        /*根据前端页面提交的用户名查询数据库*/
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        lqw.eq(User::getUserName, modify.getUserName());
        User user1 = userService.getOne(lqw);

        /*如果没有查询到则返回修改失败信息*/
        if (user1 == null) {
            return new Res(false, "用户名不存在〒▽〒");
        }
        /*如果获取到了再对比前端输入的用户旧密码和类型与数据库查出来的是否一致*/
        boolean b = Objects.equals(modify.getOldPassword(), user1.getPassword()) &&
                Objects.equals(modify.getUserType(), user1.getUserType());
        /*任意一项不一致就返回密码错误信息*/
        if (!b) {
            return new Res(false, "旧密码错误〒▽〒");
        }
        /*所有条件都符合以后,把前端用户输入的注册信息封装到一个用户对象中*/
        user1.setPassword(modify.getNewPassword());
        user1.setNetName(modify.getNetName());
        user1.setPhoneNumb(modify.getPhoneNumb());
        /*调用用户表服务对象的修改方法修改一条用户数据*/
        boolean flag = userService.updateById(user1);
        return new Res(flag, flag ? "修改成功(*^ω^*)" : "修改失败(´•̥̥̥ω•̥̥̥`)");
    }


}
