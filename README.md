# What I Learned 📚

> ## jpashop 개발이 완료되면, 반드시 패키지 모듈 관계 다시 살펴보기❗️ <br>
> ex. **controller**에서 **service**의 메소드를 호출 하며, **service**에서는 **repository** 메소드를 호출한다.

- `자바 ORM 표준 JPA 프로그래밍 - 기본편` 수강이 완료되면
    - ▶️ `yh/Desktop/study-docs/spring-inflearn`에 있는 강의자료 보면서 개발 Tips, 개념 이해하기❕

---
## ✅ Project Flow 
- Gitflow Workflow 기반으로, 이슈별로 나눠진 `READ.ME` 파일에서 챕터별로 어떤 내용을 배웠는지 각 이슈 `READ.ME`에서 확인할 것.
- Project board 기능을 활용한 스케줄링(TODO, In progress, Done)

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

