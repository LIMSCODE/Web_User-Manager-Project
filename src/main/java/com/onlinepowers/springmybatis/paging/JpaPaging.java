package com.onlinepowers.springmybatis.paging;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Data
@AllArgsConstructor
public class JpaPaging {

    //현재 페이지 번호
    int page;

    //페이지당 출력 개수
    public int size;

    //화면 하단에 출력할 페이지 개수
    public int totalPage;

    //검색 키워드
    public String searchKeyword;

    //검색 유형
    public String searchType;

    public JpaPaging() {
        this.page = 1;
        this.size = 2;
        this.totalPage = 10;
    }

    public String makeQueryString(int page) {
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .queryParam("page", page)
                .queryParam("size", size)
                .queryParam("totalPage", totalPage)
                .queryParam("searchType", searchType)
                .queryParam("searchKeyword", searchKeyword)
                .build()
                .encode();

        return uriComponents.toUriString();
    }

}

