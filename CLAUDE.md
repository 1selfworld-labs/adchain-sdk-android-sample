# AdChain SDK Android Sample - 개발자 가이드

## 프로젝트 개요

AdChain SDK Android 샘플 애플리케이션으로, SDK의 주요 기능들을 테스트하고 통합 방법을 보여주는 예제 프로젝트입니다.

이 프로젝트는 **탭 기반 UI**, **SDK 초기화 제어**, **사용자 인증**, **Quiz/Mission/Offerwall/Banner 통합**, **AdchainOfferwallView 통합**을 시연하며, 특히 **SDK 미초기화 상태에서의 graceful error handling**과 **WebView 기반 오퍼월 화면**을 테스트할 수 있도록 설계되었습니다.

## 프로젝트 구조

```
adchain-sdk-android-sample/
├── app/                                    # 샘플 앱 모듈
│   ├── src/main/java/com/adchain/sample/
│   │   ├── LoginActivity.kt                # 로그인 화면 (SDK 초기화, 인증)
│   │   ├── MainActivity.kt                 # 탭 컨테이너 (홈/혜택)
│   │   ├── HomeFragment.kt                 # 홈 탭 (SDK 테스트 메뉴)
│   │   ├── BenefitsFragment.kt             # 혜택 탭 (AdchainOfferwallView)
│   │   ├── SampleApplication.kt            # Application 클래스 (SDK 초기화 로직)
│   │   ├── quiz/
│   │   │   ├── QuizActivity.kt            # Quiz 목록 및 참여
│   │   │   ├── QuizAdapter.kt             # RecyclerView 어댑터
│   │   │   └── QuizViewHolder.kt          # ViewHolder
│   │   └── mission/
│   │       ├── MissionActivity.kt         # Mission 목록 및 관리
│   │       ├── MissionAdapter.kt          # RecyclerView 어댑터
│   │       └── MissionViewHolder.kt       # ViewHolder
│   ├── src/main/res/
│   │   ├── layout/
│   │   │   ├── activity_login.xml         # 로그인 화면
│   │   │   ├── activity_main_tabs.xml     # 탭 컨테이너
│   │   │   ├── fragment_home.xml          # 홈 탭
│   │   │   ├── fragment_benefits.xml      # 혜택 탭
│   │   │   ├── activity_quiz.xml          # Quiz 화면
│   │   │   ├── activity_mission.xml       # Mission 화면
│   │   │   ├── item_quiz.xml              # Quiz 아이템
│   │   │   ├── item_mission.xml           # Mission 아이템
│   │   │   └── item_offerwall_promotion.xml
│   │   ├── menu/
│   │   │   └── bottom_navigation_menu.xml # 하단 탭 메뉴
│   │   ├── drawable/
│   │   │   ├── ic_home.xml                # 홈 아이콘
│   │   │   ├── ic_benefits.xml            # 혜택 아이콘
│   │   │   └── ...                        # 기타 아이콘
│   │   ├── color/
│   │   │   └── bottom_nav_color.xml       # 탭 색상 selector
│   │   ├── values/                        # 색상, 문자열, 테마
│   │   │   ├── colors.xml
│   │   │   ├── strings.xml
│   │   │   └── themes.xml
│   │   └── xml/                           # 설정 파일
│   │       ├── network_security_config.xml
│   │       ├── backup_rules.xml
│   │       └── data_extraction_rules.xml
│   └── AndroidManifest.xml
├── build.gradle.kts                       # 루트 빌드 설정
├── settings.gradle.kts                    # 프로젝트 설정 (SDK 모듈 연결)
├── gradle.properties                      # Gradle 프로퍼티
├── README.md                              # 사용자 문서
├── CLAUDE.md                              # 개발자 가이드 (이 문서)
└── BENEFITS_TAB_IMPLEMENTATION.md         # 혜택 탭 구현 가이드
```

## SDK 모듈 연결

이 프로젝트는 상위 폴더의 `adchain-sdk-android` 프로젝트를 **로컬 모듈**로 직접 참조합니다:

### settings.gradle.kts 설정
```kotlin
rootProject.name = "AdchainSample"
include(":app")
include(":adchain-sdk")
project(":adchain-sdk").projectDir = file("../adchain-sdk-android/adchain-sdk")
```

