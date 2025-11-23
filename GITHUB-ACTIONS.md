# GitHub Actions Setup Guide

This repository includes two GitHub Actions workflows:

## 1. ✅ CI Workflow (Automatic - Already Working!)

**File:** `.github/workflows/ci.yml`

Runs automatically on every push and pull request.

**What it does:**
- Builds the application with Gradle
- Runs all unit tests
- Uploads test reports on failure

**What it does NOT do:**
- No AWS deployment
- No secrets required
- No costs

**Setup:** None! Works out of the box.

---

## 2. 🚀 AWS Deployment Workflow (Manual Only)

**File:** `.github/workflows/aws-deploy.yml`

Runs only when manually triggered from GitHub Actions UI.

**What it does:**
- Builds the application
- Deploys to AWS Elastic Beanstalk

**Requirements:**
- AWS credentials configured as GitHub secrets
- AWS resources created first
- 💰 Incurs AWS costs

**Setup:** See steps below

### Prerequisites for AWS Deployment

Before enabling AWS deployment, you need:

1. **AWS Account** with Elastic Beanstalk access
2. **AWS CLI** and **EB CLI** installed locally
3. **AWS resources created** (one-time setup)

### Step-by-Step Setup

#### Step 1: Create AWS Resources Locally

First, create your AWS Elastic Beanstalk application and environment:

```bash
# Install EB CLI
pip install awsebcli

# Build your application
.\gradlew.bat build

# Initialize Elastic Beanstalk
eb init -p "Corretto 21" game-of-war --region us-east-1

# Create environment (this deploys your app for the first time)
eb create game-of-war-env
```

This creates:
- Elastic Beanstalk application named `game-of-war`
- Environment named `game-of-war-env`
- All necessary AWS resources

#### Step 2: Create AWS Access Keys

1. Go to AWS Console → IAM
2. Create a new user or use existing user
3. Attach policy: `AWSElasticBeanstalkFullAccess`
4. Create access key
5. **Save** the Access Key ID and Secret Access Key (you won't see the secret again!)

#### Step 3: Configure GitHub Secrets

1. Go to your GitHub repository
2. Click: `Settings` → `Secrets and variables` → `Actions`
3. Click: `New repository secret`
4. Add two secrets:

   **Secret 1:**
   - Name: `AWS_ACCESS_KEY_ID`
   - Value: Your AWS Access Key ID

   **Secret 2:**
   - Name: `AWS_SECRET_ACCESS_KEY`
   - Value: Your AWS Secret Access Key

#### Step 4: Manually Trigger Deployment

Now you can deploy to AWS from GitHub:

1. Go to your repository on GitHub
2. Click the `Actions` tab
3. Select `Deploy to AWS` workflow (left sidebar)
4. Click `Run workflow` button (right side)
5. Select branch (usually `main`)
6. Click green `Run workflow` button

GitHub Actions will:
- Build your application
- Package it
- Deploy to AWS Elastic Beanstalk
- Show progress in real-time

### Verifying Deployment

After deployment completes:

```bash
# Check status
eb status

# Open in browser
eb open
```

Your application will be live at the provided URL!

---

## Important Notes

### ✅ Automatic (No Setup)
- CI builds and tests on every push
- No AWS secrets or costs

### 🔒 Manual (Setup Required)
- AWS deployment when you trigger it
- Requires AWS secrets and resources
- Incurs AWS costs

### 💰 AWS Cost Estimates

When you deploy to AWS Elastic Beanstalk:
- **t3.small instance**: ~$20-50/month
- **Load balancer** (if enabled): ~$16/month
- **CloudWatch logs**: ~$0.50-5/month

**Total estimated**: $20-70/month depending on configuration and traffic.

You can stop/delete the environment anytime:
```bash
eb terminate game-of-war-env
```

---

## Troubleshooting

### "Deploy to AWS" workflow fails

**If you haven't set up AWS:**
- This is expected! The workflow only works after AWS setup
- Follow the setup steps above
- Or ignore it - it only runs when manually triggered

**If you have set up AWS:**
- Verify secrets are configured in GitHub
- Check AWS credentials are valid
- Ensure application name matches: `game-of-war`
- Ensure environment name matches: `game-of-war-env`

### CI workflow fails

**Build or test failures:**
- Check the logs in GitHub Actions
- Run `.\gradlew.bat test` locally to reproduce
- Fix the issue and push again

### Need to deploy to different AWS region?

Edit `.github/workflows/aws-deploy.yml`:
```yaml
region: us-west-2  # Change from us-east-1
```

And initialize EB CLI with same region:
```bash
eb init -p "Corretto 21" game-of-war --region us-west-2
```

---

## Summary

### For Normal Development
✅ Just push code - CI runs automatically  
✅ No AWS setup needed  
✅ No costs  

### For AWS Deployment
⚙️ Follow 4-step setup (one time)  
🚀 Manually trigger deployments from GitHub UI  
💰 AWS charges apply  

---

## Next Steps

1. **Right now:** Push your changes - CI will run and verify everything works
2. **Later:** When ready for AWS, follow Step-by-Step Setup above
3. **Anytime:** Manually trigger AWS deployments from GitHub Actions UI

For more AWS deployment options, see **[deploy-aws.md](deploy-aws.md)**

