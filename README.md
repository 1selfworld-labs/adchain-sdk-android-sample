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
- Adjoe Offerwall 통합
- App Launch Test (앱 설치 여부 확인)

### 기술 스택

- **언어**: Kotlin 1.9.24
- **최소 SDK**: 24 (Android 7.0 Nougat)
- **타겟 SDK**: 35 (Android 15)
- **빌드 도구**: Gradle 8.5
- **SDK 배포**: JitPack (v1.0.32)
- **아키텍처**: Multi-Activity + Fragment, Material Design 3, BottomNavigationView

## ✨ 주요 기능

### 1. 탭 기반 UI (NEW v1.2.0)
- **홈 탭**: 기존 SDK 테스트 기능 (Quiz, Mission, Offerwall 등)
- **혜택 탭**: AdchainOfferwallView 통합 - WebView 기반 오퍼월 화면
- **BottomNavigationView**: Material Design 3 스타일 하단 탭
- **로그인 화면 분리**: LoginActivity로 인증 플로우 독립

### 2. SDK 초기화 제어
- **수동 초기화**: "Initialize SDK" 버튼을 통한 명시적 SDK 초기화
- **Skip Login**: SDK 미초기화 상태에서 graceful error handling 테스트
- **3가지 플로우 지원**:
  - 정상 플로우: Initialize SDK → Login
  - 테스트 플로우: Skip Login (SDK 미초기화)
  - 혼합 플로우: Initialize SDK → Skip Login

### 3. 사용자 인증
- 사용자 ID 기반 로그인
- 사용자 프로필 정보 설정 (성별, 출생년도, 커스텀 속성)
- 로그아웃 기능

### 4. Quiz 시스템
- Quiz 목록 조회
- Quiz 참여 및 완료
- 보상 획득
- Empty state 처리

### 5. Mission 시스템
- Mission 목록 표시
- Mission 진행 상태 추적
- Mission 이벤트 리스너 (완료, 진행, 새로고침)
- 인스턴스 재사용 패턴으로 메모리 효율 개선
- Offerwall 프로모션 연동
- 보상 시스템

### 6. Offerwall
- Offerwall 화면 표시 (팝업 방식)
- Placement ID 기반 오퍼월 관리
- 콜백 처리 (Open, Close, Error, Reward)

### 7. AdchainOfferwallView (NEW v1.2.0)
- **WebView 기반 오퍼월**: 혜택 탭에 내장된 웹뷰
- **백버튼 처리**: handleBackPress()로 WebView 네비게이션 관리
- **이벤트 콜백**: onCustomEvent, onDataRequest 지원
- **Placement ID**: "sample-test-android-placement"

### 8. Banner 광고
- Banner 데이터 조회
- 내부/외부 링크 처리
- 이미지 표시

### 9. Adjoe Offerwall
- Adjoe 플랫폼 기반 오퍼월
- Placement ID 기반 관리
- 콜백 처리 (Open, Close, Error, Reward)
- 사용자 프로필 연동 (Gender/Age)

### 10. App Launch Test
- WebView에서 앱 설치 여부 확인 테스트
- 클립보드를 통한 테스트 코드 자동 복사
- JavaScript Bridge 테스트 지원

## 📁 프로젝트 구조

