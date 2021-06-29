package com.travel.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel.domain.ResultInfo;
import com.travel.domain.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.LogRecord;

@WebFilter(urlPatterns = {"/cart/*", "/order/*"}) //配置过滤路径 ,调用购物车方法的时候需要有登陆的权限
public class UserLoginFilterForCart implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {

        //1. 把request与response先强制类型转换为HttpServletRequest、HttpServletResponse,为了可以使用子类特有的方法
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        //2. 得到session
        HttpSession session = request.getSession();

        //3. 从session获取登陆用户对象
        User user = (User) session.getAttribute("user");

        //4. 判断登陆的用户是否为空，如果为空提示用户要先登陆，如果不为空直接放行
        if(user == null) {
            //设置响应头为json
            response.setHeader("content-type", "application/json;charset=utf-8");
//        因为是异步请求，被拦截的话直接输出JSON数据，封装成ResultInfo对象
            //4.1 把ResultInfo对象转换为json字符串
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonStr = objectMapper.writeValueAsString(new ResultInfo(false, "请先登录"));

            //4.2 再把json写出
            response.getWriter().print(jsonStr);
        } else {
            //5. 已经登陆
            chain.doFilter(request,response);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }


    @Override
    public void destroy() {

    }
}
