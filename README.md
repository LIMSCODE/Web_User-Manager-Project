# User-Jpa.Jsp.Api.Security.Token.Saleson

[User프로젝트]
```
- 회원/관리자 기능
- 회원기능 : 회원가입, 로그인, 로그아웃, 정보수정
- 관리자기능 : 회원리스트, 페이징, 검색, 회원등록, 수정, 삭제
```

[Skill]
```
- Mybatis, Thymeleaf, Jpa, Jsp, QueryDsl, pageable
- UserUtils, 인터셉터, 레이아웃(thymeleaf/Jsp-Tiles), Server/Front Validation, Hash알고리즘, Junit Test
- Spring Security, JwtToken, Api
- Vue
1. html 방식(cdn) : 기존의 타임리프 html파일로 작성 (로그인 및 관리자리스트)
2. npm : VUE 폴더 따로만듬
```

[순서]
```
1. user-Mybatis.Thymeleaf
2. user-Mybatis.Jsp 
3. user-Jpa.Jsp (QueryDsl, pageable 포함)
4. user-Jpa.Jsp.Api.Security.Token
5. user-Jpa.Jsp.Api.Security.Token.Saleson (Vue 포함) (최종, 이전의 모든파일 포함)
```

[Database]
```
- ERD, 테이블 정의서 확인 
- DB, 중요데이터 암기
- 이슈처리, 통합테스트
```



