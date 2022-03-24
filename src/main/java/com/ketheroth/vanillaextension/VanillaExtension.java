package com.ketheroth.vanillaextension;

import com.ketheroth.vanillaextension.init.VEBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

import static com.ketheroth.vanillaextension.init.VEBlocks.BLOCKS;
import static com.ketheroth.vanillaextension.init.VEItems.ITEMS;

@Mod("vanillaextension")
public class VanillaExtension {

	public static final Logger LOGGER = LogManager.getLogger();
	public static final String MODID = "vanillaextension";


	public VanillaExtension() {
		final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		modEventBus.addListener(this::doClientStuff);
		BLOCKS.register(modEventBus);
		ITEMS.register(modEventBus);
	}

	private void doClientStuff(final FMLClientSetupEvent event) {
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.grass_block_fence.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.oak_leaves_fence.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.spruce_leaves_fence.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.birch_leaves_fence.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.jungle_leaves_fence.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.acacia_leaves_fence.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.dark_oak_leaves_fence.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.AZALEA_LEAVES_FENCE.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.FLOWERING_AZALEA_LEAVES_FENCE.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.glass_fence.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.spawner_fence.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.ice_fence.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.soul_sand_fence.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.white_stained_glass_fence.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.orange_stained_glass_fence.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.magenta_stained_glass_fence.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.light_blue_stained_glass_fence.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.yellow_stained_glass_fence.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.lime_stained_glass_fence.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.pink_stained_glass_fence.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.gray_stained_glass_fence.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.light_gray_stained_glass_fence.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.cyan_stained_glass_fence.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.purple_stained_glass_fence.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.blue_stained_glass_fence.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.brown_stained_glass_fence.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.green_stained_glass_fence.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.red_stained_glass_fence.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.black_stained_glass_fence.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.oak_trapdoor_fence.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.spruce_trapdoor_fence.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.birch_trapdoor_fence.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.jungle_trapdoor_fence.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.acacia_trapdoor_fence.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.dark_oak_trapdoor_fence.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.iron_bars_fence.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.vine_fence.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.GLOW_LICHEN_FENCE.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.slime_block_fence.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.iron_trapdoor_fence.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.sea_lantern_fence.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.honey_block_fence.get(), RenderType.translucent());

		ItemBlockRenderTypes.setRenderLayer(VEBlocks.grass_block_stairs.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.oak_leaves_stairs.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.spruce_leaves_stairs.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.birch_leaves_stairs.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.jungle_leaves_stairs.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.acacia_leaves_stairs.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.dark_oak_leaves_stairs.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.AZALEA_LEAVES_STAIRS.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.FLOWERING_AZALEA_LEAVES_STAIRS.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.glass_stairs.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.spawner_stairs.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.ice_stairs.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.soul_sand_stairs.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.white_stained_glass_stairs.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.orange_stained_glass_stairs.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.magenta_stained_glass_stairs.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.light_blue_stained_glass_stairs.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.yellow_stained_glass_stairs.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.lime_stained_glass_stairs.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.pink_stained_glass_stairs.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.gray_stained_glass_stairs.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.light_gray_stained_glass_stairs.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.cyan_stained_glass_stairs.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.purple_stained_glass_stairs.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.blue_stained_glass_stairs.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.brown_stained_glass_stairs.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.green_stained_glass_stairs.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.red_stained_glass_stairs.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.black_stained_glass_stairs.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.oak_trapdoor_stairs.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.spruce_trapdoor_stairs.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.birch_trapdoor_stairs.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.jungle_trapdoor_stairs.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.acacia_trapdoor_stairs.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.dark_oak_trapdoor_stairs.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.iron_bars_stairs.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.vine_stairs.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.GLOW_LICHEN_STAIRS.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.slime_block_stairs.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.iron_trapdoor_stairs.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.sea_lantern_stairs.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.honey_block_stairs.get(), RenderType.translucent());

		ItemBlockRenderTypes.setRenderLayer(VEBlocks.grass_block_slab.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.oak_leaves_slab.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.spruce_leaves_slab.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.birch_leaves_slab.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.jungle_leaves_slab.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.acacia_leaves_slab.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.dark_oak_leaves_slab.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.AZALEA_LEAVES_SLAB.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.FLOWERING_AZALEA_LEAVES_SLAB.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.glass_slab.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.spawner_slab.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.ice_slab.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.soul_sand_slab.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.white_stained_glass_slab.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.orange_stained_glass_slab.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.magenta_stained_glass_slab.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.light_blue_stained_glass_slab.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.yellow_stained_glass_slab.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.lime_stained_glass_slab.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.pink_stained_glass_slab.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.gray_stained_glass_slab.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.light_gray_stained_glass_slab.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.cyan_stained_glass_slab.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.purple_stained_glass_slab.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.blue_stained_glass_slab.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.brown_stained_glass_slab.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.green_stained_glass_slab.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.red_stained_glass_slab.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.black_stained_glass_slab.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.oak_trapdoor_slab.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.spruce_trapdoor_slab.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.birch_trapdoor_slab.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.jungle_trapdoor_slab.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.acacia_trapdoor_slab.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.dark_oak_trapdoor_slab.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.iron_bars_slab.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.vine_slab.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.GLOW_LICHEN_SLAB.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.slime_block_slab.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.iron_trapdoor_slab.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.sea_lantern_slab.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.honey_block_slab.get(), RenderType.translucent());

