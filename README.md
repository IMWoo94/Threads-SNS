

## 개요
**Threads 스타일의 SNS 를 만들어 보면서 외부적으로 많은 사용자를 단숨에 끌어들이고, 이를 대응할 수 있었던 방법을 구현해보면서 생각해보기.**
<br>**최근 Java21 에서 제공되는 Virtual Thread 방식을 적용하게 된다면 기존의 Thread 보다 더 나은 성능을 발휘 할 수 있을지 알아본다.**
```
Instagram 을 기반으로수 많은 트래픽을 감당해온 경험과 노하우를 통해 Threads 를 운영하지 않을까라는 생각을 해본다.
Instagram 이 수 억명의 트래픽을 어떻게 감당할 수 있었는지 알아본다.
```
### [WIKI](https://github.com/IMWoo94/Threads-SNS/wiki)
* [인스타그램의 대규모 트래픽 노하우](https://github.com/IMWoo94/Threads-SNS/wiki/Instagram-%EC%9D%B4-%EC%88%98%EC%96%B5%EB%AA%85%EC%9D%98-%EC%9C%A0%EC%A0%80%EB%A5%BC-%EA%B0%90%EB%8B%B9%ED%95%9C-%EB%B0%A9%EB%B2%95)

### 트러블 슈팅
* [Spring Security Filter, Application Filter](https://imwoo94.notion.site/Spring-Security-Invalid-CSRF-token-found-for-Error-response-status-is-403-ca540e4d417849e686696f7fb5e57b54?pvs=4)
***

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

***
## 아키텍처
![Threads_Architecture_v3](https://github.com/user-attachments/assets/e35bef2e-a9cc-4a1a-a93c-050e3f4ca172)

***
### 왜? MySQL, PostgreSQL 에서 PostgreSQL 인가?
> Threads와 같은 시스템에서의 복잡한 트랜잭션 처리, 비관계형 데이터의 효율적 관리, 높은 수준의 데이터 무결성 보장 등의 이유로 인해 PostgreSQL이 MySQL보다 더 적합 할 수 있다.
> <br> MySQL도 충분히 강력한 데이터베이스 시스템이지만, PostgreSQL의 고급 기능과 유연성이 이러한 구체적인 요구사항에 더 잘 부합하기 때문에 PostgreSQL을 선택
- 복잡한 트랜잭션 및 동시성 처리
  ```
  PostgreSQL의 MVCC 구현은 더 정교하게 설계되어, 다중 사용자 환경에서 높은 수준의 동시성 처리를 보장하면서도 데이터의 일관성을 유지.
  특히, 복잡한 트랜잭션이 빈번하게 발생하는 Threads와 같은 소셜 네트워킹 시스템에서는 PostgreSQL의 안정적인 동시성 처리 능력이 큰 이점.
  ```
- 고급 데이터 타입 지원 및 유연성
  ```
  PostgreSQL은 JSONB와 같은 비관계형 데이터 타입을 효율적으로 처리할 수 있는 기능을 제공.
  이로 인해 구조화되지 않은 데이터를 다룰 때, 더 나은 성능과 유연성을 확보할 수 있습니다.
  소셜 네트워크에서 사용자 생성 콘텐츠가 다양한 형식을 가질 수 있다는 점을 고려할 때, PostgreSQL의 이러한 기능은 중요한 이점.
  ```
- 데이터 무결성 및 확장성
  ```
  PostgreSQL은 데이터 무결성 제약 조건을 엄격하게 준수하며,
  복잡한 데이터 모델링을 필요로 하는 애플리케이션에서 더 유연하고 강력한 데이터 무결성 보장을 제공.
  또한, PostgreSQL의 확장성과 커스터마이징 가능성은 대규모 애플리케이션의 미래 확장 요구를 충족시킬 수 있습니다.
  ```

***
### JWT 선정 이유는?
> <b>사용자에 대한 인증 작업에 필요한 정보를 저장하는 형태를 왜 JWT 를 사용하는가?</b>
> <br> 클라이언트와 서버간에 정보를 제공할 수 있는 방법으로는 여러가지가 존재한다.
> <br> ( Cookie, Session, SSO, JWT ) 와 같이 방법이 존재하는데 고려해야 하는 부분들이 있다.
```
인증 정보는 서비스 상에서 사용자 식별에 중요한 요소 이다.
인증 정보를 탈취하여 제 3자가 타인의 권한을 사용해 악용 할 수 있으며, 개인 정보를 수집 할 수 있다.

그럼 CSRF 를 방지하기 위해서 Spring Secuirty 에서 CSRF 공격에 대해서 활성화를 하면 안되는가?
물론 CSRF 방지를 통해서 해결 할 수 있지만, CSRF 토큰의 정보를 매번 서버상에 저장해두어야 한다.
이렇게 되면 Stateless API 를 만들고 싶은 목적에 부합하지 않는다.
```
- **정리를 하면, Stateless 한 성질을 가진 API 로 구성하고 보안적인 이슈는 지키고 싶다.**
  - 이를 위해서 SSO, JWT 에 눈길이 간다.
    - 다만, SSO 를 사용하기에는 추가적인 구축의 작업이 필요하여 현재 상황에서는 부담이 된다.
    <br>추가적으로 SSO 서버에 문제가 생기면 모든 애플리케이션에 영향을 받을 수 있다.
  - **애플리케이션 단에서 쉽게 접근하여 생성할 수 있고 Stateless 하고, 토큰 자체의 서명 값을 통해 변질 여부 등을 판단할 수 있는 JWT 를 활용하기로 한다.**
> <b>JWT 는 그럼 모든 상황에 부합하는가?</b>
  - **이 답에는 "No" 라고 당당히 말 할 수 있다.**
  - JWT 도 악의적인 사용자에게 탈취 될 수 있고 이를 통해 악용할 수 있다.
    - **엑세스 토큰, 리프레시 토큰**을 통해 토큰의 짧은 유효기간과 재발급에 대한 처리를 이용해 탈취된 토큰에 대해서 어느정도 방어 할 수 있다.
    - 유효기간 안에 탈취된 토큰의 경우에는 무효화 할 수는 없다 이를 위해서 **블랙리스트 또는 JWT 버전 정보를 기반**으로 무효화를 할 수 있다.
