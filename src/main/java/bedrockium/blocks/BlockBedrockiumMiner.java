package bedrockium.blocks;

import bedrockium.Main;
import bedrockium.energy.BlockItemEnergyContainer;
import bedrockium.init.ModBlocks;
import bedrockium.tileentity.TileEntityBedrockiumMiner;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;

public class BlockBedrockiumMiner extends Block {

    public static final ResourceLocation MINER = new ResourceLocation(Main.MOD_ID, "miner");

    public BlockBedrockiumMiner(Block.Properties properties) {
        super(properties);
        this.setRegistryName(MINER);
        this.setDefaultState(this.getDefaultState().with(BlockStateProperties.POWERED, false));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new StringTextComponent("Place above bedrock for the miner to work."));
        if (stack.hasTag()) {
            int i = stack.getTag().getInt("Energy");
            tooltip.add(new StringTextComponent("Energy: " + i));
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return VoxelShapes.create(0.0F, 0.0F, 0.0F, 1.0F, 1.0F-2*1F/16F, 1.0F);
    }

    @Override
    public boolean isSolid(BlockState state) {
        return false;
    }


    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.POWERED);
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!worldIn.isRemote) {
            if (!player.isCreative()) {
                TileEntity te = worldIn.getTileEntity(pos);
                if (te != null && te instanceof TileEntityBedrockiumMiner) {
                    ItemStack stack = new ItemStack(ModBlocks.miner);
                    int energy = ((TileEntityBedrockiumMiner) te).getEnergy();
                    ((BlockItemEnergyContainer) stack.getItem()).receiveEnergy(stack, energy, false);
                    worldIn.addEntity(new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), stack));
                    InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityBedrockiumMiner) te);
                    worldIn.updateComparatorOutputLevel(pos, this);
                }
            }
            worldIn.removeTileEntity(pos);
        }
        super.onBlockHarvested(worldIn, pos, state, player);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
        if (entity != null) {
            TileEntityBedrockiumMiner te = (TileEntityBedrockiumMiner) world.getTileEntity(pos);
            if (stack.hasDisplayName()) {
                te.setCustomName(stack.getDisplayName());
            }
            if (stack.getItem() instanceof BlockItemEnergyContainer) {
                BlockItemEnergyContainer item = (BlockItemEnergyContainer) stack.getItem();
                if (item.getEnergyStored(stack) > 0) {
                    te.getCapability(CapabilityEnergy.ENERGY).ifPresent(h -> {
                        h.receiveEnergy(item.getEnergyStored(stack), false);
                    });
                }
            }
        }
    }

    @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
        if (!world.isRemote) {
            this.interactWith(world, pos, player);
        }
        return true;
    }

    private void interactWith(World world, BlockPos pos, PlayerEntity player) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof INamedContainerProvider) {
            NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) tileEntity, tileEntity.getPos());
        }
    }

    @Override
    public float getExplosionResistance(BlockState state, IWorldReader world, BlockPos pos, @Nullable Entity exploder, Explosion explosion) {
        return 99.0F;
    }


    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileEntityBedrockiumMiner();
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public ToolType getHarvestTool(BlockState state) {
        return ToolType.PICKAXE;
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.with(BlockStateProperties.HORIZONTAL_FACING, rot.rotate(state.get(BlockStateProperties.HORIZONTAL_FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.toRotation(state.get(BlockStateProperties.HORIZONTAL_FACING)));
    }
}
