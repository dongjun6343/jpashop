package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    private Long id;

    private String name;

    @Embedded
    private Address address;

    // mappedBy : 맵핑된 거울일 뿐이야.
    //            여기서는 조회만 한다.
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();


}
