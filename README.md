# util

## Overview
Spring Boot backend (guest management API) plus React frontend. Frontend is deployed to GitHub Pages at `https://nchand02.github.io/util/`. Backend JAR is built via Maven CI workflow and can be deployed separately (e.g. to a cloud host) referenced by the frontend via `REACT_APP_API_BASE_URL`.

## Project Structure
```
backend (Spring Boot) : src/main/java,...
frontend (React)      : frontend/
GitHub Actions        : .github/workflows/
```

## Local Development
### Backend
Use the dev profile for local defaults (non‑secret):
```bash
./mvnw spring-boot:run -Dspring.profiles.active=dev
```
Run tests:
```bash
./mvnw test
```

### Frontend
```bash
cd frontend
npm install
npm start
```

## Environment Variables (Secrets)
Set these as GitHub repository secrets (Settings → Secrets and variables → Actions → New repository secret):

| Secret Name | Purpose |
|-------------|---------|
| `JWT_SECRET` | Signing JWT tokens |
| `GOOGLE_CLIENT_ID` | Google OAuth client id (optional) |
| `GOOGLE_CLIENT_SECRET` | Google OAuth secret (optional) |
| `GITHUB_CLIENT_ID` | GitHub OAuth client id (optional) |
| `GITHUB_CLIENT_SECRET` | GitHub OAuth secret (optional) |
| `CORS_ALLOWED_ORIGINS` | Comma list of allowed origins (include Pages URL) |
| `OAUTH2_REDIRECT_URIS` | Comma list of redirect URIs |
| `REACT_APP_API_BASE_URL` | Base URL of deployed backend API |

## Production Configuration
`application.yml` now requires secrets (no fallbacks). Provide them via environment variables in deployment or GitHub Actions.

## GitHub Actions Workflows
1. `backend.yml`: Builds & tests backend, publishes JAR artifact.
2. `frontend-pages.yml`: Builds React app and deploys to GitHub Pages.

## Deploying Frontend to GitHub Pages
Push changes to `main` affecting `frontend/` to trigger workflow. Workflow uses:
```yaml
REACT_APP_API_BASE_URL: ${{ secrets.REACT_APP_API_BASE_URL }}
```
Set that secret to the public backend API base (e.g. `https://api.example.com`).

## Adding a Backend Host
Deploy the JAR (artifact) to a runtime (Render, Railway, AWS Elastic Beanstalk, etc.) setting required env vars. Example run:
```bash
export JWT_SECRET=yourStrongSecret
export CORS_ALLOWED_ORIGINS=https://nchand02.github.io/util
export OAUTH2_REDIRECT_URIS=https://nchand02.github.io/util/oauth2/redirect
java -jar util-0.0.1-SNAPSHOT.jar
```

## CORS
Ensure `CORS_ALLOWED_ORIGINS` includes `https://nchand02.github.io/util`. Frontend will call the backend using `REACT_APP_API_BASE_URL`.

## Cleaning the Repo
`.gitignore` excludes build artifacts (`target/`, `frontend/build/`, `data/`). If already committed, remove them:
```bash
git rm -r --cached target data frontend/build
git commit -m "Remove build artifacts"
```

## GitHub Pages Cache Busting
`homepage` field in `package.json` sets correct asset paths. A `.nojekyll` file is added in build via `build:pages` script to avoid Jekyll processing.

## Manual Frontend Build
```bash
cd frontend
REACT_APP_API_BASE_URL=http://localhost:8080 npm run build
```
Output in `frontend/build/` can be served statically.

## Security Notes
No secrets stored in VCS; all sensitive values sourced from environment.
Change the dev secret in `application-dev.yml` if risk of accidental production use.

## Next Steps
1. Deploy backend to a public host.
2. Set `REACT_APP_API_BASE_URL` to that host URL.
3. Confirm CORS and OAuth flows (when OAuth configured).
# Guest Management Application - Backend

A production-ready Spring Boot application with OAuth2 authentication and JWT token-based authorization.

## 🚀 Features

- ✅ OAuth2 Login (Google, GitHub, Microsoft, Facebook)
- ✅ JWT Token Authentication
- ✅ RESTful API for Guest Management (CRUD)
- ✅ H2 Persistent Database
- ✅ Global Exception Handling
- ✅ Input Validation
- ✅ CORS Configuration
- ✅ Security Best Practices
- ✅ Actuator Health Checks

