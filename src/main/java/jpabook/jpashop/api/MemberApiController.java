package jpabook.jpashop.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController //@Controller + @ResponseBody
@RequiredArgsConstructor
@Tag(name = "Member", description = "Member 관련된 API 입니다.")
public class MemberApiController {

    private final MemberService memberService;

    /**
     * 조회 V1: 응답 값으로 엔티티를 직접 외부에 노출한다.
     * 문제점
     * - 엔티티에 프레젠테이션 계층을 위한 로직이 추가된다.
     * - 기본적으로 엔티티의 모든 값이 노출된다.
     * - 응답 스펙을 맞추기 위해 로직이 추가된다. (@JsonIgnore, 별도의 뷰 로직 등등)
     * - 실무에서는 같은 엔티티에 대해 API가 용도에 따라 다양하게 만들어지는데, 한 엔티티에 각각의 API를 위한 프레젠테이션 응답 로직을 담기는 어렵다.
     * - 엔티티가 변경되면 API 스펙이 변한다.
     * - 추가로 컬렉션을 직접 반환하면 항후 API 스펙을 변경하기 어렵다.(별도의 Result 클래스 생성으로 해결)
     * 결론
     * - API 응답 스펙에 맞추어 별도의 DTO를 반환한다.
     */
    //조회 V1: 안 좋은 버전, 모든 엔티티가 노출, @JsonIgnore -> 이건 정말 최악
    @GetMapping("/api/v1/members")
    public List<Member> membersV1() {
        return memberService.findMembers();
    }

    /**
     * 조회 V2: 응답 값으로 엔티티가 아닌 별도의 DTO를 return한다.
     */
    @GetMapping("/api/v2/members")
    @Operation(summary = "전체 회원 정보 조회 API", description = "모든 멤버의 정보(이름, 주소)를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema()))
    })
    public Result membersV2() {
        List<Member> findMembers = memberService.findMembers();
        List<MemberDto> collect = findMembers.stream()
                .map(m -> new MemberDto(m.getName(), m.getAddress()))
                .collect(Collectors.toList());

        // collect으로 리스트를 바로 내보내게 되면 배열의 형태로 return 되기 때문에, json 형식으로 Result를 거쳐서 반환되어야 함.
        return new Result(collect);
    }

    // 더 나은 유연성을 위해 배열 타입의 데이터를 json으로 감싸주는 역할
    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String name;
        private Address address;
    }

    /**
     * @RequestBody @Valid Member member
     * -> json 데이터로 받은 body를 유효성 검사 후에 Member로 자동 매핑해줌
     */

    /**
     * 등록 V1: 요청 값으로 Member 엔티티를 직접 받는다.
     * [문제점]
     * - 엔티티에 프레젠테이션 계층을 위한 로직이 추가된다.
     * - 엔티티에 API 검증을 위한 로직이 들어간다. (@NotEmpty 등등)
     * - 실무에서는 회원 엔티티를 위한 API가 다양하게 만들어지는데, 한 엔티티에 각각의 API를 위한 모든 요청 요구사항을 담기는 어렵다.
     * - 엔티티가 변경되면 API 스펙이 변한다.
     * [결론]
     * - API 요청 스펙에 맞추어 별도의 DTO를 파라미터로 받는다.
     */
    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);

    }

    /**
     * 등록 V2: 요청 값으로 Member 엔티티 대신에 별도의 DTO를 받는다.
     */
    @PostMapping("/api/v2/members")
    @Operation(summary = "유저 등록 API", description = "특정 멤버를(이름) 등록한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "404", description = "이미 존재하는 이름입니다.",
                    content = @Content(schema = @Schema())),
    })
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    /**
     * 수정 API
     * 1. 파라미터로 pathVariable {id} 값과 json 객체로 "name" : "new-hello" 가 Request DTO로 변환되어 요청된다.
     * 2. 요청된 데이터를 가지고 service 단에서 update를 실행시킨다.
     * 3. update가 실행되면 @Transactional로 구현한 update 메서드를 통해 값이 바뀌면서 트랜잭션이 끝나고 commit 되는 시점에 jpa가 변경 감지 하여 DB에 update 쿼리를 날린다.
     * 4. 그러면 다시 controller 단에서 변경된 데이터를 findMember 에 저장하여 return 함으로써 (포스트 맨에서 확인하면) 응답 데이터가 반환된다.
     */
    @PutMapping("/api/v2/members/{id}")
    @Operation(summary = "단건 회원 정보 수정 API", description = "특정 멤버의 id 값으로 이름을 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema()))
    })
    public UpdateMemberResponse updateMemberV2(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest request
    ) {
        memberService.update(id, request.getName());
        Member findMember = memberService.findOne(id);
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }

    // DTO
    @Data
    static class UpdateMemberRequest {
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long id;
        private String name;
    }

    @Data
    static class CreateMemberRequest {
        private String name;
    }

    @Data
    static class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }

}
