package net.jensensagastudio.continuumuniverses.block;

import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.DropExperienceBlock;

public class AethyrilOreBlock extends DropExperienceBlock {

    public AethyrilOreBlock(BlockBehaviour.Properties properties,
                            IntProvider experience) {
        super(experience, properties); // ← ORDER MATTERS
    }
}