### 장점
- SDK 코드 수정 시 샘플 앱에 **즉시 반영**
- 빌드 시간 단축 (매번 SDK 재빌드 불필요)
- 디버깅 용이

### 주의사항
- 상위 폴더에 `adchain-sdk-android` 프로젝트가 반드시 존재해야 함
- SDK 프로젝트의 폴더 구조 변경 시 경로 업데이트 필요

## 주요 기능 및 변경사항

### 0. 탭 기반 UI 아키텍처 (NEW v1.2.0)

**변경 이유**: Expo Sample과 동일한 탭 구조 구현, AdchainOfferwallView 통합

#### 아키텍처 변경

**Before (Single Activity)**:
```
MainActivity (Login + Menu + SDK Tests)
  - 로그인 화면
  - 메뉴 화면
  - 모든 기능이 하나의 Activity
```

**After (Multi Activity + Fragment)**:
```
LoginActivity (Login Screen)
  - SDK 초기화
  - 사용자 로그인
  - Skip Login 기능
    ↓
MainActivity (Tab Container)
  ├── HomeFragment (기존 메뉴 기능)
  │   - Quiz Test
  │   - Mission Test
  │   - Offerwall Test (팝업)
  │   - Banner Test
  │   - Adjoe Offerwall Test
  │   - App Launch Test
  │   - Logout
  └── BenefitsFragment (AdchainOfferwallView)
      - WebView 기반 오퍼월
      - 백버튼 처리
      - 이벤트 콜백
```

#### 주요 변경 파일

**추가된 파일:**
- `LoginActivity.kt` - 로그인 전용 화면
- `MainActivity.kt` (새 버전) - 탭 컨테이너
- `HomeFragment.kt` - 홈 탭 (기존 MainActivity 기능)
- `BenefitsFragment.kt` - 혜택 탭 (AdchainOfferwallView)
- `activity_login.xml` - 로그인 레이아웃
- `activity_main_tabs.xml` - 탭 컨테이너 레이아웃
- `fragment_home.xml` - 홈 탭 레이아웃
- `fragment_benefits.xml` - 혜택 탭 레이아웃
- `menu/bottom_navigation_menu.xml` - 하단 탭 메뉴
- `drawable/ic_home.xml`, `ic_benefits.xml` - 탭 아이콘
- `color/bottom_nav_color.xml` - 탭 색상 selector

**삭제된 파일:**
- `activity_main.xml` (기존 MainActivity 레이아웃)

**SDK 버전 업그레이드:**
- v1.0.29 → v1.0.32 (AdchainOfferwallView 및 OfferwallEventCallback 지원)

### 1. SDK 초기화 제어 (v1.1.0)

**변경 이유**: SDK 미초기화 상태에서 graceful error handling 테스트를 위해

#### Before (자동 초기화)
```kotlin
// SampleApplication.kt
override fun onCreate() {
    super.onCreate()
    instance = this
    initializeAdchainSdk()  // 자동 초기화
}
```

#### After (수동 초기화)
```kotlin
// SampleApplication.kt
override fun onCreate() {
    super.onCreate()
    instance = this

    // SDK 초기화를 자동으로 하지 않음
    // MainActivity에서 선택적으로 초기화할 수 있도록 함
    Log.d(TAG, "Application created - SDK initialization skipped for testing")
}

// 외부에서 호출 가능한 초기화 함수
fun initializeAdchainSdk() {
    Log.d(TAG, "Initializing Adchain SDK...")

    // SDK 로그 레벨 설정
    AdchainSdk.setLogLevel(LogLevel.VERBOSE)

    val config = AdchainSdkConfig.Builder(APP_ID, APP_SECRET)
        .setEnvironment(AdchainSdkConfig.Environment.DEVELOPMENT)
        .setTimeout(30000L)
        .build()

    AdchainSdk.initialize(this, config)

    Log.d(TAG, "Adchain SDK initialized successfully with App ID: $APP_ID")
}
```

### 2. 3가지 테스트 플로우 지원

#### Flow 1: 정상 플로우 (Initialize → Login)
1. "Initialize SDK" 버튼 클릭
2. User ID 입력
3. "Login" 버튼 클릭
4. 메뉴 화면 표시

