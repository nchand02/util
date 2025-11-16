#!/bin/bash
# Install pre-commit hook to prevent committing secrets

echo "🔧 Installing pre-commit hook..."

# Check if .git directory exists
if [ ! -d ".git" ]; then
    echo "❌ Error: .git directory not found. Are you in the project root?"
    exit 1
fi

# Check if pre-commit.sh exists
if [ ! -f "pre-commit.sh" ]; then
    echo "❌ Error: pre-commit.sh not found"
    exit 1
fi

# Copy pre-commit hook
cp pre-commit.sh .git/hooks/pre-commit
chmod +x .git/hooks/pre-commit

echo "✅ Pre-commit hook installed successfully!"
echo ""
echo "The hook will now check for sensitive files before each commit."
echo "To bypass the hook (not recommended): git commit --no-verify"

