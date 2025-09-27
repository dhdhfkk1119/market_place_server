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



## 4. Tasks & Responsibilities (작업 및 역할 분담)
|  |  |  |
|-----------------|-----------------|-----------------|
| 조정우    |  <img src="[https://avatars.githubusercontent.com/u/140272714?v=4](https://github.com/dhdhfkk1119)" alt="조정우" width="100"> | <ul><li>프로젝트 계획 및 관리</li><li>채팅,Flutter 레이아웃 작업</li><li>Flutter 상품 및 마이페이지 상태관리</li></ul>     |
| 유류진   |  <img src="https://github.com/user-attachments/assets/78ec4937-81bb-4637-975d-631eb3c4601e" alt="유류진" width="100">| <ul><li>메인 페이지 개발</li><li>동아리 만들기 페이지 개발</li><li>커스텀훅 개발</li></ul> |
| 양성빈   |  <img src="https://github.com/user-attachments/assets/78ce1062-80a0-4edb-bf6b-5efac9dd992e" alt="양성빈" width="100">    |<ul><li>홈 페이지 개발</li><li>로그인 페이지 개발</li><li>동아리 찾기 페이지 개발</li><li>동아리 프로필 페이지 개발</li><li>커스텀훅 개발</li></ul>  |
| 조충희    |  <img src="https://github.com/user-attachments/assets/beea8c64-19de-4d91-955f-ed24b813a638" alt="조충희" width="100">    | <ul><li>회원가입 페이지 개발</li><li>마이 프로필 페이지 개발</li><li>커스텀훅 개발</li></ul>    |
| 손지윤    |  <img src="https://github.com/user-attachments/assets/beea8c64-19de-4d91-955f-ed24b813a638" alt="손지윤" width="100">    | <ul><li>회원가입 페이지 개발</li><li>마이 프로필 페이지 개발</li><li>커스텀훅 개발</li></ul>    |
| 황지백    |  <img src="https://github.com/user-attachments/assets/beea8c64-19de-4d91-955f-ed24b813a638" alt="황지백" width="100">    | <ul><li>회원가입 페이지 개발</li><li>마이 프로필 페이지 개발</li><li>커스텀훅 개발</li></ul>    |
| 조현진    |  <img src="https://github.com/user-attachments/assets/beea8c64-19de-4d91-955f-ed24b813a638" alt="조현진" width="100">    | <ul><li>회원가입 페이지 개발</li><li>마이 프로필 페이지 개발</li><li>커스텀훅 개발</li></ul>    |

<br/>
<br/>