```
adchain-sdk-android-sample/
├── app/
│   ├── src/main/
│   │   ├── java/com/adchain/sample/
│   │   │   ├── LoginActivity.kt            # 로그인 화면 (SDK 초기화, 인증)
│   │   │   ├── MainActivity.kt             # 탭 컨테이너 (홈/혜택)
│   │   │   ├── HomeFragment.kt             # 홈 탭 (SDK 테스트 메뉴)
│   │   │   ├── BenefitsFragment.kt         # 혜택 탭 (AdchainOfferwallView)
│   │   │   ├── SampleApplication.kt        # Application 클래스
│   │   │   ├── quiz/
│   │   │   │   ├── QuizActivity.kt         # Quiz 목록 및 참여
│   │   │   │   ├── QuizAdapter.kt          # Quiz RecyclerView 어댑터
│   │   │   │   └── QuizViewHolder.kt       # Quiz 아이템 뷰홀더
│   │   │   └── mission/
│   │   │       ├── MissionActivity.kt      # Mission 목록 및 관리
│   │   │       ├── MissionAdapter.kt       # Mission RecyclerView 어댑터
│   │   │       └── MissionViewHolder.kt    # Mission 아이템 뷰홀더
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   │   ├── activity_login.xml      # 로그인 화면
│   │   │   │   ├── activity_main_tabs.xml  # 탭 컨테이너
│   │   │   │   ├── fragment_home.xml       # 홈 탭
│   │   │   │   ├── fragment_benefits.xml   # 혜택 탭
│   │   │   │   └── ...                     # 기타 레이아웃
│   │   │   ├── menu/
│   │   │   │   └── bottom_navigation_menu.xml  # 하단 탭 메뉴
│   │   │   ├── drawable/
│   │   │   │   ├── ic_home.xml             # 홈 아이콘
│   │   │   │   ├── ic_benefits.xml         # 혜택 아이콘
│   │   │   │   └── ...
│   │   │   ├── color/
│   │   │   │   └── bottom_nav_color.xml    # 탭 색상 selector
│   │   │   ├── values/                     # 색상, 문자열, 테마
│   │   │   └── xml/                        # 네트워크 보안 설정 등
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── README.md
├── CLAUDE.md
└── BENEFITS_TAB_IMPLEMENTATION.md          # 혜택 탭 구현 가이드
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

2. **SDK 의존성 설정**

   이 샘플 앱은 JitPack을 통해 배포된 AdChain SDK를 사용합니다.

   #### JitPack 배포 버전 사용 (권장)

   `app/build.gradle.kts` 파일에서 SDK 버전 확인:
   ```kotlin
   dependencies {
       implementation("com.github.1selfworld-labs:adchain-sdk-android:v1.0.32")
   }
   ```

   #### 로컬 모듈 참조 (SDK 개발 시)

   SDK를 직접 수정하고 테스트하려면 로컬 모듈로 참조할 수 있습니다:

   1. `settings.gradle.kts` 파일 수정:
      ```kotlin
      include(":adchain-sdk")
      project(":adchain-sdk").projectDir = file("../adchain-sdk-android/adchain-sdk")
      ```

   2. `app/build.gradle.kts` 파일 수정:
      ```kotlin
      dependencies {
          // implementation("com.github.1selfworld-labs:adchain-sdk-android:v1.0.23")
          implementation(project(":adchain-sdk"))
      }
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

### 7. Adjoe 통합

Adjoe는 AdChain SDK와 통합된 써드파티 광고 플랫폼으로, 다양한 광고 형태와 보상 시스템을 제공합니다.

#### Adjoe Offerwall 열기

MainActivity.kt에서 실제 구현된 코드:

```kotlin
// MainActivity.kt
adjoeButton.setOnClickListener {
    AdchainSdk.openAdjoeOfferwall(
        context = this,
        placementId = "main_adjoe_test",
        callback = object : OfferwallCallback {
            override fun onOpened() {
                Log.d(TAG, "Adjoe Offerwall opened successfully")
            }

            override fun onClosed() {
                Log.d(TAG, "Adjoe Offerwall closed by user")
            }

            override fun onError(message: String) {
                Log.e(TAG, "Adjoe Offerwall error: $message")
                Toast.makeText(this@MainActivity, "Adjoe Error: $message", Toast.LENGTH_LONG).show()
            }

            override fun onRewardEarned(amount: Int) {
                Log.d(TAG, "Adjoe reward earned: $amount")
                Toast.makeText(this@MainActivity, "Adjoe reward: $amount points!", Toast.LENGTH_SHORT).show()
            }
        }
    )
}
```

#### API 파라미터

| 파라미터 | 타입 | 설명 | 필수 |
|---------|------|------|------|
| `context` | `Context` | Activity 컨텍스트 | ✅ |
| `placementId` | `String` | Adjoe Placement ID | ✅ |
| `callback` | `OfferwallCallback` | 이벤트 콜백 | ✅ |

#### OfferwallCallback 인터페이스

```kotlin
interface OfferwallCallback {
    fun onOpened()                    // Offerwall이 성공적으로 열렸을 때
    fun onClosed()                    // 사용자가 Offerwall을 닫았을 때
    fun onError(message: String)      // 오류 발생 시
    fun onRewardEarned(amount: Int)   // 보상 획득 시
}
```

#### Gender/Age 정보 전달 (선택사항)

Adjoe SDK는 사용자의 성별과 나이 정보를 활용하여 더 타겟팅된 광고를 제공합니다.
AdChain SDK는 로그인 시 제공된 사용자 정보를 자동으로 Adjoe SDK에 전달합니다.

