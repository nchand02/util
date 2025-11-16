# 🚀 GitHub Repository Setup Guide

This guide will help you configure GitHub secrets for secure deployment.

---

## 📋 Prerequisites

- GitHub repository created (public or private)
- Code pushed to repository
- Admin access to repository settings

---

## 🔐 Setting Up GitHub Secrets

GitHub Secrets allow you to store sensitive information securely and use them in GitHub Actions without exposing them in your code.

### Step 1: Navigate to Repository Settings

1. Go to your GitHub repository
2. Click on **Settings** tab
3. In the left sidebar, click **Secrets and variables** → **Actions**

### Step 2: Add Repository Secrets

Click **New repository secret** button and add each of the following secrets:

#### Frontend Deployment Secrets

| Secret Name | Description | Example Value |
|-------------|-------------|---------------|
| `REACT_APP_API_URL` | Production backend API URL | `https://your-backend.herokuapp.com` |
| `PUBLIC_URL` | GitHub Pages path | `/your-repo-name` |

**To add each secret:**
1. Click **New repository secret**
2. Enter the **Name** (e.g., `REACT_APP_API_URL`)
3. Enter the **Value** (e.g., `https://your-backend.herokuapp.com`)
4. Click **Add secret**

### Step 3: Enable GitHub Pages

1. Go to **Settings** → **Pages**
2. Under **Source**, select **GitHub Actions**
3. Save the changes

### Step 4: Configure Workflow Permissions

1. Go to **Settings** → **Actions** → **General**
2. Scroll to **Workflow permissions**
3. Select **Read and write permissions**
4. Check **Allow GitHub Actions to create and approve pull requests**
5. Click **Save**

---

## 🔄 Triggering Deployment

### Automatic Deployment

The frontend will automatically deploy to GitHub Pages when you push changes to the `main` branch:

```bash
git add .
git commit -m "Update frontend"
git push origin main
```

### Manual Deployment

1. Go to **Actions** tab
2. Click **Deploy Frontend to GitHub Pages**
3. Click **Run workflow**
4. Select branch and click **Run workflow**

---

## 🌐 Accessing Your Deployed Application

After successful deployment, your frontend will be available at:

```
https://<your-github-username>.github.io/<your-repo-name>/
```

Example: `https://johndoe.github.io/guest-management/`

---

## 🔍 Verifying Secrets

### Check if Secrets are Set

1. Go to **Settings** → **Secrets and variables** → **Actions**
2. You should see your secrets listed (values will be hidden)

### Test Deployment

1. Make a small change to frontend code
2. Push to main branch
3. Go to **Actions** tab
4. Watch the workflow run
5. Check for any errors in the logs

---

## 🐛 Troubleshooting

### Issue: Workflow fails with "Secret not found"

**Solution:** Verify secret names match exactly (case-sensitive):
- `REACT_APP_API_URL` (not `REACT_APP_API_Url`)
- Check for extra spaces in secret names

### Issue: 404 error after deployment

**Solution:** Update `PUBLIC_URL` secret:
```
PUBLIC_URL=/your-repo-name
```

Also update `package.json` homepage:
```json
{
  "homepage": "https://your-username.github.io/your-repo-name"
}
```

### Issue: Workflow permission denied

**Solution:** 
1. Go to **Settings** → **Actions** → **General**
2. Enable **Read and write permissions**

### Issue: Backend CORS errors

**Solution:** Update your backend's `application.yml`:
```yaml
app:
  cors:
    allowed-origins: https://your-username.github.io
  oauth2:
    authorized-redirect-uris:
      - https://your-username.github.io/your-repo-name/oauth2/redirect
```

Also update OAuth provider redirect URIs to include production URL.

---

## 📊 Monitoring Deployments

### View Deployment History

1. Go to **Actions** tab
2. Click on any workflow run to see details
3. Check logs for each step

### View Deployment Status

1. Go to **Settings** → **Pages**
2. You'll see the deployment status and URL

---

## 🔒 Security Best Practices

### ✅ DO:
- Use GitHub Secrets for all sensitive data
- Regularly rotate secrets
- Use different secrets for staging and production
- Review workflow logs for sensitive data leaks
- Enable branch protection on main/master

### ❌ DON'T:
- Commit secrets to `.env` files
- Print secrets in workflow logs
- Share secrets via insecure channels
- Use the same secrets across environments
- Disable security features

---

## 🔄 Updating Secrets

To update a secret:

1. Go to **Settings** → **Secrets and variables** → **Actions**
2. Click on the secret name
3. Click **Update secret**
4. Enter new value
5. Click **Update secret**

**Note:** Updating a secret does not trigger a new workflow run. You must push new code or manually trigger the workflow.

---

## 📱 Production Checklist

Before going live, verify:

- [ ] All GitHub secrets configured
- [ ] Workflow permissions enabled
- [ ] GitHub Pages source set to GitHub Actions
- [ ] Backend CORS configured for production URL
- [ ] OAuth redirect URIs updated for production
- [ ] Production backend URL is HTTPS
- [ ] Frontend builds successfully
- [ ] Manual test of production deployment
- [ ] All API endpoints tested from production frontend
- [ ] Mobile responsiveness verified
- [ ] Error pages tested

---

## 📚 Additional Resources

- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [GitHub Pages Documentation](https://docs.github.com/en/pages)
- [Encrypted Secrets](https://docs.github.com/en/actions/security-guides/encrypted-secrets)
- [Security Hardening](https://docs.github.com/en/actions/security-guides/security-hardening-for-github-actions)

---

## 🆘 Need Help?

If you encounter issues:

1. Check workflow logs in the **Actions** tab
2. Review `SECURITY.md` for secret management
3. Verify all secrets are set correctly
4. Check backend CORS configuration
5. Test backend API separately

**Last Updated:** 2025-01-16