		ItemBlockRenderTypes.setRenderLayer(VEBlocks.grass_block_wall.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.oak_leaves_wall.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.spruce_leaves_wall.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.birch_leaves_wall.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.jungle_leaves_wall.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.acacia_leaves_wall.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.dark_oak_leaves_wall.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.AZALEA_LEAVES_WALL.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.FLOWERING_AZALEA_LEAVES_WALL.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.glass_wall.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.spawner_wall.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.ice_wall.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.soul_sand_wall.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.white_stained_glass_wall.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.orange_stained_glass_wall.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.magenta_stained_glass_wall.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.light_blue_stained_glass_wall.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.yellow_stained_glass_wall.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.lime_stained_glass_wall.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.pink_stained_glass_wall.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.gray_stained_glass_wall.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.light_gray_stained_glass_wall.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.cyan_stained_glass_wall.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.purple_stained_glass_wall.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.blue_stained_glass_wall.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.brown_stained_glass_wall.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.green_stained_glass_wall.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.red_stained_glass_wall.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.black_stained_glass_wall.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.oak_trapdoor_wall.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.spruce_trapdoor_wall.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.birch_trapdoor_wall.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.jungle_trapdoor_wall.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.acacia_trapdoor_wall.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.dark_oak_trapdoor_wall.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.iron_bars_wall.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.vine_wall.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.GLOW_LICHEN_WALL.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.slime_block_wall.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.iron_trapdoor_wall.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.sea_lantern_wall.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.honey_block_wall.get(), RenderType.translucent());

