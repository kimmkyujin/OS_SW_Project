# 1. Conceptualization
Project Title: 자취생 요리 도우미

Logo: 든든(Deun Deun) 

김규진 22212060 kimmkyujin1208@naver.com

---


## [Recision history]
| Revision date | Version | Description | Author |
|---|---|---|---|
|     26.03.20        |  0.1.0       |   Conceptualization       |  kyujin      |
||||
||||

---

## Content
1. Business purpose
2. System context diagram
3. Use case list
4. Concept of operation
5. Problem statement
6. Glossary
7. References
---

### 1. Business purpose
1) Project background
- 통계청이 발표한 '2023 통계로 보는 1인 가구' 자료에 따르면, 대한민국 1인 가구 비중은 전체 가구의 35.5%를 기록하며 역대 최고치인 782만 9천 가구에 도달했습니다. 1인 가구는 이제 우리사회의 큰 부분이 되었는데 시중의 요리 콘텐츠 혹은 레시피들은 아직도 다인 가구의 완벽한 주방환경을 기준으로 제공되고 있습니다.
저는 직접 자취를 하며 1인 가구의 현실적인 한계를 체감했습니다.
좁은 주방에 모든 조미료와 조리 기구를 갖추기란 불가능에 가까우며 레시피에 나온 재료가 없어서 요리를 포기하거나, 1인분 조리 시 간 조절에 실패해 아까운 식재료를 버리게 되는 경험은 저뿐만 아니라 대부분의 자취 초보들이 겪는 문제점입니다.
본 프로젝트는 이러한 사회적 흐름에 발맞추어, 완벽한 환경을 강요하는 기존 레시피 앱의 틀을 깨고, 단순히 레시피만 알려주는게 아닌 사용자의 현 상황에 맞추어 농동적인 도움으로 요리의 완성을 돕는 '자취생 전용 보조 요리사' 서비스를 구현하고자 합니다.

---
### 2. System context diagram
<img width="741" height="258" alt="image" src="https://github.com/user-attachments/assets/1e367acd-67d6-4ae2-8229-26d97341dc9e" />


- login: 사용자 로그인 요청
- search: 레시피 검색
- select: 레시피에서 옵션 선택
- send inquiry: 문의사항
- ask for tips: 재료보관 팁
- recipe info: 레시피 제공
- select option: 옵션 제공
- storage guide: 재료보관 방법 제공
- edit recipe: 옵션에 맞춘 레시피 제공
- send inquiry: 사용자 문의사항 제공
- add recipe: 추가된 레시피 제공
- solve: 문의사항 답변

---

### 3.Use case list
1) Login

|Actor | User |
|---|---|
| Description | 사용자가 자신의 아이디로 로그인을 시도함.  |
2) Search

|Actor | User |
|---|---|
| Description | 사용자가 자신이 만들고 싶은 요리의 레시피를 검색함. |
3) Select

|Actor | User |
|---|---|
| Description | 레시피 화면에서 인원, 요리도구, 대체 재료를 선택한다.|
4) Send inquiry

|Actor | User |
|---|---|
| Description | 레시피 오류 혹은 소프트웨어 사용시 문의사항을 보낸다.  |
5) Ask for tips

|Actor | User |
|---|---|
| Description | 남은 식재료 보관 방법을 버튼을 누른다.|
6) Recipe info

|Actor | User, System |
|---|---|
| Description | 사용자가 검색한 요리의 가장 기본적인 버전의 레시프를 보여준다.  |
7) Select option

|Actor | System |
|---|---|
| Description | 레시피 화면에서 인원, 요리도구, 대체 재료의 선택지를 제공한다.  |
8) Storage guide

|Actor | User, System |
|---|---|
| Description | 사용자가 보관벙법 버튼을 누르면 식재료들의 보관방법이 나온다. |
9) Edit recipe

|Actor | User, System |
|---|---|
| Description | 사용자가 고른 옵션에 맞추어 레시피를 일부 수정한다.  |
10) Send inquiry

|Actor | User, System, Administrator |
|---|---|
| Description | 사용자가 보내온 문의사항을 관리자에게 보낸다.  |
11) Add recipe

|Actor | Aministrator, System |
|---|---|
| Description |  관리자가 해당 시스템에 새로운 레시피를 추가해준다. |
12) Solve

|Actor | Administrator, System |
|---|---|
| Description | 관리자가 문의사항에 대한 답변 혹은 버그에 대한 해결을 해준다.  |




  
