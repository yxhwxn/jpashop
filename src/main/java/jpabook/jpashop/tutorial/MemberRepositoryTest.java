package jpabook.jpashop.tutorial;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

// Repository: 엔티티들을 찾아주는 역할
@Repository
public class MemberRepositoryTest {

    @PersistenceContext
    private EntityManager em;

    public Long save(MemberTest memberTest){
        em.persist(memberTest);
        return memberTest.getId();
    }

    public MemberTest find(Long id){
        return em.find(MemberTest.class, id);
    }
}
