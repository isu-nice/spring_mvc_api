package com.codestates.member.controller;

import com.codestates.dto.MultiResponseDto;
import com.codestates.dto.SingleResponseDto;
import com.codestates.member.dto.MemberDto;
import com.codestates.member.entity.Member;
import com.codestates.member.mapper.MemberMapper;
import com.codestates.member.service.MemberService;
import com.codestates.stamp.Stamp;
import com.codestates.utils.UriCreator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;

/**
 * - DI 적용
 * - Mapstruct Mapper 적용
 * - @ExceptionHandler 적용
 */
@RestController
@RequestMapping(value = "/v11/members") // produces 설정 제거
@Validated
@Slf4j
public class MemberController {
    private final static String MEMBER_DEFAULT_URL = "/v11/members";

    private final MemberService memberService;
    private final MemberMapper mapper;

    public MemberController(MemberService memberService, MemberMapper mapper) {
        this.memberService = memberService;
        this.mapper = mapper;
    }

    // 회원 정보 등록
    @PostMapping
    public ResponseEntity postMember(@Valid @RequestBody MemberDto.Post requestBody) {
        // mapper를 이용해서 MemberPostDto를 Member로 변환
        Member member = mapper.memberPostToMember(requestBody);
        // 스탬프 추가
        member.setStamp(new Stamp());

        Member createdMember = memberService.createMember(member);
        // "/v11/members/{member-id}"
        URI location =
                UriCreator.createUri(MEMBER_DEFAULT_URL, createdMember.getMemberId());

        return ResponseEntity.created(location).build();
    }

    // 회원 정보 수정
    @PatchMapping("/{member-id}")
    public ResponseEntity patchMember(
            @PathVariable("member-id") @Positive long memberId,
            @Valid @RequestBody MemberDto.Patch requestBody
    ) {

        requestBody.setMemberId(memberId);

        Member member = mapper.memberPatchToMember(requestBody);
        Member updatedMember = memberService.updateMember(member);

        SingleResponseDto singleResponse
                = new SingleResponseDto<>(mapper.memberToMemberResponse(updatedMember));

        return new ResponseEntity<>(singleResponse, HttpStatus.OK);
    }

    // 한명의 회원 정보 조회
    @GetMapping("/{member-id}")
    public ResponseEntity getMember(
            @PathVariable("member-id") @Positive long memberId) {

        Member member = memberService.findMember(memberId);
        SingleResponseDto singleResponse =
                new SingleResponseDto<>(mapper.memberToMemberResponse(member));

        return new ResponseEntity<>(singleResponse, HttpStatus.OK);
    }

    // 모든 회원 정보 조회
    @GetMapping
    public ResponseEntity getMembers(@Positive @RequestParam int page,
                                     @Positive @RequestParam int size) {

        Page<Member> pageMembers = memberService.findMembers(page - 1, size);
        List<Member> members = pageMembers.getContent();

        MultiResponseDto multiResponse =
                new MultiResponseDto<>(mapper.membersToMemberResponses(members), pageMembers);

        return new ResponseEntity<>(multiResponse, HttpStatus.OK);
    }

    // 회원 정보 삭제
    @DeleteMapping("/{member-id}")
    public ResponseEntity deleteMember(
            @PathVariable("member-id") @Positive long memberId) {

        memberService.deleteMember(memberId);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
