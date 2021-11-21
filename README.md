# 사내 도서관 API 서버

## Commands

```bash
# DB만 실행
docker-compose -f ./scripts/docker-compose-local-db.yaml up -d
# DB 중단
docker-compose -f ./scripts/docker-compose-local-db.yaml down

# 실행
docker-compose -f ./scripts/docker-compose.yaml up -d
# 중단
docker-compose -f ./scripts/docker-compose.yaml down
# 실행 전 이미지 빌드
docker-compose -f ./scripts/docker-compose.yaml up --build
```
