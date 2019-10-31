package org.springframework.beans;

import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class MutablePropertyValuesTests extends AbstractPropertyValuesTests {

    @Test
    public void testValid(){
        MutablePropertyValues pvs = new MutablePropertyValues();
        pvs.addPropertyValue(new PropertyValue("forname","Tony"));
        pvs.addPropertyValue(new PropertyValue("surname","Blair"));
        pvs.addPropertyValue(new PropertyValue("age","50"));
        doTestTony(pvs);

        MutablePropertyValues deepCopy = new MutablePropertyValues(pvs);
        doTestTony(deepCopy);
        deepCopy.setPropertyValueAt(new PropertyValue("name","Gordon"),0);
        doTestTony(pvs);
        assertThat(deepCopy.getPropertyValue("name").getValue()).isEqualTo("Gordon");
    }

    @Test
    public void testAddOrOverride(){
        MutablePropertyValues pvs = new MutablePropertyValues();
        pvs.addPropertyValue(new PropertyValue("forname","Tony"));
        pvs.addPropertyValue(new PropertyValue("surname","Blair"));
        pvs.addPropertyValue(new PropertyValue("age","50"));
        doTestTony(pvs);
        PropertyValue addPv = new PropertyValue("rod","Rod");
        pvs.addPropertyValue(addPv);
        assertThat(pvs.getPropertyValue("rod").getValue().equals("Rod")).isTrue();
        PropertyValue changedPv = new PropertyValue("forname","Greg");
        pvs.addPropertyValue(changedPv);
        assertThat(pvs.getPropertyValue("forname").equals(changedPv)).isTrue();
    }

    @Test
    public void testChangeOfOneField(){
        MutablePropertyValues pvs = new MutablePropertyValues();
        pvs.addPropertyValue(new PropertyValue("forname","Tony"));
        pvs.addPropertyValue(new PropertyValue("surname","Blair"));
        pvs.addPropertyValue(new PropertyValue("age","50"));

        MutablePropertyValues pvs2 = new MutablePropertyValues(pvs);
        PropertyValues chanages = pvs2.changesSince(pvs);
        assertThat(chanages.getPropetyValues().length == 0).as("changes are empty not of length").isTrue();

        pvs2.addPropertyValue(new PropertyValue("forname","Greg"));
        chanages = pvs2.changesSince(pvs);
        assertThat(chanages.getPropetyValues().length).as("1 change").isEqualTo(1);
        PropertyValue fn = chanages.getPropertyValue("forname");
        assertThat(fn != null).as("change is forname").isTrue();
        assertThat(fn.getValue().equals("Greg")).as("New value equals Greg").isTrue();

        MutablePropertyValues pv3 = new MutablePropertyValues(pvs);
        chanages = pv3.changesSince(pvs);
        assertThat(chanages.getPropetyValues().length == 0).as("changes are empty.not of length =" + chanages.getPropetyValues().length);

        pv3.addPropertyValue(new PropertyValue("foo","bar"));
        pv3.addPropertyValue(new PropertyValue("fi","fum"));
        chanages = pv3.changesSince(pvs);
        assertThat(chanages.getPropetyValues().length == 2).as("2 changes").isTrue();
        fn = chanages.getPropertyValue("foo");
        assertThat(fn != null).as("change in foo").isTrue();
        assertThat(fn.getValue().equals("bar")).as("New value is bar").isTrue();
    }

    @Test
    public void iteratorContainsPropertyValue(){
        MutablePropertyValues pvs = new MutablePropertyValues();
        pvs.addPropertyValue(new PropertyValue("foo","bar"));

        Iterator<PropertyValue> it = pvs.iterator();
        assertThat(it.hasNext()).isTrue();
        PropertyValue pv = it.next();
        assertThat(pv.getName()).isEqualTo("foo");
        assertThat(pv.getValue()).isEqualTo("bar");
        assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(it::remove);
        assertThat(it.hasNext()).isFalse();
    }

    @Test
    public void iteratorIsEmptyForEmptyValues(){
        MutablePropertyValues pvs =  new MutablePropertyValues();
        Iterator<PropertyValue> it = pvs.iterator();
        assertThat(it.hasNext()).isFalse();
    }

    @Test
    public void streamContainsPropertyValue(){
        MutablePropertyValues pvs = new MutablePropertyValues();
        pvs.addPropertyValue(new PropertyValue("foo","bar"));

        assertThat(pvs.stream()).isNotNull();
        assertThat(pvs.stream().count()).isEqualTo(1L);
        assertThat(pvs.stream().anyMatch(pv -> "foo".equals(pv.getName()) && "bar".equals(pv.getValue()))).isTrue();
        assertThat(pvs.stream().anyMatch(pv -> "bar".equals(pv.getName()) && "foo".equals(pv.getValue()))).isFalse();
    }

    @Test
    public void streamIsEmptyForEmptyValue(){
        MutablePropertyValues pvs = new MutablePropertyValues();
        assertThat(pvs.stream()).isNotNull();
        assertThat(pvs.stream().count()).isEqualTo(0L);
    }
}
