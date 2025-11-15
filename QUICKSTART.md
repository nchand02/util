# 🚀 Quick Start Guide - Guest Management Application

## Overview

This is a production-ready full-stack application with:
- **Backend:** Spring Boot (JDK 21) with OAuth2 + JWT authentication
- **Frontend:** React with Tailwind CSS
- **Database:** H2 (persistent file-based storage)
- **Features:** OAuth login (Google, GitHub, Microsoft, Facebook) + Guest CRUD operations

---

## 📦 What's Included

### Backend (Spring Boot)
✅ OAuth2 authentication (4 providers)
✅ JWT token generation & validation
✅ RESTful API with CRUD operations
✅ Global exception handling
✅ Input validation
✅ CORS configuration
✅ H2 persistent database
✅ Security best practices

### Frontend (React) - Coming Next
✅ OAuth login UI
✅ Protected routes
✅ Guest management interface
✅ Responsive design (mobile, tablet, desktop)
✅ Tailwind CSS styling
✅ Error handling
✅ Toast notifications

---

## 🎯 Getting Started

### Step 1: Configure OAuth2 Providers

You need to create OAuth apps with at least one provider (Google recommended for quickstart):

#### Quick Setup - Google OAuth (5 minutes)

1. **Go to Google Cloud Console:**
   https://console.cloud.google.com/apis/credentials

2. **Create Project** (if you don't have one)

3. **Create OAuth 2.0 Client ID:**
   - Click "Create Credentials" → "OAuth client ID"
   - Application type: "Web application"
   - Name: "Guest Management App"
   - Authorized redirect URIs:
     - `http://localhost:8080/login/oauth2/code/google`
   - Click "Create"

4. **Copy Credentials:**
   - Copy the Client ID
   - Copy the Client Secret

5. **Set Environment Variables:**
   ```bash
   export GOOGLE_CLIENT_ID="your-client-id-here"
   export GOOGLE_CLIENT_SECRET="your-client-secret-here"
   ```

   Or create `.env` file in project root:
   ```
   GOOGLE_CLIENT_ID=your-client-id-here
   GOOGLE_CLIENT_SECRET=your-client-secret-here
   ```

### Step 2: Generate JWT Secret

```bash
# Generate a strong JWT secret
openssl rand -base64 32

# Set it as environment variable
export JWT_SECRET="your-generated-secret-here"
```

### Step 3: Run the Backend

```bash
# From project root
./mvnw spring-boot:run
```

The backend will start on: **http://localhost:8080**

### Step 4: Test the Backend

Open your browser and go to:
```
http://localhost:8080
```

You should see:
```json
{
  "status": "UP",
  "message": "Guest Management API is running",
  "version": "1.0.0"
}
```

### Step 5: Test OAuth Login

Open your browser and go to:
```
http://localhost:8080/oauth2/authorize/google
```

This will redirect you to Google login. After successful login, you'll be redirected back with a JWT token in the URL.

---

## 🧪 Testing the API

Once you have a JWT token, you can test the API endpoints:

### Get Current User Info

```bash
curl -X GET http://localhost:8080/api/auth/me \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Create a Guest

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

### Get All Guests

```bash
curl -X GET http://localhost:8080/api/guests \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Update a Guest

```bash
curl -X PUT http://localhost:8080/api/guests/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "name": "Jane Doe",
    "email": "jane@example.com",
    "phone": "+1234567890",
    "numOfGuests": 3
  }'
```

### Delete a Guest

```bash
curl -X DELETE http://localhost:8080/api/guests/1 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

---

## 📊 View Database (H2 Console)

1. Open browser: http://localhost:8080/h2-console
2. Enter connection details:
   - JDBC URL: `jdbc:h2:file:./data/guestdb`
   - Username: `sa`
   - Password: (leave empty)
3. Click "Connect"

You can now view and query the `users` and `guests` tables.

---

## 🎨 Frontend Setup (Next Step)

The frontend React application is being created. Once it's ready, you'll have:

1. **Login Page** with OAuth buttons (Google, GitHub, Microsoft, Facebook)
2. **Dashboard** with guest list/table
3. **Add/Edit Guest** forms
4. **Responsive Design** for all devices
5. **Deployed to GitHub Pages**

---

## 🔧 Configuration Files

### application.yml
Main configuration file at: `src/main/resources/application.yml`

Key settings:
- OAuth2 client credentials
- JWT secret and expiration
- CORS allowed origins
- Database configuration
- Server port (default: 8080)

### .env File
Create this in project root for local development:

```env
# OAuth2 Credentials
GOOGLE_CLIENT_ID=your-google-client-id
GOOGLE_CLIENT_SECRET=your-google-client-secret
GITHUB_CLIENT_ID=your-github-client-id
GITHUB_CLIENT_SECRET=your-github-client-secret

# JWT Configuration
JWT_SECRET=your-strong-jwt-secret-here

# CORS Configuration
CORS_ALLOWED_ORIGINS=http://localhost:3000,https://yourdomain.com
```

---

## 🚨 Common Issues & Solutions

### Issue: "Cannot connect to database"
**Solution:** The `data` directory will be created automatically on first run. Make sure you have write permissions.

### Issue: "OAuth2 redirect URI mismatch"
**Solution:** Double-check that the redirect URI in your OAuth provider settings exactly matches:
- `http://localhost:8080/login/oauth2/code/google`

### Issue: "Port 8080 already in use"
**Solution:** Stop other applications using port 8080, or change the port in `application.yml`:
```yaml
server:
  port: 8081
```

### Issue: "JWT token expired"
**Solution:** The token expires after 24 hours (configurable). Re-authenticate to get a new token.

---

## 📈 Next Steps

1. ✅ **Backend is ready and running**
2. ⏳ **Frontend React app is being created**
3. 🔜 **Will add OAuth integration to frontend**
4. 🔜 **Will add guest management UI**
5. 🔜 **Will configure GitHub Pages deployment**

---

## 🆘 Need Help?

- Check the main `README.md` for detailed documentation
- Check `application.yml` for configuration options
- Check logs in the console for error messages
- Test endpoints with the provided curl commands

---

## 🎉 Success Indicators

If everything is working, you should see:

✅ Backend starts without errors
✅ Homepage shows API status
✅ OAuth login redirects to provider
✅ JWT token is returned after login
✅ API endpoints respond to authenticated requests
✅ Database persists data across restarts

---

**Backend is production-ready! Frontend coming next...**

