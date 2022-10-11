package reflix.datatest;

import net.bytebuddy.utility.RandomString;

import java.util.stream.Stream;


public class Sources {

    public static Stream<String> randomTitleTooLong() {
        // org.apache.commons => commons-lang3
        // return RandomStringUtils.random(251);
        return Stream.of(RandomString.make(251));
    }
}
