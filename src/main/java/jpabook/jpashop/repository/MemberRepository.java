package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

// 자동으로 스프링 빈으로 등록.
@Repository
@RequiredArgsConstructor
public class MemberRepository {

    // EntityManager 주입(injection).
    // @PersistenceContext -> @Autowired -> @RequiredArgsConstructor , final로 수정가능.
    private final EntityManager em;

    public void save(Member member){
        em.persist(member);
    }

    public Member findOne(Long id){
        return em.find(Member.class, id);
    }

    // JPQL
    public List<Member> findAll(){
        // ctrl + alt + n
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    // :name ==> 파라미터 바인딩.
    public List<Member> findByName(String name){
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
