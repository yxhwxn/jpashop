package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@Getter @Setter
public class Address {

    private String city;
    private String street;
    private String zipcode;

    /* 값 타입은(Address) 변경 불가능하게 설계해야 한다.
        why? -> JPA가 이런 제약을 두는 이유는 JPA 구현 라이브러리가 객체를 생성할 때 리플랙션 같은 기술을
                사용할 수 있도록 지원해야 하기 때문이다.
    * */
    protected Address(){
    }

    // 생성자에서 값을 모두 초기화해서 변경 불가능한 클래스를 만듦.
    public Address(String city, String street, String zipcode){
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
