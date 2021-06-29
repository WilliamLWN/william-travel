import com.github.pagehelper.PageInfo;
import com.travel.domain.ResultInfo;
import com.travel.domain.Route;
import com.travel.domain.User;
import com.travel.service.RouteService;
import com.travel.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class AppTest {

    @Autowired
    private UserService userService;

    @Autowired
    private RouteService routeService;


    @Test
    public void test01(){
        User user = new User(0, "Kate","123","15678909877",null);
        ResultInfo resultInfo = userService.register(user, "12345");
        System.out.println("操作的结果："+resultInfo);
    }

//    @Test
//    public void test02(){
//        //pageInfo就是你们以前PageBean
//        PageInfo<Route> pageInfo = routeService.findRouteByCid(1, 2);
//        System.out.println("页面的数据："+pageInfo.getList());
//        System.out.println("总记录数："+pageInfo.getTotal());
//        System.out.println("总页数："+pageInfo.getPages());
//        System.out.println("当前页："+pageInfo.getPageNum());
//        System.out.println(pageInfo.getPageSize());
//    }
}