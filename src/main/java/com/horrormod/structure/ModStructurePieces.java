package com.horrormod.structure;

import com.horrormod.HorrorMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModStructurePieces
{
    public static final DeferredRegister<StructurePieceType> STRUCTURE_PIECES = DeferredRegister.create(Registries.STRUCTURE_PIECE, HorrorMod.MODID);

    public static final RegistryObject<StructurePieceType> RITUAL_CIRCLE = STRUCTURE_PIECES.register("ritual_circle",
            () -> (StructurePieceType.ContextlessType) RitualCirclePiece::new);
}
