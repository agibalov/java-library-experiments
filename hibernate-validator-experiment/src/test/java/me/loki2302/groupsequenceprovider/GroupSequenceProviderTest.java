package me.loki2302.groupsequenceprovider;

import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.group.GroupSequenceProvider;
import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GroupSequenceProviderTest {
    private Validator validator;

    @Before
    public void initValidator() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    public void whenIsDocumentedEqualsFalseAndDescriptionIsEmptyItShouldSucceed() {
        ClassModel classModel = new ClassModel();
        classModel.name = "test";
        classModel.isDocumented = false;
        classModel.description = "";
        assertTrue(validator.validate(classModel).isEmpty());
    }

    @Test
    public void whenIsDocumentedEqualsTrueAndDescriptionIsNotEmptyItShouldSucceed() {
        ClassModel classModel = new ClassModel();
        classModel.name = "test";
        classModel.isDocumented = true;
        classModel.description = "hello there";
        assertTrue(validator.validate(classModel).isEmpty());
    }

    @Test
    public void whenIsDocumentedEqualsTrueAndDescriptionIsEmptyItShouldFail() {
        ClassModel classModel = new ClassModel();
        classModel.name = "test";
        classModel.isDocumented = true;
        classModel.description = "";

        Set<ConstraintViolation<ClassModel>> violations = validator.validate(classModel);
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(c -> c.getPropertyPath().toString().equals("description")));
    }

    @GroupSequenceProvider(ClassModelGroupSequenceProvider.class)
    public static class ClassModel {
        @NotEmpty(groups = Core.class)
        public String name;

        public boolean isDocumented;

        @NotEmpty(groups = Documented.class)
        public String description;
    }

    public static class ClassModelGroupSequenceProvider implements DefaultGroupSequenceProvider<ClassModel> {
        public List<Class<?>> getValidationGroups(ClassModel classModel) {
            List<Class<?>> groups = new ArrayList<>();
            groups.add(ClassModel.class);

            if(classModel != null) {
                if (classModel.isDocumented) {
                    groups.add(Documented.class);
                }
                groups.add(Core.class);
            }

            return groups;
        }
    }

    public interface Core {}
    public interface Documented {}
}
