package org.openjfx.constants;

import java.util.regex.Pattern;

public class DatePattern {

    public static final Pattern PATTERN
            = Pattern.compile("^(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[012])-((19|2[0-9])[0-9]{2})$");

    public static final String FORMAT = "dd-MM-yyyy";
}
