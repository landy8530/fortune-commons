package org.springframework.boot.autoconfigure.condition;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
@Documented
@Conditional(OnPropertyCondition.class)
public @interface ConditionalOnProperty {
    /**
     * Alias for {@link #name()}.
     * @return the names
     */
    String[] value() default {};

    /**
     * A prefix that should be applied to each property. The prefix automatically ends
     * with a dot if not specified. A valid prefix is defined by one or more words
     * separated with dots (e.g. {@code "acme.system.feature"}).
     * @return the prefix
     */
    String prefix() default "";

    /**
     * The name of the properties to test. If a prefix has been defined, it is applied to
     * compute the full key of each property. For instance if the prefix is
     * {@code app.config} and one value is {@code my-value}, the full key would be
     * {@code app.config.my-value}
     * <p>
     * Use the dashed notation to specify each property, that is all lower case with a "-"
     * to separate words (e.g. {@code my-long-property}).
     * @return the names
     */
    String[] name() default {};

    /**
     * The string representation of the expected value for the properties. If not
     * specified, the property must <strong>not</strong> be equal to {@code false}.
     * @return the expected value
     */
    String havingValue() default "";
    /**
     * Specify if the condition should match if the property is not set. Defaults to
     * {@code false}.
     * @return if should match if the property is missing
     */
    boolean matchIfMissing() default false;
}
