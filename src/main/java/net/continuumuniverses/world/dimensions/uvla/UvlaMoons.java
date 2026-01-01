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
            14.0F,
            0.0F // distance (unused right now)
    );

    public static final UvlaMoon NENJA = new UvlaMoon(
            "nenja",
            16,
            0.0F,
            15.0F,
            4.5F,
            0.3F,
            28.0F,
            0.0F
    );

    public static final UvlaMoon MAIRA = new UvlaMoon(
            "maira",
            16,
            0.0F,
            35.0F,
            6.5F,
            0.45F,
            32.0F,
            0.0F
    );

    public static final List<UvlaMoon> ALL = List.of(KAIRA, NENJA, MAIRA);
}
