# Spring WebFlux R2DBC H2 for m_project

---
## Introduction
1. **Spring WebFlux** + **R2DBC** + **H2 database** 를 이용한 비동기 논블로킹(Asynchronous Non-Blocking) 리액티브 프로그램.
2. 기동시 H2 DB 스키마와 데이터가 자동으로 초기화됩니다. file기반 H2 DB는 처음 한번만 초기화 됩니다. 관련파일 :arrow_right: [schema.sql](src/main/resources/init/schema.sql), [data.sql](src/main/resources/init/data.sql)

---
## Technologies Used
- **Spring Boot**
- **Spring WebFlux**
- **Spring Data R2DBC**
- **H2 Database (in-memory mode, file mode both)**
- **Lombok**
- **Swagger**

---
## Prerequisites
- **Java 21+**
- **Maven 3+**

---
## Getting Started

### Clone the repository
```sh
git clone https://github.com/jin-jarvis/goods-manager.git
cd goods-manager
```

### Build and Run the Application
```sh
mvn clean package
mvn spring-boot:run
```

### API Request
- API Request 명세는 Swagger를 참고하세요! :arrow_right: [Swagger](http://localhost:8081/swagger-ui/index.html)
- API 실행하는데 Postman이 편하시다면 :arrow_right: [m_project.postman_collection.json](m_project.postman_collection.json) import 해서 사용하세요!

### API Response
- API Response는 다음 규격을 따릅니다.
```json
{
    "code": "결과 코드",
    "message": "결과 메세지",
    "data": {
        "데이터_key": "데이터_value"
        ...
    }
}
```
- 결과 코드와 결과 메세지는 다음과 같습니다.

| 결과 코드  | 결과 메세지             | 상세     |
|--------|--------------------|--------|
| 0000   | Success            | 성공     |
| 1404   | Resource Not Found | 데이터 없음 |
| 9999    | Fail               | 실패     |


---
## Configuration
- 어플리케이션 설정 파일 :arrow_right: [application.yml](src/main/resources/application.yml):
- H2 DB 설정 : 영구적인 저장을 위한 테스트는 file 방식을 이용하세요. 메모리 방식은 재기동시 초기화 됩니다!
```yaml
spring:
  application:
    name: goods-manager
  # R2DBC
  r2dbc:
    # memory 방식
    url: r2dbc:h2:mem:///m_project
    # file 방식
#    url: r2dbc:h2:file:///./m_project
```
- H2 콘솔포트는 8082 입니다.

---
## Running Tests
```sh
mvn test
```

---
## License
MIT License.

