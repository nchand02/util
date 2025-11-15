# 🔐 OAuth2 Provider Setup Guide

Complete setup instructions for all 4 OAuth providers.

---

## 🟦 1. Google OAuth Setup

### Step 1: Create Google Cloud Project

1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Click "Select a project" → "New Project"
3. Enter project name: "Guest Management App"
4. Click "Create"

### Step 2: Enable Google+ API

1. In the left sidebar, go to "APIs & Services" → "Library"
2. Search for "Google+ API"
3. Click on it and press "Enable"

### Step 3: Create OAuth Credentials

1. Go to "APIs & Services" → "Credentials"
2. Click "Create Credentials" → "OAuth client ID"
3. If prompted, configure the OAuth consent screen:
   - User Type: External
   - App name: "Guest Management App"
   - User support email: your email
   - Developer contact: your email
   - Click "Save and Continue" through all steps

4. Back to "Create OAuth client ID":
   - Application type: **Web application**
   - Name: "Guest Management Web Client"
   
5. **Authorized JavaScript origins:**
   ```
   http://localhost:8080
   http://localhost:3000
   ```

6. **Authorized redirect URIs:**
   ```
   http://localhost:8080/login/oauth2/code/google
   ```

7. Click "Create"

### Step 4: Copy Credentials

- **Client ID:** `xxxxx.apps.googleusercontent.com`
- **Client Secret:** `GOCSPX-xxxxx`

### Step 5: Add to Configuration

**Environment Variables:**
```bash
export GOOGLE_CLIENT_ID="your-client-id.apps.googleusercontent.com"
export GOOGLE_CLIENT_SECRET="your-client-secret"
```

**Or in .env file:**
```env
GOOGLE_CLIENT_ID=your-client-id.apps.googleusercontent.com
GOOGLE_CLIENT_SECRET=your-client-secret
```

---

## 🐙 2. GitHub OAuth Setup

### Step 1: Create OAuth App