#### Flow 2: Skip Login (SDK 미초기화 테스트)
1. "Skip Login (Test without initialization)" 버튼 클릭
2. 메뉴 화면 표시 (경고 메시지)
3. 각 기능 클릭 시 graceful error 확인

#### Flow 3: 혼합 플로우 (Initialize → Skip Login)
1. "Initialize SDK" 버튼 클릭
2. "Skip Login" 버튼 클릭
3. 메뉴 화면 표시 (SDK 초기화는 됨, 로그인은 안 됨)

### 3. UI 상태 관리 (3-State)

```kotlin
// MainActivity.kt - updateUI()
private fun updateUI() {
    val isLoggedIn = AdchainSdk.isLoggedIn

    // Update SDK init button state
    initSdkButton.isEnabled = !isSdkInitialized
    initSdkButton.text = if (isSdkInitialized) "SDK Initialized ✓" else "Initialize SDK"

    when {
        // Skip mode: Show menu without SDK initialization
        isSkipMode -> {
            loginContainer.visibility = View.GONE
            menuContainer.visibility = View.VISIBLE
            userInfoText.text = "⚠️ Test Mode: SDK Not Initialized\nTesting graceful error handling"
        }
        // Normal flow: Logged in
        isLoggedIn -> {
            loginContainer.visibility = View.GONE
            menuContainer.visibility = View.VISIBLE
            userInfoText.text = "✓ Logged in as: ${AdchainSdk.getCurrentUser()?.userId ?: "Unknown"}"
        }
        // Show login screen
        else -> {
            loginContainer.visibility = View.VISIBLE
            menuContainer.visibility = View.GONE
            userIdInput.setText("")
        }
    }
}
```

### 4. SDK API 업데이트 반영

#### openOfferwall API - placementId 추가
```kotlin
// Before
AdchainSdk.openOfferwall(this)

// After
AdchainSdk.openOfferwall(
    context = this,
    placementId = "main_adchain_hub",  // NEW
    callback = object : OfferwallCallback {
        // ...
    }
)
```

#### Quiz/Mission 생성자 변경
```kotlin
// Before
adchainQuiz = AdchainQuiz("quiz_unit_id")
adchainMission = AdchainMission("mission_unit_id")

// After
adchainQuiz = AdchainQuiz()  // unit ID 제거
adchainMission = AdchainMission()  // unit ID 제거
```

#### getQuizList 응답 구조 변경
```kotlin
// Before
adchainQuiz?.getQuizList(
    onSuccess = { events ->  // List<QuizEvent> 직접 반환
        // ...
    }
)

// After
adchainQuiz?.getQuizList(
    onSuccess = { quizResponse ->  // QuizResponse 래핑
        val events = quizResponse.events  // List<QuizEvent> 추출
        // ...
    }
)
```

#### Banner API - 링크 구조 변경
```kotlin
// Before
val linkUrl = bannerResponse.linkUrl

// After
val linkUrl = bannerResponse.internalLinkUrl
    ?: bannerResponse.externalLinkUrl
    ?: "N/A"
```

### 5. Mission 이벤트 리스너 구현 (v1.3.0)

**변경 이유**: iOS/React Native 샘플과 동일한 이벤트 처리 패턴 적용, 메모리 효율 개선

#### 문제점 분석

**Before (v1.2.0)**:
- MissionActivity에 이벤트 리스너 미구현
- Mission 완료/새로고침 시 자동 갱신 없음
- 수동으로 새로고침 버튼 클릭 필요

#### 개선 내용

##### 1. AdchainMissionEventsListener 구현
```kotlin
// MissionActivity.kt
class MissionActivity : AppCompatActivity(), AdchainMissionEventsListener {

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
}
```

##### 2. refreshMissionData() 메서드 - 인스턴스 재사용 패턴
```kotlin
private fun refreshMissionData() {
    Log.d(TAG, "Refreshing mission data (reusing existing instance)...")
    showLoadingState()

    // ✅ 기존 AdchainMission 인스턴스 재사용 (새로 만들지 않음!)
    adchainMission?.getMissionList(
        onSuccess = { missions ->
            runOnUiThread {
                // 미션 상태도 함께 조회
                adchainMission?.getMissionStatus(
                    onSuccess = { status ->
                        runOnUiThread {
                            // 진행 상태 UI 업데이트
                            val progress = MissionProgress(status.current, status.total)
                            updateProgress(progress)

                            when {
                                status.isCompleted && status.total > 0 -> showRewardState()
                                missions.isEmpty() -> showEmptyState()
                                else -> showSuccessState(missions)
                            }
                        }
                    },
                    onFailure = { error ->
                        // 에러 처리
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
```

