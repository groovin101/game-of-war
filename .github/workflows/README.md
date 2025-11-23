# GitHub Actions Workflows

This directory contains GitHub Actions workflows for CI/CD.

## Workflows

### 1. `ci.yml` - Continuous Integration
✅ Automatic on every push and pull request

**What it does:**
- Builds application with Gradle
- Runs all tests
- Uploads test reports on failure

**What it does NOT do:**
- No AWS deployment
- No secrets required
- No costs

**Setup:** None - works out of the box!

See file comments for details.

---

### 2. `aws-deploy.yml` - AWS Deployment
🔒 Manual trigger only (never automatic)

**What it does:**
- Builds application
- Deploys to AWS Elastic Beanstalk

**Requirements:**
- AWS credentials as GitHub secrets
- AWS resources created first
- 💰 Incurs AWS costs

**Setup:** See [GITHUB-ACTIONS.md](../../GITHUB-ACTIONS.md)

---

## Setup Instructions

For detailed setup instructions, see: **[GITHUB-ACTIONS.md](../../GITHUB-ACTIONS.md)** in the root directory.

### Quick Links:
- **CI Setup:** None needed! Already working ✅
- **AWS Setup:** See [GITHUB-ACTIONS.md](../../GITHUB-ACTIONS.md) for step-by-step guide

---

## Important Notes

✅ **CI is safe** - Runs automatically, no AWS involved

🔒 **AWS is opt-in** - Only runs when you manually trigger it after setup

---

## Modifying Workflows

### To change CI behavior:
Edit `ci.yml` - builds and tests only, no AWS

### To change AWS deployment:
Edit `aws-deploy.yml` - only runs when manually triggered

### To add a new workflow:
Create a new `.yml` file in this directory following GitHub Actions syntax.

