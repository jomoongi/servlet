package hello.servlet.web.frontcontroller.v1;

import hello.servlet.web.frontcontroller.v1.controller.MemberFormControllerV1;
import hello.servlet.web.frontcontroller.v1.controller.MemberListControllerV1;
import hello.servlet.web.frontcontroller.v1.controller.MemberSaveControllerV1;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "frontControllerServletV1", urlPatterns = "/front-controller/v1/*")
public class FrontControllerServletV1 extends HttpServlet {

    private Map<String, ControllerV1> controllerMap = new HashMap<>();

    //이게 서버 로딩시 호출되는 이유는
    //수업 내에서는 @ServletComponentScan을 사용하고 있어서 @WebServlet이 붙은 클래스들이 SpringBoot 로드시 빈으로 등록됩니다.
    //그때 빈이 생성될 것이므로 수업 내 코드 기준으로는 @ServletComponentScan으로 인해 @WebServlet이 스캔되고 빈이 등록될 때 생성자가 호출될 것입니다
    public FrontControllerServletV1() {
        System.out.println("FrontControllerServletV1.FrontControllerServletV1");
        controllerMap.put("/front-controller/v1/members/new-form", new MemberFormControllerV1());
        controllerMap.put("/front-controller/v1/members/save", new MemberSaveControllerV1());
        controllerMap.put("/front-controller/v1/members", new MemberListControllerV1());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("FrontControllerServletV1.service");

        String requestURI = request.getRequestURI();

        //요청 url에 해당하는 인스턴스를 가져온다
        ControllerV1 controller = controllerMap.get(requestURI);
        System.out.println(requestURI);
        if(controller == null){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        //인터페이스 상속받의 클래스의 process를 호출
        controller.process(request, response);
    }
}
