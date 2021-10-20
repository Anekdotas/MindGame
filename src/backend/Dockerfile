FROM golang:1.17-alpine AS build

WORKDIR /go/src/app
COPY go.mod go.sum ./
RUN go mod download

COPY . .
RUN CGO_ENABLED=0 go build -o /app.bin ./cmd/anekdotas/main.go

FROM alpine:latest

COPY --from=build /app.bin /app.bin

ENTRYPOINT ["/app.bin"]
