package org.springframework.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StringUtilsTest {
    @Test
    void replace() {
        String inString = "a6AazAaa77abaa";
        String oldPattern = "aa";
        String newPattern = "foo";

        // Simple replace
        String s = StringUtils.replace(inString, oldPattern, newPattern);
        assertThat(s.equals("a6AazAfoo77abfoo")).as("Replace 1 worked").isTrue();

        // Non match: no change
        s = StringUtils.replace(inString, "qwoeiruqopwieurpoqwieur", newPattern);
        assertThat(s).as("Replace non-matched is returned as-is").isSameAs(inString);

        // Null new pattern: should ignore
        s = StringUtils.replace(inString, oldPattern, null);
        assertThat(s).as("Replace non-matched is returned as-is").isSameAs(inString);

        // Null old pattern: should ignore
        s = StringUtils.replace(inString, null, newPattern);
        assertThat(s).as("Replace non-matched is returned as-is").isSameAs(inString);
    }

    @Test
    void deleteAny() {
        String inString = "Able was I ere I saw Elba";

        String res = StringUtils.deleteAny(inString, "I");
        assertThat(res.equals("Able was  ere  saw Elba")).as("Result has no Is [" + res + "]").isTrue();

        res = StringUtils.deleteAny(inString, "AeEba!");
        assertThat(res.equals("l ws I r I sw l")).as("Result has no Is [" + res + "]").isTrue();

        String mismatch = StringUtils.deleteAny(inString, "#@$#$^");
        assertThat(mismatch.equals(inString)).as("Result is unchanged").isTrue();

        String whitespace = "This is\n\n\n    \t   a messagy string with whitespace\n";
        assertThat(whitespace.contains("\n")).as("Has CR").isTrue();
        assertThat(whitespace.contains("\t")).as("Has tab").isTrue();
        assertThat(whitespace.contains(" ")).as("Has  sp").isTrue();
        String cleaned = StringUtils.deleteAny(whitespace, "\n\t ");
        boolean condition2 = !cleaned.contains("\n");
        assertThat(condition2).as("Has no CR").isTrue();
        boolean condition1 = !cleaned.contains("\t");
        assertThat(condition1).as("Has no tab").isTrue();
        boolean condition = !cleaned.contains(" ");
        assertThat(condition).as("Has no sp").isTrue();
        assertThat(cleaned.length() > 10).as("Still has chars").isTrue();
    }


    @Test
    void cleanPath() {
        assertThat(StringUtils.cleanPath("mypath/myfile")).isEqualTo("mypath/myfile");
        assertThat(StringUtils.cleanPath("mypath\\myfile")).isEqualTo("mypath/myfile");
        assertThat(StringUtils.cleanPath("mypath/../mypath/myfile")).isEqualTo("mypath/myfile");
        assertThat(StringUtils.cleanPath("mypath/myfile/../../mypath/myfile")).isEqualTo("mypath/myfile");
        assertThat(StringUtils.cleanPath("../mypath/myfile")).isEqualTo("../mypath/myfile");
        assertThat(StringUtils.cleanPath("../mypath/../mypath/myfile")).isEqualTo("../mypath/myfile");
        assertThat(StringUtils.cleanPath("mypath/../../mypath/myfile")).isEqualTo("../mypath/myfile");
        assertThat(StringUtils.cleanPath("/../mypath/myfile")).isEqualTo("/../mypath/myfile");
        assertThat(StringUtils.cleanPath("/a/:b/../../mypath/myfile")).isEqualTo("/mypath/myfile");
        assertThat(StringUtils.cleanPath("/")).isEqualTo("/");
        assertThat(StringUtils.cleanPath("/mypath/../")).isEqualTo("/");
        assertThat(StringUtils.cleanPath("mypath/..")).isEqualTo("");
        assertThat(StringUtils.cleanPath("mypath/../.")).isEqualTo("");
        assertThat(StringUtils.cleanPath("mypath/../")).isEqualTo("./");
        assertThat(StringUtils.cleanPath("././")).isEqualTo("./");
        assertThat(StringUtils.cleanPath("./")).isEqualTo("./");
        assertThat(StringUtils.cleanPath("../")).isEqualTo("../");
        assertThat(StringUtils.cleanPath("./../")).isEqualTo("../");
        assertThat(StringUtils.cleanPath(".././")).isEqualTo("../");
        assertThat(StringUtils.cleanPath("file:/")).isEqualTo("file:/");
        assertThat(StringUtils.cleanPath("file:/mypath/../")).isEqualTo("file:/");
        assertThat(StringUtils.cleanPath("file:mypath/..")).isEqualTo("file:");
        assertThat(StringUtils.cleanPath("file:mypath/../.")).isEqualTo("file:");
        assertThat(StringUtils.cleanPath("file:mypath/../")).isEqualTo("file:./");
        assertThat(StringUtils.cleanPath("file:././")).isEqualTo("file:./");
        assertThat(StringUtils.cleanPath("file:./")).isEqualTo("file:./");
        assertThat(StringUtils.cleanPath("file:../")).isEqualTo("file:../");
        assertThat(StringUtils.cleanPath("file:./../")).isEqualTo("file:../");
        assertThat(StringUtils.cleanPath("file:.././")).isEqualTo("file:../");
        assertThat(StringUtils.cleanPath("file:///c:/some/../path/the%20file.txt")).isEqualTo("file:///c:/path/the%20file.txt");
    }


    @Test
    void delimitedListToStringArrayWithComma() {
        String[] sa = StringUtils.delimitedListToStringArray("a,b", ",");
        assertThat(sa.length).isEqualTo(2);
        assertThat(sa[0]).isEqualTo("a");
        assertThat(sa[1]).isEqualTo("b");
    }

    @Test
    void delimitedListToStringArrayWithSemicolon() {
        String[] sa = StringUtils.delimitedListToStringArray("a;b", ";");
        assertThat(sa.length).isEqualTo(2);
        assertThat(sa[0]).isEqualTo("a");
        assertThat(sa[1]).isEqualTo("b");
    }

    @Test
    void delimitedListToStringArrayWithSemicolon1() {
        String[] sa = StringUtils.delimitedListToStringArray("a;b;", ";");
        assertThat(sa.length).isEqualTo(3);
        assertThat(sa[0]).isEqualTo("a");
        assertThat(sa[1]).isEqualTo("b");
        assertThat(sa[2]).isEqualTo("");
    }

    @Test
    void delimitedListToStringArrayWithEmptyString() {
        String[] sa = StringUtils.delimitedListToStringArray("a,b", "");
        assertThat(sa.length).isEqualTo(3);
        assertThat(sa[0]).isEqualTo("a");
        assertThat(sa[1]).isEqualTo(",");
        assertThat(sa[2]).isEqualTo("b");
    }



    @Test
    void delimitedListToStringArrayWithNullDelimiter() {
        String[] sa = StringUtils.delimitedListToStringArray("a,b", null);
        assertThat(sa.length).isEqualTo(1);
        assertThat(sa[0]).isEqualTo("a,b");
    }
}
