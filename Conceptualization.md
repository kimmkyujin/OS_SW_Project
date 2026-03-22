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
<img width="400" height="491" alt="image" src="https://github.com/user-attachments/assets/4d6303c0-4419-43bc-9e16-28cd94d5ee33" />

 좁은 주방에 모든 조미료와 조리 기구를 갖추기란 불가능에 가까우며 레시피에 나온 재료가 없어서 요리를 포기하거나, 1인분 조리 시 간 조절에 실패해 아까운 식재료를 버리게 되는 경험은 저뿐만 아니라 대부분의 자취 초보들이 겪는 문제점입니다.
  
 본 프로젝트는 이러한 사회적 흐름에 발맞추어, 완벽한 환경을 강요하는 기존 레시피 앱의 틀을 깨고, 단순히 레시피만 알려주는게 아닌 사용자의 현 상황에 맞추어 농동적인 도움으로 요리의 완성을 돕는 '자취생 전용 보조 요리사' 서비스를 구현하고자 합니다.

---
### 2. System context diagram




- Register: 회원가입
- Login: 사용자 로그인 
- Recipe search: 레시피 검색 
- Select custom option: 레시피에서 옵션 선택
- Send inquiry: 문의사항 전송
- Storage & Care guide: 재료, 도구보관법 제공
- Adapted recipe: 옵션에 맞춘 레시피 제공
- Customer support: 문의사항 해결 및 답변
- Recipe update: 레시피 추가
- User management: 유저 목록 관리

---

### 3.Use case list

1) Register

|Actor | User |
|---|---|
| Description | 사용자가 시스템에 회원가입을 시도한다.  |

2) Login

|Actor | User |
|---|---|
| Description | 사용자가 시스템에 로그인을 시도한다.   |

3) Recipe search

|Actor | User |
|---|---|
| Description | 사용자가 시스템에 레시피를 검색하여 원하는 레시피를 찾는다.  |

4) Select custom option

|Actor | User |
|---|---|
| Description | 사용자가 제공된 레시피에서 몇인분, 요리도구, 대체 식재료를 선택한다.  |

5) Send inquiry

|Actor | User, Administrator |
|---|---|
| Description | 사용자가 시스템에 문의사항을 남기면 시스템 관리자에게 전송한다.  |

6) Storage & Care guide

|Actor | User |
|---|---|
| Description | 사용자가 보관방법 버튼을 누르면 재료, 요리도구 보관법에 대한 정보를 제공한다.  |

7) Adapted recipe

|Actor | User |
|---|---|
| Description |  사용자가 선택한 옵션에 맞추어 레시피를 수정하여 보여준다. |

8) Customer support

|Actor | User, Administrator |
|---|---|
| Description | 관리작 문의사항을 해결 또는 답변하여 시스템을 통해 사용자에게 보낸다.  |


9) Recipe update

|Actor | Administrator |
|---|---|
| Description | 관리자가 서버에 새로운 레시피를 추가한다.   |

10) User management

|Actor | Administrator |
|---|---|
| Description | 관리자가 서버에 있는 사용자 정보를 관리한다.| 

---




### 4.Concept of operation

1. Register

|Purpose|사용자가 앱|
|---|---|
|Approach||
|Dynamics||
|Goals||

- Login: 사용자 로그인 
- Recipe search: 레시피 검색
- Select custom option: 레시피에서 옵션 선택
- Send inquiry: 문의사항 전송
- Storage & Care guide: 재료, 도구보관법 제공
- Provide recipe: 레시피 제공
- Adapted recipe: 옵션에 맞춘 레시피 제공
- Customer support: 문의사항 해결 및 답변
- Create account: 계정 생성
- Request recipe: 레시피 요청
- Recipe update: 레시피 추가
- User management: 유저 목록 관리
