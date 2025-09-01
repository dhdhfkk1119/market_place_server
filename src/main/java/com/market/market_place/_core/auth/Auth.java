package com.market.market_place._core.auth;

import com.market.market_place.members.domain.Role;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) // @Auth 어노테이션을 메소드에만 붙이겠다
@Retention(RetentionPolicy.RUNTIME) // 런타임에 이 정보를 JVM이 볼수있다
public @interface Auth {
    Role[] roles() default {}; // 독립된 Role 열거형을 사용하도록 수정
    boolean isOwner() default false; // 리소스 소유자 확인 여부
}
