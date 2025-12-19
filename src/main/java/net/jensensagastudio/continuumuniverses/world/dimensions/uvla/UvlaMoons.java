package net.jensensagastudio.continuumuniverses.world.dimensions.uvla;

import net.minecraft.resources.Identifier;
import java.util.List;

public class UvlaMoons {
    public static final Identifier KAIRA_ID =
            Identifier.fromNamespaceAndPath("continuumuniverses", "kaira");
    public static final Identifier NENJA_ID =
            Identifier.fromNamespaceAndPath("continuumuniverses", "nenja");
    public static final Identifier MAIRA_ID =
            Identifier.fromNamespaceAndPath("continuumuniverses", "maira");
    public static final UvlaMoon KAIRA = new UvlaMoon(
            "kaira",
            KAIRA_ID,
            16,
            0.0F,
            40.0F,
            0.3F,
            14.0F
    );

    public static final UvlaMoon NENJA = new UvlaMoon(
            "nenja",
            NENJA_ID,
            16,
            120.0F,
            28.0F,
            0.5F,
            28.0F
    );

    public static final UvlaMoon MAIRA = new UvlaMoon(
            "maira",
            MAIRA_ID,
            16,
            120.0F,
            28.0F,
            0.5F,
            32.0F
    );

    public static final List<UvlaMoon> ALL =
            List.of(KAIRA, NENJA, MAIRA);
}
