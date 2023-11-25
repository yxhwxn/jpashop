package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.service.MemberService;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

public class MemberServiceTest {

    private MemberService memberService;
    private MemberRepository memberRepository;

    @Before
    public void setUp() {
        memberRepository = mock(MemberRepository.class);
        memberService = new MemberService(memberRepository);
    }

    @Test
    public void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("회원1");

        //when
        when(memberRepository.findByName(anyString())).thenReturn(null); // 중복 회원 없음을 가정
        Long savedId = memberService.join(member);

        //then
        assertEquals(member, memberRepository.findOne(savedId));
    }

    @Test
    public void 중복_회원_예외() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("회원1");

        Member member2 = new Member();
        member2.setName("회원1");

        //when
        when(memberRepository.findByName(anyString())).thenReturn((List<Member>) member1); // 중복 회원 존재를 가정

        //then
        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> memberService.join(member2));
        assertEquals("이미 존재하는 회원입니다.", thrown.getMessage());

        // 중복 회원 예외가 발생했을 때, memberRepository.save()가 호출되지 않았는지 확인
        verify(memberRepository, never()).save(any());
    }
}
