#!/bin/bash
# Pre-commit hook to prevent committing sensitive files
# Install: cp pre-commit.sh .git/hooks/pre-commit && chmod +x .git/hooks/pre-commit

RED='\033[0;31m'
YELLOW='\033[1;33m'
GREEN='\033[0;32m'
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

# Files that are safe to contain secret variable names (documentation, templates, etc.)
SAFE_FILES=(
    ".env.template"
    ".env.example"
    "SECURITY.md"
    "SECURITY_ALERT.md"
    "SECURITY_IMPLEMENTATION_SUMMARY.md"
    "OAUTH_SETUP.md"
    "GITHUB_SETUP.md"
    "README.md"
    "frontend/README.md"
    "pre-commit.sh"
    "install-hooks.sh"
    "application.yml"
    "application-template.yml"
)

# Patterns that indicate actual secrets (not just variable names)
FORBIDDEN_PATTERNS=(
    "GOCSPX-[A-Za-z0-9_-]{28}"              # Google OAuth secret format
    "ghp_[A-Za-z0-9]{36}"                    # GitHub personal access token
    "github_pat_[A-Za-z0-9]{22}_[A-Za-z0-9]{59}"  # GitHub fine-grained PAT
    "-----BEGIN PRIVATE KEY-----"
    "-----BEGIN RSA PRIVATE KEY-----"
    "-----BEGIN OPENSSH PRIVATE KEY-----"
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

# Function to check if a file is in the safe list
is_safe_file() {
    local file="$1"
    for safe_file in "${SAFE_FILES[@]}"; do
        if [[ "$file" == *"$safe_file" ]] || [[ "$file" == "$safe_file" ]]; then
            return 0  # File is safe
        fi
    done
    return 1  # File is not safe
}

# Check for forbidden patterns in staged files (excluding safe files)
echo "🔎 Scanning for actual secrets (excluding documentation and templates)..."

for pattern in "${FORBIDDEN_PATTERNS[@]}"; do
    # Get list of files that match the pattern
    matched_files=$(git diff --cached --name-only -G"${pattern}" 2>/dev/null)

    if [ -n "$matched_files" ]; then
        # Check each matched file
        unsafe_files=""
        while IFS= read -r file; do
            if ! is_safe_file "$file"; then
                # Check if the pattern appears in the actual diff (not just context)
                if git diff --cached -U0 "$file" | grep -E "^\+.*${pattern}" > /dev/null; then
                    unsafe_files+="  - $file"$'\n'
                fi
            fi
        done <<< "$matched_files"

        if [ -n "$unsafe_files" ]; then
            echo -e "${RED}❌ ERROR: Found potential secret pattern in staged files${NC}"
            echo -e "${YELLOW}Pattern detected: ${pattern}${NC}"
            echo ""
            echo "Files containing this pattern:"
            echo -e "$unsafe_files"
            echo ""
            echo "To fix this:"
            echo "  1. Remove the sensitive data from the file"
            echo "  2. Use environment variables instead"
            echo "  3. Commit again"
            echo ""
            echo "If this is a false positive, add the file to SAFE_FILES in pre-commit.sh"
            exit 1
        fi
    fi
done

# Check if .env files are in .gitignore
if [ -f .gitignore ]; then
    if ! grep -q "^\.env$" .gitignore; then
        echo -e "${YELLOW}⚠️  WARNING: .env is not in .gitignore${NC}"
        echo "Add .env to .gitignore to prevent accidental commits"
    fi
fi

echo -e "${GREEN}✅ Pre-commit checks passed! No secrets detected.${NC}"
echo "   Documentation and template files are allowed to contain variable names."
exit 0