##### 3. loadMissionData() vs refreshMissionData()

**loadMissionData()** - 초기 로드:
- 새로운 `AdchainMission()` 인스턴스 생성
- 이벤트 리스너 등록
- 어댑터 초기화
- onCreate()나 재시도 버튼에서 호출

**refreshMissionData()** - 데이터 갱신:
- 기존 인스턴스 재사용 (**메모리 효율**)
- 인스턴스 생성 안 함
- 리스너 재등록 안 함
- 어댑터 재생성 안 함
- onCompleted(), onRefreshed()에서 호출

#### 해결된 문제

##### 1. ConcurrentModificationException 방지
```kotlin
// 문제가 있던 패턴 (onRefreshed에서 새 인스턴스 생성):
override fun onRefreshed(unitId: String?) {
    loadMissionData()  // ❌ 새 인스턴스 생성
    // → activeMissionInstances 리스트 순회 중 수정
    // → ConcurrentModificationException 발생!
}

// 올바른 패턴 (기존 인스턴스 재사용):
override fun onRefreshed(unitId: String?) {
    refreshMissionData()  // ✅ 기존 인스턴스 재사용
    // → 리스트 수정 없음
    // → 안전!
}
```

##### 2. 메모리 효율 개선
- 불필요한 인스턴스 생성 방지
- 이벤트 리스너 중복 등록 방지
- `activeMissionInstances` 리스트 크기 최소화

##### 3. UI 반응성 향상
- Mission 완료 시 자동 리스트 갱신
- WebView에서 missionRefreshed 메시지 수신 시 자동 갱신
- 진행 상태 업데이트 시 UI만 빠르게 갱신

#### onProgressed 최적화

진행 상태 업데이트는 빈번하게 발생할 수 있으므로, 전체 API 재호출 대신 **UI만 업데이트**:

```kotlin
override fun onProgressed(mission: Mission) {
    runOnUiThread {
        // ❌ refreshMissionData() 호출하지 않음 (너무 빈번)
        // ✅ UI만 업데이트
        missionAdapter.notifyDataSetChanged()
    }
}
```

#### iOS/React Native와의 일관성

| 플랫폼 | onCompleted | onRefreshed | onProgressed |
|--------|-------------|-------------|--------------|
| **iOS** | `loadMissionData()` | `loadMissionData()` | 구현 안 함 |
| **React Native** | `loadMissionList()` | `loadMissionList()` | 구현 안 함 |
| **Android (v1.3.0)** | `refreshMissionData()` | `refreshMissionData()` | `notifyDataSetChanged()` |

**패턴 개선**: Android는 인스턴스 재사용으로 iOS보다 **더 효율적**

## 빌드 및 실행

### 요구사항
- **Android Studio**: Arctic Fox (2020.3.1) 이상
- **Kotlin**: 1.9.24
- **Android Gradle Plugin**: 8.1.1
- **JDK**: 8 이상
- **Min SDK**: 24 (Android 7.0 Nougat)
- **Target SDK**: 35 (Android 15)
- **Compile SDK**: 35

### 빌드 명령

```bash
# Clean 빌드
./gradlew clean build

# Debug APK 생성
./gradlew assembleDebug

# Release APK 빌드
./gradlew assembleRelease

# APK 설치
./gradlew installDebug

# Lint 검사 건너뛰고 빌드
./gradlew assembleDebug -x lint

# 테스트 실행
./gradlew test

# 연결된 디바이스에서 테스트 실행
./gradlew connectedAndroidTest
```

### Android Studio에서 실행

1. Android Studio 실행
2. `File` → `Open` → 프로젝트 폴더 선택
3. Gradle Sync 완료 대기
4. Run 버튼 (▶️) 클릭 또는 `Shift+F10`

### 일반적인 빌드 오류 해결

