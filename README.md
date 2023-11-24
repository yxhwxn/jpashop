# What I Learned 📚

### 구현 요구사항💡

핵심 로직 위주(CRUD 기반)로 구현

---
1. [x] 회원 기능
  * 회원 등록
  * 회원 조회
2. [x] 상품 기능
  * 상품 등록
  * 상품 수정
  * 상품 조회
3. [x] 주문 기능
  * 상품 주문
  * 주문 내역 조회
  * 주문 취소

4. [x] 예제를 단순화 하기 위해 다음 기능은 구현 X
  - **로그인과 권한** 관리 X
  - **파라미터 검증**과 **예외 처리** X
  - 상품은 **도서**만 사용
  - **카테고리**는 사용 X
  - **배송정보**는 사용 X

### 애플리케이션 아키텍처 🔒
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
    
* 개발 순서: 
  1. 서비스, 리포지토리 계층을 개발
  2. 테스트 케이스를 작성해서 검증
  3. 웹 계층 적용
