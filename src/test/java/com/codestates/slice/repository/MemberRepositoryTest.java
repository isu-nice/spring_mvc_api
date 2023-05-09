package com.codestates.slice.repository;

import com.codestates.member.entity.Member;
import com.codestates.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    private Member member;

    @BeforeEach
    public void init() {
        this.member = new Member();
        member.setEmail("hgd@gmail.com");
        member.setName("홍길동");
        member.setPhone("010-1111-2222");
    }

    @Test
    public void saveMemberTest() {
        // given
        // init()

        // when
        Member savedMember = memberRepository.save(member);

        // then
        assertNotNull(savedMember);
        assertEquals(member.getEmail(), savedMember.getEmail());
        assertTrue(member.getName().equals(savedMember.getName()));
        assertEquals(member.getPhone(), savedMember.getPhone());
    }

    @Test
    public void findByEmailTest() {
        // given
        // init()

        // when
        memberRepository.save(member);
        Optional<Member> findMember = memberRepository.findByEmail(member.getEmail());

        assertTrue(findMember.isPresent());
        assertEquals(findMember.get().getEmail(), member.getEmail());
    }
}
