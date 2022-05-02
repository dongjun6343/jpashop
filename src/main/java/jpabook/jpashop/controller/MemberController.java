package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    // 화면열기
    @GetMapping("/members/new")
    public String createForm(Model model){
        //model : controller에서 view로 넘어갈때 데이터를 실어서 넘긴다.
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    // 등록
    @PostMapping("/members/new")
    // @Valid : MemberForm에서 Validation.
    // BindingResult : 오류가 담겨서 실행됨.
    // MemberForm : 실무에서는 폼화면이 단순하지 않으므로 멤버엔터티를 파라미터로 받아서 하기에는 힘들다.
    //                  ==> 그래서 MemberForm을 새로 만들어서 파라미터로 넘김.
    public String create(@Valid MemberForm form, BindingResult result){

        // 에러가 있으면 다시 멤버 생성화면으로 보냄.
        if ( result.hasErrors()){
            return "members/createMemberForm";
        }

        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);

        memberService.join(member);
        // 첫번째 페이지로 넘어감.
        return "redirect:/";
    }

    @GetMapping("/members")
    public String list(Model model){
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }
}
