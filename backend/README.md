# Backend for trenings app

## Domene
```go
type Exercises struct {
	Id               string   `bson:"exerciseId" json:"exerciseId"`
	Name             string   `bson:"name" json:"name"`
	TargetMuscles    []string `bson:"targetMuscles" json:"targetMuscles"`
	BodyParts        []string `bson:"bodyParts" json:"bodyParts"`
	Equipments       []string `bson:"equipments" json:"equipments"`
	SecondaryMuscles []string `bson:"secondaryMuscles" json:"secondaryMuscles"`
	GifUrl           string   `bson:"gifUrl" json:"gifUrl"`
	Instructions     []string `bson:"instructions" json:"instructions"`
}

type Template struct {
	TemplateId string             `bson:"templateId" json:"templateId"`
	Name       string             `bson:"name" json:"name"`
	Exercises  []ExerciseTemplate `bson:"exercises" json:"exercises"`
}
```


## API Endpoints
- /api/v1/exercises
- /api/v1/exercises/{id}
- /api/vi/templates
- /api/vi/templates?include=exercises
- /api/vi/templates/{id}
- /api/vi/templates/{id}?include=exercises
---
### `GET /exercises`
Henter alle øvelser
Response:
```go
[
	{
	  "exerciseId": "IpONWYv",
	  "name": "dumbbell side bend",
	  "targetMuscles": [
	    "abs"
	  ],
	  "bodyParts": [
	    "waist"
	  ],
	  "equipments": [
	    "dumbbell"
	  ],
	  "secondaryMuscles": [
	    "obliques"
	  ],
	  "gifUrl": "https://yourdomain.com/IpONWYv.gif",
	  "instructions": [
	    "Step:1 Stand up straight with your feet shoulder-width apart and hold a dumbbell in one hand, letting it hang down by your side.",
	    "Step:2 Keeping your back straight and your core engaged, slowly bend sideways at the waist towards the opposite side of the dumbbell, lowering the weight as far as you comfortably can.",
	    "Step:3 Pause for a moment, then slowly return to the starting position.",
	    "Step:4 Repeat for the desired number of repetitions, then switch sides and repeat."
	  ]
	}
]
```

### `GET /exercises/{id}`
Henter en øvelse basert på `exerciseId`
Response:
```go
{
  "exerciseId": "IpONWYv",
  "name": "dumbbell side bend",
  "targetMuscles": [
    "abs"
  ],
  "bodyParts": [
    "waist"
  ],
  "equipments": [
    "dumbbell"
  ],
  "secondaryMuscles": [
    "obliques"
  ],
  "gifUrl": "https://yourdomain.com/IpONWYv.gif",
  "instructions": [
    "Step:1 Stand up straight with your feet shoulder-width apart and hold a dumbbell in one hand, letting it hang down by your side.",
    "Step:2 Keeping your back straight and your core engaged, slowly bend sideways at the waist towards the opposite side of the dumbbell, lowering the weight as far as you comfortably can.",
    "Step:3 Pause for a moment, then slowly return to the starting position.",
    "Step:4 Repeat for the desired number of repetitions, then switch sides and repeat."
  ]
}

```

### `POST /templates`
Lager en ny mal
```go
{
  "templateId": "123",
  "name": "Upper Body Day",
  "exercises": [
    {
      "exerciseId": "hah128",
      "set": [
        { "rep": 10, "kg": 0, "TypeSet": 2 }
      ]
    }
  ]
}
```
Response:
```go
{
  "id": "123",
  "message": "template created successfully"
}
```

### `GET /templates/{id}`
Henter en mal basert på id
Response:
```go
{
  "templateId": "tmp_001",
  "name": "upper body",
  "exercises": [
    {
      "exerciseId": "JrOHAZc",
      "set": [
        {
          "rep": 12,
          "kg": 40,
          "TypeSet": 2
        },
        {
          "rep": 10,
          "kg": 50,
          "TypeSet": 0
        },
        {
          "rep": 8,
          "kg": 55,
          "TypeSet": 1
        }
      ]
    },
    {
      "exerciseId": "7F1DVzn",
      "set": [
        {
          "rep": 15,
          "kg": 20,
          "TypeSet": 2
        },
        {
          "rep": 12,
          "kg": 25,
          "TypeSet": 0
        },
        {
          "rep": 10,
          "kg": 30,
          "TypeSet": 1
        }
      ]
    },
    {
      "exerciseId": "gAwDzB3",
      "set": [
        {
          "rep": 10,
          "kg": 60,
          "TypeSet": 2
        },
        {
          "rep": 8,
          "kg": 70,
          "TypeSet": 0
        },
        {
          "rep": 6,
          "kg": 80,
          "TypeSet": 1
        }
      ]
    }
  ]
}
```


### `GET /templates/{id}?include=exercises`
Samme som `GET /templates/{id}` bare `exerciseId` er byttet ut med all informasjon om en øvelse


## Kjøre kode:
Sett opp .env fil i root:

## Run
Docker:
```bash
docker build -t backend .
docker run -p 8000:8000 --env-file .env backend
```

## Uthenting av server sertificat

### Klient sertifikat
```bash
mkcd certs
openssl genrsa -out ca.key 4096

openssl req -x509 -new -nodes \
  -key ca.key \
  -sha256 -days 3650 \
  -out ca.crt \
  -subj "/CN=<your-ca-name>" \
  -addext "basicConstraints=critical,CA:TRUE" \
  -addext "keyUsage=critical,keyCertSign,cRLSign"
```

### Server nøkkel 
```bash
openssl genrsa -out server.key 2048
```

### Server sertifikat
```bash
openssl req -new -key server.key -out server.csr \
  -subj "/CN=<your-ca-name" \
  -addext "subjectAltName=DNS:localhost,IP:127.0.0.1,IP:10.212.168.186" \
  -addext "keyUsage=digitalSignature,keyEncipherment" \
  -addext "extendedKeyUsage=serverAuth"
```

### Signer server sertifikat med klient sertifikat
```bash
openssl x509 -req \
  -in server.csr \
  -CA ca.crt -CAkey ca.key -CAcreateserial \
  -out server.crt \
  -days 365 -sha256 \
  -extfile <(printf "subjectAltName=DNS:localhost,IP:127.0.0.1,IP:10.212.168.186\nkeyUsage=digitalSignature,keyEncipherment\nextendedKeyUsage=serverAuth")
```

### Lager fullchain for klient og caddy
```bash
cat server.crt ca.crt > fullchain.crt
```

## Eksterne Bibliotek
[godotenv](https://github.com/joho/godotenv)
- setter miljø variabler

[Mongodb](https://mongodb.com)
- database

[Zap](https://github.com/uber-go/zap?tab=readme-ov-file#readme)
- For rask å strukturert tilbakemelding
