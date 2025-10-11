# AdChain SDK Android Sample

AdChain SDK의 주요 기능을 시연하고 통합 방법을 보여주는 Android 샘플 애플리케이션입니다.

## 📋 목차

- [프로젝트 개요](#프로젝트-개요)
- [주요 기능](#주요-기능)
- [프로젝트 구조](#프로젝트-구조)
- [시작하기](#시작하기)
- [SDK 통합 가이드](#sdk-통합-가이드)
- [화면별 기능](#화면별-기능)
- [빌드 및 실행](#빌드-및-실행)
- [문제 해결](#문제-해결)

## 🎯 프로젝트 개요

이 샘플 앱은 AdChain SDK의 다음 기능들을 시연합니다:
- SDK 초기화 및 사용자 인증
- Quiz 시스템 통합
- Mission 시스템 통합
- Offerwall 통합
- Banner 광고 통합

### 기술 스택

- **언어**: Kotlin 1.9.24
- **최소 SDK**: 24 (Android 7.0 Nougat)
- **타겟 SDK**: 35 (Android 15)
- **빌드 도구**: Gradle 8.1.1
- **아키텍처**: Activity 기반, Material Design 3

## ✨ 주요 기능

### 1. SDK 초기화 제어
- **수동 초기화**: "Initialize SDK" 버튼을 통한 명시적 SDK 초기화
- **Skip Login**: SDK 미초기화 상태에서 graceful error handling 테스트
- **3가지 플로우 지원**:
  - 정상 플로우: Initialize SDK → Login
  - 테스트 플로우: Skip Login (SDK 미초기화)
  - 혼합 플로우: Initialize SDK → Skip Login

### 2. 사용자 인증
- 사용자 ID 기반 로그인
- 사용자 프로필 정보 설정 (성별, 출생년도, 커스텀 속성)
- 로그아웃 기능

### 3. Quiz 시스템
- Quiz 목록 조회
- Quiz 참여 및 완료
- 보상 획득
- Empty state 처리

### 4. Mission 시스템
- Mission 목록 표시
- Mission 진행 상태 추적
- Offerwall 프로모션 연동
- 보상 시스템

### 5. Offerwall
- Offerwall 화면 표시
- Placement ID 기반 오퍼월 관리
- 콜백 처리 (Open, Close, Error, Reward)

### 6. Banner 광고
- Banner 데이터 조회
- 내부/외부 링크 처리
- 이미지 표시

## 📁 프로젝트 구조

```
adchain-sdk-android-sample/
├── app/
│   ├── src/main/
│   │   ├── java/com/adchain/sample/
│   │   │   ├── MainActivity.kt              # 메인 화면 (로그인, SDK 초기화)
│   │   │   ├── SampleApplication.kt         # Application 클래스
│   │   │   ├── quiz/
│   │   │   │   ├── QuizActivity.kt         # Quiz 목록 및 참여
│   │   │   │   ├── QuizAdapter.kt          # Quiz RecyclerView 어댑터
│   │   │   │   └── QuizViewHolder.kt       # Quiz 아이템 뷰홀더
│   │   │   └── mission/
│   │   │       ├── MissionActivity.kt      # Mission 목록 및 관리
│   │   │       ├── MissionAdapter.kt       # Mission RecyclerView 어댑터
│   │   │       └── MissionViewHolder.kt    # Mission 아이템 뷰홀더
│   │   ├── res/
│   │   │   ├── layout/                     # XML 레이아웃 파일
│   │   │   ├── drawable/                   # 아이콘 및 drawable 리소스
│   │   │   ├── values/                     # 색상, 문자열, 테마
│   │   │   └── xml/                        # 네트워크 보안 설정 등
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── README.md
└── CLAUDE.md
```

## 🚀 시작하기

### 사전 요구사항

- **Android Studio**: Arctic Fox (2020.3.1) 이상
- **JDK**: 8 이상
- **Gradle**: 8.0 이상
- **Android SDK**:
  - Compile SDK: 35
  - Min SDK: 24
  - Target SDK: 35

### 설치 및 실행

1. **프로젝트 클론**
   ```bash
   git clone https://github.com/1selfworld-labs/adchain-sdk-android-sample.git
   cd adchain-sdk-android-sample
   ```

2. **SDK 모듈 연결 확인**

   이 샘플 앱은 상위 폴더의 `adchain-sdk-android` 프로젝트를 로컬 모듈로 참조합니다.

   `settings.gradle.kts` 파일 확인:
   ```kotlin
   include(":adchain-sdk")
   project(":adchain-sdk").projectDir = file("../adchain-sdk-android/adchain-sdk")
   ```

3. **앱 키 설정**

   `app/src/main/java/com/adchain/sample/SampleApplication.kt` 파일에서 앱 키를 설정합니다:
   ```kotlin
   private const val APP_ID = "your_app_id"
   private const val APP_SECRET = "your_app_secret"
   ```

4. **Android Studio에서 프로젝트 열기**
   ```bash
   # Android Studio가 설치된 경우
   open -a "Android Studio" .
   ```

5. **Gradle Sync**
   - Android Studio에서 자동으로 Gradle sync가 실행됩니다
   - 또는 `File` → `Sync Project with Gradle Files`

6. **빌드 및 실행**
   - 상단의 Run 버튼 (▶️) 클릭
   - 또는 터미널에서:
     ```bash
     ./gradlew assembleDebug
     ./gradlew installDebug
     ```

## 📖 SDK 통합 가이드

### 1. SDK 초기화

#### 수동 초기화 (권장 - 테스트 목적)

```kotlin
// SampleApplication.kt
class SampleApplication : Application() {

    companion object {
        lateinit var instance: SampleApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        // SDK 초기화를 자동으로 하지 않음
        // MainActivity에서 버튼을 통해 수동으로 초기화
        Log.d(TAG, "Application created - SDK initialization skipped for testing")
    }

    fun initializeAdchainSdk() {
        Log.d(TAG, "Initializing Adchain SDK...")

        // SDK 로그 레벨 설정
        AdchainSdk.setLogLevel(LogLevel.VERBOSE)

        // SDK Config 생성
        val config = AdchainSdkConfig.Builder(APP_ID, APP_SECRET)
            .setEnvironment(AdchainSdkConfig.Environment.DEVELOPMENT)
            .setTimeout(30000L) // 30초 타임아웃
            .build()

        // SDK 초기화
        AdchainSdk.initialize(this, config)

        Log.d(TAG, "Adchain SDK initialized successfully")
    }
}
```

#### 프로덕션 환경에서 자동 초기화

프로덕션 앱에서는 `Application.onCreate()`에서 자동으로 SDK를 초기화하는 것을 권장합니다:

```kotlin
override fun onCreate() {
    super.onCreate()
    instance = this

    // 프로덕션에서는 앱 시작 시 자동 초기화
    initializeAdchainSdk()
}
```

### 2. 사용자 로그인

```kotlin
// MainActivity.kt
private fun performLogin() {
    val userId = userIdInput.text?.toString()?.trim()

    if (userId.isNullOrEmpty()) {
        Toast.makeText(this, "Please enter a user ID", Toast.LENGTH_SHORT).show()
        return
    }

    // 사용자 정보 생성
    val user = AdchainSdkUser.Builder(userId)
        .setGender(AdchainSdkUser.Gender.MALE)
        .setBirthYear(1990)
        .setCustomProperty("test_user", "true")
        .build()

    // 로그인 수행
    AdchainSdk.login(user, object : AdchainSdkLoginListener {
        override fun onSuccess() {
            Log.d(TAG, "Login successful")
            Toast.makeText(this@MainActivity, "Login successful!", Toast.LENGTH_SHORT).show()
            updateUI()
        }

        override fun onFailure(errorType: AdchainSdkLoginListener.ErrorType) {
            Log.e(TAG, "Login failed: $errorType")
            val errorMessage = when (errorType) {
                AdchainSdkLoginListener.ErrorType.NOT_INITIALIZED -> "SDK not initialized"
                AdchainSdkLoginListener.ErrorType.INVALID_USER_ID -> "Invalid user ID"
                AdchainSdkLoginListener.ErrorType.AUTHENTICATION_FAILED -> "Authentication failed"
                AdchainSdkLoginListener.ErrorType.NETWORK_ERROR -> "Network error"
                AdchainSdkLoginListener.ErrorType.UNKNOWN -> "Unknown error"
            }
            Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_LONG).show()
        }
    })
}
```

### 3. Quiz 통합

```kotlin
// QuizActivity.kt
class QuizActivity : AppCompatActivity() {

    private var adchainQuiz: AdchainQuiz? = null

    private fun loadQuizEvents() {
        // AdchainQuiz 인스턴스 생성
        adchainQuiz = AdchainQuiz()

        // Quiz 목록 조회
        adchainQuiz?.getQuizList(
            onSuccess = { quizResponse ->
                val events = quizResponse.events
                runOnUiThread {
                    if (events.isEmpty()) {
                        showEmptyState()
                    } else {
                        showQuizList(events)
                    }
                }
            },
            onFailure = { error ->
                Log.e(TAG, "Failed to load quiz events: $error")
                runOnUiThread {
                    showErrorState(error)
                }
            }
        )
    }
}
```

### 4. Mission 통합

```kotlin
// MissionActivity.kt
class MissionActivity : AppCompatActivity() {

    private var adchainMission: AdchainMission? = null

    private fun loadMissions() {
        // AdchainMission 인스턴스 생성
        adchainMission = AdchainMission()

        // Mission 목록 조회
        adchainMission?.getMissionList(
            onSuccess = { missions ->
                runOnUiThread {
                    if (missions.isEmpty()) {
                        showEmptyState()
                    } else {
                        showMissionList(missions)
                    }
                }
            },
            onFailure = { error ->
                Log.e(TAG, "Failed to load missions: $error")
                runOnUiThread {
                    showErrorState(error)
                }
            }
        )
    }
}
```

### 5. Offerwall 통합

```kotlin
// MainActivity.kt
adchainHubButton.setOnClickListener {
    AdchainSdk.openOfferwall(
        context = this,
        placementId = "main_adchain_hub",
        callback = object : OfferwallCallback {
            override fun onOpened() {
                Log.d(TAG, "Offerwall opened successfully")
            }

            override fun onClosed() {
                Log.d(TAG, "Offerwall closed by user")
            }

            override fun onError(message: String) {
                Log.e(TAG, "Offerwall error: $message")
                Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
            }

            override fun onRewardEarned(amount: Int) {
                Log.d(TAG, "User earned reward: $amount")
                Toast.makeText(
                    this@MainActivity,
                    "You earned $amount points!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    )
}
```

### 6. Banner 통합

```kotlin
// MainActivity.kt
private fun performBannerTest() {
    AdchainBanner.getBanner(
        placementId = "test_placement_001",
        onSuccess = { bannerResponse ->
            Log.d(TAG, "Banner loaded successfully")
            val linkUrl = bannerResponse.internalLinkUrl
                ?: bannerResponse.externalLinkUrl
                ?: "N/A"

            val message = """
                Banner Data Received:

                Title: ${bannerResponse.titleText}
                Image URL: ${bannerResponse.imageUrl}
                Link Type: ${bannerResponse.linkType}
                Link URL: $linkUrl
            """.trimIndent()

            // Display banner data
            showBannerDialog(message)
        },
        onFailure = { error ->
            Log.e(TAG, "Banner load failed: $error")
            showErrorDialog(error)
        }
    )
}
```

## 📱 화면별 기능

### MainActivity

**주요 기능:**
- SDK 초기화 제어
- 사용자 로그인/로그아웃
- Skip Login (테스트 모드)
- 기능별 화면 이동 (Quiz, Mission, Offerwall, Banner)

**UI 상태:**
1. **Login Screen**: SDK 초기화 및 로그인 화면
2. **Skip Mode**: SDK 미초기화 상태 테스트 모드
3. **Menu Screen**: 로그인 완료 후 기능 메뉴

### QuizActivity

**주요 기능:**
- Quiz 목록 표시
- Quiz 참여 가능 여부 표시
- Quiz 완료 상태 표시
- Empty state 및 Retry 기능

**Quiz 아이템 정보:**
- Quiz 제목
- 보상 포인트
- 참여 가능/완료 상태
- 진행 표시

### MissionActivity

**주요 기능:**
- Mission 목록 표시
- Mission 진행 상태 추적
- Offerwall 프로모션 표시
- Empty state 및 Retry 기능

**Mission 타입:**
- 일반 Mission
- Offerwall 프로모션 Mission

## 🛠 빌드 및 실행

### Gradle 명령어

```bash
# Clean 빌드
./gradlew clean

# Debug APK 빌드
./gradlew assembleDebug

# Release APK 빌드
./gradlew assembleRelease

# APK 설치
./gradlew installDebug

# 테스트 실행
./gradlew test

# Lint 검사
./gradlew lint

# Lint 검사 건너뛰고 빌드
./gradlew assembleDebug -x lint
```

### Android Studio에서 빌드

1. **Build** → **Make Project** (⌘F9 / Ctrl+F9)
2. **Build** → **Rebuild Project**
3. **Build** → **Clean Project**
4. **Build** → **Generate Signed Bundle / APK**

### 디버깅

```bash
# Logcat 필터링
adb logcat -s AdchainSdk:D AdchainSample:D

# 특정 태그만 보기
adb logcat | grep "AdchainSdk"

# 앱 로그만 보기
adb logcat --pid=$(adb shell pidof -s com.adchain.sample)
```

## 🧪 테스트 시나리오

### 1. SDK 초기화 테스트

**정상 플로우:**
1. 앱 실행
2. "Initialize SDK" 버튼 클릭
3. Toast: "SDK initialized successfully" 확인
4. 버튼 상태: "SDK Initialized ✓"

**오류 처리:**
1. 중복 초기화 시도
2. Toast: "SDK already initialized" 확인

### 2. 로그인 테스트

**정상 플로우:**
1. SDK 초기화 완료
2. User ID 입력 (예: test_user_123)
3. "Login" 버튼 클릭
4. Toast: "Login successful!" 확인
5. 메뉴 화면 표시

**오류 처리:**
1. SDK 미초기화 상태에서 로그인 시도
2. Error: "SDK not initialized" 확인

### 3. Skip Login 테스트

**테스트 플로우:**
1. 앱 실행
2. "Skip Login (Test without initialization)" 버튼 클릭
3. 메뉴 화면 표시 (경고 메시지 표시)
4. 각 기능 클릭 시 graceful error handling 확인

### 4. Quiz 테스트

1. 메뉴에서 "Quiz Test" 클릭
2. Quiz 목록 로딩 확인
3. Quiz 아이템 클릭하여 참여
4. 보상 획득 확인

### 5. Mission 테스트

1. 메뉴에서 "Mission System Test" 클릭
2. Mission 목록 로딩 확인
3. Mission 진행 상태 확인
4. Offerwall 프로모션 클릭하여 Offerwall 이동

### 6. Offerwall 테스트

1. 메뉴에서 "Adchain Hub Test" 클릭
2. Offerwall 화면 열림 확인
3. 광고 참여 및 보상 획득
4. Offerwall 닫기

### 7. Banner 테스트

1. 메뉴에서 "Banner Test" 클릭
2. Banner 데이터 로딩 확인
3. Banner 정보 Dialog 표시 확인

## 🔧 문제 해결

### 일반적인 문제

#### 1. SDK 모듈을 찾을 수 없음

**증상:**
```
> Could not resolve project :adchain-sdk
```

**해결:**
- `settings.gradle.kts`에서 SDK 경로 확인
- 상위 폴더에 `adchain-sdk-android` 프로젝트 존재 확인
- Gradle Sync 재실행

#### 2. 빌드 오류 - Lint 에러

**증상:**
```
Execution failed for task ':app:lint'
```

**해결:**
```bash
# Lint 검사 건너뛰고 빌드
./gradlew assembleDebug -x lint

# 또는 build.gradle.kts에 추가
android {
    lint {
        checkReleaseBuilds = false
        abortOnError = false
    }
}
```

#### 3. Network Error - Cleartext Traffic

**증상:**
```
java.net.UnknownServiceException: CLEARTEXT communication not permitted
```

**해결:**
- `AndroidManifest.xml`에서 `usesCleartextTraffic="true"` 확인
- `res/xml/network_security_config.xml` 설정 확인

#### 4. SDK 초기화 실패

**증상:**
```
Login failed: NOT_INITIALIZED
```

**해결:**
1. "Initialize SDK" 버튼을 먼저 클릭했는지 확인
2. Toast 메시지 "SDK initialized successfully" 확인
3. 앱 재시작 후 다시 시도

#### 5. Gradle Sync 실패

**해결:**
```bash
# Gradle 캐시 클리어
./gradlew clean
rm -rf ~/.gradle/caches/

# 의존성 새로고침
./gradlew --refresh-dependencies
```

### 로그 확인

SDK 동작을 확인하려면 Logcat을 사용하세요:

```bash
# 전체 SDK 로그
adb logcat -s AdchainSdk:V

# 샘플 앱 로그
adb logcat -s AdchainSample:D

# 네트워크 로그 포함
adb logcat -s AdchainSdk:V okhttp:D
```

## 📝 주의사항

### 보안

1. **API 키 관리**
   - 실제 앱에서는 API 키를 코드에 하드코딩하지 마세요
   - `local.properties` 또는 환경 변수 사용
   - `.gitignore`에 민감한 파일 추가

2. **Network Security**
   - 프로덕션에서는 `usesCleartextTraffic="false"` 설정
   - `network_security_config.xml` 재검토
   - HTTPS 사용 권장

3. **ProGuard/R8**
   - Release 빌드 시 ProGuard 규칙 확인
   - SDK 관련 클래스는 난독화에서 제외

### 퍼포먼스

1. **메모리 관리**
   - Activity 종료 시 SDK 리소스 정리
   - 이미지 로딩 시 Glide 사용으로 메모리 최적화

2. **네트워크**
   - 타임아웃 설정 적절히 조정
   - 재시도 로직 구현

## 📚 추가 리소스

- **AdChain SDK 문서**: [SDK Documentation]
- **API Reference**: [API Docs]
- **GitHub Issues**: [Report Issues](https://github.com/1selfworld-labs/adchain-sdk-android-sample/issues)
- **Support**: support@adchain.com

## 📄 라이선스

이 샘플 앱은 MIT 라이선스 하에 배포됩니다.

## 👥 기여

버그 리포트, 기능 제안, Pull Request를 환영합니다!

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

**최종 업데이트**: 2025-01-11
