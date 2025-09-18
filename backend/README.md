# Backend for trenings app

## Kjøre kode:
Sett opp .env fil i root:

## Prosjekt struktur

## Eksterne Bibliotek
[godotenv](https://github.com/joho/godotenv)
- setter miljø variabler

[Mongodb](https://mongodb.com)
- database

## Endpoints

- /api/v1/users
- /api/v1/users/{id}

- /api/v1/exercises
- /api/v1/exercises/{id}, GET

## Run
Docker:
```bash
docker build -t backend .
docker run -p 8000:8000 --env-file .env backend
```
