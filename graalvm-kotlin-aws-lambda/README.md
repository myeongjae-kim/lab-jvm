# graalvm-kotlin-aws-lambda

JDK21, GraalVM, Kotlin, AWS Lambda, AWS API Gateway, AWS SAM

## Build Command

### Without Lambda Runtime

```bash
./gradlew :graalvm-kotlin-aws-lambda:shadowJar
java -cp graalvm-kotlin-aws-lambda/build/libs/graalvm-kotlin-aws-lambda-all.jar kim.myeongjae.graalvmkotlin.awslambda.MyFunctionKt "first" "second"

native-image -jar graalvm-kotlin-aws-lambda/build/libs/graalvm-kotlin-aws-lambda-all.jar -o graalvm-kotlin/build/libs/graalvm-kotlin-aws-lambda-all
```

### With Lambda Runtime

```bash
./gradlew :graalvm-kotlin-aws-lambda:shadowJar

native-image \
  --enable-url-protocols=http,https \
  -H:+UnlockExperimentalVMOptions \
  -H:ReflectionConfigurationFiles=graalvm-kotlin-aws-lambda/src/main/resources/reflect.json \
  -H:+ReportUnsupportedElementsAtRuntime \
  --no-server \
  -jar graalvm-kotlin-aws-lambda/build/libs/graalvm-kotlin-aws-lambda-all.jar \
  -o graalvm-kotlin/build/libs/graalvm-kotlin-aws-lambda-all
  
_HANDLER=kim.myeongjae.graalvmkotlin.awslambda.MyFunction::handleRequest ./graalvm-kotlin/build/libs/graalvm-kotlin-aws-lambda-all
```

로컬에서 실행할 때는 LambdaContext가 없으므로 아래처럼 보이는게 정상이다.

```bash
$ _HANDLER=kim.myeongjae.graalvmkotlin.awslambda.MyFunction::handleRequest ./graalvm-kotlin/build/libs/graalvm-kotlin-aws-lambda-all                                                                                                                             ─╯

Hello, world!
java.lang.NullPointerException
        at com.formkiq.lambda.runtime.graalvm.LambdaRuntime.invokeClass(LambdaRuntime.java:225)
        at com.formkiq.lambda.runtime.graalvm.LambdaRuntime.invoke(LambdaRuntime.java:169)
        at com.formkiq.lambda.runtime.graalvm.LambdaRuntime.main(LambdaRuntime.java:393)
        at java.base@21.0.1/java.lang.invoke.LambdaForm$DMH/sa346b79c.invokeStaticInit(LambdaForm$DMH)
```

#### Deploy

project root에서

```bash
sam build

AWS_PROFILE=your-profile sam deploy --stack-name GraalvmKotlinAwsLambda --parameter-overrides Stage=staging 
```

배포 관련 파일 목록

- [../samconfig.toml](../samconfig.toml)
- [../template.yaml](../template.yaml)
- [build_graalvm.sh](build_graalvm.sh)
- [Makefile](Makefile)
- [build.gradle.kts](build.gradle.kts)

## [lambda-runtime-graalvm](https://github.com/formkiq/lambda-runtime-graalvm)

이 라이브러리를 쓰는 이유는 function routing을 위해서이다. 

GraalVM으로 컴파일을 하면 실행 가능한 바이너리 파일이 나온다. 프로그램의 entry point가 있어야 바이너리로 컴파일이 가능한데, 하나 하나의 람다 함수마다
gradle module을 만들어 실행파일을 생성할 수는 없는 일.

그리고 AWS Lambda에서 바이너리 파일을 실행하기 위해서는 바이너리 파일 안에서 환경변수로 Lambda를 실행하고 있는 맥락정보에 접근해야 한다. 이런 일들을
lambda-runtime-graalvm이 해준다. [LambdaRuntime.java의 main함수](https://github.com/formkiq/lambda-runtime-graalvm/blob/master/src/main/java/com/formkiq/lambda/runtime/graalvm/LambdaRuntime.java#L381)
에서 시스템 환경변수를 읽어 해시맵을 만들고, `invoke` 메서드를 호출해서 환경변수에 따른 함수를 실행한다. 이 때 reflection을 사용해 클래스를 찾아
인스턴스를 생성하므로 GraalVM으로 컴파일할 때 reflection의 대상이 되는 클래스를 명시해줘야 한다.

[가이드](https://www.formkiq.com/blog/tutorials/aws-lambda-graalvm/)에서는 `src/main/resources/reflect.json`을 생성하고
GraalVM 매개변수로 넣어준다 (`-H:ReflectionConfigurationFiles=/working/src/main/resources/reflect.json`)

객체를 json으로 serialize/deserialize할 때 reflection을 사용하므로 아래를 참고해서 설정한다.

```json lines
[
  // deserialize
  {
    "name": "com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent",
    "allDeclaredConstructors": true,
    "allPublicConstructors": true,
    "allDeclaredMethods": true,
    "allPublicMethods": true,
    "unsafeAllocated": true // 기본 생성자 호출
  },
  // serialize
  {
    "name": "com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent",
    "allDeclaredFields": true,
    "allPublicFields": true
  }
]
```

[lambda-runtime-graalvm](https://github.com/formkiq/lambda-runtime-graalvm)도 충분히 잘 작동할 것 같지만 운영 환경에서 사용하기에는
라이브러리의 스타 수가 너무 적음.. 코드가 간단한 편이라 잘 검토하고 써봐도 될 것 같다. 굳이 프레임워크 쓰는 것보다 간단한데?
찜찜하면 Spring Cloud Function, Quarkus, Micronaut 중에 하나를 선택해서 써도 괜찮을 것 같다. 이 프레임워크들도
lambda-runtime-graalvm가 하는 일들을 해준다. 테스트 환경도 제공해주고.

이 셋 중에서 뭐가 제일 좋을지는 잘 모르겠다. 일단 Spring Cloud Function으로 해보고 latency에 별 문제 없으면 그대로 쓰면 될듯.

- Spring Boot: https://docs.spring.io/spring-boot/docs/current/reference/html/native-image.html
- Quarkus: https://quarkus.io/guides/aws-lambda-http
- Micronaut: https://guides.micronaut.io/latest/mn-application-aws-lambda-graalvm-gradle-java.html

Quarkus, Micronaut 중에는 Quarkus가 더 인기가 좋아보인다.

AWS에서도 위 3개 프레임워크에 대한 예시 코드를 제공한다.
- [Serverless Spring Boot Application Demo](https://github.com/aws-samples/serverless-java-frameworks-samples/tree/main/springboot)
- [Serverless Quarkus Application Demo](https://github.com/aws-samples/serverless-java-frameworks-samples/tree/main/micronaut)
- [Serverless Micronaut Application Demo](https://github.com/aws-samples/serverless-java-frameworks-samples/tree/main/quarkus)

## Reference

- https://www.formkiq.com/blog/tutorials/aws-lambda-graalvm/
- https://medium.com/@robertoperezrodriguez_37307/improving-aws-lambda-performance-with-native-spring-boot-3-cloud-functions-d21a70952ce3