1. Go to [GitHub Developer Settings](https://github.com/settings/developers)
2. Click "OAuth Apps" → "New OAuth App"

### Step 2: Fill in Application Details

- **Application name:** Guest Management App
- **Homepage URL:** `http://localhost:3000`
- **Application description:** (optional)
- **Authorization callback URL:** `http://localhost:8080/login/oauth2/code/github`

### Step 3: Register Application

Click "Register application"

### Step 4: Generate Client Secret

1. After creating, you'll see the Client ID
2. Click "Generate a new client secret"
3. Copy the secret immediately (you won't see it again)

### Step 5: Copy Credentials

- **Client ID:** `Iv1.xxxxxxxxxxxxx`
- **Client Secret:** `xxxxxxxxxxxxxxxxxxxxx`

### Step 6: Add to Configuration

**Environment Variables:**
```bash
export GITHUB_CLIENT_ID="your-github-client-id"
export GITHUB_CLIENT_SECRET="your-github-client-secret"
```

**Or in .env file:**
```env
GITHUB_CLIENT_ID=your-github-client-id
GITHUB_CLIENT_SECRET=your-github-client-secret
```

---

## 🪟 3. Microsoft OAuth Setup

### Step 1: Register Application

1. Go to [Azure Portal - App Registrations](https://portal.azure.com/#blade/Microsoft_AAD_RegisteredApps)
2. Click "New registration"

### Step 2: Fill in Application Details

- **Name:** Guest Management App
- **Supported account types:** Accounts in any organizational directory and personal Microsoft accounts
- **Redirect URI:** 
  - Platform: Web
  - URI: `http://localhost:8080/login/oauth2/code/microsoft`

### Step 3: Register

Click "Register"

### Step 4: Copy Application (Client) ID

From the Overview page, copy the **Application (client) ID**

### Step 5: Create Client Secret

1. In left sidebar, click "Certificates & secrets"
2. Click "New client secret"
3. Description: "Backend secret"
4. Expires: 24 months (or your preference)
5. Click "Add"
6. **Copy the Value immediately** (you won't see it again)

### Step 6: Configure API Permissions (Optional but Recommended)

1. Click "API permissions" in left sidebar
2. Click "Add a permission"
3. Select "Microsoft Graph"
4. Select "Delegated permissions"
5. Add: `User.Read`, `email`, `openid`, `profile`
6. Click "Add permissions"

### Step 7: Copy Credentials

- **Application (client) ID:** `xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx`
- **Client Secret Value:** `xxxxxxxxxxxxxxxxxxxxx`

### Step 8: Add to Configuration

**Environment Variables:**
```bash
export MICROSOFT_CLIENT_ID="your-application-client-id"
export MICROSOFT_CLIENT_SECRET="your-client-secret-value"
```

**Or in .env file:**
```env
MICROSOFT_CLIENT_ID=your-application-client-id
MICROSOFT_CLIENT_SECRET=your-client-secret-value
```

---

## 📘 4. Facebook OAuth Setup

### Step 1: Create Facebook App

1. Go to [Facebook Developers](https://developers.facebook.com/)
2. Click "My Apps" → "Create App"

### Step 2: Choose App Type

- Select "Consumer" (for most use cases)
- Click "Next"

### Step 3: Fill in App Details

- **App name:** Guest Management App
- **App contact email:** your email
- Click "Create App"

### Step 4: Add Facebook Login Product

1. In the dashboard, find "Facebook Login"
2. Click "Set Up"
3. Choose "Web" platform
4. Site URL: `http://localhost:3000`
5. Click "Save" and "Continue"

### Step 5: Configure OAuth Redirect URIs

1. In left sidebar, click "Facebook Login" → "Settings"
2. In "Valid OAuth Redirect URIs", add:
   ```
   http://localhost:8080/login/oauth2/code/facebook
   ```
3. Click "Save Changes"

### Step 6: Make App Public (for testing)

1. In left sidebar, click "Settings" → "Basic"
2. Find "App Mode" - it should be in "Development" mode
3. For production, you'll need to submit for review

### Step 7: Copy Credentials

From "Settings" → "Basic":
- **App ID:** `xxxxxxxxxxxxxxxx`
- **App Secret:** Click "Show" to reveal, then copy

### Step 8: Add to Configuration

**Environment Variables:**
```bash
export FACEBOOK_CLIENT_ID="your-facebook-app-id"
export FACEBOOK_CLIENT_SECRET="your-facebook-app-secret"
```

**Or in .env file:**
```env
FACEBOOK_CLIENT_ID=your-facebook-app-id
FACEBOOK_CLIENT_SECRET=your-facebook-app-secret
```

---

## 🚀 Production Setup

When deploying to production, update the redirect URIs:

### Google
```
https://yourdomain.com/login/oauth2/code/google
https://your-backend-url.com/login/oauth2/code/google
```

### GitHub
```
https://your-backend-url.com/login/oauth2/code/github
```

### Microsoft
```
https://your-backend-url.com/login/oauth2/code/microsoft
```

### Facebook
```
https://your-backend-url.com/login/oauth2/code/facebook
```

Also update the `authorized-redirect-uris` in `application.yml`:
```yaml
app:
  oauth2:
    authorized-redirect-uris:
      - https://yourdomain.com/oauth2/redirect
      - https://your-github-username.github.io/your-repo-name/oauth2/redirect
```

---

## 🧪 Testing OAuth Configuration

### Test Each Provider

1. **Start your backend:** `./mvnw spring-boot:run`

2. **Test Google:**
   ```
   http://localhost:8080/oauth2/authorize/google
   ```

3. **Test GitHub:**
   ```
   http://localhost:8080/oauth2/authorize/github
   ```

4. **Test Microsoft:**
   ```
   http://localhost:8080/oauth2/authorize/microsoft
   ```

5. **Test Facebook:**
   ```
   http://localhost:8080/oauth2/authorize/facebook
   ```

If successful, you'll be redirected to the provider's login page, and after authentication, redirected back with a JWT token.

---

## 🔍 Troubleshooting

### Error: "redirect_uri_mismatch"
- **Solution:** Double-check that the redirect URI in your OAuth provider settings exactly matches the one in your application

### Error: "invalid_client"
- **Solution:** Verify that your Client ID and Secret are correct

### Error: "access_denied"
- **Solution:** Make sure you're using the correct OAuth scopes and that the user has granted permission

### Error: "unauthorized_client"
- **Solution:** Check that your OAuth app is not in restricted mode and allows the requested grant type

---

## 📝 Complete .env File Template

```env
# Google OAuth2
GOOGLE_CLIENT_ID=your-google-client-id.apps.googleusercontent.com
GOOGLE_CLIENT_SECRET=your-google-client-secret

# GitHub OAuth2
GITHUB_CLIENT_ID=your-github-client-id
GITHUB_CLIENT_SECRET=your-github-client-secret

# Microsoft OAuth2
MICROSOFT_CLIENT_ID=your-microsoft-application-client-id
MICROSOFT_CLIENT_SECRET=your-microsoft-client-secret-value

# Facebook OAuth2
FACEBOOK_CLIENT_ID=your-facebook-app-id
FACEBOOK_CLIENT_SECRET=your-facebook-app-secret

# JWT Secret (generate with: openssl rand -base64 32)
JWT_SECRET=your-strong-jwt-secret-key-here

# CORS Configuration
CORS_ALLOWED_ORIGINS=http://localhost:3000,https://yourdomain.com
```

---

## ✅ Verification Checklist

Before running the application, verify:

- [ ] Created OAuth app for at least one provider (Google recommended)
- [ ] Copied Client ID and Client Secret
- [ ] Set redirect URI to `http://localhost:8080/login/oauth2/code/{provider}`
- [ ] Added credentials to `.env` file or environment variables
- [ ] Generated and set JWT_SECRET
- [ ] Updated CORS allowed origins if needed

---

**You're all set! Start the backend and test your OAuth login.**