#### 1. SDK 경로 오류
```
Could not resolve project :adchain-sdk
```
**해결**: `settings.gradle.kts`에서 SDK 경로 확인
```kotlin
project(":adchain-sdk").projectDir = file("../adchain-sdk-android/adchain-sdk")
```

#### 2. Lint 오류
```
Execution failed for task ':app:lint'
```
**해결**: Lint 검사 건너뛰기
```bash
./gradlew assembleDebug -x lint
```

#### 3. Vector Drawable tint 오류
```
Error: app:tint not defined
```
**해결**: `android:tint` → `app:tint` 변경

#### 4. Cleartext Traffic 오류
```
java.net.UnknownServiceException: CLEARTEXT communication not permitted
```
**해결**: `AndroidManifest.xml` 설정 확인
```xml
<application
    android:usesCleartextTraffic="true"
    android:networkSecurityConfig="@xml/network_security_config">
```

## 배포 관련 (JitPack)

### JitPack을 통한 SDK 배포 프로세스

이 샘플 앱은 로컬 모듈로 SDK를 참조하지만, 실제 프로덕션 앱에서는 JitPack을 통해 배포된 SDK를 사용할 수 있습니다.

#### 1. 배포 준비
```bash
# SDK 프로젝트로 이동
cd ../adchain-sdk-android

# 버전 태그 생성 (Semantic Versioning)
git tag -a v1.0.0 -m "Release version 1.0.0"
```

#### 2. GitHub 저장소 Public 전환
⚠️ **중요**: JitPack 빌드를 위해 일시적으로 Public 필요

1. GitHub 저장소 Settings 접속
2. "Danger Zone" → "Change repository visibility"
3. "Change to public" 선택
4. 저장소 이름 입력하여 확인

#### 3. JitPack 빌드 트리거
```bash
# 태그 푸시
git push origin v1.0.0

# JitPack 빌드 상태 확인
# https://jitpack.io/#[username]/adchain-sdk-android/v1.0.0
```

#### 4. 빌드 성공 확인 후 통합
샘플 앱의 `build.gradle.kts`에 의존성 추가:

```kotlin
// app/build.gradle.kts
repositories {
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    // JitPack 의존성 (배포 후)
    implementation("com.github.[username]:adchain-sdk-android:v1.0.0")

    // 또는 로컬 모듈 참조 (개발 중)
    // implementation(project(":adchain-sdk"))
}
```

#### 5. Private 저장소로 복구
✅ **배포 완료 후 즉시 Private으로 전환**

1. GitHub Settings → "Change repository visibility"
2. "Change to private" 선택
3. 저장소 이름 입력하여 확인

### Private 저장소에서 JitPack 사용

Private 저장소 배포 후 사용 시:

