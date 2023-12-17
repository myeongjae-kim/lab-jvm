# lab-jvm

## GraalVM

```bash
brew install --cask graalvm-jdk
```

```bash
# ~/.zshrc
alias gvm='export JAVA_HOME=/Library/Java/JavaVirtualMachines/graalvm-21.jdk/Contents/Home; export PATH=/Library/Java/JavaVirtualMachines/graalvm-21.jdk/Contents/Home/bin:$PATH'
```

### Deploy graalvm-kotlin-aws-lambda

```bash
sam build
AWS_PROFILE=your-profile sam deploy --stack-name GraalvmKotlinAwsLambda --parameter-overrides Stage=staging 
```