		ItemBlockRenderTypes.setRenderLayer(VEBlocks.grass_block_trapdoor.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.oak_leaves_trapdoor.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.spruce_leaves_trapdoor.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.birch_leaves_trapdoor.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.jungle_leaves_trapdoor.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.acacia_leaves_trapdoor.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.dark_oak_leaves_trapdoor.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.AZALEA_LEAVES_TRAPDOOR.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.FLOWERING_AZALEA_LEAVES_TRAPDOOR.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.glass_trapdoor.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.spawner_trapdoor.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.ice_trapdoor.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.soul_sand_trapdoor.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.white_stained_glass_trapdoor.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.orange_stained_glass_trapdoor.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.magenta_stained_glass_trapdoor.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.light_blue_stained_glass_trapdoor.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.yellow_stained_glass_trapdoor.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.lime_stained_glass_trapdoor.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.pink_stained_glass_trapdoor.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.gray_stained_glass_trapdoor.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.light_gray_stained_glass_trapdoor.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.cyan_stained_glass_trapdoor.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.purple_stained_glass_trapdoor.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.blue_stained_glass_trapdoor.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.brown_stained_glass_trapdoor.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.green_stained_glass_trapdoor.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.red_stained_glass_trapdoor.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.black_stained_glass_trapdoor.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.iron_bars_trapdoor.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.vine_trapdoor.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.GLOW_LICHEN_TRAPDOOR.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.slime_block_trapdoor.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.sea_lantern_trapdoor.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(VEBlocks.honey_block_trapdoor.get(), RenderType.translucent());

		Minecraft.getInstance().getBlockColors().register((a, r, g, b) -> r != null && g != null ? BiomeColors.getAverageGrassColor(r, g) : GrassColor.get(0.5D, 1.0D), VEBlocks.grass_block_stairs.get(), VEBlocks.grass_block_slab.get(), VEBlocks.grass_block_fence.get(), VEBlocks.grass_block_wall.get(), VEBlocks.grass_block_trapdoor.get());
		Minecraft.getInstance().getBlockColors().register((a, r, g, b) -> FoliageColor.getEvergreenColor(), VEBlocks.spruce_leaves_stairs.get(), VEBlocks.spruce_leaves_slab.get(), VEBlocks.spruce_leaves_fence.get(), VEBlocks.spruce_leaves_wall.get(), VEBlocks.spruce_leaves_trapdoor.get());
		Minecraft.getInstance().getBlockColors().register((a, r, g, b) -> FoliageColor.getBirchColor(), VEBlocks.birch_leaves_stairs.get(), VEBlocks.birch_leaves_slab.get(), VEBlocks.birch_leaves_fence.get(), VEBlocks.birch_leaves_wall.get(), VEBlocks.birch_leaves_trapdoor.get());
		Minecraft.getInstance().getBlockColors().register((a, r, g, b) -> r != null && g != null ? BiomeColors.getAverageFoliageColor(r, g) : FoliageColor.getDefaultColor(), VEBlocks.oak_leaves_stairs.get(), VEBlocks.oak_leaves_slab.get(), VEBlocks.oak_leaves_fence.get(), VEBlocks.oak_leaves_wall.get(), VEBlocks.oak_leaves_trapdoor.get(),
				VEBlocks.jungle_leaves_stairs.get(), VEBlocks.jungle_leaves_slab.get(), VEBlocks.jungle_leaves_fence.get(), VEBlocks.jungle_leaves_wall.get(), VEBlocks.jungle_leaves_trapdoor.get(),
				VEBlocks.acacia_leaves_stairs.get(), VEBlocks.acacia_leaves_slab.get(), VEBlocks.acacia_leaves_fence.get(), VEBlocks.acacia_leaves_wall.get(), VEBlocks.acacia_leaves_trapdoor.get(),
				VEBlocks.dark_oak_leaves_stairs.get(), VEBlocks.dark_oak_leaves_slab.get(), VEBlocks.dark_oak_leaves_fence.get(), VEBlocks.dark_oak_leaves_wall.get(), VEBlocks.dark_oak_leaves_trapdoor.get(),
				VEBlocks.vine_stairs.get(), VEBlocks.vine_slab.get(), VEBlocks.vine_fence.get(), VEBlocks.vine_wall.get(), VEBlocks.vine_trapdoor.get());

		Minecraft.getInstance().getItemColors().register((blockItem, tintIndexIn) -> {
					BlockState blockstate = ((BlockItem) blockItem.getItem()).getBlock().defaultBlockState();
					return Minecraft.getInstance().getBlockColors().getColor(blockstate, null, null, tintIndexIn);
				}, VEBlocks.grass_block_stairs.get(), VEBlocks.grass_block_slab.get(), VEBlocks.grass_block_fence.get(), VEBlocks.grass_block_wall.get(), VEBlocks.grass_block_trapdoor.get(),
				VEBlocks.vine_stairs.get(), VEBlocks.vine_slab.get(), VEBlocks.vine_fence.get(), VEBlocks.vine_wall.get(), VEBlocks.vine_trapdoor.get(),
				VEBlocks.oak_leaves_stairs.get(), VEBlocks.oak_leaves_slab.get(), VEBlocks.oak_leaves_fence.get(), VEBlocks.oak_leaves_wall.get(), VEBlocks.oak_leaves_trapdoor.get(),
				VEBlocks.spruce_leaves_stairs.get(), VEBlocks.spruce_leaves_slab.get(), VEBlocks.spruce_leaves_fence.get(), VEBlocks.spruce_leaves_wall.get(), VEBlocks.spruce_leaves_trapdoor.get(),
				VEBlocks.birch_leaves_stairs.get(), VEBlocks.birch_leaves_slab.get(), VEBlocks.birch_leaves_fence.get(), VEBlocks.birch_leaves_wall.get(), VEBlocks.birch_leaves_trapdoor.get(),
				VEBlocks.jungle_leaves_stairs.get(), VEBlocks.jungle_leaves_slab.get(), VEBlocks.jungle_leaves_fence.get(), VEBlocks.jungle_leaves_wall.get(), VEBlocks.jungle_leaves_trapdoor.get(),
				VEBlocks.acacia_leaves_stairs.get(), VEBlocks.acacia_leaves_slab.get(), VEBlocks.acacia_leaves_fence.get(), VEBlocks.acacia_leaves_wall.get(), VEBlocks.acacia_leaves_trapdoor.get(),
				VEBlocks.dark_oak_leaves_stairs.get(), VEBlocks.dark_oak_leaves_slab.get(), VEBlocks.dark_oak_leaves_fence.get(), VEBlocks.dark_oak_leaves_wall.get(), VEBlocks.dark_oak_leaves_trapdoor.get());

	}

	public static class VanillaExtensionItemGroup extends CreativeModeTab {

		public static final VanillaExtensionItemGroup instance = new VanillaExtensionItemGroup(CreativeModeTab.TABS.length, "vanillaextensiontab");

		private VanillaExtensionItemGroup(int index, String label) {
			super(index, label);
		}

		@Override
		@Nonnull
		public ItemStack makeIcon() {
			return new ItemStack(Blocks.STRUCTURE_BLOCK);
		}

	}

}
