package com.emirovschi.pad.lab2.chat.ui;

import com.google.common.hash.HashFunction;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;

import java.nio.charset.Charset;
import java.util.function.Function;

import static com.google.common.hash.Hashing.sha256;

public class JavafxUtils
{
    private static HashFunction HASH_FUNCTION = sha256();

    public static double getHue(final String code)
    {
        final int color = HASH_FUNCTION.hashString(code, Charset.defaultCharset()).asInt() % 20;
        final double hue = color / 20D;
        return hue;
    }

    public static Color getColor(final String code)
    {
        return Color.hsb((getHue(code) + 1) * 360, 1, 0.5);
    }

    public static <T> StringConverter<T> uniStringConverter(final Function<T, String> converter)
    {
        return new StringConverter<T>()
        {
            @Override
            public String toString(final T object)
            {
                return converter.apply(object);
            }

            @Override
            public T fromString(final String string)
            {
                return null;
            }
        };
    }
}
