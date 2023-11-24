# What I Learned 📚

## 엔티티 설계시 주의점

1. 엔티티에는 가급적 Setter를 사용하지 말 것.<br>
2. **모든 연관관계는 지연로딩으로 설정!! (중요)**
  - `why?` : 즉시로딩(EAGER)은 예측이 어렵기 때문. 특히 JPQL을 실행할 때 N+1 문제가 자주 발생하게 됨.
  - `then` : 따라서, 모든 연관관계는 지연로딩(LAZY)으로 설정해야 함.
  - `if` : 만약, 연관된 엔티티를 함께 DB에서 조회해야 한다면, `fetch join` 또는 `엔티티 그래프` 기능 사용 
  - @XToOne(@OneToOne, @ManyToOne)은 무조건 아래와 같이 바꿔줘야 함 !
  ```java
    @ManyToOne(fetch = FetchType.LAZY);
  ```
3. 컬렉션은 필드에서 바로 초기화 할 것.

4. 테이블, 컬럼명 생성 전략(CleanCode, Best Practice 관련 문서 참조)
  - https://docs.spring.io/spring-boot/docs/2.1.3.RELEASE/reference/htmlsingle/#howto-configure-hibernate-naming-strategy
  - https://docs.jboss.org/hibernate/orm/5.4/userguide/html_single/Hibernate_User_Guide.html#naming

5. 양방향 관계에서의 연관 관계 편의 메서드 사용할 것.
