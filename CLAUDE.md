# AdchainSDK Android Sample 프로젝트

## 프로젝트 개요
AdchainSDK Android 샘플 애플리케이션으로, SDK의 주요 기능들을 테스트하고 통합 방법을 보여주는 예제 프로젝트입니다.

## 프로젝트 구조
```
AdchainSDK-Android-Sample/
├── app/                         # 샘플 앱 모듈
│   ├── src/main/java/          # 소스 코드
│   │   └── com/adchain/sample/
│   │       ├── MainActivity.kt  # 메인 화면
│   │       ├── mission/        # 미션 시스템
│   │       ├── quiz/          # 퀴즈 시스템
│   │       └── offerwall/     # 오퍼월
│   └── src/main/res/          # 리소스 파일
├── build.gradle.kts           # 루트 빌드 설정
└── settings.gradle.kts        # 프로젝트 설정
```

## SDK 모듈 연결
이 프로젝트는 상위 폴더의 `adchain-sdk-android` 프로젝트를 직접 참조합니다:
- 경로: `../adchain-sdk-android/adchain-sdk`
- SDK 코드 수정 시 샘플 앱에 즉시 반영됨

## 주요 기능
- **사용자 로그인**: SDK 초기화 및 사용자 인증
- **퀴즈 시스템**: 퀴즈 참여 및 보상
- **미션 시스템**: 다양한 미션 수행
- **오퍼월**: 광고 참여 및 포인트 획득

## 빌드 및 실행

### 요구사항
- Android Studio Arctic Fox 이상
- Kotlin 1.9.0
- Android Gradle Plugin 8.1.1
- Min SDK: 23 (Android 6.0)
- Target SDK: 35

### 빌드 명령
```bash
# 클린 빌드
./gradlew clean build

# 디버그 APK 생성
./gradlew assembleDebug

# Lint 검사 건너뛰고 빌드
./gradlew assembleDebug -x lint

# 테스트 실행
./gradlew test
```

### 일반적인 빌드 오류 해결
1. **SDK 경로 오류**: `settings.gradle.kts`에서 SDK 경로 확인
2. **Lint 오류**: `android:tint` → `app:tint` 변경 필요

## 배포 관련 (JitPack)

### JitPack을 통한 SDK 배포 프로세스

#### 1. 배포 준비
```bash
# SDK 프로젝트로 이동
cd ../adchain-sdk-android

# 버전 태그 생성
git tag -a v1.0.0 -m "Release version 1.0.0"
```

#### 2. GitHub 저장소 Public 전환
**⚠️ 중요: JitPack 빌드를 위해 일시적으로 Public 필요**

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
dependencies {
    // JitPack 의존성 (배포 후)
    implementation("com.github.[username]:adchain-sdk-android:v1.0.0")
    
    // 또는 로컬 모듈 참조 (개발 중)
    // implementation(project(":adchain-sdk"))
}
```

#### 5. Private 저장소로 복구
**✅ 배포 완료 후 즉시 Private으로 전환**

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
- [ ] SDK 초기화 정상 동작
- [ ] 사용자 로그인/로그아웃
- [ ] 네트워크 통신 정상 동작

### 기능별 테스트
- [ ] 퀴즈 목록 로딩
- [ ] 퀴즈 참여 및 완료
- [ ] 미션 목록 표시
- [ ] 미션 진행 상태 업데이트
- [ ] 오퍼월 표시 및 상호작용

### 빌드 및 배포
- [ ] 로컬 빌드 성공
- [ ] Lint 검사 통과
- [ ] APK 생성 및 설치
- [ ] ProGuard/R8 규칙 적용

## 주의사항

### 보안
- API 키와 시크릿은 절대 코드에 하드코딩하지 않음
- `.gitignore`에 `local.properties` 포함 확인
- 민감한 정보는 환경 변수 사용

### 코드 스타일
- Kotlin 코딩 컨벤션 준수
- 불필요한 주석 제거
- 의미 있는 변수명 사용

### Git 관리
- 배포 전 반드시 Public 전환
- 배포 후 즉시 Private 복구
- 태그는 의미 있는 버전 번호 사용 (Semantic Versioning)

## 문제 해결

### 빌드 실패
```bash
# Gradle 캐시 클리어
./gradlew clean
rm -rf ~/.gradle/caches/

# 의존성 새로고침
./gradlew --refresh-dependencies
```

### SDK 모듈을 찾을 수 없음
`settings.gradle.kts` 파일 확인:
```kotlin
project(":adchain-sdk").projectDir = file("../adchain-sdk-android/adchain-sdk")
```

## 연락처
- 기술 지원: [이메일 또는 이슈 트래커]
- 문서: [문서 링크]

---
*최종 업데이트: 2025-01-08*