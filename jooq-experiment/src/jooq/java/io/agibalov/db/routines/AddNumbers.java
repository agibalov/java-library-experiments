/*
 * This file is generated by jOOQ.
 */
package io.agibalov.db.routines;


import io.agibalov.db.DefaultSchema;

import org.jooq.Parameter;
import org.jooq.impl.AbstractRoutine;
import org.jooq.impl.Internal;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class AddNumbers extends AbstractRoutine<java.lang.Void> {

    private static final long serialVersionUID = -247568341;

    /**
     * The parameter <code>AddNumbers.a</code>.
     */
    public static final Parameter<Integer> a = Internal.createParameter("a", org.jooq.impl.SQLDataType.INTEGER, false, false);

    /**
     * The parameter <code>AddNumbers.b</code>.
     */
    public static final Parameter<Integer> b = Internal.createParameter("b", org.jooq.impl.SQLDataType.INTEGER, false, false);

    /**
     * Create a new routine call instance
     */
    public AddNumbers() {
        super("AddNumbers", DefaultSchema.DEFAULT_SCHEMA);

        addInParameter(a);
        addInParameter(b);
    }

    /**
     * Set the <code>a</code> parameter IN value to the routine
     */
    public void a(Integer value) {
        setValue(a, value);
    }

    /**
     * Set the <code>b</code> parameter IN value to the routine
     */
    public void b(Integer value) {
        setValue(b, value);
    }
}