1. JitPack 인증 토큰 생성 (https://jitpack.io/private)
2. `gradle.properties`에 토큰 추가:
```properties
authToken=jp_xxxxxxxxxxxxx
```

3. 저장소 설정에 인증 추가:
```kotlin
repositories {
    maven {
        url = uri("https://jitpack.io")
        credentials {
            username = "jp_token"
            password = findProperty("authToken") as String
        }
    }
}
```

## 테스트 체크리스트

### SDK 통합 테스트
- [x] SDK 수동 초기화 정상 동작
- [x] SDK 중복 초기화 방지 확인
- [x] 사용자 로그인/로그아웃
- [x] Skip Login 테스트 모드 동작
- [x] 네트워크 통신 정상 동작
- [x] 로그 레벨 설정 (VERBOSE)

### UI/UX 테스트
- [x] Login Container ↔ Menu Container 전환
- [x] Initialize SDK 버튼 상태 관리
- [x] Skip Login 버튼 동작
- [x] 3-State UI 전환 확인
- [x] Toast 메시지 표시

### 기능별 테스트
- [x] Quiz 목록 로딩
- [x] Quiz 참여 및 완료
- [x] Quiz Empty State 처리
- [x] Mission 목록 표시
- [x] Mission 진행 상태 업데이트
- [x] Mission 이벤트 리스너 (onCompleted, onRefreshed, onProgressed)
- [x] Mission 인스턴스 재사용 패턴
- [x] ConcurrentModificationException 방지
- [x] Offerwall 표시 및 상호작용
- [x] Offerwall Callback 처리 (Open, Close, Error, Reward)
- [x] Banner 데이터 조회 및 표시

### API 업데이트 테스트
- [x] openOfferwall placementId 파라미터 전달
- [x] Quiz/Mission 생성자 unit ID 제거
- [x] getQuizList QuizResponse 래핑 처리
- [x] Banner 링크 구조 변경 (internalLinkUrl/externalLinkUrl)

### 빌드 및 배포
- [x] 로컬 빌드 성공
- [ ] Lint 검사 통과
- [x] APK 생성 및 설치
- [ ] ProGuard/R8 규칙 적용 (Release 빌드)

### 에러 처리 테스트
- [x] SDK 미초기화 상태에서 로그인 시도 → Error handling
- [x] Skip mode에서 각 기능 접근 → Graceful error
- [x] 네트워크 오류 → Retry 기능
- [x] Empty state → UI 피드백

## 주의사항

### 보안

1. **API 키 관리**
   - ⚠️ 실제 앱에서는 API 키를 코드에 하드코딩하지 마세요
   - `local.properties` 또는 환경 변수 사용 권장
   - `.gitignore`에 `local.properties` 포함 확인
   ```kotlin
   // 권장 방법
   val appId = BuildConfig.APP_ID
   val appSecret = BuildConfig.APP_SECRET
   ```

2. **Network Security**
   - 개발 환경: `usesCleartextTraffic="true"` 허용
   - 프로덕션: `usesCleartextTraffic="false"` 설정 필수
   - `network_security_config.xml` 재검토
   - HTTPS 사용 권장

3. **ProGuard/R8 규칙**
   - Release 빌드 시 ProGuard 규칙 확인
   - SDK 관련 클래스는 난독화에서 제외
   ```proguard
   -keep class com.adchain.sdk.** { *; }
   -keepclassmembers class com.adchain.sdk.** { *; }
   ```

### 코드 스타일

1. **Kotlin 코딩 컨벤션 준수**
   - 함수명: camelCase
   - 상수명: UPPER_SNAKE_CASE
   - 클래스명: PascalCase

2. **주석 및 문서화**
   - 불필요한 주석 제거
   - 복잡한 로직에만 설명 추가
   - KDoc 사용 권장

3. **변수명**
   - 의미 있는 변수명 사용
   - 약어 최소화

### Git 관리

1. **커밋 메시지**
   - 명확하고 간결하게 작성
   - Conventional Commits 형식 권장
   ```
   feat: Add skip login feature for testing
   fix: Fix SDK initialization crash
   docs: Update README.md with new features
   ```

2. **브랜치 전략**
   - `main`: 안정 버전
   - `develop`: 개발 브랜치
   - `feature/*`: 기능 개발
   - `hotfix/*`: 긴급 수정

3. **배포 관리**
   - 배포 전 반드시 Public 전환
   - 배포 후 즉시 Private 복구
   - 태그는 Semantic Versioning 사용

## 문제 해결

### 빌드 실패

```bash
# Gradle 캐시 클리어
./gradlew clean
rm -rf ~/.gradle/caches/

# 의존성 새로고침
./gradlew --refresh-dependencies

# Android Studio 캐시 클리어
# File → Invalidate Caches → Invalidate and Restart
```

### SDK 모듈을 찾을 수 없음

`settings.gradle.kts` 파일 확인:
```kotlin
include(":adchain-sdk")
project(":adchain-sdk").projectDir = file("../adchain-sdk-android/adchain-sdk")
```

상위 폴더에 `adchain-sdk-android` 프로젝트 존재 확인:
```bash
ls -la ../adchain-sdk-android/adchain-sdk
```

### SDK 초기화 실패

1. **로그 확인**
```bash
adb logcat -s AdchainSdk:V AdchainSample:D
```

2. **초기화 순서 확인**
   - "Initialize SDK" 버튼 먼저 클릭
   - Toast: "SDK initialized successfully" 확인
   - 로그인 진행

3. **네트워크 연결 확인**
   - WiFi/모바일 데이터 연결 확인
   - VPN 연결 해제 후 재시도

### Memory Leak 확인

```bash
# LeakCanary 추가 (build.gradle.kts)
dependencies {
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.12")
}
```

## 디버깅 팁

### Logcat 필터링

```bash
# SDK 로그만 보기
adb logcat -s AdchainSdk:V

# 샘플 앱 로그만 보기
adb logcat -s AdchainSample:D

# 네트워크 로그 포함
adb logcat -s AdchainSdk:V okhttp:D

# 특정 프로세스만 보기
adb logcat --pid=$(adb shell pidof -s com.adchain.sample)
```

### Network Inspection

```bash
# Charles Proxy 또는 Proxyman 사용
# Android 7.0+ 에서는 network_security_config.xml 설정 필요

# res/xml/network_security_config.xml
<network-security-config>
    <debug-overrides>
        <trust-anchors>
            <certificates src="user" />
            <certificates src="system" />
        </trust-anchors>
    </debug-overrides>
</network-security-config>
```

### Performance Profiling

1. Android Studio Profiler 사용
   - `View` → `Tool Windows` → `Profiler`
   - CPU, Memory, Network 모니터링

2. Systrace 분석
```bash
# Systrace 캡처
python $ANDROID_HOME/platform-tools/systrace/systrace.py \
    --time=10 -o trace.html sched gfx view
```

## 연락처 및 지원

- **기술 지원**: support@adchain.com
- **GitHub Issues**: [Report Issues](https://github.com/1selfworld-labs/adchain-sdk-android-sample/issues)
- **문서**: [Documentation](https://docs.adchain.com)
- **Slack**: [AdChain Developers](https://adchain-dev.slack.com)

## 변경 로그

### v1.3.0 (2025-01-24)
- ✨ MissionActivity에 AdchainMissionEventsListener 구현 추가
- ✨ refreshMissionData() 메서드로 인스턴스 재사용 패턴 도입
- 🐛 ConcurrentModificationException 수정 (인스턴스 생성 최소화)
- ✨ onCompleted() 이벤트에서 자동 미션 리스트 갱신
- ✨ onRefreshed() 이벤트에서 미션 데이터 갱신
- ✨ onProgressed() 이벤트에서 UI만 업데이트
- 🎨 메모리 효율 개선 및 불필요한 인스턴스 생성 방지
- 📝 iOS/React Native 샘플과 동일한 이벤트 처리 패턴 적용

### v1.2.0 (2025-01-30)
- 🎨 **아키텍처 변경**: Single Activity → Multi Activity + Fragment 구조
- ✨ **LoginActivity 추가**: 로그인 전용 화면 분리
- ✨ **탭 기반 UI 구현**: BottomNavigationView (홈/혜택 탭)
- ✨ **HomeFragment 추가**: 기존 MainActivity 기능 이관
- ✨ **BenefitsFragment 추가**: AdchainOfferwallView 통합
- 🆙 **SDK 버전 업그레이드**: v1.0.29 → v1.0.32
- ✨ **AdchainOfferwallView 통합**: WebView 기반 오퍼월 화면
- ✨ **백버튼 처리**: handleBackPress()로 WebView 네비게이션 관리
- ✨ **이벤트 콜백**: OfferwallEventCallback (onCustomEvent, onDataRequest)
- 🎨 **UI/UX 개선**: Material Design 3 하단 탭 네비게이션
- 📝 **문서 업데이트**: README.md, CLAUDE.md 최신화
- 📝 **구현 가이드 추가**: BENEFITS_TAB_IMPLEMENTATION.md

### v1.1.0 (2025-01-11)
- ✨ SDK 수동 초기화 기능 추가
- ✨ Skip Login 테스트 모드 추가
- ✨ 3-State UI 관리 구현
- 🔧 openOfferwall API placementId 파라미터 추가
- 🔧 Quiz/Mission 생성자에서 unit ID 제거
- 🔧 getQuizList 응답 구조 변경 (QuizResponse 래핑)
- 🔧 Banner API 링크 구조 변경 (internalLinkUrl/externalLinkUrl)
- 📝 README.md 및 CLAUDE.md 업데이트

### v1.0.0 (Initial Release)
- 🎉 Initial commit
- ✨ SDK 초기화 및 로그인
- ✨ Quiz, Mission, Offerwall, Banner 통합

## 라이선스

이 샘플 앱은 MIT 라이선스 하에 배포됩니다.

---

**최종 업데이트**: 2025-01-24
**작성자**: AdChain Development Team
**문서 버전**: 1.3.0
