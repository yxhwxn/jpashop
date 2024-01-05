# What I Learned 📚

> ## jpashop 개발이 완료되면, 반드시 패키지 모듈 관계 다시 살펴보기❗️ <br>
> ex. **controller**에서 **service**의 메소드를 호출 하며, **service**에서는 **repository** 메소드를 호출한다.

- `자바 ORM 표준 JPA 프로그래밍 - 기본편` 수강이 완료되면
    - ▶️ `yh/Desktop/study-docs/spring-inflearn`에 있는 강의자료 보면서 개발 Tip, 개념 이해하기❕

---

## ✅ Project Flow

- Gitflow Workflow(issue branch -> PR)
- Project board 기능을 활용한 스케줄링(TODO, In progress, Done)

## ✅ Database

- H2 사용법
    1. 데이터베이스 실행 상태 확인
        - `{{H2설치경로}}/bin/h2.sh` 파일을 실행해서 H2 데이터베이스를 실행
    2. `jdbc:h2:tcp://localhost/~/jpashop` 로 접속
    3. `localhost:8082` 로 url 바꿔서 접속

## ✅ 애플리케이션 아키텍처

---

![Architecture img](https://github.com/yxhwxn/jpashop/assets/87745916/c4a99738-4b10-4d29-bb11-f757fa569e31)

➡️ Controller는 Service만 호출하는 것이 아닌 Repository에도 바로 접근할 수 있는 구조로 설계할 예정(단, 단방향은 동일하게)

* 계층형 구조 사용
    * controller, web: 웹 계층
    * service: 비즈니스 로직, 트랜잭션 처리
    * repository: JPA를 직접 사용하는 계층, 엔티티 매니저 사용
    * domain: 엔티티가 모여 있는 계층, 모든 계층에서 사용

* 패키지 구조
    * jpabook.jpashop
    * domain
    * exception
    * repository
    * service
    * web

## ✅ issue list

> ### issue/1 : 프로젝트 환경설정
> ### issue/2 : 도메인 분석 설계
> ### issue/3 : 애플리케이션 구현 준비
> ### issue/4 : 회원 도메인 개발
> ### issue/7 : 상품 도메인 개발
> ### issue/8 : 주문 도메인 개발
> ### issue/9 : 웹 계층 개발

## ✅ 엔티티 설계시 주의점

---

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

## ✅ JPA 사용시 주의점

- 엔티티는 핵심 로직만 구현하고, 최대한 순수하게 유지해야한다.
- 최대한 다른 어디에 dependency 없이 핵심 비즈니스 로직에만 dependency가 있어야 한다.
    - 폼 객체 vs. 엔티티 직접 사용
        - 요구사항이 정말 간단할 경우에는 폼 객체(MemberForm) 없이 엔티티(Member)를 직접 등록 및 수정해도 된다.
        - 하지만, 사용자의 요구사항이 복잡해질수록(대부분의 경우), 엔티티에 화면을 처리하기 위한 API들이 증가한다.
        - 이 때, 엔티티는 화면에 종속적으로 변하게 되어 지저분해져 유지보수가 어려워진다.
        - 따라서 ❗️
            - 엔티티는 핵심 비즈니스 로직만 가지고 있고, 화면을 위한 로직은 따로 관리해야 한다.
            - 화면에 맞는 API들은 폼 객체나 DTO(Data Transfer Object)를 활용한다.