## 📋 Prerequisites

- JDK 21
- Maven 3.6+
- OAuth2 credentials from providers (Google, GitHub, Microsoft, Facebook)

## 🛠️ Setup Instructions

### Step 1: Clone and Navigate

```bash
cd /path/to/util
```

### Step 2: 🔐 Configure Secrets (IMPORTANT!)

**⚠️ NEVER commit `.env` files to Git! This repository is public.**

```bash
# Copy the environment template
cp .env.template .env

# Edit .env and add your actual OAuth2 credentials
nano .env  # or use your preferred editor
```

See `OAUTH_SETUP.md` for detailed instructions on obtaining OAuth2 credentials.

**📖 Documentation:**
- `SECURITY.md` - Security guide & credential alert
- `OAUTH_SETUP.md` - OAuth provider setup
- `.env.template` - Environment variable template

**For detailed OAuth setup instructions, see `OAUTH_SETUP.md`**

### Step 3: Generate JWT Secret

```bash
# Generate a strong random secret for JWT signing
openssl rand -base64 32
```

Add the generated secret to your `.env` file as `JWT_SECRET`.

### Step 4: Update application.yml

Edit `src/main/resources/application.yml` and update:

```yaml
app:
  cors:
    allowed-origins: http://localhost:3000,https://your-github-username.github.io
  oauth2:
    authorized-redirect-uris:
      - http://localhost:3000/oauth2/redirect
      - https://your-github-username.github.io/your-repo-name/oauth2/redirect
```

### Step 4: Build the Application

```bash
./mvnw clean install
```

### Step 5: Run the Application

```bash
./mvnw spring-boot:run
```

Or run the JAR:

```bash
java -jar target/util-0.0.1-SNAPSHOT.jar
```

The backend will start on: **http://localhost:8080**

## 📡 API Endpoints

### Public Endpoints

- `GET /` - API health check
- `GET /actuator/health` - Health status

### Authentication Endpoints

- `GET /oauth2/authorize/{provider}` - Initiate OAuth2 login (google, github, microsoft, facebook)
- `GET /api/auth/me` - Get current user info (requires JWT)

### Guest Management Endpoints (Protected)

All endpoints require `Authorization: Bearer <JWT_TOKEN>` header

- `GET /api/guests` - Get all guests for authenticated user
- `GET /api/guests/{id}` - Get specific guest
- `POST /api/guests` - Create new guest
- `PUT /api/guests/{id}` - Update guest
- `DELETE /api/guests/{id}` - Delete guest
- `GET /api/guests/count` - Get guest count

### Example Request

**Create Guest:**
```bash
curl -X POST http://localhost:8080/api/guests \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "phone": "+1234567890",
    "numOfGuests": 2
  }'
```

## 🗄️ Database

The application uses H2 database with file-based persistence.

- Database file location: `./data/guestdb.mv.db`
- H2 Console: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:file:./data/guestdb`
  - Username: `sa`
  - Password: (leave empty)

**Note:** Disable H2 console in production by setting `spring.h2.console.enabled=false`

## 🔐 Security

### OAuth2 Flow

1. Frontend redirects user to: `http://localhost:8080/oauth2/authorize/{provider}`
2. User authenticates with OAuth provider
3. Provider redirects back to backend callback
4. Backend generates JWT token
5. Backend redirects to frontend with token: `http://localhost:3000/oauth2/redirect?token=JWT_TOKEN`
6. Frontend stores token and uses it for subsequent API calls

### JWT Token

- Token expiration: 24 hours (configurable)
- Token contains: User ID, Email, Name, Provider
- All `/api/*` endpoints (except `/api/auth/**`) require valid JWT

## 🧪 Testing

### Run Tests

```bash
./mvnw test
```

### Test OAuth2 Flow Manually

1. Start the application
2. Open browser: `http://localhost:8080/oauth2/authorize/google`
3. Complete Google login
4. You'll be redirected with JWT token in URL

## 📦 Building for Production

```bash
./mvnw clean package -DskipTests
```

JAR file will be created in `target/` directory.

## 🚢 Deployment

### Deploy to Heroku

```bash
# Install Heroku CLI
heroku login
heroku create your-app-name

# Set environment variables
heroku config:set GOOGLE_CLIENT_ID=your-google-client-id
heroku config:set GOOGLE_CLIENT_SECRET=your-google-client-secret
# ... set other OAuth credentials

# Deploy
git push heroku main
```

