# Frontend

## Krav

- Android Studio
- JDK 17+
- Android SDK 34
- Backend kjørende (se `../backend/README.md`)

## Oppsett

1. Konfigurer backend-tilkobling i `app/src/main/java/com/example/workoutapp/di/NetworkModule.kt`.
   Endre `server` variabelen til:
   - Lokal backend på emulator: `"10.0.2.2:8080"`
   - Lokal backend på fysisk enhet: `"<maskin-IP>:8080"` (samme nettverk)
   - Deployed backend: Oppdater til produksjons-IP/domene

2. Sync Gradle og bygg

## Kjøring

Åpne i Android Studio og kjør på emulator eller fysisk enhet. Appen er ikke publisert til Play Store.

## Arkitektur

- `features/` - UI-skjermer (Composables + ViewModels)
- `domain/` - Domenemodeller, repository-grensesnitt, use cases
- `data/` - Repository-implementasjoner (Room DB + Ktor API)
- `di/` - Hilt dependency injection
- `core/` - Delte UI-komponenter, navigasjon, tema

**Dataflyt**: 
Room DB er sannhetskilde og appen fungerer uten nettverkstilkobling
Synkroniserer med backend API når tilkoblet internett.

**Nøkkelkomponenter**:
- Room for lokal lagring
- Ktor for HTTP-klient
- Hilt for Dependency Injection
- Jetpack Compose for UI elementer

## Testing

Enhetstester bruker mock repositories. Instrumenterte tester kjører mot ekte Room DB.

```bash
# Spesifikk testklasse
./gradlew test --tests "com.example.workoutapp.domain.usecases.HistoryWorkoutUseCaseTest"
```
