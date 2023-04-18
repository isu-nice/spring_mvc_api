package com.codestates.member.service;

import com.codestates.exception.BusinessLogicException;
import com.codestates.exception.ExceptionCode;
import com.codestates.member.entity.Member;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {
    public Member createMember(Member member) {
        // TODO should business logic

        // TODO member 객체는 나중에 DB에 저장 후, 되돌려 받는 것으로 변경 필요
        throw new BusinessLogicException(ExceptionCode.NOT_IMPLEMENTATION);
    }

    public Member updateMember(Member member) {
        // TODO should business logic

        // TODO member 객체는 나중에 DB에 업데이트 후, w되돌려 받는 것으로 변경 필요
        throw new BusinessLogicException(ExceptionCode.NOT_IMPLEMENTATION);

    }

    public Member findMember(long memberId) {
        // TODO should business logic
        // TODO member 객체는 나중에 DB에서 조회 하는 것으로 변경 필요
        throw new BusinessLogicException(ExceptionCode.NOT_IMPLEMENTATION);
    }

    public List<Member> findMembers() {
        // TODO should business logic

        // TODO member 객체는 나중에 DB에서 조회하는 것으로 변경 필요.
        throw new BusinessLogicException(ExceptionCode.NOT_IMPLEMENTATION);

    }

    public void deleteMember(long memberId) {
        // TODO should business logic
        throw new BusinessLogicException(ExceptionCode.NOT_IMPLEMENTATION);
    }
}
