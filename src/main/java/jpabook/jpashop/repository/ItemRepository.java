package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item) {
        if (item.getId() == null) {
            em.persist(item);   // JPA에 등록되지 않은 신규 아이템인 경우 persist로 처리
        } else {
            em.merge(item);     // JPA에 이미 등록된 경우에는 merge(강제 update 개념)
        }
    }

    // item 단건 조회
    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    // 여러 items 조회할 때는 JPA 사용
    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }
}
