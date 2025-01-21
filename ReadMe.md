# RNDChat
<hr>

## 🎯목표
유저간 매칭을 통해 실시간 채팅이 가능한 랜덤 채팅 프로그램을 제작하였습니다.
저번 프로젝트에서 부족했던 부분인 `실시간 채팅` 을 구현하기 위해 만든 프로젝트이고 매칭 큐에 등록된 유저를 순차적으로 매칭시켜 랜덤 매칭이 되도록 만들었습니다.

## ⚙️Tech Stack
### Backend
* **Java 21**
* **SpringBoot 3.3.2**
* **MariaDB** - UserData, MessageData
### Frontend
* **ThymeLeaf** - TemplateEngine
* **javascript** - Script
### VCS
* **Git**
### Server
* **Ubuntu 22.0.4 (OracleCloudInstance)**
* **Nginx** - ProxyPass
### Performance Tester
* **nGrinder** - login & Matching


## 🗒️Record
* [RNDChat 토이프로젝트](https://velog.io/@qwerty55558/RNDChat-%ED%86%A0%EC%9D%B4%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8) (시연, 테스팅 등등 총 정리글 링크입니다.)

## ✅주요 기능
* **계정**
  * 중복 가능한 아이디를 입력하여 채팅 가능
  * 세션이 만들어지지 않으면 인터셉터에 의해  login 페이지로 redirect 됨
* **매칭**
  * 로그인 한 유저는 매칭 버튼으로 매칭 큐에 등록됨
  * 20초 마다 한 번씩 요청할 수 있음
  * 대기열은 ConcurrentLinkedQueue 를 사용하여 선입 선출의 형태를 가짐
  * 매칭이 성사되면 채팅방이 생성되며 알림을 보냄, 각 채팅방마다 채팅을 입력하여 유저들 간의 상호작용이 가능
* **상호작용**
  * Websocket 을 사용한 채팅 시스템 구현
  * 상대방의 세션이 종료되거나 상대방이 대화창을 종료했을 경우 대화를 나누던 상대의 채팅방도 같이 삭제됨 (cascade)
* **etc**.
  * bootstrap 을 통한 반응형 웹페이지 구현
  * 리눅스 기반의 호스팅 서버 구현
## 🗂️ERD 구조

![스크린샷 2025-01-21 180117](https://github.com/user-attachments/assets/164f5b53-e25e-4da9-9e8b-b48c687cdeee)


> [ERD Cloud 링크](https://www.erdcloud.com/d/RBcM828NP7vPoNspu)

## 🔚엔드포인트 명세
<table>
    <tr>
        <td><b>Domain</b></td>
        <td><b>URL</b></td>
        <td><b>Method</b></td>
        <td><b>Description</b></td>
    </tr>
    <tr>
        <td>Login</td>
        <td>/login</td>
        <td>GET</td>
        <td>로그인 페이지</td>
    </tr>
    <tr>
        <td></td>
        <td>/login</td>
        <td>POST</td>
        <td>세션 생성 요청</td>
    </tr>
    <tr>
        <td>Home</td>
        <td>/home</td>
        <td>GET</td>
        <td>홈 페이지</td>
    </tr>
    <tr>
        <td>Matching</td>
        <td>/matching</td>
        <td>GET</td>
        <td>매칭 큐 등록 요청</td>
    </tr>
    <tr>
        <td>ChatRoom</td>
        <td>/search</td>
        <td>GET</td>
        <td>유저의 채팅방 조회(미사용)</td>
    </tr>
    <tr>
        <td>WebSocket</td>
        <td>ws:// ... /chat</td>
        <td>GET(Upgrade)</td>
        <td>웹 소켓 요청(매칭 성공시)</td>
    </tr>
</table>
