# Threads
Instagram 팀이 만든 새로운 앱이며 사람들은 자신의 Instagram 계정을 사용하여 로그인
<br>텍스트로 새로운 소식은 공유하고 사람들의 대화에 참여한다.
<br>Threads 에서는 최대 500자 길이의 게시물을 작성할 수 있으며 링크, 사진, 슬라이드, 동영상(최대5분)을 포함 할 수 있다.
<img width="1308" alt="image" src="https://github.com/user-attachments/assets/933e88fa-7a7b-4aff-a2b2-4b331cdc6931">

## 개요
Threads 스타일의 SNS 를 만들어 보면서 외부적으로 많은 사용자를 단숨에 끌어들이고, 이를 대응할 수 있었던 방법을 구현해보면서 생각해보기.
<br>Instagram 을 기반으로수 많은 트래픽을 감당해온 경험과 노하우를 기반으로 Threads 를 운영하지 않을까라는 생각을 해본다.
<br>Instagram 이 수 억명의 트래픽을 어떻게 감당할 수 있었는지 알아보고, **최근 Java21 에서 제공되는 Virtual Thread 방식을 적용하게 된다면 기존의 Thread 보다 더 나은 성능을 발휘 할 수 있을지 알아본다.**

## Instagram 이 수억명의 유저를 감당한 방법
- 인스타그램의 수 많은 트래픽 이와 함께 이야기 할 수 있는 저스틴 비버 현상을 어떻게 해결하고, 사용자에게 앱이 빠르다는 것을 인지시키기 위한 트릭을 알아보자.
- 인스타그램의 목적은 유저들이 좋아하는 것에 집중! 하며 실제로 서버가 그렇게 빠르지 않더라도 더 빨라 보이도록 했다.
- 어떻게?
  - 이미지 파일 등을 업로드 하는 경우 지연 시간이 존재하는데 이를 최대한 숨겼다.
    - 이미지 업로드 하여, 캡션을 작성하거나 게시 버튼을 누르기 전에 사진이 백그라운드 인스타그램의 서버에 미리 업로드 되도록 했다. ( 업로드를 하는 순간 백그라운드 서버에 저장이 되는 구조 ) <br> 이를 통해서 유저가 글, 해시태그를 작성하는 동안에 업로드를 진행하면서 업로드가 빠르게 된다고 보여줌.
  - 데이터베이스의 경우 최소한을 사용하며, 샤딩을 통해 데이터베이스를 분리하여 대용량의 데이터를 적재
    - 복제본과 마스터를 기반으로 읽기 작업에서는 마스터와 복제본을 / 쓰기,변경 작업에서는 마스터로 처리 후 복제본에 공유를 통해서 데이터를 적재
  - 캐싱을 이용한 잦은 정보 제공
    - 대량의 트래픽이 찾아 오면서 동일한 정보를 요구하는 작업이 있을 수 있다. <br> 이런 경우에 캐싱을 통해서 자주 조회되는 정보는 캐시에 저장해두고 이를 유저에게 제공한다.  
- 저스틴 비버 현상은 무엇?
  - 유명 인플루언서, 연예인 등이 활동하게 되면서 팔로워 수가 많아지게 되면서 사진을 올릴때 마다 인스타그램이 느려지거나, 다운이 되는 현상을 저스틴 비버 현상이라 한다.
  - 왜?
    - 인스타그램의 "좋아요" 수를 계산하는 방식에 의해서 문제가 발생
    - 사진-좋아요 의 구조를 위해 사진 테이블과 좋아요를 담아두는 테이블 2가지가 필요하게 되고, 팔로워가 많은 사진의 경우 사진마다 좋아요를 누른 사람의 테이블을 조회하게 된다.
    - 일반적인 경우 해당 방식의 접근 방식이 좋지만, 팔로워가 많은 경우 해당 팔로워에게 모두 해당 사진을 보여주고 좋아요 수를 집계해야 하게되면서 이는 좋지 않은 방식이 된다. <br>즉, 1억명에서 매번 좋아요 수를 확인해주어야 한다.
  - 어떻게 해결 했을까?
    - 인스타그램은 비정규화를 통해서 사진 테이블에 좋아요 컬럼을 직접 추가하여 총 좋아요 수를 저장하도록 하여 좋아요 테이블의 갯수를 카운팅 하는 것이 아닌 사진 컬럼의 총 좋아요 수를 보여주는 방식으로 변경하여 해결
    - 물론 이러한 방식은 사용하면 동시성 문제를 잘 해결해야 한다.


