package jpabook.jpashop.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.QMember;
import jpabook.jpashop.domain.QOrder;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
    private final EntityManager em;

    public void save(Order order){
        em.persist(order);
    }

    public Order findOne(Long id){
        return em.find(Order.class, id);
    }

    // 검색 : 파라미터 값이 넘어가야함.
    // 동적쿼리 ==> Querydsl로 처리.


    //language=JPAQL
    public List<Order> findAllByString(OrderSearch orderSearch) {

        String jpql = "select o From Order o join o.member m";
        boolean isFirstCondition = true;
        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " o.status = :status";
        }
        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " m.name like :name";
        }
        TypedQuery<Order> query = em.createQuery(jpql, Order.class)
                .setMaxResults(1000); //최대 1000건
        if (orderSearch.getOrderStatus() != null) {
            query = query.setParameter("status", orderSearch.getOrderStatus());
        }
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            query = query.setParameter("name", orderSearch.getMemberName());
        }
        return query.getResultList();
    }

    // fetch join은 JPA를 쓰려면 100% 이해를 해야함.
    // 패치 조인을 사용해서 쿼리 1번에 조회함.
    // 쿼리가 조금만 더 복잡해지면 QueryDSL을 주로 사용함.
    public List<Order> findAllWithMemberDelivery() {
        return em.createQuery(
                "select o from Order o" +
                        " join fetch o.member m" +
                        " join fetch o.delivery d", Order.class
        ).getResultList();
    }

    // distinct : db의 distinct 는 모두 동일해야 중복이 제거가 가능.
    // jpa에서는 자체적으로 distinct가 있으면 Order가 같은 아이디 값이면 중복을 제거함.

    // distinct 1) db에 distinct 날려줌.
    //          2) Root (= Order) 값 중복제거.
    //  결과적으로 페치 조인으로 SQL 1번만 실행됨. (성공)

    //  하지만 단점이 있다!!! --> 페이징이 불가능함.
    //              1대다를 페치조인을 하는 순간 페이징이 불가능해진다..! (중요)
    //              하이버네이트에서 메모리 에러가 나옴.
    public List<Order> findAllWtihItem() {
        return em.createQuery(
                "select distinct o from Order o" +
                        " join fetch o.member m" +
                        " join fetch o.delivery d" +
                        " join fetch o.orderItems oi" +
                        " join fetch oi.item i" , Order.class
        ).setFirstResult(1)
         .setMaxResults(100)
         .getResultList();
    }

    public List<Order> findAllWithMemberDelivery(int offset, int limit) {
        return em.createQuery(
                "select o from Order o" +
                        " join fetch o.member m" +
                        " join fetch o.delivery d", Order.class
        ).setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    // QueryDSL
    // generated - 코드 git commit 하지말자.
    // 빌드타임에 generated 되면 됨.
    public List<Order> findAll(OrderSearch orderSearch){
        JPAQueryFactory query = new JPAQueryFactory(em);

        // static import로 줄일 수 있다.
        // 위에서 선언.
        QOrder order = QOrder.order;
        QMember member = QMember.member;

        return query
                .select(order)
                .from(order)
                .join(order.member, member)
                .where(statusEq(orderSearch.getOrderStatus()), nameLike(orderSearch.getMemberName()))
                .limit(1000)
                .fetch();
    }

    private BooleanExpression statusEq(OrderStatus statusCond){
        if(statusCond == null ){
            return null;
        }
        return QOrder.order.status.eq(statusCond);
    }

    private BooleanExpression nameLike(String memberName) {
        if (StringUtils.hasText(memberName)){
            return null;
        }
        return QMember.member.name.like(memberName);
    }
}