**사용자 프로필 정보 설정:**

```kotlin
val user = AdchainSdkUser.Builder(userId)
    .setGender(AdchainSdkUser.Gender.MALE)   // 성별 설정 (선택사항)
    .setBirthYear(1990)                       // 출생년도 설정 (선택사항)
    .build()

AdchainSdk.login(user, loginListener)
```

**지원되는 값:**

| 속성 | 타입 | 설명 | 필수 여부 |
|------|------|------|-----------|
| `gender` | `AdchainSdkUser.Gender` | `MALE` 또는 `FEMALE` | 선택 |
| `birthYear` | `Int` | 출생년도 (예: 1990) | 선택 |

**중요 사항:**

1. **Optional 필드**: gender와 birthYear는 선택사항입니다
   - 정보가 없으면 null로 전달 → Adjoe는 정보 없이 동작
   - 정보가 있으면 자동으로 Adjoe SDK에 전달됩니다

2. **재초기화 불가**: Adjoe SDK는 재초기화를 지원하지 않습니다
   - **로그인 시점에 모든 정보를 제공**해야 합니다
   - 나중에 정보를 얻은 경우: 로그아웃 후 재로그인 필요

3. **자동 전달**: AdChain SDK가 자동으로 처리합니다
   - Android: `PlaytimeUserProfile` 객체로 변환하여 전달
   - Gender → `PlaytimeGender.MALE/FEMALE`
   - BirthYear → Java `Date` 객체 (매년 1월 1일 기준)

**예시 코드:**

