package hello.servlet.web.frontcontroller.v2;

import hello.servlet.web.frontcontroller.MyView;
import hello.servlet.web.frontcontroller.v2.ControllerV2;
import hello.servlet.web.frontcontroller.v2.controller.MemberFormControllerV2;
import hello.servlet.web.frontcontroller.v2.controller.MemberListControllerV2;
import hello.servlet.web.frontcontroller.v2.controller.MemberSaveControllerV2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "frontControllerServletV2", urlPatterns = "/front-controller/v2/*")
public class FrontControllerServletV2 extends HttpServlet {

    private Map<String, ControllerV2> controllerMap = new HashMap<>();

    //이게 서버 로딩시 호출되는 이유는
    //수업 내에서는 @ServletComponentScan을 사용하고 있어서 @WebServlet이 붙은 클래스들이 SpringBoot 로드시 빈으로 등록됩니다.
    //그때 빈이 생성될 것이므로 수업 내 코드 기준으로는 @ServletComponentScan으로 인해 @WebServlet이 스캔되고 빈이 등록될 때 생성자가 호출될 것입니다
    public FrontControllerServletV2() {
        System.out.println("FrontControllerServletV2.FrontControllerServletV2");
        controllerMap.put("/front-controller/v2/members/new-form", new MemberFormControllerV2());
        controllerMap.put("/front-controller/v2/members/save", new MemberSaveControllerV2());
        controllerMap.put("/front-controller/v2/members", new MemberListControllerV2());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        //요청 url에 해당하는 인스턴스를 가져온다
        ControllerV2 controller = controllerMap.get(requestURI);
        System.out.println(requestURI);
        if(controller == null){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        //인터페이스 상속받의 클래스의 process를 호출
        //.process를 보면 return new MyView("/WEB-INF/views/new-form.jsp");가 있다
        //MyView가 생성되면서 private String viewPath;에 위의경로가 세팅된다
        MyView view = controller.process(request, response);
        view.render(request,response);
    }
}
