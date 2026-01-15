package net.continuumuniverses.world.dimensions.uvla;

import java.util.List;

public final class UvlaMoons {
    private UvlaMoons() {}

    public static final UvlaMoon KAIRA = new UvlaMoon(
            "kaira",
            16,
            0.0F,
            0.0F,
            3.0F,
            0.6F,
            6.0F,
            110.0F
    );

    public static final UvlaMoon NENJA = new UvlaMoon(
            "nenja",
            16,
            0.0F,
            15.0F,
            4.5F,
            0.3F,
            10.0F,
            170.0F
    );

    public static final UvlaMoon MAIRA = new UvlaMoon(
            "maira",
            16,
            0.0F,
            35.0F,
            6.5F,
            0.45F,
            12.0F,
            140.0F
    );

    public static final List<UvlaMoon> ALL = List.of(KAIRA, NENJA, MAIRA);
}
