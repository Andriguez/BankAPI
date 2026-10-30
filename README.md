# BankAPI (Holla Dolla Bank)

A simulated online bank built as a team project at Inholland in 2024: a Java/Spring Boot REST API with a Vue 3 frontend. Customers register, get a current and a savings account once an employee approves them, transfer money and use a virtual ATM. Employees approve registrations, set account limits and review all transactions.

The original school submission is tagged [`v1.0-school`](https://github.com/Andriguez/BankAPI/tree/v1.0-school). The `main` branch has since received security fixes from a later code review (see [Security](#security)).

## Stack

| Layer | Technology |
| --- | --- |
| Backend | Java 21, Spring Boot 3.2, Spring Security, JPA, H2 (file database) |
| Auth | JWT signed with an RSA key (RS256), role checks with `@PreAuthorize` |
| Frontend | Vue 3, Pinia, Vue Router, Axios, Bootstrap |
| Tests | JUnit 5 + Mockito, Cucumber (BDD) |

## Running locally

Requirements: JDK 21 and Node 18 or newer.

### 1. Create a local keystore (once)

The keystore that signs JWTs and serves HTTPS is not in the repository. Create your own; `keytool` asks for a password, remember it.

```powershell
cd Bank_rest_api
mkdir keystore
& "$env:JAVA_HOME\bin\keytool.exe" -genkeypair -alias bankapi -keyalg RSA -keysize 2048 `
  -storetype PKCS12 -keystore keystore\bankapi.p12 -validity 365 -dname "CN=localhost"
```

(macOS/Linux: `keytool -genkeypair -alias bankapi -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore keystore/bankapi.p12 -validity 365 -dname "CN=localhost"`)

`keystore/` is in `.gitignore`.

### 2. Start the backend

```powershell
$env:KEYSTORE_PASSWORD = "<the password you chose>"
.\mvnw spring-boot:run
```

The API runs on https://localhost:8443 with a self-signed certificate. Open that address once in your browser and accept the warning, otherwise the frontend's requests fail.

Optional settings (environment variables):

| Variable | Default | Purpose |
| --- | --- | --- |
| `KEYSTORE_PATH` | `file:./keystore/bankapi.p12` | Location of the keystore |
| `CORS_ALLOWED_ORIGINS` | `http://localhost:5173` | Frontend origin(s), comma-separated |
| `H2_CONSOLE_ENABLED` | `false` | H2 web console; keep off outside local debugging |
| `DB_PASSWORD` | `password` | Password of the local demo database |

### 3. Start the frontend

```powershell
cd bank_frontend
npm install
npm run dev
```

Then open http://localhost:5173. Demo logins (local demo data only): `customer2@email.com` and `admin@email.com`, both with password `password`.

## Tests

```powershell
cd Bank_rest_api
.\mvnw test                              # unit tests
.\mvnw test -Dtest=CucumberTestRunner    # Cucumber scenarios (stop the running backend first)
```

Tests use the `test` profile and a throwaway keystore in `src/test/resources`, so they need no local setup.

Known limitation: the Cucumber scenarios run against the persistent demo database, so some only pass once per database state (for example, registration creates a fixed user, and transfers use up daily limits). A fresh in-memory database per test run would fix this.

## Security

In 2026 I reviewed this project against the OWASP API Security Top 10. Fixed on `main`:

| # | Finding | Severity | Fix |
| --- | --- | --- | --- |
| F1 | Customers could transfer money out of any account | Critical | Ownership check on the debited account |
| F2 | JWT signing key and passwords committed to the repo | Critical | Keystore and password moved out of the repo; old key retired |
| F3 | A DEPOSIT between two accounts credited both | High | Transaction type derived on the server |
| F4 | H2 database console open to anyone | High | Console off by default, frame protection restored |
| F6 | CORS allowed every origin | Low | Only the configured frontend origin |
| F7 | Internal error messages returned to clients | Low | Generic messages, details logged on the server |
| F8 | No default-deny rule | Low | `anyRequest().authenticated()` |

Not fixed: F5 (money stored as `double` instead of `BigDecimal`), which would change the data model, database and frontend.

The old keystore remains in the git history and must be treated as compromised; it is no longer used anywhere.
-- 