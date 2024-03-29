/*
 * This file is generated by jOOQ.
 */
package io.agibalov.db.tables.records;


import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record3;
import org.jooq.Row3;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Students extends UpdatableRecordImpl<Students> implements Record3<String, String, String> {

    private static final long serialVersionUID = 778095076;

    /**
     * Setter for <code>Students.id</code>.
     */
    public void id(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>Students.id</code>.
     */
    public String id() {
        return (String) get(0);
    }

    /**
     * Setter for <code>Students.schoolId</code>.
     */
    public void schoolId(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>Students.schoolId</code>.
     */
    public String schoolId() {
        return (String) get(1);
    }

    /**
     * Setter for <code>Students.name</code>.
     */
    public void name(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>Students.name</code>.
     */
    public String name() {
        return (String) get(2);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<String> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record3 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row3<String, String, String> fieldsRow() {
        return (Row3) super.fieldsRow();
    }

    @Override
    public Row3<String, String, String> valuesRow() {
        return (Row3) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return io.agibalov.db.tables.Students.Students.id;
    }

    @Override
    public Field<String> field2() {
        return io.agibalov.db.tables.Students.Students.schoolId;
    }

    @Override
    public Field<String> field3() {
        return io.agibalov.db.tables.Students.Students.name;
    }

    @Override
    public String component1() {
        return id();
    }

    @Override
    public String component2() {
        return schoolId();
    }

    @Override
    public String component3() {
        return name();
    }

    @Override
    public String value1() {
        return id();
    }

    @Override
    public String value2() {
        return schoolId();
    }

    @Override
    public String value3() {
        return name();
    }

    @Override
    public Students value1(String value) {
        id(value);
        return this;
    }

    @Override
    public Students value2(String value) {
        schoolId(value);
        return this;
    }

    @Override
    public Students value3(String value) {
        name(value);
        return this;
    }

    @Override
    public Students values(String value1, String value2, String value3) {
        value1(value1);
        value2(value2);
        value3(value3);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached Students
     */
    public Students() {
        super(io.agibalov.db.tables.Students.Students);
    }

    /**
     * Create a detached, initialised Students
     */
    public Students(String id, String schoolId, String name) {
        super(io.agibalov.db.tables.Students.Students);

        set(0, id);
        set(1, schoolId);
        set(2, name);
    }
}
