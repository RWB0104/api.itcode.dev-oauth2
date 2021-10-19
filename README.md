# OAuth Project Backend

<p align="center"><img src="https://project.itcode.dev/oauth2/assets/images/logo.png" alt="logo" /></p>

<h4 align="center">OAuth Project Backend</h4>

## 📙 Information

* [![Java](http://img.shields.io/badge/java-v16.0.2-007396?style=flat&logo=java&logoWidth=25)](https://www.java.com/ko/)
* ![License](https://img.shields.io/github/license/RWB0104/api.itcode.dev-oauth2)

<br />
<br />

## 📥 Download

* [최신 릴리즈](https://github.com/RWB0104/api.itcode.dev-oauth2/releases/latest)

<br />
<br />

## 📚 JAVA Dependency

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

## 📋 Contents

지원하는 플랫폼은 아래와 같음

* NAVER
* Google
* KAKAO
* GitHub

### buid.gradle 수정하기

`build.gradle` 배포 태스크가 존재하므로, Gradle 빌드가 제대로 진행되지 않을 수 있다.

파일의 아래쪽 `remotes`, `task deployCache`, `task deploy` 관련 설정을 제거한다.

### OAuth2.0 설정파일 생성하기

`WEB-INF/` 경로 아래에 `.properties` 파일을 생성하여 API Key, Secret Key, Callback URL을 입력해야함.

파일이름은 각 플랫폼의 lowercase와 동일하게 구성됨.

|  이름  |       파일        |
| :----: | :---------------: |
| NAVER  | naver.properties  |
| Google | google.properties |
| KAKAO  | kakao.properties  |
| GitHub | github.properties |

위의 파일은 `.gitignore`에 제외하도록 지정되어있다.

양식은 동일한 경로의 `sample.properties`를 복사하여 사용할 것.

### API 명세

* [OAuth2.0 Backend API Wiki](https://github.com/RWB0104/api.itcode.dev-oauth2/wiki/OAuth2.0-Backend)

## 🌐 Publish

* [OAuth2.0 Project](https://project.itcode.dev/oauth2)

<br />
<br />

## 📄 관련 문서

* [OAuth2.0 Frontend](https://github.com/RWB0104/oauth2)
* [OAuth2.0 Backend API Wiki](https://github.com/RWB0104/api.itcode.dev-oauth2/wiki/OAuth2.0-Backend)
* OAuth 개발기 추가 예정