# AWS Deployment Guide for Game of War

This guide provides multiple options for deploying the Game of War web application to AWS.

## Prerequisites

- AWS Account
- AWS CLI installed and configured
- Java 21 installed locally
- Gradle installed locally

## Option 1: AWS Elastic Beanstalk (Recommended for Simplicity)

AWS Elastic Beanstalk is the easiest way to deploy Java applications.

### Steps:

1. **Build the application:**
   ```bash
   .\gradlew.bat clean build
   ```

2. **Install EB CLI:**
   ```bash
   pip install awsebcli
   ```

3. **Initialize Elastic Beanstalk:**
   ```bash
   eb init -p "Corretto 21 running on 64bit Amazon Linux 2023" game-of-war
   ```

4. **Create an environment and deploy:**
   ```bash
   eb create game-of-war-env
   ```

5. **Open the application:**
   ```bash
   eb open
   ```

6. **For updates:**
   ```bash
   .\gradlew.bat clean build
   eb deploy
   ```

### Configuration

The `.ebextensions/application.config` file configures:
- Java 21 runtime
- Port 5000
- NGINX proxy

## Option 2: AWS ECS with Docker (For Containerized Deployment)

Use AWS ECS (Elastic Container Service) for a containerized deployment.

### Steps:

1. **Build the Docker image:**
   ```bash
   docker build -t game-of-war .
   ```

2. **Test locally:**
   ```bash
   docker run -p 5000:5000 game-of-war
   ```
   Visit http://localhost:5000

3. **Push to Amazon ECR:**
   ```bash
   # Authenticate Docker to ECR
   aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin <your-account-id>.dkr.ecr.us-east-1.amazonaws.com

   # Create ECR repository
   aws ecr create-repository --repository-name game-of-war --region us-east-1

   # Tag and push
   docker tag game-of-war:latest <your-account-id>.dkr.ecr.us-east-1.amazonaws.com/game-of-war:latest
   docker push <your-account-id>.dkr.ecr.us-east-1.amazonaws.com/game-of-war:latest
   ```

4. **Create ECS Task Definition and Service** (via AWS Console or CLI)

## Option 3: AWS App Runner (Easiest Container Deployment)

AWS App Runner automatically builds and deploys from source.

### Steps:

1. **Push code to GitHub**

2. **Create App Runner service:**
   - Go to AWS App Runner console
   - Create service from source code
   - Connect to GitHub repository
   - Configure:
     - Runtime: Java 21
     - Build command: `./gradlew build`
     - Start command: `java -jar build/libs/gameOfWar-1.0-SNAPSHOT.jar`
     - Port: 5000

3. **Deploy** - App Runner will automatically build and deploy

## Option 4: AWS Lambda with Spring Cloud Function (Serverless)

For a serverless approach, you can adapt the application to use AWS Lambda.

This requires additional refactoring to work with Lambda's request/response model.

## Environment Variables

For production, configure these environment variables:

```bash
SERVER_PORT=5000
SPRING_PROFILES_ACTIVE=prod
```

## Cost Considerations

- **Elastic Beanstalk**: ~$20-50/month (t3.small instance)
- **ECS Fargate**: ~$15-40/month (minimal resources)
- **App Runner**: Pay per request, ~$5-30/month for low traffic
- **Lambda**: Pay per invocation, very cheap for low traffic

## Monitoring

All options support CloudWatch for logs and metrics:

```bash
# Elastic Beanstalk logs
eb logs

# ECS/Fargate logs
View in CloudWatch console

# App Runner logs
View in App Runner console
```

## Health Checks

The application exposes a health endpoint at:
```
GET /actuator/health
```

This is used by AWS load balancers and container orchestrators.

## SSL/HTTPS

For production, enable HTTPS:

1. **Elastic Beanstalk:**
   - Add a load balancer
   - Configure SSL certificate in AWS Certificate Manager
   - Update `.ebextensions` to redirect HTTP to HTTPS

2. **App Runner:**
   - Custom domain with SSL is included by default

3. **ECS:**
   - Use Application Load Balancer
   - Configure SSL certificate

## Custom Domain

1. Register domain in Route 53 (or use existing domain)
2. Create hosted zone
3. Point domain to your AWS service:
   - **EB**: Use CNAME to EB environment URL
   - **App Runner**: Use custom domain feature
   - **ECS**: Point to ALB DNS name

## Troubleshooting

### Application won't start
- Check CloudWatch logs
- Verify Java 21 is being used
- Ensure port 5000 is exposed

### 502 Bad Gateway
- Check application health endpoint
- Verify application is listening on correct port
- Check security group rules

### Out of Memory
- Increase instance size or container memory
- Add JVM options: `-Xmx512m`

## Next Steps

1. Set up CI/CD with GitHub Actions or CodePipeline
2. Add database (RDS) if needed for game persistence
3. Configure auto-scaling
4. Set up monitoring and alerts
5. Implement caching with ElastiCache if needed

## GitHub Actions CI/CD

### Automated CI - `.github/workflows/ci.yml`
Runs automatically on every push: builds, tests, reports failures. No AWS or secrets required.

### Manual AWS Deployment - `.github/workflows/aws-deploy.yml`
To enable manual AWS deployment:

1. **Configure AWS Secrets** in your repository:
   - Go to: `Settings` > `Secrets and variables` > `Actions`
   - Add secrets:
     - `AWS_ACCESS_KEY_ID`
     - `AWS_SECRET_ACCESS_KEY`

2. **Create AWS resources** first:
   ```bash
   eb init -p "Corretto 21" game-of-war
   eb create game-of-war-env
   ```

3. **Trigger deployment manually**:
   - Go to: `Actions` > `Deploy to AWS`
   - Click `Run workflow`
   - Select branch and run

**Note:** AWS deployment does NOT run automatically on push. It must be triggered manually from the GitHub Actions UI to prevent accidental deployments and AWS charges.

## Support

For issues, check:
- CloudWatch Logs
- AWS Support
- Application logs via `eb logs` or CloudWatch

