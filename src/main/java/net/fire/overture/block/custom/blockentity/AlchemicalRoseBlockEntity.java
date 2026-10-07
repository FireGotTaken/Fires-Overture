package net.fire.overture.block.custom.blockentity;

import net.fire.overture.block.ModBlocks;
import net.fire.overture.datagen.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public class AlchemicalRoseBlockEntity extends BlockEntity {

    @Nullable
    private MobEffect effect;
    private int amplifier;
    private int duration = 40;
    public static final String EFFECT_KEY = "Effect";
    public static final String AMPLIFIER_KEY = "Amplifier";
    public static final String DURATION_KEY = "Duration";

    public void remember(MobEffect pEffect, int pAmplifier, int pDuration) {
        if (this.effect != null) {
            return;
        }
        this.effect = pEffect;
        this.amplifier = pAmplifier;
        this.duration = pDuration;
        this.setChanged();
        if (this.level != null) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    public AlchemicalRoseBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.ALCHEMICAL_ROSE.get(), pPos, pBlockState);
    }

    @Nullable
    public MobEffect getEffect() {
        return this.effect;
    }

    public int getAmplifier() {
        return this.amplifier;
    }

    public void writeEffect(CompoundTag pTag) {
        if (this.effect != null) {
            ResourceLocation key = ForgeRegistries.MOB_EFFECTS.getKey(this.effect);
            if (key != null) {
                pTag.putString(EFFECT_KEY, key.toString());
                pTag.putInt(AMPLIFIER_KEY, this.amplifier);
                pTag.putInt(DURATION_KEY, this.duration);
            }
        }
    }

    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        this.writeEffect(pTag);
    }

    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.effect = null;
        this.amplifier = 0;
        this.duration = 40;
        if (pTag.contains(EFFECT_KEY)) {
            ResourceLocation key = ResourceLocation.tryParse(pTag.getString(EFFECT_KEY));
            if (key != null) {
                this.effect = ForgeRegistries.MOB_EFFECTS.getValue(key);
            }

            if (this.effect != null) {
                this.amplifier = pTag.getInt(AMPLIFIER_KEY);
            }

            if (this.effect != null) {
                this.amplifier = pTag.getInt(AMPLIFIER_KEY);
                this.duration = pTag.contains(DURATION_KEY) ? pTag.getInt(DURATION_KEY) : 40;
            }
        }
    }

    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    @Nullable
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public static void clientTick(Level pLevel, BlockPos pPos, BlockState pState, AlchemicalRoseBlockEntity pRose) {
        if (pRose.effect == null || !pLevel.random.nextBoolean()) {
            return;
        }

        int color = pRose.effect.getColor();
        double red = (double)(color >> 16 & 255) / 255.0D;
        double green = (double)(color >> 8 & 255) / 255.0D;
        double blue = (double)(color & 255) / 255.0D;

        Vec3 offset = pState.getOffset(pLevel, pPos);
        double x = (double)pPos.getX() + 0.5D + offset.x + (pLevel.random.nextDouble() - 0.5D) * 0.4D;
        double y = (double)pPos.getY() + 0.3D + pLevel.random.nextDouble() * 0.4D;
        double z = (double)pPos.getZ() + 0.5D + offset.z + (pLevel.random.nextDouble() - 0.5D) * 0.4D;

        pLevel.addParticle(ParticleTypes.ENTITY_EFFECT, x, y, z, red, green, blue);
    }

    public ItemStack createRoseItem() {
        ItemStack stack = new ItemStack(ModBlocks.ALCHEMICAL_ROSE.get());
        if (this.effect != null) {
            this.writeEffect(stack.getOrCreateTagElement("BlockEntityTag"));
        }
        return stack;
    }

    public void loadFromItem(ItemStack pStack) {
        CompoundTag tag = BlockItem.getBlockEntityData(pStack);
        if (tag != null) {
            this.load(tag);
            this.setChanged();
            if (this.level != null) {
                this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
            }
        }
    }

    public int getDuration() {
        return this.duration;
    }

    @Nullable
    public static MobEffect getEffectFromItem(ItemStack pStack) {
        CompoundTag tag = BlockItem.getBlockEntityData(pStack);
        if (tag == null || !tag.contains(EFFECT_KEY)) {
            return null;
        }
        ResourceLocation key = ResourceLocation.tryParse(tag.getString(EFFECT_KEY));
        return key == null ? null : ForgeRegistries.MOB_EFFECTS.getValue(key);
    }
}
