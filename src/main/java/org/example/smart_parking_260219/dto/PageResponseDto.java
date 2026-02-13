package org.example.smart_parking_260219.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResponseDto {
//     request.setAttribute("boardList", boardDtoList);
//        request.setAttribute("pageNum", pageRequestDto.getPageNum()); // 현재 페이지 번호
//        request.setAttribute("totalCount", count); // 전체 게시글 개수
//        request.setAttribute("totalPage", totalPage); // 전체 페이지 수

    private List<FeePolicyDTO> boardList;
    private int pageNum;  // 현재 페이지 번호
    private int totalCount; // 전체 게시글 개수
    private int totalPage; // 전체 페이지 수

}
