package kim.myeongjae.kotlin.inlinelcasss

@JvmInline
value class InlineClass(private val value: String) {
    fun getValue(): String = value
}

/*
프로퍼티를 하나만 갖는 value class를 선언할 수 있다. `typealias`와 유사한 용도로 사용하면 된다. 어떤 타입을 다른 이름으로 선언해서 도메인 언어를 더
풍부하게 사용하고 싶을 때 `value class`를 사용하면 됨. `typealias`와 다른 점은 다른 타입을 할당할 수 없다는 점이다.
https://kotlinlang.org/docs/inline-classes.html#inline-classes-vs-type-aliases

```kotlin
var password = Password("don't do this on production")
password = "plain string" // 불가능
```

inline class라고 불리는 이유는 실행시간(runtime)에 `Password`라는 클래스의 인스턴스를 생성하는게 아니기 때문이다.

```kotlin
class UseInlineClass {
    fun useInlineClass() {
        val inlineClass = InlineClass("inline class")
        println(inlineClass.getValue())

        val basicClass = BasicClass("basic class")
        println(basicClass.getValue())
    }
}
```

위 코드를 컴파일하고 자바 코드로 디컴파일하면 아래처럼 나온다.

```java
public final class UseInlineClass {
   public final void useInlineClass() {
      String inlineClass = InlineClass.constructor-impl("inline class");
      System.out.println(InlineClass.getValue-impl(inlineClass));

      BasicClass basicClass = new BasicClass("basic class");
      System.out.println(basicClass.getValue());
   }
}
```

`new`로 클래스를 만드는게 아니라 `constructor-impl`함수를 호출해서 `String`을 받는다. `constructor-impl`함수는 아래처럼 생겼음.

```java
public final class InlineClass {
  ...

@NotNull
  public static String constructor_impl/* $FF was: constructor-impl*/(@NotNull String value) {
     Intrinsics.checkNotNullParameter(value, "value");
     return value;
  }

  ...
}
```

함수 호출을 하는 오버헤드는 있지만 객체 생성을 하지 않기 때문에 추가로 메모리를 사용하지 않는다.
 */
