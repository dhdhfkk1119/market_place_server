package com.market.market_place._core.auth;

import com.market.market_place.members.domain.Member.MemberRole;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) // @Auth 어노테이션을 메소드에만 붙이겠다
@Retention(RetentionPolicy.RUNTIME) // 런타임에 이 정보를 JVM이 볼수있다
public @interface Auth {
    MemberRole[] roles() default {}; // 역할 지정 허용
}
