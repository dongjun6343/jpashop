package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// T : 제네릭, 타입.
// 스프링 데이터 JPA -> JpaRepository
public interface MemberRepository extends JpaRepository<Member, Long> {


    // findByName처럼 일반화하기 어려운 기능도 메서드의 이름으로 정확한 JPQL 쿼리를 실행한다.
    // findByName -> select m from Member m where m.name = :name
    // 개발자는 인터페이스만 만들어주면 되고, 구현체는 스프링 데이터 JPA가 애플리케이션 실행시점에 주입해준다.
    //      - 생산성 높아짐. (JPA에 대해서 잘 이해를 하자!)

    // 스프링, JPA , 스프링 데이터 JPA, QueryDSL(JPQL을 자바코드로 작성하게 해줌)R
    List<Member> findByName(String name);
}
