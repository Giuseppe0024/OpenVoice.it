# Open Voice

Open Voice è un'applicazione web di giornalismo online sviluppata con **Spring Boot**. Permette di pubblicare e consultare articoli di cronaca organizzati per categoria, con un flusso editoriale a più ruoli: chi scrive, chi revisiona e chi amministra la testata.

## Funzionalità

### Lettori
- Consultazione degli articoli pubblicati, ordinati per data
- Dettaglio dell'articolo con immagine
- Ricerca per parola chiave
- Navigazione per categoria

### Redattori (`ROLE_WRITER`)
- Creazione, modifica e cancellazione dei propri articoli
- Caricamento dell'immagine di copertina
- Dashboard personale

### Revisori (`ROLE_REVISOR`)
- Dashboard con gli articoli in attesa
- Accettazione o rifiuto degli articoli: solo quelli accettati diventano visibili al pubblico

### Amministratori (`ROLE_ADMIN`)
- Gestione delle categorie: creazione, modifica e cancellazione
- Una categoria può essere cancellata anche se collegata ad articoli: gli articoli restano pubblicati e vengono sganciati dalla categoria
- Revisione delle richieste di collaborazione ricevute dagli utenti

### Utenti registrati
- Registrazione e accesso
- Richiesta di collaborazione per diventare redattore o revisore

## Tecnologie

| Ambito | Strumenti |
|---|---|
| Linguaggio | Java 21 |
| Framework | Spring Boot 3.3.4 (Web, Data JPA, Security, Validation) |
| Template | Thymeleaf con extras Spring Security |
| Database | MySQL |
| Mapping | ModelMapper |
| Utility | Lombok, Spring Boot DevTools, Spring Boot Starter Mail |
| Frontend | Bootstrap 5.3.3 |
| Storage immagini | Supabase Storage (bucket pubblico) |
| Build | Maven |

## Architettura

Il progetto segue un'architettura a livelli, con DTO separati dalle entità:

```
controllers  →  services  →  repositories  →  database
     ↑              ↑
    dtos         models
```

Package principali, sotto `it.aulab.final_project_giuseppe`:
- `models`: entità JPA (`User`, `Role`, `Article`, `Category`, `Image`, `CareerRequest`)
- `dtos`: oggetti di trasferimento verso le viste
- `repositories`: interfacce Spring Data JPA
- `services`: logica di business, con un'interfaccia `CrudService` generica
- `controllers`: rotte MVC
- `resources/templates`: viste Thymeleaf

### Modello dati

- `users` e `roles`: relazione molti a molti tramite `users_roles`
- `articles`: collegati a un utente (autore) e, in modo opzionale, a una categoria
- `categories`: un articolo appartiene a una sola categoria, una categoria può avere più articoli
- `career_request`: richieste di collaborazione degli utenti

## Requisiti

- JDK 21
- Maven 3.9 o superiore (oppure il wrapper `mvnw` incluso)
- MySQL in esecuzione su `localhost:3306`
- Un progetto Supabase con un bucket pubblico per le immagini

## Avvio in locale

**1. Clona il repository**

```bash
git clone https://github.com/Giuseppe0024/NOME-REPOSITORY.git
cd NOME-REPOSITORY
```

**2. Crea il database**

Crea un database MySQL ed esegui gli script nella cartella `sql/`:

```bash
mysql -u root -p -e "CREATE DATABASE progettoFinaleGiuseppe;"
mysql -u root -p progettoFinaleGiuseppe < sql/create.sql
mysql -u root -p progettoFinaleGiuseppe < sql/insert.sql
```

Lo script `insert.sql` inserisce ruoli, categorie iniziali e un utente amministratore.

**3. Configura `application.properties`**

Modifica `src/main/resources/application.properties` con i tuoi valori:

```properties
spring.application.name=final-project-giuseppe

spring.datasource.url=jdbc:mysql://localhost:3306/progettoFinaleGiuseppe
spring.datasource.username=root
spring.datasource.password=LA_TUA_PASSWORD

supabase.url=URL_DEL_TUO_PROGETTO_SUPABASE
supabase.key=LA_TUA_CHIAVE
supabase.bucket=NOME_BUCKET/
supabase.image=URL_PUBBLICO_DEL_BUCKET/public/
```

> Non pubblicare mai password o chiavi in un repository. Usa variabili d'ambiente oppure un file di configurazione locale escluso da Git.

**4. Avvia l'applicazione**

```bash
./mvnw spring-boot:run
```

L'applicazione è disponibile su `http://localhost:8080`.

## Ruoli

| Ruolo | Permessi principali |
|---|---|
| `ROLE_USER` | Consultare gli articoli, inviare una richiesta di collaborazione |
| `ROLE_WRITER` | Scrivere e gestire i propri articoli |
| `ROLE_REVISOR` | Accettare o rifiutare gli articoli |
| `ROLE_ADMIN` | Gestire categorie e richieste di collaborazione |

## Possibili sviluppi

- Test automatici con JUnit, Mockito e Testcontainers
- Documentazione delle API con OpenAPI/Swagger
- Containerizzazione con Docker e `docker-compose`
- Pipeline CI con GitHub Actions
- Cancellazione tramite richiesta `POST` con protezione CSRF

## Autore

**Giuseppe Berardi**
GitHub: [Giuseppe0024](https://github.com/Giuseppe0024)
