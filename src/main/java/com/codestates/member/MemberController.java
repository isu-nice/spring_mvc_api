package com.codestates.member;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/v1/members") // produces 설정 제거
public class MemberController {

    // 회원 정보 등록
    @PostMapping
    public ResponseEntity postMember(@RequestBody MemberPostDto memberPostDto) {
        return new ResponseEntity<>(memberPostDto, HttpStatus.CREATED);
    }

    // 회원 정보 수정
    @PatchMapping("/{member-id}")
    public ResponseEntity patchMember(
            @PathVariable("member-id") long memberId,
            @RequestBody MemberPatchDto memberPatchDto
    ) {
        memberPatchDto.setMemberId(memberId);
        memberPatchDto.setName("홍길동");

        // No need Business logic

        return new ResponseEntity<>(memberPatchDto, HttpStatus.OK);
    }

    // 한명의 회원 정보 조회
    @GetMapping("/{member-id}")
    public ResponseEntity getMember(@PathVariable("member-id") long memberId) {
        System.out.println("# memberId = " + memberId);
        // not implementation

        // 리턴 값을 ResponseEntity 객체로 변경
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 모든 회원 정보 조회
    @GetMapping
    public ResponseEntity getMembers() {
        System.out.println("# get Members");
        // not implementation

        // 리턴 값을 ResponseEntity 객체로 변경
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 회원 정보 삭제
    @DeleteMapping("/{member-id}")
    public ResponseEntity deleteMember(@PathVariable("member-id") long memberId) {
        // No need Business logic
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
