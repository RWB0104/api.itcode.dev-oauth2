# OAuth2.0 Backend

<p align="center"><img src="https://project.itcode.dev/oauth2/assets/images/logo.png" alt="logo" /></p>

<h4 align="center">OAuth2.0 Backend v1.1</h4>

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

#### 플랫폼 로그인 URL API

플랫폼 로그인 URL을 반환하는 API.

플랫폼 로그인 수행 후 결과를 URL에 입력된 `redirect_url`로 전달한다.

정상일 경우 `code` 파라미터를 전송하며, 오류일 경우 `error` 파라미터를 전송한다.

##### 요청

``` txt
GET https://api.itcode.dev/oauth2/api/login/{:platform}
```

|  parameter  | type  |   data   | required |     description      |
| :---------: | :---: | :------: | :------: | :------------------: |
| {:platform} | path  | `String` |    Y     | 플랫폼 (소문자 표기) |

##### 응답

``` json
{
	"flag": true,
	"title": "success",
	"message": "naver authrorization url response success",
	"body": "https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=czCaqAOB1aAjNRk6N_Oq&redirect_uri=https%3A%2F%2Fproject.itcode.dev%2Foauth2%2Fcallback%3Fplatform%3Dnaver&state=b73ed0eb-abcc-4f95-b1d9-d52623e90946"
}
```

| parameter |   data    |    description    |
| :-------: | :-------: | :---------------: |
|   flag    | `boolean` |     동작 결과     |
|   title   | `String`  |       제목        |
|  message  | `String`  |       내용        |
|   body    | `String`  | 플랫폼 로그인 URL |

#### 로그인 API (Access Token)

플랫폼 로그인 결과로 발급받은 `code`를 통해 Access Token으로 발급받는 API.

발급받은 API를 통해 실질적으로 서비스에서 로그인 처리를 한다.

Access Token과 Refresh Token을 서비스 내부에서 JWT로 생성하여 제공한다. 서비스는 이를 쿠키로 저장하며, 인증이 필요한 각종 요청에 사용한다.

##### 요청

``` txt
POST https://api.itcode.dev/oauth2/api/login/{:platform}
```

|  parameter  | type  |   data   | required |     description      |
| :---------: | :---: | :------: | :------: | :------------------: |
| {:platform} | path  | `String` |    Y     | 플랫폼 (소문자 표기) |

##### 응답

``` json
{
	"flag": true,
	"title": "success",
	"message": "authorized success",
	"body": null
}
```

``` txt
# Header
Set-Cookie: access={access JWT}
Set-Cookie: refresh={refresh JWT}
```

| parameter |   data    | description |
| :-------: | :-------: | :---------: |
|   flag    | `boolean` |  동작 결과  |
|   title   | `String`  |    제목     |
|  message  | `String`  |    내용     |
|   body    |  `null`   |   `null`    |

#### 자동 로그인 API

로그인 페이지 접속 시, access, refresh 쿠키를 검증하여 유효할 경우 자동로그인을 진행하는 API.

access 쿠키가 있을 경우, 이미 인증 정보를 보유하고 있으므로 별다른 동작 없이 홈 페이지로 이동한다.

refresh 쿠키만 있을 경우, Access Token을 갱신하여 로그인 수행.

##### 요청

``` txt
POST https://api.itcode.dev/oauth2/api/login/auto
```

##### 응답

``` json
{
	"flag": true,
	"title": "success",
	"message": "auto authorized success",
	"body": null
}
```

``` txt
# Header
Set-Cookie: access={access JWT}
Set-Cookie: refresh={refresh JWT}
```

| parameter |   data    | description |
| :-------: | :-------: | :---------: |
|   flag    | `boolean` |  동작 결과  |
|   title   | `String`  |    제목     |
|  message  | `String`  |    내용     |
|   body    |  `null`   |   `null`    |

#### 로그아웃 API

로그아웃을 수행하는 API.

access, refresh 쿠키를 삭제한다.

``` txt
POST https://api.itcode.dev/oauth2/api/logout
```

<br />
<br />

## 🌐 Publish

* [OAuth2.0 Project](https://project.itcode.dev/oauth2)

<br />
<br />

## 📄 관련 문서

* [OAuth2.0 Frontend](https://github.com/RWB0104/oauth2)
* OAuth 개발기 추가 예정