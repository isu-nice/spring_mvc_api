package com.codestates.member.repository;

import com.codestates.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
}
