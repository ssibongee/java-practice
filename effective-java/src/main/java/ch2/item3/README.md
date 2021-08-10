# 아이템 3. private 생성자나 열거 타입으로 싱글턴임을 보증하라

### 싱글턴
- 인스턴스를 오직 하나만 생성할 수 있는 클래스를 의미한다.
- 무상태 객체나 설계상 유일해야하는 시스템 컴포넌트를 싱글톤으로 설계할 수 있다.
- 싱글턴으로 생성하면 이를 사용하는 클라이언트를 테스트하기가 어려워질 수 있다.

<br>

#### 싱글턴이 테스트하기 어려운 이유
- 인터페이스를 구현한 싱글턴이 아닌 경우 싱글턴 인스턴스를 `Mock` 객체로 대체할 수 없다.
- 해당 클래스를 상속받는 클래스를 생성하려고 해도 `private` 으로 기본 생성자가 막혀있기 때문에 부모 클래스의 생성자를 호출할 수 없어서 불가능하다.

<br>

### `public static final` 필드 방식의 싱글턴

```java
public class Singleton {
    public static final Singleton INSTANCE = new Singleton();
    
    private Singleton() { };
}
```

- `public static final` 필드를 초기화 할 때 한번만 생성자가 호출되기 때문에 인스턴스가 전체 시스템에서 단 한개만 존재하는 것이 보장된다.
- 권한이 있는 클라이언트의 경우 예외적으로 리플렉션 API를 이용해서 `private` 생성자를 호출할 수 있지만 이를 예방하기 위해서 객체가 두 번 생성될 때 예외를 발생시킬 수 있다.
- 해당 클래스가 싱글턴 클래스임이 API에 명백하게 들어난다.
- 싱글턴 클래스를 간결하게 작성할 수 있다.

<br>

### 정적 팩터리 방식의 싱글턴 

```java
public class Singleton {
    private static final Singleton INSTANCE = new Singleton();
    
    private Singleton() { };
    
    public static Singleton getInstance() {
        return INSTANCE;
    }
}
```
- 인스턴스 필드를 `private`으로 선언하고 정적 팩터리 메서드를 통해서만 이를 제공한다.
- API를 변경하지 않고도 원하는 경우 싱글턴이 아니게 변경할 수 있다.
- 정적 팩터리를 제네릭 싱글턴 팩터리로 만들 수 있다.
- 정적 팩터리를 메서드 참조를 `Supplier`로 사용할 수 있다. 

<br>

### 싱글턴 클래스의 직렬화 
- 싱글턴 클래스를 직렬화 할 때 `Serializable`을 구현한다고 선언하는 것 만으로는 부족하다.
- 단순히 위의 방법으로 직렬화를 수행하려고 할 때 예상과는 다르게 역직렬화 한 객체의 결과가 서로다른 인스턴스임을 확인할 수 있다.
- 즉, 역직렬화 할 때마다 객체가 새로운 인스턴스를 생성하는 꼴이 되어버리기 때문에 이를 방지하기 위해서는 다음과 같은 추가적인 방법이 필요하다.
- 모든 인스턴스 필드를 `transient`라고 선언한 다음 `readResolve()` 메서드를 제공해야한다.
```java
public class Singleton {
    private transient static final Singleton INSTANCE = new Singleton();
    
    // ...
    
    private Object readResolve() {
        return INSTANCE;
    }
}
``` 
- 이러한 복잡한 방법을 사용하지 않고 원소가 하나인 `Enum`을 사용해서 싱글턴을 선언한다면 `public` 필드 방식과 비슷하지만 더 간결하고 추가 노력 없이 직렬화가 가능하다.
- 뿐만 아니라 복잡한 직렬화 상황이나 리플렉션 공격에도 또 다른 인스턴스가 생성되는 것을 완벽하게 막아주기 때문에 가장 좋은 방법이 될 수 있다.
- 하지만 만드려고 하는 싱글턴이 `Enum` 외의 클래스를 상속해야하는 경우에는 이 방법을 사용할 수 없다.