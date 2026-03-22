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
| Description | 사용자가 제공된 레시피에서 몇인분, 요리도구, 대체 식재료를 선택하고 이에 맞추어 레시프를 변경한다..  |

5) Send inquiry

|Actor | User, Administrator |
|---|---|
| Description | 사용자가 시스템에 문의사항을 남기면 시스템 관리자에게 전송한다.  |

6) Storage & Care guide

|Actor | User |
|---|---|
| Description | 사용자가 보관방법 버튼을 누르면 재료, 요리도구 보관법에 대한 정보를 제공한다.  |

7) Customer support

|Actor | User, Administrator |
|---|---|
| Description | 관리작 문의사항을 해결 또는 답변하여 시스템을 통해 사용자에게 보낸다.  |


8) Recipe update

|Actor | Administrator |
|---|---|
| Description | 관리자가 서버에 새로운 레시피를 추가한다.   |

9) User management

|Actor | Administrator |
|---|---|
| Description | 관리자가 서버에 있는 사용자 정보를 관리한다.| 

---




### 4.Concept of operation

1. Register

|Purpose|신규 사용자를 서버에 등록하기위함. |
|---|---|
|Approach|신규 사용자 일시 앱에 아이디, 비밀번호, 이메일 등을 입력하게 하여 중복여부를 확인하고, 중복이 아니라면 새로운 계정 데이터를 서버에 생성함.|
|Dynamics|앱을 처음 설치한 유저가 회원가입 버튼을 누르고 회원정보를 입력한 경우.|
|Goals|신규 사용자 등록 시스템을 구현한다.|

2. Login

|Purpose|현재 앱을 이용할려는 사람이 인증된 사용자인지 확인하기 위함. |
|---|---|
|Approach|사용자가 앱을 실행하여 아이디와 비번을 입력하면 시스템이 서버에 해당 정보의 검증을 요청하고 유저목록에 해당 사용자가 있다면 앱을 사용할 권한을 주고 없다면 다시 로그인 하게 함.|
|Dynamics|사용자가 어플을 실행하여 로그인한 경우.|
|Goals|로그인 시스템을 구현한다.|

3. Recipe search

|Purpose| 사용자가 원하는 요리의 레시피를 찾게하기 위함.|
|---|---|
|Approach|사용자가 화면에 있는 검색창에 원하는 요리의 이름을 검색하면 시스템에서 해당 요리의 레시피를 서버에 요청하고 데이터베이스에 레시피가 있다면 서바가 그 정보를 시스템에 전달하여 유저에게 보여준다. 해당 레시피에는 인원수, 요리도구, 재료의 정보가 있으며 각 도구와 재료에는 중요도가 표시되어 있다. 또한 레시피 전송시에 옵션에 따른 변경된 레시피와 보관방법까지 같이 보내게 된다.|
|Dynamics| 사용자가 요리를 검색한 경우. |
|Goals|사용자가 검색한 요리의 레시피가 화면에 보이도록 한다.|

4. Select custom option

|Purpose| 사용자에게 맞춤형 레시피를 제공.|
|---|---|
|Approach| 레시피 화면에 들어간 사용자가 화면 상단에 있는 인원, 요리도구, 재료 탭의 각 항목의 터치하여 인원수 및 대체도구와 대체재료를 선택하면 시스템에서 해당 옵션에 맞게 레시피를 변경시킨다.|
|Dynamics| 사용자가 레시피의 옵션항목에서 특정 옵션을 선택한 경우.|
|Goals| 사용자가 선택한 옵션에 맞게 레시피가 변경되도록 한다.|

5. Send inquiry

|Purpose| 유저의 문의사항을 관리자가 알 수 있게 한다. |
|---|---|
|Approach| 유저가 메인화면의 문의사항 버튼을 누르게 되면 문의사항을 유형 선택칸과 메세지를 보낼 수 있는 칸이 뜨게 된다. 그곳에 문의사항 유형을 고르고 메세지를 적은 다음 전송 버튼을 누르면 시스템이 해당 내용을 관리자가에게 전송하게 된다. |
|Dynamics| 사용자가 문의사항 탭에 들어가 메세지를 전송한 경우.|
|Goals| 유저의 문의사항을 관리자가 알 수 있게 한다. |

6. Storage & Care gudie

|Purpose| 이용자에게 식재료 및 요리도구의 보관법을 알려준다. |
|---|---|
|Approach| 유저가 해당 레시피의 하단에 있는 보관방법이라는 버튼을 누르게 되면 해당 레시피에서 사용된 식재료 보관방법과 요리도구의 보관 및 관리법이 나오게 된다. |
|Dynamics| 사용자가 보관방법 버튼을 누른 경우. |
|Goals| 사용자에게 식재료와 요리도구의 보관 및 관리방법을 알려주기 위함. |

7. Customer support

|Purpose| 관리자가 유저의 문의사항에 답하고 시스템의 기술적 결함을 해결한다. |
|---|---|
|Approach| 관리자가 시스템에 접수된 문의사항을 열람하여 이에 답변하고 필요한 경우 서버의 데이터를 수정하거나 버그를 패치함. |
|Dynamics| 관리자가 등록된 문의사항을 확인하고 이에 조치를 취하는 경우. |
|Goals| 사용자의 앱 만족도를 높이고 시스템을 정상적으로 운영하기 위함. |

8. Recipe update

|Purpose| 다양한 레시피를 추가하고 레시피 데이터를 관리한다. |
|---|---|
|Approach| 관리자가 서버에 새로운 레시피를 추가하고 필요시 기존 레시피를 수정함. |
|Dynamics| 신규 레시피를 추가하거나 기존 레시피의 보완이 필요한 경우.  |
|Goals| 레시피의 정확성을 높이고 다양한 레시피를 제공한다. |

9. User management

|Purpose| 등록된 사용자들의 정보를 체계적으로 관리 및 유지하기 위함. |
|---|---|
|Approach| 관리자가 서버에 접속하여 등록된 유저목록을 열람하며 계정을 휴면처리 하거나 불량 이용자를 제재하는 등 특정계정의 정보 수정 및 권한을 변경한다. |
|Dynamics|  관리자가 유저목록에 대해 관리가 필요한 경우. |
|Goals| 유저목록의 안전성을 유지하고 원활한 서비스 환경을 조성한다. |
