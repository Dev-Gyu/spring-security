package com.example.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class SecurityController {
    @GetMapping("/")
    public String home(HttpSession session){

        // Authentication(인증)객체는 AuthenticationProvider에서 인증이 성공할경우
        // AuthenticationManager에서 해당 유저 정보, 권한, 인증객체의 부가옵션(details), 인증여부(isAuthenticated) 세팅
        // 세팅 후 SecurityContext에 저장하게되고, SecurityContext는 다시 세션영역에 저장되어 참조가능해짐
        // 다만, SecurityContext의 전략(strategyName)에따라 해당 쓰레드에서만 인증객체 공유할지, 다른쓰레드에서도 인증객체 공유할지 결정됨
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityContext context = (SecurityContext)session.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
        Authentication authentication1 = context.getAuthentication();


        return "home";
    }

    @GetMapping("/thread")
    public String thread(){

        // SecurityContext의 기본 전략은 각각의 ThreadLocal마다 따로 인증객체 저장하도록 되어있기때문에
        // MainThread의 쓰레드랑 다른쓰레드(자식쓰레드포함)에서는 인증객체를 공유할 수 없음
        // 공유하려면 SecurityContextHolder의 strategyName을 다른걸로 변경해줘야
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        Authentication authenticaion = SecurityContextHolder.getContext().getAuthentication();


                    }
                }
        ).start();
        return "thread";
    }
}
