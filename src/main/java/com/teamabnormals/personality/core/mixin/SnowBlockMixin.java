package com.teamabnormals.personality.core.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.SnowBlock;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(SnowBlock.class) // TODO: Config, Make snow layers stack
public abstract class SnowBlockMixin extends Block {
    public SnowBlockMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "updatePostPlacement", at = @At("HEAD"), cancellable = true)
    public void updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos, CallbackInfoReturnable<BlockState> cir) {
        worldIn.getPendingBlockTicks().scheduleTick(currentPos, this, 2);
        cir.setReturnValue(super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos));
    }

    @Override
    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        worldIn.getPendingBlockTicks().scheduleTick(pos, this, 2);
    }

    @Override
    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        if (worldIn.isAirBlock(pos.down()) || FallingBlock.canFallThrough(worldIn.getBlockState(pos.down())) && pos.getY() >= 0) {
            FallingBlockEntity fallingblockentity = new FallingBlockEntity(worldIn, (double) pos.getX() + 0.5D, pos.getY(), (double) pos.getZ() + 0.5D, worldIn.getBlockState(pos));
            worldIn.addEntity(fallingblockentity);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (rand.nextInt(16) == 0) {
            BlockPos blockpos = pos.down();
            if (worldIn.isAirBlock(blockpos) || FallingBlock.canFallThrough(worldIn.getBlockState(blockpos))) {
                double d0 = (double) pos.getX() + rand.nextDouble();
                double d1 = (double) pos.getY() - 0.05D;
                double d2 = (double) pos.getZ() + rand.nextDouble();
                worldIn.addParticle(new BlockParticleData(ParticleTypes.FALLING_DUST, stateIn), d0, d1, d2, 0.0D, 0.0D, 0.0D);
            }
        }
    }
}
