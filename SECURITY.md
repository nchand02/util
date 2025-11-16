# 🔐 Security Guide

## Secret Management

### ⚠️ CRITICAL: Never commit secrets to Git!

This project uses environment variables to manage sensitive credentials. **All secrets must be stored outside of version control.**

---

## 🛡️ Security Measures in Place

### 1. `.gitignore` Protection
The following patterns are excluded from Git:
- `.env`
- `.env.local`
- `.env.production`
- `.env.development`
- `*.secret`
- `application-local.yml`
- `application-secrets.yml`

### 2. Environment Variable Templates
Instead of committing secrets, we commit templates:
- **Backend:** `.env.template`
- **Frontend:** `frontend/.env.template`

### 3. GitHub Actions Secrets
For CI/CD, secrets are stored in GitHub repository settings and injected at build time.

---

## 🚀 Local Development Setup

### Step 1: Copy Environment Templates

**Backend:**
```bash
cp .env.template .env
```

**Frontend:**
```bash
cd frontend
cp .env.template .env
```

### Step 2: Fill in Your Credentials

Edit the `.env` files with your actual credentials. See `OAUTH_SETUP.md` for instructions on obtaining OAuth credentials.

**Backend `.env` example:**
```env
GOOGLE_CLIENT_ID=123456789.apps.googleusercontent.com
GOOGLE_CLIENT_SECRET=GOCSPX-abc123xyz
GITHUB_CLIENT_ID=Iv1.abc123def456
GITHUB_CLIENT_SECRET=ghp_xyz789abc123
JWT_SECRET=$(openssl rand -base64 32)
```

**Frontend `.env` example:**
```env
REACT_APP_API_URL=http://localhost:8080
REACT_APP_ENV=development
```

### Step 3: Generate JWT Secret

```bash
# Generate a strong random secret
openssl rand -base64 32
```

Copy the output and add it to your backend `.env` file as `JWT_SECRET`.

### Step 4: Verify .env is Ignored

```bash
# This should show nothing (means .env is properly ignored)
git status | grep .env
```

---

## 🌐 Production Deployment

### GitHub Pages (Frontend)

1. **Go to your GitHub repository**
2. Navigate to: `Settings` → `Secrets and variables` → `Actions`
3. Click **New repository secret**
4. Add the following secrets:

| Secret Name | Description | Example |
|-------------|-------------|---------|
| `REACT_APP_API_URL` | Your production backend URL | `https://api.yourdomain.com` |
| `PUBLIC_URL` | Your GitHub Pages URL path | `/your-repo-name` |

### Backend Deployment Options

#### Option 1: Heroku
```bash
# Set config vars
heroku config:set GOOGLE_CLIENT_ID="your-id"
heroku config:set GOOGLE_CLIENT_SECRET="your-secret"
heroku config:set JWT_SECRET="your-jwt-secret"
# ... add all other secrets
```

#### Option 2: Railway
1. Go to your Railway project
2. Click on **Variables** tab
3. Add each environment variable

#### Option 3: AWS Elastic Beanstalk
```bash
# Create .ebextensions/environment.config (not committed)
eb setenv GOOGLE_CLIENT_ID="your-id" \
  GOOGLE_CLIENT_SECRET="your-secret" \
  JWT_SECRET="your-jwt-secret"
```

#### Option 4: Docker + Cloud
```bash
# Pass secrets as environment variables at runtime
docker run -d \
  -e GOOGLE_CLIENT_ID="your-id" \
  -e GOOGLE_CLIENT_SECRET="your-secret" \
  -e JWT_SECRET="your-jwt-secret" \
  your-backend-image
```

---

## 🔍 Secret Detection

### Pre-commit Hook (Recommended)

Install `git-secrets` to prevent accidental commits:

```bash
# Install git-secrets
brew install git-secrets

# Initialize in your repo
cd /path/to/your/repo
git secrets --install
git secrets --register-aws

# Add custom patterns
git secrets --add 'GOOGLE_CLIENT_SECRET=.*'
git secrets --add 'GITHUB_CLIENT_SECRET=.*'
git secrets --add 'JWT_SECRET=.*'
```

### GitHub Secret Scanning

GitHub automatically scans public repositories for leaked secrets. Enable additional features:

1. Go to: `Settings` → `Code security and analysis`
2. Enable **Secret scanning**
3. Enable **Push protection**

---

## 🚨 If You Accidentally Commit Secrets

### Immediate Actions:

1. **Revoke the compromised credentials immediately**
   - Google: Delete OAuth client in Google Cloud Console
   - GitHub: Regenerate client secret
   - Microsoft: Delete client secret in Azure Portal
   - Facebook: Reset app secret

2. **Generate new credentials**
   - Follow `OAUTH_SETUP.md` to create new OAuth apps

3. **Remove from Git history**
   ```bash
   # WARNING: This rewrites Git history
   git filter-branch --force --index-filter \
     "git rm --cached --ignore-unmatch .env" \
     --prune-empty --tag-name-filter cat -- --all
   
   # Force push (coordinate with team first!)
   git push origin --force --all
   ```

4. **Use BFG Repo-Cleaner (faster alternative)**
   ```bash
   # Install BFG
   brew install bfg
   
   # Remove .env from history
   bfg --delete-files .env
   
   # Clean up
   git reflog expire --expire=now --all
   git gc --prune=now --aggressive
   
   # Force push
   git push origin --force --all
   ```

---

## 📋 Security Checklist

Before deploying to production, verify:

- [ ] No `.env` files committed to Git
- [ ] All secrets use environment variables in `application.yml`
- [ ] GitHub Actions secrets configured for frontend deployment
- [ ] Production OAuth redirect URIs updated in provider consoles
- [ ] JWT secret is strong (at least 32 random bytes)
- [ ] Different credentials used for development and production
- [ ] CORS origins restricted to known domains
- [ ] H2 console disabled in production (or secured)
- [ ] HTTPS enforced for production endpoints
- [ ] Secret scanning enabled on GitHub
- [ ] Team members have access to password manager with shared secrets

---

## 🔐 Best Practices

### DO:
✅ Use environment variables for all secrets  
✅ Keep separate credentials for dev/staging/prod  
✅ Rotate secrets regularly (every 90 days)  
✅ Use a password manager for team secret sharing  
✅ Limit OAuth scopes to minimum required  
✅ Enable 2FA on all OAuth provider accounts  
✅ Monitor logs for unauthorized access attempts  
✅ Use HTTPS everywhere in production  

### DON'T:
❌ Commit `.env` files  
❌ Share secrets via email or chat  
❌ Use the same secrets across environments  
❌ Hardcode secrets in source code  
❌ Use weak or predictable JWT secrets  
❌ Leave default credentials unchanged  
❌ Disable security features "temporarily"  
❌ Store secrets in browser localStorage  

---

## 📚 Additional Resources

- [OWASP Security Cheat Sheet](https://cheatsheetseries.owasp.org/)
- [GitHub Secret Scanning](https://docs.github.com/en/code-security/secret-scanning/about-secret-scanning)
- [Spring Boot Security Best Practices](https://spring.io/guides/topicals/spring-security-architecture)
- [OAuth 2.0 Security Best Practices](https://datatracker.ietf.org/doc/html/draft-ietf-oauth-security-topics)

---

## 🆘 Support

If you discover a security vulnerability, please email security@yourcompany.com instead of creating a public issue.

**Last Updated:** 2025-01-16

