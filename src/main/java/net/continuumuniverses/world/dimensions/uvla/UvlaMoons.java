package net.continuumuniverses.world.dimensions.uvla;

import java.util.List;

public final class UvlaMoons {
    private UvlaMoons() {}

    public static final UvlaMoon KAIRA = new UvlaMoon(
            "kaira",
            16,
            0.0F,
            18.0F,
            0.6F,
            14.0F,
            0.0F // distance (unused right now)
    );

    public static final UvlaMoon NENJA = new UvlaMoon(
            "nenja",
            16,
            120.0F,
            27.0F,
            0.3F,
            28.0F,
            0.0F
    );

    public static final UvlaMoon MAIRA = new UvlaMoon(
            "maira",
            16,
            240.0F,   // (you had 120 twice; I made this 240 so the 3 moons are evenly spaced)
            41.0F,
            0.45F,
            32.0F,
            0.0F
    );

    public static final List<UvlaMoon> ALL = List.of(KAIRA, NENJA, MAIRA);
}
