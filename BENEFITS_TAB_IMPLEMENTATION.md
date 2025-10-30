# Benefits Tab Implementation Guide

## Overview
Android Sample 앱에 Expo Sample과 동일한 "혜택 탭" 기능을 구현했습니다.

## 변경 사항

### 1. SDK 버전 업그레이드
- **이전**: v1.0.29
- **현재**: v1.0.32
- **변경 파일**: `app/build.gradle.kts`

### 2. 아키텍처 변경
**Before (Single Activity)**:
```
MainActivity (Login + Menu)
```

**After (Multi Activity + Fragment)**:
```
LoginActivity (Login Screen)
  ↓
MainActivity (Tab Container)
  ├── HomeFragment (기존 기능들)
  └── BenefitsFragment (AdchainOfferwallView)
```

### 3. 추가된 파일

#### Activity
- `LoginActivity.kt` - SDK 초기화 및 로그인 화면
- `MainActivity.kt` (새 버전) - Tab 컨테이너

#### Fragment
- `HomeFragment.kt` - 기존 MainActivity 기능 이관
- `BenefitsFragment.kt` - AdchainOfferwallView 통합

#### Layout
- `activity_login.xml` - 로그인 화면
- `activity_main_tabs.xml` - Tab 컨테이너 레이아웃
- `fragment_home.xml` - 홈 탭
- `fragment_benefits.xml` - 혜택 탭 (AdchainOfferwallView)

#### Resources
- `menu/bottom_navigation_menu.xml` - 하단 탭 메뉴
- `drawable/ic_home.xml` - 홈 아이콘
- `drawable/ic_benefits.xml` - 혜택 아이콘
- `color/bottom_nav_color.xml` - 탭 색상 selector

#### Backup
- `MainActivity.kt.backup` - 기존 MainActivity
- `activity_main.xml.backup` - 기존 레이아웃

### 4. 주요 기능

#### BenefitsFragment
```kotlin
// AdchainOfferwallView 통합
offerwallView?.loadOfferwall(PLACEMENT_ID)

// 백버튼 처리
backPressCallback = object : OnBackPressedCallback(true) {
    override fun handleOnBackPressed() {
        val handled = offerwallView?.handleBackPress() ?: false
        if (!handled) {
            // Allow default back behavior (will exit app)
            isEnabled = false
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }
}

// 이벤트 콜백 (v1.0.32+)
offerwallView?.setEventCallback(object : OfferwallEventCallback {
    override fun onCustomEvent(eventType: String, payload: Map<String, Any?>) {
        // Handle custom events from WebView
    }

    override fun onDataRequest(requestId: String, requestType: String, params: Map<String, Any?>): Map<String, Any?>? {
        // Return data to WebView
        return when (requestType) {
            "user_points" -> mapOf("points" to 12345, "currency" to "KRW")
            "user_profile" -> mapOf("userId" to "test_123", "nickname" to "TestPlayer")
            else -> null
        }
    }
})
```

## 빌드 및 실행

### 1. Gradle Sync
```bash
./gradlew clean build
```

### 2. 의존성 다운로드
v1.0.32 SDK가 JitPack에서 자동으로 다운로드됩니다.

### 3. 실행
```bash
./gradlew installDebug
```

또는 Android Studio에서 Run 버튼 클릭

## 테스트 플로우

### Flow 1: 정상 플로우
1. LoginActivity 실행
2. "Initialize SDK" 버튼 클릭
3. User ID 입력 (test_user_123)
4. "Login" 버튼 클릭
5. MainActivity (Tab) 이동
6. "혜택" 탭 클릭
7. AdchainOfferwallView 표시

### Flow 2: Skip Login
1. LoginActivity 실행
2. "Initialize SDK" 버튼 클릭
3. "Skip Login" 버튼 클릭
4. MainActivity (Tab) 이동 (테스트 모드)

## Expo Sample과의 비교

| 구분 | Expo Sample | Android Sample |
|------|-------------|----------------|
| **Tab 구조** | TabNavigation (2 tabs) | BottomNavigationView (2 tabs) |
| **혜택 탭 View** | AdchainOfferwallView (React Native) | AdchainOfferwallView (Native) |
| **백버튼 처리** | BackHandler + handleBackPress | OnBackPressedCallback + handleBackPress |
| **이벤트 콜백** | onCustomEvent, onDataRequest | OfferwallEventCallback (동일) |
| **Placement ID** | "sample-test-expo-placement" | "sample-test-android-placement" |

## 주의사항

### 1. SDK 버전
- v1.0.32 이상 필요
- AdchainOfferwallView 및 OfferwallEventCallback 지원

### 2. 백버튼 동작
- WebView 스택이 1개일 때: 앱 종료
- WebView 스택이 2개 이상일 때: handleClose() 호출 (페이지 뒤로가기)

### 3. Fragment Lifecycle
- `onViewCreated`: AdchainOfferwallView 초기화 및 로드
- `onDestroyView`: 백버튼 콜백 제거

## 롤백 방법

기존 방식으로 돌아가려면:

```bash
# 1. 기존 파일 복원
mv app/src/main/java/com/adchain/sample/MainActivity.kt.backup app/src/main/java/com/adchain/sample/MainActivity.kt
mv app/src/main/res/layout/activity_main.xml.backup app/src/main/res/layout/activity_main.xml

# 2. AndroidManifest.xml 수정 (MainActivity를 LAUNCHER로)
# 3. 신규 파일 삭제
rm app/src/main/java/com/adchain/sample/LoginActivity.kt
rm app/src/main/java/com/adchain/sample/HomeFragment.kt
rm app/src/main/java/com/adchain/sample/BenefitsFragment.kt
# ... (기타 신규 리소스 파일)

# 4. SDK 버전 다운그레이드 (선택)
# app/build.gradle.kts에서 v1.0.29로 변경
```

## 참고 자료
- [Expo Sample TabNavigation](../adchain-sdk-react-native-expo-sample/src/components/TabNavigation.tsx)
- [Android SDK AdchainOfferwallView](../adchain-sdk-android/adchain-sdk/src/main/kotlin/com/adchain/sdk/offerwall/AdchainOfferwallView.kt)
- [Android Navigation Component Docs](https://developer.android.com/guide/navigation)
- [BottomNavigationView Docs](https://developer.android.com/reference/com/google/android/material/bottomnavigation/BottomNavigationView)
