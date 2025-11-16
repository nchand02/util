#!/bin/bash
# Pre-commit hook to prevent committing sensitive files
# Install: cp pre-commit.sh .git/hooks/pre-commit && chmod +x .git/hooks/pre-commit

RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Files that should never be committed
FORBIDDEN_FILES=(
    ".env"
    ".env.local"
    ".env.production"
    ".env.development"
    "application-local.yml"
    "application-secrets.yml"
)

# Patterns to check in staged files
FORBIDDEN_PATTERNS=(
    "GOOGLE_CLIENT_SECRET"
    "GITHUB_CLIENT_SECRET"
    "MICROSOFT_CLIENT_SECRET"
    "FACEBOOK_CLIENT_SECRET"
    "JWT_SECRET"
    "-----BEGIN PRIVATE KEY-----"
    "-----BEGIN RSA PRIVATE KEY-----"
)

echo "🔍 Checking for sensitive files..."

# Check for forbidden files
for file in "${FORBIDDEN_FILES[@]}"; do
    if git diff --cached --name-only | grep -q "^${file}$"; then
        echo -e "${RED}❌ ERROR: Attempting to commit forbidden file: ${file}${NC}"
        echo -e "${YELLOW}This file contains sensitive information and should not be committed.${NC}"
        echo ""
        echo "To fix this:"
        echo "  1. Run: git reset HEAD ${file}"
        echo "  2. Verify ${file} is in .gitignore"
        echo "  3. Commit again"
        exit 1
    fi
done

# Check for forbidden patterns in staged files
for pattern in "${FORBIDDEN_PATTERNS[@]}"; do
    if git diff --cached | grep -q "${pattern}"; then
        echo -e "${RED}❌ ERROR: Found potential secret in staged files: ${pattern}${NC}"
        echo -e "${YELLOW}You may be attempting to commit sensitive information.${NC}"
        echo ""
        echo "Files containing this pattern:"
        git diff --cached --name-only -G"${pattern}"
        echo ""
        echo "To fix this:"
        echo "  1. Remove the sensitive data from the file"
        echo "  2. Use environment variables instead"
        echo "  3. Commit again"
        exit 1
    fi
done

# Check if .env files are in .gitignore
if [ -f .gitignore ]; then
    if ! grep -q "^\.env$" .gitignore; then
        echo -e "${YELLOW}⚠️  WARNING: .env is not in .gitignore${NC}"
        echo "Add .env to .gitignore to prevent accidental commits"
    fi
fi

echo "✅ Pre-commit checks passed!"
exit 0

