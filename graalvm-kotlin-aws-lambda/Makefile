CUR_DIR := $(abspath $(patsubst %/,%,$(dir $(abspath $(lastword $(MAKEFILE_LIST))))))

build-GraalvmKotlinAwsLambda:
	cd $(CUR_DIR) && ../gradlew :graalvm-kotlin-aws-lambda:build
	cp $(CUR_DIR)/build/graalvm/server $(ARTIFACTS_DIR)
	cp $(CUR_DIR)/bootstrap $(ARTIFACTS_DIR)

deploy:
	sam build && sam deploy --stack-name "GraalvmKotlinAwsLambda-${PROFILE}" --parameter-overrides Profile="${PROFILE}"

delete:
	sam delete --stack-name "GraalvmKotlinAwsLambda-${PROFILE}"

deploy-prod:
	PROFILE=prod make deploy

delete-prod:
	PROFILE=prod make delete

deploy-staging:
	PROFILE=staging make deploy

delete-staging:
	PROFILE=staging make delete
