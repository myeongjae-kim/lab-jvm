CUR_DIR := $(abspath $(patsubst %/,%,$(dir $(abspath $(lastword $(MAKEFILE_LIST))))))

build-GraalvmKotlinAwsLambda:
	cd $(CUR_DIR) && ../gradlew :graalvm-kotlin-aws-lambda:build
	cp $(CUR_DIR)/build/graalvm/server $(ARTIFACTS_DIR)
	cp $(CUR_DIR)/bootstrap $(ARTIFACTS_DIR)
