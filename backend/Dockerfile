FROM golang:1.25.1 AS builder

WORKDIR /app

COPY go.mod go.sum ./
RUN go mod download

COPY . .

RUN CGO_ENABLED=0 GOOS=linux go build -o server ./cmd/api
FROM alpine:latest
WORKDIR /app
COPY --from=builder /app/server .
COPY .env .env
EXPOSE 8080

CMD ["./server"]
