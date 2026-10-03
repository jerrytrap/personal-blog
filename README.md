# Personal Blog

roadmap.sh 사이트의 [[과제]](https://roadmap.sh/projects/personal-blog)를 해결한 학습용 프로젝트입니다.

## 목차

1. [프로젝트 개요](#프로젝트-개요)
2. [기능 요구사항](#기능-요구사항)
3. [구현 요구사항](#구현-요구사항)
4. [실행 방법](#실행-방법)
5. [트러블슈팅](#트러블슈팅)

---

## 프로젝트 개요

- 게시글을 파일 시스템에 저장하는 개인 블로그입니다. 게스트는 게시글을 열람만 할 수 있고, 관리자는 인증 후 게시글을 작성/수정/삭제할 수 있습니다.
- 관리자 인증은 **Basic 인증 vs 세션 인증**, **직접 구현 vs Spring Security** 두 축으로 나눠 총 4가지 방식을 각각 구현하고 비교했습니다.
- Spring Security를 바로 쓰기보다, 필터·세션 관리·CSRF 토큰 검증을 먼저 직접 구현해본 뒤 Spring Security로 전환하면서 `httpBasic()`, `formLogin()` 같은 설정 한 줄이 내부적으로 어떤 로직을 대체하는지 직접 확인하는 것을 목표로 했습니다.

| 브랜치                     | 인증 방식                              |
|-------------------------|------------------------------------|
| `basic-auth-filter`     | 직접 구현한 HTTP Basic 인증               |
| `basic-auth-security`   | Spring Security HTTP Basic 인증      |
| `session-auth-filter`   | 직접 구현한 세션 인증 및 CSRF 검증             |
| `session-auth-security` | Spring Security 폼 로그인, 세션, CSRF 보호 |

## 기능 요구사항

**게스트 영역** (인증 불필요)
- 게시글 목록 조회
- 게시글 상세 조회

**관리자 영역** (인증 필요)
- 대시보드
- 게시글 작성 / 수정 / 삭제

## 구현 요구사항

**저장 방식**
- 파일 시스템 (JSON 또는 Markdown)

**백엔드**
- 언어 자유
- 서버에서 HTML을 렌더링해서 내려주는 방식으로 구현 (REST API 형태 x)

**프론트엔드**
- HTML + CSS 사용

**인증 방식**
- Basic 인증 또는 Session 인증

## 실행 방법

관리자 계정은 환경변수로 지정합니다. (코드에 평문 비밀번호를 두지 않기 위함)
```bash
ADMIN_USERNAME=admin ADMIN_PASSWORD=원하는_비밀번호 ./gradlew bootRun
```

---

## 트러블슈팅

### 1. 필터만으로 세션 인증을 구현했을 때 빠져 있던 보안 처리

로그인 여부만 확인하는 세션 인증 필터를 먼저 구현했는데, 세션 ID를 탈취·악용하는 공격들에 대한 방어가 빠져 있다는 것을 확인 후 추가했습니다.

#### 세션 고정 공격 방어 누락
로그인 성공 후에도 로그인 전에 발급된 세션 ID를 그대로 사용되고 있어서, 로그인 시 기존 세션을 무효화하고 새 세션을 발급하도록 수정했습니다.

```java
HttpSession oldSession = request.getSession(false);
if (oldSession != null) oldSession.invalidate();

HttpSession newSession = request.getSession(true);
newSession.setAttribute(SessionConstant.LOGIN_USER, loginRequest.getUsername());
```

#### CSRF 방어 누락
로그인 여부만 확인할 뿐, 실제로 우리 페이지에서 요청했는지는 검증하지 않고 있었습니다.  
아래와 같은 페이지에 로그인한 관리자가 접근하면, 외부에서 게시글을 조작하는게 가능하다는 문제가 있었습니다.

```html
<!-- 외부에서 게시글 삭제 요청을 보내는 예시 코드 -->
<form action="http://localhost:8080/admin/articles/1234/delete" method="POST">
</form>
<script>document.forms[0].submit()</script>
```

이를 해결하기 위해, 로그인 시 세션에 CSRF 토큰을 발급하고 폼에 hidden 필드로 노출해 제출 시 세션 값과 비교하도록 추가했습니다.

```html
<form th:action="@{/admin/create}" method="post">
    <input type="hidden" name="csrfToken" th:value="${csrfToken}" />
    <!-- ... -->
</form>
```

### 2. Spring Security 도입

직접 구현한 인증 방식에서는 인증이 필요한 URL을 개별적으로 관리하고, 폼마다 CSRF 토큰을 추가해야 했습니다.  
만약 기능과 페이지가 늘어난다면 인증·세션·CSRF 관련 코드가 여러 곳에 분산되어 유지보수 부담이 커질 수 있다고 판단했습니다.  
이를 해결하기 위해 Spring Security를 도입했고, URL별 접근 제어, 로그인·로그아웃 처리, 세션 관리, CSRF 보호를 보안 설정으로 일관되게 관리하도록 변경했습니다.

**Spring Security 적용**

```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	http
		.authorizeHttpRequests(auth -> auth
			.requestMatchers("/admin/**").authenticated()
			.anyRequest().permitAll()
		)
		.formLogin(form -> form
			.loginPage("/login")
			.defaultSuccessUrl("/admin")
		)
		.logout(logout -> logout
			.logoutUrl("/logout")
			.logoutSuccessUrl("/")
		)
		.csrf(Customizer.withDefaults())
		.sessionManagement(session -> session
			.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
			.sessionFixation(fixation -> fixation.changeSessionId())
		);

	return http.build();
}
```
- `authorizeHttpRequests(...)`: URL별 접근 권한을 설정할 수 있었습니다.  
  기존에 직접 구현한 필터에서는 요청 URL을 `if` 문으로 확인해야 했는데, URL이 늘어나면 복잡해진다는 단점이 있었습니다.
- `formLogin(...)`: 로그인 처리 흐름을 Spring Security에 맡길 수 있습니다.  
  인증되지 않은 사용자가 인증이 필요한 페이지에 접근하면 자동으로 /login으로 리다이렉트됩니다.
- `logout(...)`: 현재 인증 정보를 제거하고 HTTP 세션을 무효화합니다.
- `csrf(...)`: CSRF 보호 설정을 적용합니다.  
  게시글 생성·수정·삭제처럼 서버 상태를 변경하는 요청은 CSRF 토큰 검증을 거치며, 토큰이 없거나 일치하지 않으면 403 Forbidden 응답을 반환합니다.
- `sessionFixation(...)`: 로그인 성공 후 기존 세션의 ID를 새로운 값으로 변경합니다. (세션 고정 공격 방어)
