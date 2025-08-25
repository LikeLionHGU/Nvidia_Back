### 공실 추천/예약 플랫폼

대학생과 공실 소유자를 연결하는 AI 기반 공실 추천/예약 서비스입니다.
사용자는 원하는 시간과 특징(분위기/용도 등)을 자연어로 입력하면, LLM이 자동으로 공실의 “칩(chip)”을 매칭하여 적합한 공간을 추천합니다.
공실 소유자는 대여 가능한 시간대를 간편하게 등록·관리할 수 있습니다.
---
### 핵심 기능

- 공실 추천

사용자가 입력한 자연어 요구사항을 LLM(Gemini)으로 분석 → 사전에 정의된 칩(canonical chips)으로 정규화 → 칩이 일치하는 공실을 추천(일치 개수 기준 정렬).

- 예약

날짜/시간 슬롯을 기반으로 원하는 시간대 예약.

- 호스트(공실 소유자) 기능

주소/연락처/계좌/가격/최대 인원 등 기본 정보 및 사진/옵션/칩 리스트 등록.

시간표(스케줄) 기반 대여 가능 시간 관리.

- 운영/배포

Spring Boot 백엔드, MySQL, S3(이미지), Nginx 리버스 프록시, Certbot(HTTPS), Vercel 프런트엔드(구성 가능).

### 아키텍처 개요

- Backend: Spring Boot 3.x (Java 17+), Web/MVC + Spring Data JPA

- DB: MySQL 8.x

- Storage: AWS S3 (포스터/사진)

- Infra: AWS EC2 + Nginx(+Certbot) Reverse Proxy

- LLM: Google Gemini API (1.5 Flash / 2.5 Pro 중 선택)

### 도메인 모델

Address
id, roadName, latitude, longitude
Room과 1:1

Room
id, enName, enPhoneNumber, address(1:1), account, maxPeople, price, memo
optionList / chipList / photoList (ElementCollection)
reservations(1:N), schedules(1:N)

Reservation
id, name, phoneNumber, date(LocalDate), slotIndex(Set<Integer>), room(ManyToOne)

Chip (Enum)
활기찬, 따뜻한, 포근한, 여유로운, 레트로, 영감을_주는, 모던한, 로맨틱, 세련된, 컬러풀한, 깔끔한, 심플, 감성적인, 회의, 스터디, 촬영, 상담, 연습, 시험, 조용한, 넓은, 이벤트, 럭셔리, 유니크, 창의적인, 빈티지

참고: enum 표기(영감을_주는)는 언더스코어 포함 형태로 통일합니다.

### AI 추천(칩 매칭) 설계

- Prompt 규칙(요약)

사용자의 입력을 허용 칩 목록 중 정확한 canonical 표기로 매핑

동의어/오탈자 보정, 부정어(“아닌/제외/빼고/not”) 제외

JSON 스키마: {"chips":[{"chip":"모던한"}]}

일치 칩이 없으면 {"chips":[]}

- 모델 설정

gemini-1.5-flash-latest (저지연/저비용)