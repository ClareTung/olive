package com.olive.springframwork.beans.factory;

import com.olive.springframwork.beans.BeansException;

/**
 * 类BeanFactory的实现描述：bean工厂
 *
 * @author dongtangqiang 2022/5/26 22:05
 */
public interface BeanFactory {

    Object getBean(String name) throws BeansException;

    Object getBean(String name, Object... args)throws BeansException;

    <T> T getBean(String name, Class<T> requiredType) throws BeansException;

    <T> T getBean(Class<T> requiredType) throws BeansException;

    /**
     * Does this bean factory contain a bean definition or externally registered singleton
     * instance with the given name?
     * <p>If the given name is an alias, it will be translated back to the corresponding
     * canonical bean name.
     * <p>If this factory is hierarchical, will ask any parent factory if the bean cannot
     * be found in this factory instance.
     * <p>If a bean definition or singleton instance matching the given name is found,
     * this method will return {@code true} whether the named bean definition is concrete
     * or abstract, lazy or eager, in scope or not. Therefore, note that a {@code true}
     * return value from this method does not necessarily indicate that {@link #getBean}
     * will be able to obtain an instance for the same name.
     * @param name the name of the bean to query
     * @return whether a bean with the given name is present
     */
    boolean containsBean(String name);
}