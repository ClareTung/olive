## DefaultRedisScript类型报错

* 问题描述
```
java.lang.IllegalStateException: null
```

* 问题原因
    * DefaultRedisScript<Integer>就会报错。原因在于lua并不支持Integer类型
    
* lua类型和java类型一个对应关系

```
// ReturnType.class  
public static ReturnType fromJavaType(@Nullable Class<?> javaType) {
		if (javaType == null) {
			return ReturnType.STATUS;
		}
		if (javaType.isAssignableFrom(List.class)) {
			return ReturnType.MULTI;
		}
		if (javaType.isAssignableFrom(Boolean.class)) {
			return ReturnType.BOOLEAN;
		}
		if (javaType.isAssignableFrom(Long.class)) {
			return ReturnType.INTEGER;
		}
		return ReturnType.VALUE;
}

```

* 解决方法
    * 改成Long返回