<a href="https://club-project-one.vercel.app/" target="_blank">
// 홍보 이미지 넣기
</a>

<br/>
<br/>

# 📝 Back Server 소개 


## 💻 0. Getting Started 프로그램 시작하는 법 
- server 깃 코드 다운 받으신 후 프로그램 실행 (IntelliJ , VScode , Eclipse) 등등
- 실행하면 Server에서 등록된 Gemini기본키 주석 처리 하셔야 할겁니다


## 📖 1. Project Overview (프로젝트 개요)
- 프로젝트 이름: Markit_Place 
- 프로젝트 설명: Ai 기술을 활용한 실시간 GPS 중고 거래 커뮤니티 


## 🛠️ 2. 개발 환경 
- **언어** : JAVA
- **프레임워크** : SpringBoot
- **JDK** : Java 21 
- **Build Tool** : Gradle (groovy)
- **DBMS** : H2-Console 
- **ORM** : JPA
- **개발기간** : 2025.08.25 ~ 2025.09.26


## 🔑 3. Key Features (주요 기능)
- **회원 가입**
  - 아이디 형식 회원 가입 및 이메일 회원 가입
  - 약관 동의 (체크)
  - 중복 체크 및 이메일 인증(google mail 인증방식)
    
- **로그인**
  -  소셜 로그인 (naver,google)
  -  일반 로그인 및 이메일 로그인
    
- **채팅 기능**
  -  1:1 채팅 기능 
  -  WebSocket 사용
  -  STOMP 사용

- **상품 등록**
  - 상품 등록 수정 및 삭제 (중고상품)
  - 상품 신고 하기 (신고 승인 결과 따라 정지 유무 결정)
    - 정지 횟수에 따라 로그인 정지 시간이 정해짐 
      
- **게시물 등록**
  - 게시물 등록 삭제 수정 (커뮤니티)
  - 게시물 신고 하기 (신고 승인 결과 따라 정지 유무 결정)
    - 신고 횟수에 따른 게시물 등록 하지 못하는 시간이 늘어남

- **거래 관리 및 유저 관리**
  - 상품 구매후 리뷰 작성 (리뷰는 해당 판매 유저의 평가에 남는다)
  - 평점 매기기 (유저의 매너 점수를 나타냄 5점 만점)
  - 칭찬하기 (해당 유저의 평점 점수를 올려줌)

- **어드민 기능**
  - 공지사항 작성
  - QnA 관리
  - 상품,커뮤니티 신고 목록 검사하기 (신고 승인)
  - 모든 기능에 접근 가능



## ♻️ 4. Tasks & Responsibilities (작업 및 역할 분담)
|  |  |  |
|-----------------|-----------------|-----------------|
| 조정우    |  [<img src="https://avatars.githubusercontent.com/u/140272714?v=4" alt="조정우" width="100">](https://github.com/dhdhfkk1119) | <ul><li>전체적인 프로젝트 계획 및 관리</li><li>Back 1:1 채팅 기능 ,Flutter 레이아웃 작업</li><li>Flutter 상품 전체 기능 및 마이페이지 상태관리</li></ul>     |
| 유류진   |  [<img src="https://avatars.githubusercontent.com/u/208729786?v=4" alt="유류진" width="100">](https://github.com/yooryujin)| <ul><li>Figma 레이아웃</li><li>유저 거래 리뷰 및 평점</li><li>flutter 게시물 등록 수정 삭제</li></ul> |
| 양성빈   |  [<img src="https://avatars.githubusercontent.com/u/197378605?v=4" alt="양성빈" width="100">](https://github.com/ysb5397)    |<ul><li>Flutter GPS 기능 </li><li>Toss 결제 하기 기능</li><li>상품 등록 Ai 이미지 인식 기능</li></ul>  |
| 조충희    |  [<img src="https://avatars.githubusercontent.com/u/105851912?v=4" alt="조충희" width="100">](https://github.com/dovahk11m)    | <ul><li>회원가입 및 로그인 기능</li><li>flutter 상태관리 회원가입 및 로그인 기능</li></ul>    |
| 손지윤    |  [<img src="https://avatars.githubusercontent.com/u/208729868?v=4" alt="손지윤" width="100">](https://github.com/sonjiyoon12)    | <ul><li>Back 게시물 전체적인 기능 및 신고 SSE 알람기능</li><li>Front 상태관리 게시물 리스트 및 상세보기</li><li>게시물 댓글 작성 좋아요</li></ul>    |
| 황지백    |  [<img src="https://avatars.githubusercontent.com/u/208729937?v=4" alt="황지백" width="100">](https://github.com/jibaek1)    | <ul><li>Back 상품 등록 수정 삭제 좋아요 이미지 추가 삭제</li><li>Back 상품 신고 하기</li><li>Back 신고 처리 기능 로그인 정지 게시글 작성 금지 신고 처리된 게시글 삭제 정지 해제일이 되면 자동해제</li></ul>    |
| 조현진    |  <img src="https://github.com/user-attachments/assets/beea8c64-19de-4d91-955f-ed24b813a638" alt="조현진" width="100">    | <ul><li>Back 공지사항 기능</li><li>Back Q&A 기능</li></ul>    |

<br/>
<br/>

## 기능들에 대한 영상 및 이미지는 -> 플러터 프론트 에서 관리하고있습니다
👀 [눈으로 보고 싶다 -> 프론트 주소](https://github.com/dhdhfkk1119/markit_place_front.git)


