# timedeal-numble

## 1. 전체적인 시스템 구조도(아키텍쳐)  
![타임딜 drawio](https://user-images.githubusercontent.com/60431816/225598375-68fc9b2c-e963-4c0b-84f2-c2766f75e5e4.png)

## 2. API 목록  
https://documenter.getpostman.com/view/24793112/2s93Jxr1Nw

## 3. 와이어 프레임  
https://www.figma.com/file/aCwsHHK9mna4GGsYhySZMj/%ED%83%80%EC%9E%84%EB%94%9C-%EC%84%9C%EB%B2%84-%EA%B5%AC%EC%B6%95?node-id=0%3A1&t=i2gzl8wYdh0EvjKq-1

## 4. ERD
![numbleERD](https://user-images.githubusercontent.com/60431816/225566600-3dfac7dc-fb3e-45cd-aa63-e5fcd26aba82.jpg)

## 5. 기술적으로 고민한 부분들
### 5-1. service에서의 반환타입은 도메인 객체를 넘겨주는게 좋을까 DTO로 전달해주는것이 좋을까?  
service 에서 반환타입을 도메인 객체로 넘겨주면 service내의 메서드가 다른 메서드를 호출하여 사용할때 더 편리할것이다.  
따라서 유저로그인을 처리할때까지만해도 service에서 도메인객체를 반환하는 방식으로 진행하였다.  
그런데 다른 사람들이 구현한 여러 코드들에서 service계층에서 DTO로 바꿔 반환하는 코드를 보고 고민에 빠졌다.  
service에서 도메인 객체를 반환하는 경우는
반환할 필요가 없는 데이터까지 표현계층(컨트롤러)에 넘어가기도 하며  
controller가 여러 도메인 객체의 정보를 조합해서 DTO를 생성해야할 때 하나의 controller에 의존하는 service개수가 비대해진다는 점을 단점으로 볼수있다고한다.  
결국 논리적으로 맞는 소리같아서 service 계층에서 dto를 반환하도록 수정하였지만  
이렇게되면 또 너무 서로 종속적이게 되는건 아닐까 싶기도하여 아직까지 어떻게 반환하는것이 좋을지는 감이 잘 오지 않는다.  


### 5-2. 유효성 검사는 어느단에서 하는것이 맞을까  
controller, service, domain 모든 단에서 유효성 검사를 진행하면 그만큼 중복처리가 반복될텐데 성능상으로는 크게 문제가 없을것인가?
-> 유효성검사 단위의 기준이 모호한게 각각 시선이 다 다르기때문에 가급적이면 다 넣는게 좋다는 조언을 얻었다.


### 5-3. 동시성 처리 이슈
- 레이스 컨디션  
  공유 자원에 대해 여러 프로세스(스레드)가 동시에 접근을 시도할 때,
  타이밍이나 순서 등이 결과값에 영향을 줄 수 있는 상태

동시성을 처리하는 방법으로 DB Lock방식만을 생각했는데 팀원들과 이야기를 나눠보니 더 많은 방식이 있음을 알게 되었다.  
- Synchronized
- Pessimistic Lock
- Optimistic Lock
- Named Lock
- Redis
  - Lettuce
  - Redisson

#### 1) Synchronized
자바에서 Synchronized를 메소드에 명시해주면 현재 데이터를 사용하고 있는 스레드를 제외하고 나머지 스레드들은 데이터 접근을 막아 순차적으로 데이터에 접근할 수 있도록 해준다.  
하지만 이는 하나의 프로세스 안에서만 보장이 되기때문에 현 프로젝트에서는 문제가 없으나  
서버가 여러대인 다른 프로젝트를 진행할 때에는 문제가 될 가능성이 크다.  

#### 2) 비관적락 Pessimistic Lock  
실제 DB에 락을 거는 방식으로 동시성 문제가 발생할것이라고 예상 한 뒤 잠금을 건다.  
다른 트랜젝션에서는 락이 해제되기 전까지 데이터를 가져갈 수 없게되고 데드락이 걸릴 수 있기때문에 주의  
동시성이 떨어져 성능저하가 발생할 수 있다는 단점  
읽기가 많이 이루어지는 데이터베이스의 경우에는 손해가 더 크다.  

#### 3) 낙관적락 Optimistic Lock
실제 락을 사용하지 않고 Version을 이용하여 정합성을 맞추는 방법  
업데이트가 실패했을 경우 재시도 로직을 개발자가 직접 작성해야한다.  
충돌이 빈번하게 발생한다면 낙관락의 경우는 롤백처리를 해주어야하기 때문에 Pessimistic Lock을 이용하는 것이 좋다.  

#### 4) NamedLock
이름을 가진 락을 획득한 후 해지될때 까지 다른 세션은 이 락을 획득할 수 없다.  
비관락은 row 나 table 단위로 락을 걸지만, Named Lock은 metadata 단위로 락을 건다는 차이  
주로 분산락을 구현할 때 사용한다.  

#### 5-1) Redis Lettuce
구현이 간단하다.  
Spin Lock 방식(스레드가 Lock획득 반복 시도)이기 때문에 동시에 많은 스레드가 lock 획득 대기 상태라면 redis에 부하가 갈 수 있다.

#### 5-2) Redis Redisson
pub-sub 방식으로 구현이 되어있기 때문에 lettuce 와 비교했을 때 redis에 부하가 덜 간다.  
pub-sub 방식 : 채널을 하나 만들고, 락을 점유중인 스레드가 락을 해제했음을 채널을 통해 대기중인 스레드에게 알려주면 대기중인 스레드가 락 점유를 시도


## 6. 성능측정 및 개선 내용  
- 구매하기 API가 TPS가 얼마나 나오는지 체크 nGrinder
- 어플리케이션의 상태를 점검할 수 있는 APM 툴 도입
    - pinpoint/scouter
    - 성능 테스트 시 이를 활용하여 상태를 점검

