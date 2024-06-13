## V. 1.14
제품에 신상 및 추천 상태 추가(new, best)

## V 1.13
위시 객체 생성 <br />
lombok 사용 삭제

## V 1.12
주문 정보 유저와 연결 <br />
주문 개수 변경하는 로직 작성

## V 1.11
회원 정보 추가 <br />
리뷰와 회원정보 연결

## V 1.10
상품 사진들 추가

## V 1.09
로그인 기능 추가 및 토큰 추가

## V 1.08
사진 폴더 추가

## V 1.07
사진 추가 가능<br />
POST http://localhost:8080/products/{id}/uploadImage <br />
Body: form-data / Key: image (Type: File) / Value: 업로드할 이미지 파일 선택

## V 1.06
로그인 기능 추가 <br />
아직 보안이 제대로 진행되지않아 수정요망

## V 1.05
제품 리뷰 기능 추가 <br />
userId, products.id, content, rating

## V 1.04
제품 주문 시 주문 내역 정보 추가 <br />
userId, products.id <br />
주문 내역 수정은 PUT http://localhost:8080/orders/{id}/status?status=COMPLETED

## V 1.03
제품 기본적인 정보 추가, 삭제, 수정, 검색 기능 <br />
name, description, price, category, stock

## V 1.02
프로젝트 진행을 위해 폴더 구조 및 파일 생성

## V 1.01
start.spring.io를 이용해 프로젝트 제작
