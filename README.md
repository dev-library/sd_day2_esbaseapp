# Elasticsearch 기반 상품 검색 성능 체험 애플리케이션

이 애플리케이션은 Elasticsearch에 대용량 상품 데이터를 자동으로 생성하고, 이를 기반으로 검색 성능을 체험할 수 있도록 제작되었습니다.

---

## 🚀 실행 방법

1. Elasticsearch 클러스터가 **9200번 포트**에서 실행되고 있어야 합니다.  
2. 애플리케이션을 실행하면 자동으로 더미 상품 데이터를 Elasticsearch에 입력합니다.  
3. 웹 페이지 또는 API를 통해 아래와 같은 옵션으로 검색을 수행할 수 있습니다.
4. 검색 결과와 함께 조회된 건수 및 소요 시간(ms) 이 함께 출력됩니다.
5. localhost:8080 엔드포인트 접속시 바로 확인 가능합니다.
---

## ✅ 생성되는 상품 예시

- **상품명 (Product Name)**:
  - Laptop, Phone, Tablet, Shoes, Jacket, Cookware, Headphones, Monitor, Chair, Desk

- **브랜드 (Brand)**:
  - BrandA, BrandB, BrandC, BrandD, BrandE

- **카테고리 (Category)**:
  - Electronics, Fashion, Kitchen, Office, Furniture

- **가격 (Price)**:
  - 10,000원 ~ 1,000,000원

- **별점 (Rating)**:
  - 1.0 ~ 5.0 (소수점 첫째 자리까지)

---

## 🔍 검색 옵션

다음 옵션들을 조합하여 상품 검색이 가능합니다:

| 옵션         | 설명                                                   | 타입       | 예시                          |
|--------------|--------------------------------------------------------|------------|-------------------------------|
| `name`       | 상품명에 포함된 키워드 검색 (부분 일치)               | 문자열     | `Laptop Model 635`, `Cook Model 291`             |
| `brand`      | 브랜드명 필터링                                        | 문자열     | `BrandC`                     |
| `minPrice`   | 최소 가격 (이상)                                       | 정수       | `50000`                      |
| `maxPrice`   | 최대 가격 (이하)                                       | 정수       | `300000`                     |
| `minRating`  | 최소 별점 (이상)                                       | 실수       | `3.5`                        |
| `maxRating`  | 최대 별점 (이하)                                       | 실수       | `4.8`                        |

---

## 📈 성능 체험 포인트

- Elasticsearch 인덱스를 기반으로 다양한 필터 조건 검색에 따른 성능을 측정할 수 있습니다.
- 결과 건수와 함께 **검색 소요 시간(ms)** 이 실시간으로 표시됩니다.
- Full-text 검색, 집계 기반 정렬 등을 테스트할 수 있습니다.

---

## ⚙️ 더미 데이터 개수 조정 방법

더미 데이터의 개수는 `DummyDataLoader` 클래스의 **25번 라인**에 선언된 `DATA_SIZE` 상수 값으로 조정할 수 있습니다:

```java
private static final int DATA_SIZE = 300_000;
```
## 💡 참고
- Elasticsearch에 입력된 데이터는 한 번 생성되면 유지됩니다.
- 기존 인덱스를 삭제하거나, 현재 인덱스 내 도큐먼트 수가 DATA_SIZE보다 적을 때만 재생성됩니다.
