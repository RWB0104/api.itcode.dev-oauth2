# OAuth Project Backend

<p align="center"><img src="https://project.itcode.dev/oauth2/assets/images/logo.png" alt="logo" /></p>

<h4 align="center">OAuth Project Backend</h4>

## π Information

* [![Java](http://img.shields.io/badge/java-v16.0.2-007396?style=flat&logo=java&logoWidth=25)](https://www.java.com/ko/)
* ![License](https://img.shields.io/github/license/RWB0104/api.itcode.dev-oauth2)

<br />
<br />

## π₯ Download

* [μ΅μ  λ¦΄λ¦¬μ¦](https://github.com/RWB0104/api.itcode.dev-oauth2/releases/latest)

<br />
<br />

## π JAVA Dependency

* ![jakarta.servlet-api](https://img.shields.io/badge/jakarta.servlet--api-v5.0.0-blue)
* ![lombok](https://img.shields.io/badge/lombok-v1.18.20-blue)
* ![jjwt](https://img.shields.io/badge/jjwt-v0.9.1-blue)
* ![scribejava-apis](https://img.shields.io/badge/scribejava--apis-v8.3.1-blue)
* ![jersey-server](https://img.shields.io/badge/jersey--server-v3.0.3-blue)
* ![jersey-container-servlet](https://img.shields.io/badge/jersey--container--servlet-v3.0.3-blue)
* ![jersey-hk2](https://img.shields.io/badge/jersey--hk2-v3.0.3-blue)
* ![jersey-media-json-jackson](https://img.shields.io/badge/jersey--media--json--jackson-v3.0.3-blue)

<br />
<br />

## π Contents

μ§μνλ νλ«νΌμ μλμ κ°μ

* NAVER
* Google
* KAKAO
* GitHub

### buid.gradle μμ νκΈ°

`build.gradle` λ°°ν¬ νμ€ν¬κ° μ‘΄μ¬νλ―λ‘, Gradle λΉλκ° μ λλ‘ μ§νλμ§ μμ μ μλ€.

νμΌμ μλμͺ½ `remotes`, `task deployCache`, `task deploy` κ΄λ ¨ μ€μ μ μ κ±°νλ€.

### OAuth2.0 μ€μ νμΌ μμ±νκΈ°

`WEB-INF/` κ²½λ‘ μλμ `.properties` νμΌμ μμ±νμ¬ API Key, Secret Key, Callback URLμ μλ ₯ν΄μΌν¨.

νμΌμ΄λ¦μ κ° νλ«νΌμ lowercaseμ λμΌνκ² κ΅¬μ±λ¨.

|  μ΄λ¦  |       νμΌ        |
| :----: | :---------------: |
| NAVER  | naver.properties  |
| Google | google.properties |
| KAKAO  | kakao.properties  |
| GitHub | github.properties |

μμ νμΌμ `.gitignore`μ μ μΈνλλ‘ μ§μ λμ΄μλ€.

μμμ λμΌν κ²½λ‘μ `sample.properties`λ₯Ό λ³΅μ¬νμ¬ μ¬μ©ν  κ².

### API λͺμΈ

* [OAuth2.0 Backend API Wiki](https://github.com/RWB0104/api.itcode.dev-oauth2/wiki/OAuth2.0-Backend)

## π Publish

* [OAuth2.0 Project](https://project.itcode.dev/oauth2)

<br />
<br />

## π κ΄λ ¨ λ¬Έμ

* [OAuth2.0 Frontend](https://github.com/RWB0104/oauth2)
* [OAuth2.0 Backend API Wiki](https://github.com/RWB0104/api.itcode.dev-oauth2/wiki/OAuth2.0-Backend)
* [OAuth Project κ°λ°κΈ° 1νΈ μΈ λ€μ](https://blog.itcode.dev/posts/2021/10/14/oauth2-java-server-1)