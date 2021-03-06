package com.jum.common.exception.common;

import org.springframework.core.NestedExceptionUtils;

/**
 * @ClassName CommonCheckException
 * @Description
 * Handy class for wrapping checked {@code Exceptions} with a root cause.
 *
 * <p>This class is {@code abstract} to force the programmer to extend
 * the class. {@code getMessage} will include nested exception
 * information; {@code printStackTrace} and other like methods will
 * delegate to the wrapped exception, if any.
 *
 * <p>The similarity between this class and the {@link CommonRuntimeException}
 * class is unavoidable, as Java forces these two classes to have different
 * superclasses (ah, the inflexibility of concrete inheritance!).
 * @Author jb.zhou
 * @Date 2020/2/28
 * @Version 1.0
 * @see #getMessage
 * @see #printStackTrace
 * @see CommonRuntimeException
 */
public abstract class CommonCheckedException extends Exception {

    /** Use serialVersionUID from Spring 1.2 for interoperability */
    private static final long serialVersionUID = 7100714597678207546L;

    static {
        // Eagerly load the CommonExceptionUtils class to avoid classloader deadlock
        // issues on OSGi when calling getMessage(). Reported by Don Brown; SPR-5607.
        CommonExceptionUtils.class.getName();
    }


    /**
     * Construct a {@code CommonCheckedException} with the specified detail message.
     * @param msg the detail message
     */
    public CommonCheckedException(String msg) {
        super(msg);
    }

    /**
     * Construct a {@code CommonCheckedException} with the specified detail message
     * and nested exception.
     * @param msg the detail message
     * @param cause the nested exception
     */
    public CommonCheckedException(String msg, Throwable cause) {
        super(msg, cause);
    }


    /**
     * Return the detail message, including the message from the nested exception
     * if there is one.
     */
    @Override
    public String getMessage() {
        return NestedExceptionUtils.buildMessage(super.getMessage(), getCause());
    }


    /**
     * Retrieve the innermost cause of this exception, if any.
     * @return the innermost exception, or {@code null} if none
     */
    public Throwable getRootCause() {
        Throwable rootCause = null;
        Throwable cause = getCause();
        while (cause != null && cause != rootCause) {
            rootCause = cause;
            cause = cause.getCause();
        }
        return rootCause;
    }

    /**
     * Retrieve the most specific cause of this exception, that is,
     * either the innermost cause (root cause) or this exception itself.
     * <p>Differs from {@link #getRootCause()} in that it falls back
     * to the present exception if there is no root cause.
     * @return the most specific cause (never {@code null})
     * @since 2.0.3
     */
    public Throwable getMostSpecificCause() {
        Throwable rootCause = getRootCause();
        return (rootCause != null ? rootCause : this);
    }

    /**
     * Check whether this exception contains an exception of the given type:
     * either it is of the given class itself or it contains a nested cause
     * of the given type.
     * @param exType the exception type to look for
     * @return whether there is a nested exception of the specified type
     */
    public boolean contains(Class<?> exType) {
        if (exType == null) {
            return false;
        }
        if (exType.isInstance(this)) {
            return true;
        }
        Throwable cause = getCause();
        if (cause == this) {
            return false;
        }
        if (cause instanceof CommonCheckedException) {
            return ((CommonCheckedException) cause).contains(exType);
        }
        else {
            while (cause != null) {
                if (exType.isInstance(cause)) {
                    return true;
                }
                if (cause.getCause() == cause) {
                    break;
                }
                cause = cause.getCause();
            }
            return false;
        }
    }
}
