package com.vdzon.regex;

import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class RegexTests {

    @Test
    void test2() {
        assertThat("abc".matches("^abc$")).isTrue();
        assertThat("abc".matches("abc")).isTrue(); // this is the same, the ^and $ are not needed

        assertThat("abc".matches("b")).isFalse();
        assertThat("abc".matches(".b.")).isTrue(); // . matches all

        assertThat("1+1".matches("1\\+1")).isTrue(); // escape the special char with a \

        // character set tests
        assertThat("0".matches("[0-9]")).isTrue();
        assertThat("01".matches("[0-9][0-9]")).isTrue();
        assertThat("013".matches("[0-9]*")).isTrue();
        assertThat("_".matches("[0-9_]")).isTrue(); // matches 0..9 and _

        // negated test (^ at the beginning of a character set)
        assertThat("a".matches("a[^c]")).isFalse(); // do not match a c after a
        assertThat("ac".matches("a[^c]")).isFalse(); // idem
        assertThat("ax".matches("a[^c]")).isTrue(); // idem

        assertThat("_1".matches("\\w\\w")).isTrue(); // word char
        assertThat(" ".matches("\\s")).isTrue(); // whitespace char
        assertThat("\n".matches("\\s")).isTrue(); // idem
        assertThat("\t".matches("\\s")).isTrue(); // idem
        assertThat("\r".matches("\\s")).isTrue(); // idem

        // none or more
        assertThat("fsafhlkdsjh".matches(".*")).isTrue();
        assertThat("".matches(".*")).isTrue();

        // one or more
        assertThat("fsafhlkdsjh".matches(".+")).isTrue();
        assertThat("".matches(".+")).isFalse();

        // ? : the previous is optional
        assertThat("Colour".matches("Colou?r")).isTrue();
        assertThat("Color".matches("Colou?r")).isTrue();
        assertThat("Nov".matches("Nov(ember)?")).isTrue();
        assertThat("November".matches("Nov(ember)?")).isTrue();

        // replace with regex
        assertThat("robbert".replaceAll("b.rt", "cc")).isEqualTo("robcc");

        // example: remove all spaces wetween words and a dots or commas.
        String pattern = "(\\w)(\\s+)([\\.,])";
        System.out.println("this is line1 . This is line2 with a comma , with a space before it.".replaceAll(pattern, "$1$3"));

        // replace all text between foo and bar
        // Everything between () aregroups which you can use in the replacement as $1, $2 etc
        pattern = "(.*)(foo)(.*)(bar)(.*)";
        System.out.println("test1 foo test1 bar test2".replaceAll(pattern, "$1$2 HALLO $4$5"));
        assertThat("test1 foo test1 bar test2".replaceAll(pattern, "$1$2 HALLO $4$5")).isEqualTo("test1 foo HALLO bar test2");

        // idem, but with lazy instead of eager
        // the ? after the * makes the regex find the first possible option
        pattern = "(.*)(foo)(.*?)(bar)(.*)";
        System.out.println("test1 foo test1 bar bar foo".replaceAll(pattern, "$1$2 HALLO $4$5"));
        assertThat("test1 foo test1 bar bar foo".replaceAll(pattern, "$1$2 HALLO $4$5")).isEqualTo("test1 foo HALLO bar bar foo");

        // idem, but case insensitive
        pattern = "(?i)(.*)(foo)(.*?)(bar)(.*)";
        System.out.println("test1 fOo test1 bAR bar foo".replaceAll(pattern, "$1foo HALLO bar$5"));
        assertThat("test1 fOo test1 bAR bar foo".replaceAll(pattern, "$1foo HALLO bar$5")).isEqualTo("test1 foo HALLO bar bar foo");

        // match all words that start with an a and end with a b
        Matcher matcher = Pattern.compile("a[a-z]*b").matcher("aaab xxx bbb aaa ajjjb");
        if (matcher.find()) {
            System.out.println(matcher.group()); // this will find aaab and ajjjb
        }

        // extract the text between foo and bar
        // With a Matcher you can refer to the groups, in this case we want the 3th group
        pattern = "(?i)(.*)(foo)(.*?)(bar)(.*)";
        matcher = Pattern.compile(pattern).matcher("test1 fOo test2 bAR bar foo");
        System.out.println("Finding:");
        if (matcher.find()) {
            System.out.println(matcher.group(3));
        }

        // with ?<> you can create named groups and refer to the group using the name
        pattern = "(?i)(.*)(foo)(?<mygroup>.*?)(bar)(.*)";
        matcher = Pattern.compile(pattern).matcher("test1 fOo test2 bAR bar foo");
        System.out.println("Finding:");
        if (matcher.find()) {
            System.out.println(matcher.group("mygroup"));
        }

        pattern = "([\\S]*\\s){3}(?<forth>\\S*).*";
        Matcher matcher1 = Pattern.compile(pattern).matcher("word1 word2 word3 word4 word5 word6");
        while (matcher1.find()){
            System.out.println("4th word:"+matcher1.group("forth"));
        }
    }

}
