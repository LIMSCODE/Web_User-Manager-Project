package com.onlinepowers.springmybatis.paging;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Getter
@Setter
public class Criteria {

    /** 페이징 정보 */
    public PaginationInfo paginationInfo;

    /** 현재 페이지 번호 */
    public int currentPageNo;

    /** 페이지당 출력할 데이터 개수 */
    public int recordsPerPage;

    /** 화면 하단에 출력할 페이지 사이즈 */
    public int pageSize;

    /** 검색 키워드 */
    public String searchKeyword;

    /** 검색 유형 */
    public String searchType;

    public Criteria() {
        this.currentPageNo = 1;
        this.recordsPerPage = 10;
        this.pageSize = 10;
    }

    public String makeQueryString(int pageNo) {
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .queryParam("currentPageNo", pageNo)
                .queryParam("recordsPerPage", recordsPerPage)
                .queryParam("pageSize", pageSize)
                .queryParam("searchType", searchType)
                .queryParam("searchKeyword", searchKeyword)
                .build()
                .encode();

        return uriComponents.toUriString();
    }

    public int getStartPage() {
        return (currentPageNo - 1) * recordsPerPage;
    }

}