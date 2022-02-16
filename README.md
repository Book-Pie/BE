# Book Pie
<p align=center>
<img src=https://user-images.githubusercontent.com/81568304/154110748-4e849c5d-d5c3-4464-b51c-844a969bc310.png width=80% height=280 />
</p>
<p align=center>
:calendar: 2021.12.13 ~ 2022.02.13
</p>


**북파이는 책을 중심으로 한 중고 거래 플랫폼 및 커뮤니티로
아래와 같은 기능들을 제공하고 있습니다.**

:books: **중고 거래**
판매자는 책을 자유롭게 판매할 수 있고, 구매자는 직거래 또는 포인트 결제로 원하는 중고 도서를 구매할 수 있습니다.
중고 거래의 가장 중요한 부분은 판매자와 구매자 간의 신뢰입니다. 북파이는 사용자 간 신뢰를 위해 판매자 평점을 작성하게 하여 사용자들이 신뢰를 가지고 거래할 수 있도록 하였습니다.
또한 북파이는 판매자와 구매자가 소통을 원활하게 할 수 있도록 댓글 기능과 1:1 채팅 기능을 구현하였습니다.

:mag_right: **리뷰 & 도서 정보 조회**
북파이는 구매자가 책을 구매하기 전 책의 정보와 리뷰, 평점을 볼 수 있도록 알라딘 api로 부터 책 정보를 제공합니다. 이는 구매자가 더 만족할 수 있는 구매를 할 수 있게 도와줍니다.
책 정보 조회 시 해당 책에 대한 중고 도서가 존재하면 페이지 하단에 정보를 제공하여 원하는 책을 중고 장터에서 바로 찾을 수 있도록 해줍니다.
또한 빅데이터를 활용하여 해당 책과 관련된 추천 책 리스트를 보여주어 사용자가 사이트 이용에 더욱 흥미를 가질 수 있도록 해줍니다.

:two_men_holding_hands: **커뮤니티**
북파이는 게시판을 통해 책을 중심으로 한 커뮤니티 기능도 제공하고 있습니다.
자유게시판은 자유롭게 글을 작성할 수 있고, 책 구합니다 게시판은 사용자가 구입하고 싶은 책을 구하는 용도로 사용할 수 있습니다.

## View

**Front-End 링크** : https://github.com/Book-Pie/FE

**북파이 사이트** : http://www.react-dev.p-e.kr/

## 프로젝트 구조 
  ![image](https://user-images.githubusercontent.com/18027740/154010180-82b63874-a459-4102-8ef0-369114ea59bb.png)


 ### DB 구조
 
![image](https://user-images.githubusercontent.com/18027740/154010299-c646da0b-b505-4db9-aa13-1d259cb24fd6.png)




 

 
 
 ### **패키지 구조**
 
 ├─config        -- Spring 설정 관련 클래스  
 ├                 
 ├─controller  
 ├  
 ├─domain         -- Entity 관련 클래스  
 ├ ├  
 ├ ├─dto         --  Data Transfer Object 클래스  
 ├ └─enums       --  Enumerate 클래스  
 ├  
 ├─oauth          -- 카카오,네이버 로그인 등 OAuth 2.0 관련 클래스 
 ├  
 ├─repository     
 ├  
 ├─service  
 ├  
 └─utils          -- 파일, Api 반환을 위한 wrapper 등 Util 클래스
 
 



## 기술 스택 
 - JAVA
 - Spring Boot
 - JPA
 - Query DSL
 - MySQL
 - Jenkins
 - Redis
 


## contributor 

|이민욱|조영동|김준영|안민우|  
| :---: | :---: | :---: | :---: |  
|<img src="https://github.com/dnr14.png" width=200 height=200/>|<img src=https://github.com/choyd93.png width=200 height=200/>|<img src=https://github.com/kky0426.png width=200 height=200/>|<img src=https://github.com/start-27.png width=200 height=200/>|
|[dnr14](https://github.com/dnr14)|[choyd93](https://github.com/choyd93)|[kky0426](https://github.com/kky0426)|[start-27](github.com/start-27)|
|Front-End|Front-End|Back-End|Back-End|


