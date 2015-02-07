DbUnitXmlCreator
================

DBUnit을 통해 통합테스트코드를 만들 때 사용하는 XML파일을 자동으로 생성해주는 Groovy로 만든 프로그램입니다.


- - -


아래 코드를 변형하여 다른 DBMS에서도 응용하여 사용할 수 있습니다.

* 테이블 이름을 넣고 널이 아닌 컬럼을 체크하는 SQL 구문

```
SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME=?  AND IS_NULLABLE=\'NO\' ORDER BY ORDINAL_POSITION
```

 * 테이블에서 한개의 ROW를 가져오는 SQL 구문

```
SELECT * FROM '+tableName+' LIMIT 1
```

해당 테이블의 XML파일을 만들기 위해서는 반드시 해당 테이블에 데이터가 1개 이상 있어야 합니다.


- - -


**Groovy 실행 구문 : groovy MysqlXmlCreator.groovy test(테이블이름)**
