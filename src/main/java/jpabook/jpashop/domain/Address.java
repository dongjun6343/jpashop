package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

// Embeddable : 내장타입
// 값타입은 변경이 되면 안된다.
@Embeddable
@Getter
public class Address {

    private String city;
    private String street;
    private String zipcode;

    // 기본생성자까지 만들어준다.
    // public이면 사용하기 쉬우니 protected까지 허용.
    // 함부로 new로 생성하면 안되겠네~ 이런느낌.
    // why? 값타입은 변경이 되면 안되니깐!
    protected Address() {
    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