### Deploy to Railway

1. Connect your GitHub repository to Railway
2. Add environment variables in Railway dashboard
3. Deploy automatically on push

### Deploy Frontend to GitHub Pages

**Setup GitHub Secrets:**
1. Go to Repository Settings → Secrets and variables → Actions
2. Add secrets:
   - `REACT_APP_API_URL`: Your backend URL (e.g., `https://your-app.herokuapp.com`)
   - `PUBLIC_URL`: Your repo name (e.g., `/your-repo-name`)

**Enable GitHub Pages:**
1. Go to Settings → Pages
2. Source: Select "GitHub Actions"
3. Push to main branch to trigger deployment

**Workflow:** `.github/workflows/deploy-frontend.yml` (already configured)

### Environment Variables for Production

⚠️ **IMPORTANT:** Never commit production secrets to Git!

Use your hosting platform's environment variable management:
- **Heroku:** `heroku config:set VAR_NAME=value`
- **Railway:** Variables tab in dashboard
- **AWS/Azure/GCP:** Use their secret management services

Required environment variables:
```bash
GOOGLE_CLIENT_ID=...
GOOGLE_CLIENT_SECRET=...
GITHUB_CLIENT_ID=...
GITHUB_CLIENT_SECRET=...
MICROSOFT_CLIENT_ID=...
MICROSOFT_CLIENT_SECRET=...
FACEBOOK_CLIENT_ID=...
FACEBOOK_CLIENT_SECRET=...
JWT_SECRET=your-strong-secret-key-here
CORS_ALLOWED_ORIGINS=https://your-frontend-domain.com
```

**📖 See `SECURITY.md` for security best practices**

## 📝 Project Structure

```
src/main/java/com/prgx/migration/api/util/
├── UtilApplication.java          # Main application class
├── config/
│   ├── SecurityConfig.java       # Security & OAuth2 configuration
│   └── CorsConfig.java           # CORS configuration
├── controller/
│   ├── RootController.java       # Root endpoints
│   ├── AuthController.java       # Auth endpoints
│   └── GuestController.java      # Guest CRUD endpoints
├── dto/
│   ├── request/
│   │   └── GuestRequest.java     # Guest input DTO
│   └── response/
│       ├── GuestResponse.java    # Guest output DTO
│       ├── AuthResponse.java     # Auth response DTO
│       └── ErrorResponse.java    # Error response DTO
├── model/
│   ├── User.java                 # User entity
│   └── Guest.java                # Guest entity
├── repository/
│   ├── UserRepository.java       # User data access
│   └── GuestRepository.java      # Guest data access
├── service/
│   ├── UserService.java          # User business logic
│   ├── GuestService.java         # Guest business logic
│   └── JwtService.java           # JWT operations
├── security/
│   ├── JwtAuthenticationFilter.java              # JWT filter
│   ├── OAuth2AuthenticationSuccessHandler.java   # OAuth success handler
│   └── OAuth2AuthenticationFailureHandler.java   # OAuth failure handler
└── exception/
    ├── GlobalExceptionHandler.java   # Global error handler
    ├── ResourceNotFoundException.java
    ├── UnauthorizedException.java
    └── ValidationException.java
```

## 🐛 Troubleshooting

### Issue: OAuth2 redirect URI mismatch

**Solution:** Ensure the redirect URI in your OAuth provider settings matches exactly:
- `http://localhost:8080/login/oauth2/code/{provider}`

### Issue: JWT validation fails

**Solution:** Check that:
1. JWT_SECRET is set and strong
2. Token hasn't expired
3. Token is sent in `Authorization: Bearer <token>` header

### Issue: Database file locked

**Solution:** Close all H2 console connections and restart the application

### Issue: CORS errors

**Solution:** Add your frontend URL to `app.cors.allowed-origins` in application.yml

## 📚 Additional Resources

- [Spring Security OAuth2](https://spring.io/guides/tutorials/spring-boot-oauth2/)
- [JWT.io](https://jwt.io/) - JWT Debugger
- [H2 Database](https://www.h2database.com/html/main.html)

## 🤝 Support

For issues and questions, please open an issue in the GitHub repository.

## 📄 License

This project is licensed under the MIT License.

