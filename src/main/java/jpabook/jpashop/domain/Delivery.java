package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Delivery {

    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery")
    private Order order;

    private Address address;

    // ENUM이면 @Enumerated(EnumType.STRING) 꼭 사용!
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status; // READY, COMP
}