```kotlin
// 정보가 있는 경우
val user = AdchainSdkUser.Builder("user_123")
    .setGender(AdchainSdkUser.Gender.MALE)
    .setBirthYear(1990)
    .build()
AdchainSdk.login(user, loginListener)

// 정보가 없는 경우 (Adjoe는 정보 없이 동작)
val user = AdchainSdkUser.Builder("user_123")
    .build()
AdchainSdk.login(user, loginListener)

// 나중에 정보를 얻은 경우: 로그아웃 후 재로그인
AdchainSdk.logout()
val updatedUser = AdchainSdkUser.Builder("user_123")
    .setGender(AdchainSdkUser.Gender.FEMALE)
    .setBirthYear(1995)
    .build()
AdchainSdk.login(updatedUser, loginListener)
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
class MissionActivity : AppCompatActivity(), AdchainMissionEventsListener {

    private var adchainMission: AdchainMission? = null

    private fun loadMissionData() {
        Log.d(TAG, "Loading mission data...")
        showLoadingState()

        // AdchainMission 인스턴스 생성
        adchainMission = AdchainMission()

        // 이벤트 리스너 등록
        adchainMission?.setEventsListener(this)

        // Mission 목록 조회
        adchainMission?.getMissionList(
            onSuccess = { missions ->
                runOnUiThread {
                    // Mission 상태도 함께 조회
                    adchainMission?.getMissionStatus(
                        onSuccess = { status ->
                            runOnUiThread {
                                updateProgress(MissionProgress(status.current, status.total))
                                when {
                                    status.isCompleted && status.total > 0 -> showRewardState()
                                    missions.isEmpty() -> showEmptyState()
                                    else -> showSuccessState(missions)
                                }
                            }
                        },
                        onFailure = { error ->
                            // 상태 조회 실패 시에도 미션 목록은 표시
                            if (missions.isNotEmpty()) {
                                showSuccessState(missions)
                            } else {
                                showEmptyState()
                            }
                        }
                    )
                }
            },
            onFailure = { error ->
                Log.e(TAG, "Failed to load missions: $error")
                runOnUiThread {
                    showErrorState()
                }
            }
        )
    }

    // 기존 인스턴스 재사용 - 메모리 효율적
    private fun refreshMissionData() {
        Log.d(TAG, "Refreshing mission data (reusing existing instance)...")
        showLoadingState()

        // ✅ 기존 인스턴스 재사용 (새로 만들지 않음)
        adchainMission?.getMissionList(
            onSuccess = { missions ->
                runOnUiThread {
                    adchainMission?.getMissionStatus(
                        onSuccess = { status ->
                            runOnUiThread {
                                updateProgress(MissionProgress(status.current, status.total))
                                when {
                                    status.isCompleted && status.total > 0 -> showRewardState()
                                    missions.isEmpty() -> showEmptyState()
                                    else -> showSuccessState(missions)
                                }
                            }
                        },
                        onFailure = { error ->
                            if (missions.isNotEmpty()) {
                                showSuccessState(missions)
                            } else {
                                showEmptyState()
                            }
                        }
                    )
                }
            },
            onFailure = { error ->
                runOnUiThread {
                    showErrorState()
                }
            }
        )
    }

    // ========== 이벤트 리스너 구현 ==========

    override fun onCompleted(mission: Mission) {
        Log.d(TAG, "✅ Mission completed: ${mission.id}")
        runOnUiThread {
            Toast.makeText(this, "Mission completed! Refreshing list...", Toast.LENGTH_SHORT).show()
            refreshMissionData()  // 자동 갱신
        }
    }

    override fun onProgressed(mission: Mission) {
        Log.d(TAG, "Mission progressed: ${mission.id}")
        runOnUiThread {
            // 빈번한 이벤트이므로 UI만 업데이트
            missionAdapter.notifyDataSetChanged()
        }
    }

    override fun onRefreshed(unitId: String?) {
        Log.d(TAG, "🔄 Mission list refreshed (unitId: $unitId)")
        runOnUiThread {
            Toast.makeText(this, "Refreshing mission list...", Toast.LENGTH_SHORT).show()
            refreshMissionData()  // 데이터 갱신
        }
    }

    override fun onClicked(mission: Mission) {
        Log.d(TAG, "Mission clicked: ${mission.id}")
        // SDK가 자동으로 WebView 열기 처리
    }

    override fun onImpressed(mission: Mission) {
        Log.d(TAG, "Mission impressed: ${mission.id}")
        // SDK가 자동으로 impression 추적
    }

    override fun onDestroy() {
        super.onDestroy()
        // 리소스 정리
        adchainMission?.destroy()
        adchainMission = null
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

### 8. App Launch Test (앱 설치 여부 확인)

WebView 내에서 특정 앱의 설치 여부를 확인하는 JavaScript Bridge 기능을 테스트할 수 있습니다.

```kotlin
// MainActivity.kt
private fun performAddTestButton() {
    val packageName = appLaunchInput.text?.toString()?.trim()

    if (packageName.isNullOrEmpty()) {
        appLaunchInputLayout.error = "패키지명을 입력하세요 (예: com.instagram.android)"
        return
    }

    appLaunchInputLayout.error = null
    Log.d(TAG, "Preparing app launch test for package: $packageName")

    // 테스트 코드를 클립보드에 복사
    val testCode = """
window.AdchainBridge.checkAppInstalled('$packageName');
window.onAppInstalledResult = function(result) {
    alert('설치: ' + result.installed + '\n패키지: ' + result.identifier);
};
    """.trimIndent()

    try {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
        val clip = android.content.ClipData.newPlainText("Test Code", testCode)
        clipboard.setPrimaryClip(clip)

        // 안내 다이얼로그 표시 및 Offerwall 열기
        AlertDialog.Builder(this)
            .setTitle("앱 실행 테스트 방법")
            .setMessage("""
                테스트 코드가 클립보드에 복사되었습니다!

                테스트 방법:
                1. "Adchain Hub Test" 버튼을 눌러 Offerwall를 엽니다
                2. Chrome DevTools 또는 WebView 디버깅으로 콘솔을 엽니다
                3. 복사된 코드를 콘솔에 붙여넣고 실행합니다

                테스트 패키지: $packageName

                또는 아래 버튼을 눌러 Offerwall를 바로 열 수 있습니다.
            """.trimIndent())
            .setPositiveButton("Offerwall 열기") { _, _ ->
                AdchainSdk.openOfferwall(
                    context = this,
                    placementId = "app_launch_test",
                    callback = object : OfferwallCallback {
                        override fun onOpened() {
                            Log.d(TAG, "Offerwall opened for app launch test")
                            Toast.makeText(this@MainActivity, "콘솔에서 테스트 코드를 실행하세요", Toast.LENGTH_LONG).show()
                        }

                        override fun onClosed() {
                            Log.d(TAG, "Offerwall closed")
                        }

                        override fun onError(message: String) {
                            Log.e(TAG, "Offerwall error: $message")
                            Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
                        }

                        override fun onRewardEarned(amount: Int) {
                            // No-op
                        }
                    }
                )
            }
            .setNegativeButton("취소", null)
            .show()
    } catch (e: Exception) {
        Log.e(TAG, "Failed to copy test code", e)
        Toast.makeText(this, "테스트 코드 복사 실패: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}
```

#### JavaScript Bridge API

WebView에서 사용 가능한 API:

```javascript
// 앱 설치 여부 확인
window.AdchainBridge.checkAppInstalled('com.instagram.android');

// 결과 콜백 등록
window.onAppInstalledResult = function(result) {
    console.log('Installed:', result.installed);
    console.log('Identifier:', result.identifier);
};
```

#### 테스트 예시

1. **Instagram 설치 여부 확인**
   ```javascript
   window.AdchainBridge.checkAppInstalled('com.instagram.android');
   ```

2. **YouTube 설치 여부 확인**
   ```javascript
   window.AdchainBridge.checkAppInstalled('com.google.android.youtube');
   ```

3. **커스텀 앱 확인**
   ```javascript
   window.AdchainBridge.checkAppInstalled('com.yourcompany.yourapp');
   ```

#### WebView 디버깅 설정

Chrome DevTools를 통해 WebView를 디버깅하려면:

1. Chrome 브라우저에서 `chrome://inspect` 접속
2. "Devices" 탭에서 연결된 기기 확인
3. WebView 인스턴스 선택 후 "inspect" 클릭
4. Console 탭에서 테스트 코드 실행

## 📱 화면별 기능

### LoginActivity (NEW v1.2.0)

**주요 기능:**
- SDK 초기화 제어
- 사용자 로그인/로그아웃
- Skip Login (테스트 모드)

**UI 구성:**
- Initialize SDK 버튼
- User ID 입력 필드
- Login 버튼
- Skip Login 버튼

**플로우:**
1. SDK 초기화 (선택)
2. User ID 입력 후 Login
3. 또는 Skip Login으로 테스트 모드 진입
4. → MainActivity (탭 화면)로 이동

### MainActivity (NEW v1.2.0)

**주요 기능:**
- 탭 기반 네비게이션 컨테이너
- 로그인 상태 확인 및 자동 리디렉션
- Fragment 관리

**UI 구성:**
- BottomNavigationView (홈/혜택 탭)
- Fragment Container

**탭 전환:**
- 홈 탭: HomeFragment
- 혜택 탭: BenefitsFragment

### HomeFragment (NEW v1.2.0)

**주요 기능:**
- 기존 MainActivity 기능 이관
- SDK 테스트 메뉴 화면
- 기능별 화면 이동 (Quiz, Mission, Offerwall, Banner, Adjoe, App Launch Test)

**UI 구성:**
- 사용자 정보 표시
- 테스트 버튼 그리드
- 로그아웃 버튼

### BenefitsFragment (NEW v1.2.0)

**주요 기능:**
- AdchainOfferwallView 통합
- WebView 기반 오퍼월 표시
- 백버튼 처리 (WebView 네비게이션 관리)

**UI 구성:**
- AdchainOfferwallView (전체 화면)

**이벤트 처리:**
- `onCustomEvent`: WebView에서 발생하는 커스텀 이벤트
- `onDataRequest`: WebView에서 요청하는 데이터 제공
- `handleBackPress`: 백버튼 동작 제어

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

### 1. 탭 네비게이션 테스트 (NEW v1.2.0)

**정상 플로우:**
1. LoginActivity에서 로그인 완료
2. MainActivity로 자동 이동
3. 기본적으로 홈 탭 표시
4. "혜택" 탭 클릭
5. AdchainOfferwallView 로딩 확인
6. "홈" 탭 클릭하여 다시 메뉴로 이동

**백버튼 동작:**
1. 혜택 탭에서 오퍼월 페이지 이동
2. 백버튼 클릭
3. WebView 스택이 여러 개면 이전 페이지로 이동
4. WebView 스택이 1개면 앱 종료

### 2. SDK 초기화 테스트

**정상 플로우:**
1. 앱 실행 (LoginActivity)
2. "Initialize SDK" 버튼 클릭
3. Toast: "SDK initialized successfully" 확인
4. 버튼 상태: "SDK Initialized ✓"

**오류 처리:**
1. 중복 초기화 시도
2. Toast: "SDK already initialized" 확인

### 3. 로그인 테스트

**정상 플로우:**
1. SDK 초기화 완료
2. User ID 입력 (예: test_user_123)
3. "Login" 버튼 클릭
4. Toast: "Login successful!" 확인
5. MainActivity (탭 화면)로 이동

**오류 처리:**
1. SDK 미초기화 상태에서 로그인 시도
2. Error: "SDK not initialized" 확인

### 4. Skip Login 테스트

**테스트 플로우:**
1. 앱 실행 (LoginActivity)
2. "Initialize SDK" 버튼 클릭 (선택)
3. "Skip Login (Test without initialization)" 버튼 클릭
4. MainActivity (탭 화면)로 이동
5. 홈 탭에서 각 기능 클릭 시 graceful error handling 확인

### 5. AdchainOfferwallView 테스트 (NEW v1.2.0)

**정상 플로우:**
1. MainActivity 혜택 탭 클릭
2. AdchainOfferwallView 로딩 확인
3. WebView에서 페이지 이동
4. 백버튼으로 네비게이션 확인

**이벤트 콜백 확인:**
1. 혜택 탭에서 WebView 페이지 이동
2. Logcat에서 onCustomEvent 로그 확인
3. WebView에서 데이터 요청 시 onDataRequest 호출 확인

### 6. Quiz 테스트

1. 홈 탭에서 "Quiz Test" 클릭
2. Quiz 목록 로딩 확인
3. Quiz 아이템 클릭하여 참여
4. 보상 획득 확인

### 7. Mission 테스트

**정상 플로우:**
1. 홈 탭에서 "Mission System Test" 클릭
2. Mission 목록 로딩 확인
3. Mission 진행 상태 확인
4. Mission 클릭하여 WebView 열기
5. Offerwall 프로모션 클릭하여 Offerwall 이동

**이벤트 리스너 테스트:**
1. Mission 완료 시 자동 리스트 갱신 확인
   - WebView에서 미션 완료
   - Toast: "Mission completed! Refreshing list..."
   - 자동으로 미션 목록 갱신됨
2. Mission 진행 시 UI 업데이트 확인
   - 진행 상태 변경 시 어댑터 자동 갱신
3. WebView에서 새로고침 메시지 수신 시 확인
   - Toast: "Refreshing mission list..."
   - 자동으로 미션 데이터 갱신됨

**메모리 효율 확인:**
- Logcat에서 "Refreshing mission data (reusing existing instance)" 확인
- 인스턴스 재생성 없이 데이터만 갱신되는지 확인

### 8. Offerwall 테스트 (팝업 방식)

1. 홈 탭에서 "Adchain Hub Test" 클릭
2. Offerwall 화면 열림 확인 (팝업)
3. 광고 참여 및 보상 획득
4. Offerwall 닫기

### 9. Banner 테스트

1. 홈 탭에서 "Banner Test" 클릭
2. Banner 데이터 로딩 확인
3. Banner 정보 Dialog 표시 확인

### 10. Adjoe Offerwall 테스트

**정상 플로우:**
1. 홈 탭에서 "Adjoe Offerwall Test" 클릭
2. Adjoe Offerwall 화면 열림 확인
3. 광고 참여 및 보상 획득
4. Offerwall 닫기

**콜백 확인:**
- onOpened: Logcat에서 "Adjoe Offerwall opened successfully" 확인
- onClosed: 닫기 시 "Adjoe Offerwall closed by user" 확인
- onRewardEarned: 보상 획득 시 Toast 메시지 "Adjoe reward: X points!" 확인
- onError: 오류 발생 시 Toast 메시지 "Adjoe Error: ..." 확인

**테스트 포인트:**
- SDK 미초기화 상태에서 접근 시 graceful error handling
- 로그인 시 제공한 Gender/Age 정보가 Adjoe에 전달되는지 확인
- Adjoe 광고 목록 및 리워드 시스템 정상 동작 확인

### 11. App Launch Test

**정상 플로우:**
1. Package Name 입력 (예: `com.instagram.android`)
2. "Add Test Button to Offerwall" 버튼 클릭
3. 테스트 코드가 클립보드에 복사됨
4. 안내 Dialog에서 "Offerwall 열기" 클릭
5. Offerwall 열림
6. Chrome DevTools (`chrome://inspect`)에서 WebView 디버깅 시작
7. Console에 클립보드의 코드 붙여넣기 및 실행
8. Alert으로 설치 여부 확인

**테스트 예시:**
- Instagram: `com.instagram.android`
- YouTube: `com.google.android.youtube`
- Facebook: `com.facebook.katana`
- WhatsApp: `com.whatsapp`

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

**최종 업데이트**: 2025-01-24
**버전**: v1.3.0 - Mission 이벤트 리스너 및 인스턴스 재사용 패턴 구현
