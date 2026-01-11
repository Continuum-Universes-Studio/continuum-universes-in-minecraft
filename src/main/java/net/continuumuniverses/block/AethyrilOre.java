package net.continuumuniverses.block;

import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.DropExperienceBlock;

public class AethyrilOre extends DropExperienceBlock {

    public AethyrilOre(BlockBehaviour.Properties properties,
                       IntProvider experience) {
        super(experience, properties); // ← ORDER MATTERS
    }
}