## 사용 스택

![Java](https://img.shields.io/badge/Java21-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)

![Spring](https://img.shields.io/badge/Spring6.1.x-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
<img src="https://img.shields.io/badge/Spring Boot3.3.x-6DB33F?style=for-the-badge&logo=Spring Boot&logoColor=white">
<img src="https://img.shields.io/badge/Spring Data JPA-FF3621?style=for-the-badge&logo=databricks&logoColor=white">
<img src="https://img.shields.io/badge/Spring Security6.3.x-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white">

![Gradle](https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&logo=Gradle&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)
![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)

<img src="https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=white"> <img src="https://img.shields.io/badge/Springdoc-4285F4?style=for-the-badge&logo=googledocs&logoColor=white">

<img src="https://img.shields.io/badge/JUnit5-25A162?style=for-the-badge&logo=junit5&logoColor=white"> ![Testing-Library](https://img.shields.io/badge/-Mockito-%23E33332?style=for-the-badge&logo=testing-library&logoColor=white)

![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white)


## 아키텍처
### 1차 아키텍처
![Threads_Architecture_v1](https://github.com/user-attachments/assets/03d750fe-665d-42f6-8f97-372311bfa69a)

- 1차 아키텍처의 경우 로컬 환경에서 애플리케이션 메모리를 활용한 단순 CRUD 구현. ( 실제 운영에서 절대 활용 불가 )
- 생각나는 문제점
  - [x] 로컬 환경으로 외부 유입이 불가능
  - [x] 영구적인 데이터 보관 불가
  - [x] 동시성 이슈 발생 가능
- 해결 방안
  - [x] DB 연결

- 1차 아키텍처를 좀 더 발전시켜서 진행을 해보자.

### 2차 아키텍처
![Threads_Architecture_v2](https://github.com/user-attachments/assets/39f76d65-b557-4627-b640-b285d58810d2)
![Threads_Architecture_v3](https://github.com/user-attachments/assets/e35bef2e-a9cc-4a1a-a93c-050e3f4ca172)


- 1차 아키텍처에서 부족한 부분을 보완하여 처리
- 메모리를 사용한 데이터 저장을 RDBMS 를 활용하여 영구 저장
  - PostgreSQL 을 사용하며 Application 과 DB 의 연결을 위해 Spring Data JPA 를 활용하여 연결
- Spring Security 를 도입하여 기존의 Web 환경에서 보안되어야 하는 보안 이슈를 편히 작용 ( CORS, CSRF 등 )
  - 다만, CSRF 의 경우 Stateless 한 환경의 API 구성으로 인해 비활성화
  - 이를 보안하기 위해서 사용자에 대한 인증 및 허가 작업을 <b>JWT</b> 기반으로 활용

- TODO
  - [ ] 가상 인프라 구축
  - [ ] JWT 시크릿 키 저장 방식 ( 멀티 키 등 )
  - [ ] JWT 리프레시, 액세스 구현

### 왜? MySQL, PostgreSQL 에서 PostgreSQL 인가?
- Threads와 같은 시스템에서의 복잡한 트랜잭션 처리, 비관계형 데이터의 효율적 관리, 높은 수준의 데이터 무결성 보장 등의 이유로 인해 PostgreSQL이 MySQL보다 더 적합 할 수 있다.<br> MySQL도 충분히 강력한 데이터베이스 시스템이지만, PostgreSQL의 고급 기능과 유연성이 이러한 구체적인 요구사항에 더 잘 부합하기 때문에 PostgreSQL을 선택
  - 복잡한 트랜잭션 및 동시성 처리: <br>PostgreSQL의 MVCC 구현은 더 정교하게 설계되어, 다중 사용자 환경에서 높은 수준의 동시성 처리를 보장하면서도 데이터의 일관성을 유지. <br>특히, 복잡한 트랜잭션이 빈번하게 발생하는 Threads와 같은 소셜 네트워킹 시스템에서는 PostgreSQL의 안정적인 동시성 처리 능력이 큰 이점.
  - 고급 데이터 타입 지원 및 유연성: <br>PostgreSQL은 JSONB와 같은 비관계형 데이터 타입을 효율적으로 처리할 수 있는 기능을 제공.<br> 이로 인해 구조화되지 않은 데이터를 다룰 때, 더 나은 성능과 유연성을 확보할 수 있습니다. <br>소셜 네트워크에서 사용자 생성 콘텐츠가 다양한 형식을 가질 수 있다는 점을 고려할 때, PostgreSQL의 이러한 기능은 중요한 이점.
  - 데이터 무결성 및 확장성: <br>PostgreSQL은 데이터 무결성 제약 조건을 엄격하게 준수하며, 복잡한 데이터 모델링을 필요로 하는 애플리케이션에서 더 유연하고 강력한 데이터 무결성 보장을 제공. <br>또한, PostgreSQL의 확장성과 커스터마이징 가능성은 대규모 애플리케이션의 미래 확장 요구를 충족시킬 수 있습니다.

### JWT 선정 이유는?
- <b>사용자에 대한 인증 작업에 필요한 정보를 저장하는 형태를 왜 JWT 를 사용하는가?</b>
- 클라이언트와 서버간에 정보를 제공할 수 있는 방법으로는 여러가지가 존재한다. 
  - ( Cookie, Session, SSO, JWT ) 와 같이 방법이 존재하는데 고려해야 하는 부분들이 있다.
- 이는 서비스 상에서 사용자 식별에 사용되는 중요한 요소 이다. <br> 그렇기 때문에 CSRF 공격에 의해서 제 3자가 타인의 권한을 사용해 악용 할 수 있으며, 정보를 훔쳐 갈 수 있다.
  - 그럼 CSRF 를 방지하기 위해서 Spring Secuirty 에서 CSRF 공격에 대해서 활성화를 하면 안되는가?
    - 물론 CSRF 방지를 통해서 CSRF 토큰을 주고받아 문제를 해결 할 수 있지만, CSRF 토큰의 정보를 매번 서버상에 저장해두어야 한다. <br>이렇게 되면서 Stateless 한 성질과 맞지 않아진다.
- 정리를 하면, Stateless 한 성질을 가진 API 로 구성하고 싶고 보안적인 이슈는 지키고 싶다.
  - 이를 위해서 SSO, JWT 가 주목되게 되는데 SSO 를 사용하기에는 추가적인 구축의 작업이 필요하여 현재 상황에서는 부담이 된다. <br>추가적으로 SSO 서버에 문제가 생기면 모든 애플리케이션에 영향을 받을 수 있다.
  - 애플리케이션 단에서 쉽게 접근하여 생성할 수 있고 Stateless 하고, 토큰 자체의 서명 값을 통해 변질 여부 등을 판단할 수 있는 JWT 를 활용하기로 한다.
- <b>JWT 는 그럼 모든 상황에 부합하는가?</b>
  - 이 답에는 "No" 라고 당당히 말 할 수 있다.
  - JWT 도 Cookie 와 같이 클라이언트에게 전달이 된다.
    - 그 말은 JWT 도 악의적인 사용자에게 탈취 될 수 있고 이를 통해 악용할 수 있다.
  - 이를 방지하기 위해서 엑세스 토큰, 리프레시 토큰을 통해 토큰의 짧은 유효기간과 재발급에 대한 처리를 이용해 탈취된 토큰에 대해서 어느정도 방어 할 수 있다.
  - 유효기간 안에 탈취된 토큰의 경우에는 무효화 할 수는 없다 이를 위해서 블랙리스트 또는 JWT 버전 정보를 기반으로 무효화를 할 수 있다.
    
 

## 참고
[인스타그램 백엔드가 20억 유저를 감당하는 방법!](https://www.youtube.com/watch?v=V27XkmVPqYQ&t=14s)
