package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
// 데이터를 변경하는 곳에는 트랜젝션이 꼭 있어야 한다.
// 조회(읽기)하는 곳에서는 true 해주는게 성능이 올라감.
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

//  @Autowired
    private final MemberRepository memberRepository;

    // 생성자 인젝션 사용.
//    @Autowired 이게 없어도 스프링이 자동으로 Autowired해준다 --> 위에 final로 변경
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    // RequiredArgsConstructor => final로 만들어져있는 것을 자동으로 위에 코드를 만들어줌.

    /*
     * 회원 가입
     */
    @Transactional
    public Long join(Member member){
        validateDuplicateMember(member); // 중복 회원 검사
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        // EXCEPTION
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //회원 전체 조회
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }
}
