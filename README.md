## SpringBoot 회원제 게시판
1. Entity 설계
   1. 모든 테이블의 베이스 (BaseEntity) : createTime(생성 시간), updateTime(업데이트 시간)
   2. 회원 테이블 (MemberEntity) : id(회원 번호), memberEmail(이메일), memberPassword(비밀번호), memberName(이름), memberPhone(전화번호), memberProfileName(프로필)
   3. 게시판 테이블 (BoardEntity) : id(게시판 번호), boardWriter(작성자), boardTitle(글제목), boardContents(글내용), boardHits(조회수), boardFileName(파일), createTime(게시물 생성 시간), updateTime(게시물 수정 시간)
   4. 댓글 테이블 (CommentEntity) : id(댓글 번호), commentWriter(작성자), commentContents(댓글 내용), createTime(댓글 생성 시간)
2. 테이블 참조 관계
   1. 회원 이메일(중복 불가) == 게시글 작성자, 댓글 작성자
   2. 게시판 글 번호 == 댓글이 달린 글의 번호
3. 기능
   1. Member
      1. 회원가입
         1. 회원가입시 이메일, 비밀번호, 이름, 전화번호, 프로필 사진을 입력
         2. Ajax로 이메일 중복 확인
      2. 로그인
         1. 이메일, 비밀번호 입력
         2. 로그인 실패시 페이지 이동 없음.
         3. 로그인 성공시 글목록이나 요청했던 페이지로 이동.
      3. 로그아웃
         1. session.invalidate(); 로 로그아웃 완료 시 index 페이지로 이동.
      4. 일반 회원
         1. 게시글 작성 및 조회 가능
         2. 본인이 작성한 글에 대해 상세 조회 시 수정 및 삭제 가능
      5. 관리자(admin)
         1. 관리자 페이지에서 회원목록 페이지로 이동 가능
         2. 회원 삭제 가능
   2. Board
      1. 글 작성
         1. 로그인 시 세션에 저장된 이메일이 작성자에 입력되어 있음.
         2. 제목, 내용, 첨부파일 업로드 가능
      2. 페이징
         1. 한 화면에 5개씩 글이 노출되고 페이지 번호는 3개가 나옴.
      3. 글 수정, 글 삭제
         1. 작성자 본인만 수정 및 삭제 가능
         2. 관리자는 글 삭제만 가능
      4. 검색
         1. 작성자, 제목으로 검색 가능
         2. 검색 결과 페이징 처리
   3. Comment
      1. 댓글 작성
         1. 로그인 계정이 작성자에 입력되어 있음.
      2. 댓글 삭제
   4. MyPage
      1. 로그인 시 마이페이지로 이동하는 링크가 보임.
      2. 마이페이지 접속 시 본인의 정보가 출력되어 있음.
      3. 회원 정보 수정 화면으로 이동하여 수정 가능
      4. 회원 정보 수정 시 비밀번호를 체크하여 일치하지 않으면 수정처리를 하지 않고 alert 창을 띄움.
   5. 기타
      1. RESTful한 주소 사용
         1. 조회 : get
         2. 저장 : post
         3. 수정 : put
         4. 삭제 : delete
      2. 타임리프(Thymeleaf) 활용
         1. validation check, global error 등
      3. 인터셉터(Interceptor)
         1. 인터셉터를 사용해 로그인하지 않은 사용자가 허용되지 않은 페이지에 접근할 경우 로그인페이지로 가도록 함. (회원가입, 로그인, 로그이웃 배제)