package org.springframework.beans;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class AbstractPropertyValuesTests {

    protected void doTestTony(PropertyValues pvs){
        assertThat(pvs.getPropetyValues().length == 3).as("Contains 3").isTrue();
        assertThat(pvs.contains("forname")).as("Contains forname").isTrue();
        assertThat(pvs.contains("surname")).as("Contains surname").isTrue();
        assertThat(pvs.contains("age")).as("Contains age").isTrue();
        boolean condition1 = !pvs.contains("tory");
        assertThat(condition1).as("Contains tory").isTrue();

        PropertyValue[] ps = pvs.getPropetyValues();
        Map<String,String> m = new HashMap<>(3);
        m.put("forname","Tony");
        m.put("surname","Blair");
        m.put("age","50");

        for(int i=0;i<ps.length;i++){
            Object val = m.get(ps[i].getName());
            assertThat(val != null).as("Can not have excepted value").isTrue();
            boolean condition = val instanceof String;
            assertThat(condition).as("Val i string").isTrue();
            assertThat(val.equals(ps[i].getValue())).as("val matches excepted").isTrue();
            m.remove(ps[i].getName());
        }

        assertThat(m.size() == 0).as("Map size is 0").isTrue();
    }
}
