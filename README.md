<h1 align="center">
  <img height="250" src="https://github.com/738/inko/blob/master/images/inko_logo.png?raw=true" alt=""/>
  <br>Inko.kt
</h1>
<p align="center">
<a href="https://github.com/kimcore/inko.kt/releases">
  <img src="https://img.shields.io/jitpack/v/github/kimcore/inko.kt?style=flat-square" alt=""/>
</a>
<a href="https://github.com/kimcore/inko.kt/blob/master/LICENSE">
  <img src="https://img.shields.io/github/license/kimcore/inko.kt?style=flat-square" alt=""/>
</a>
</p>
<p align="center">
  <b>Open Source Library, Converting Misspelled English characters into Korean letters (& vice versa)</b><br/>
  <b>Kotlin implementation of <a href="https://github.com/738/inko">Inko.js</a>, also works in Java!</b><br/>
</p>
<br />

## 설치
Gradle
```gradle
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
    implementation 'com.github.kimcore:inko.kt:1.1'
}
```
Maven
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.kimcore</groupId>
        <artifactId>inko.kt</artifactId>
        <version>1.1</version>
    </dependency>
</dependencies>
```
## 사용법
inko.kt는 인스턴스를 생성하거나, 코틀린의 extension property를 사용하여 변환할 수 있습니다.
```kotlin
import com.github.kimcore.inko.Inko
import com.github.kimcore.inko.Inko.Companion.asKorean
import com.github.kimcore.inko.Inko.Companion.asEnglish

val inko = Inko()

// 영어 (en) -> 한글 (ko)
println(inko.en2ko("dkssudgktpdy!")) // 안녕하세요!
println("dkssudgktpdy!".asKorean) // 안녕하세요!

// 한글 (ko) -> 영어 (en)
println(inko.ko2en("ㅗ디ㅣㅐ, 재깅!")) // hello, world!
println("ㅗ디ㅣㅐ, 재깅!".asEnglish) // hello, world!
```
## 설정
|이름|타입|기본값|설명| 
|---|---|---| --------------- |
|allowDoubleConsonant|Boolean|false|복자음 설정여부|

예시:
```kotlin
import com.github.kimcore.Inko
import com.github.kimcore.Inko.Companion.asKoreanWithDoubleConsonant

// Inko 인스턴스를 생성할때 설정 부여하기
val inko = Inko(allowDoubleConsonant = true)

// config 함수를 사용해 설정 부여하기
inko.config(allowDoubleConsonant = true)

// en2ko 함수의 인자로 설정 부여하기
inko.en2ko("dhk toswm!", allowDoubleConsonant = true)

// Extension property 사용시, String.asKoreanWithDoubleConsonant 사용
"dhk toswm!".asKoreanWithDoubleConsonant
```
## 관련 프로젝트
* [inko-js](https://github.com/738/inko) - Inko javascript library
* [inko-cli](https://github.com/738/inko-cli) - Use inko on the command line
* [inko-web](https://github.com/738/inko-web) - Inko official website
* [inko-chrome-extension](https://github.com/738/inko-chrome-extension) - Inko chrome extension
* [alfred-inko](https://github.com/738/alfred-inko) - Alfred 3 workflow to convert misspelled English characters into Korean letters (& vice versa)
* [inko.py](https://github.com/JackCme/inko.py) - Python implementation of inko.js
## 기여하기
이 오픈소스 프로젝트에 누구나 기여할 수 있습니다. 기여하고 싶은 분들은 이 레포지토리를 포크한 후 풀리퀘스트 요청해주세요!
## 라이선스
Inko.kt 라이브러리는 MIT 라이선스를 따르고 있습니다. 자세한 내용은 [LICENSE](https://github.com/JackCme/inko.py/blob/master/LICENSE) 파일을 참고해주세요.
## 감사한 분들
* [inko.js](https://github.com/738/inko) 를 개발해주신 [738](https://github.com/738) 님께 정말 감사드립니다!
* [inko.py](https://github.com/JackCme/inko.py) 를 개발해주신 [JackCme](https://github.com/JackCme) 님 또한 정말 감사드립니다. 개발하며 많은 도움이 되었습니다.