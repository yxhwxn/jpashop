package jpabook.jpashop;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberTestRepositoryTest {
    
    @Autowired MemberRepository memberRepository;
    
    @Test
    @Transactional
    @Rollback(false)
    public void testMember() throws Exception {
        //given
        MemberTest memberTest = new MemberTest();
        memberTest.setUsername("memberA");

        //when
        Long savedId = memberRepository.save(memberTest);
        MemberTest findMemberTest = memberRepository.find(savedId);

        //then
        assertThat(findMemberTest.getId()).isEqualTo(memberTest.getId());
        assertThat(findMemberTest.getUsername()).isEqualTo(memberTest.getUsername());
        assertThat(findMemberTest).isEqualTo(memberTest);
    }
    

}