package com.ketheroth.vanillaextension;

import com.ketheroth.vanillaextension.init.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

@Mod("vanillaextension")
public class VanillaExtension {

	public static final Logger LOGGER = LogManager.getLogger();
	public static final String MODID = "vanillaextension";

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

	public VanillaExtension() {
		final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		modEventBus.addListener(this::doClientStuff);
		BLOCKS.register(modEventBus);
		ITEMS.register(modEventBus);
	}

	private void doClientStuff(final FMLClientSetupEvent event) {
		ItemBlockRenderTypes.setRenderLayer(StairsInit.grass_block_stairs.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(StairsInit.oak_leaves_stairs.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(StairsInit.spruce_leaves_stairs.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(StairsInit.birch_leaves_stairs.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(StairsInit.jungle_leaves_stairs.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(StairsInit.acacia_leaves_stairs.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(StairsInit.dark_oak_leaves_stairs.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(StairsInit.AZALEA_LEAVES_STAIRS.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(StairsInit.FLOWERING_AZALEA_LEAVES_STAIRS.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(StairsInit.glass_stairs.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(StairsInit.spawner_stairs.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(StairsInit.ice_stairs.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(StairsInit.soul_sand_stairs.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(StairsInit.white_stained_glass_stairs.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(StairsInit.orange_stained_glass_stairs.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(StairsInit.magenta_stained_glass_stairs.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(StairsInit.light_blue_stained_glass_stairs.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(StairsInit.yellow_stained_glass_stairs.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(StairsInit.lime_stained_glass_stairs.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(StairsInit.pink_stained_glass_stairs.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(StairsInit.gray_stained_glass_stairs.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(StairsInit.light_gray_stained_glass_stairs.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(StairsInit.cyan_stained_glass_stairs.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(StairsInit.purple_stained_glass_stairs.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(StairsInit.blue_stained_glass_stairs.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(StairsInit.brown_stained_glass_stairs.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(StairsInit.green_stained_glass_stairs.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(StairsInit.red_stained_glass_stairs.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(StairsInit.black_stained_glass_stairs.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(StairsInit.oak_trapdoor_stairs.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(StairsInit.spruce_trapdoor_stairs.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(StairsInit.birch_trapdoor_stairs.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(StairsInit.jungle_trapdoor_stairs.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(StairsInit.acacia_trapdoor_stairs.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(StairsInit.dark_oak_trapdoor_stairs.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(StairsInit.iron_bars_stairs.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(StairsInit.vine_stairs.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(StairsInit.GLOW_LICHEN_STAIRS.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(StairsInit.slime_block_stairs.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(StairsInit.iron_trapdoor_stairs.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(StairsInit.sea_lantern_stairs.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(StairsInit.honey_block_stairs.get(), RenderType.translucent());

		ItemBlockRenderTypes.setRenderLayer(SlabInit.grass_block_slab.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(SlabInit.oak_leaves_slab.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(SlabInit.spruce_leaves_slab.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(SlabInit.birch_leaves_slab.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(SlabInit.jungle_leaves_slab.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(SlabInit.acacia_leaves_slab.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(SlabInit.dark_oak_leaves_slab.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(SlabInit.AZALEA_LEAVES_SLAB.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(SlabInit.FLOWERING_AZALEA_LEAVES_SLAB.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(SlabInit.glass_slab.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(SlabInit.spawner_slab.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(SlabInit.ice_slab.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(SlabInit.soul_sand_slab.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(SlabInit.white_stained_glass_slab.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(SlabInit.orange_stained_glass_slab.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(SlabInit.magenta_stained_glass_slab.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(SlabInit.light_blue_stained_glass_slab.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(SlabInit.yellow_stained_glass_slab.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(SlabInit.lime_stained_glass_slab.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(SlabInit.pink_stained_glass_slab.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(SlabInit.gray_stained_glass_slab.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(SlabInit.light_gray_stained_glass_slab.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(SlabInit.cyan_stained_glass_slab.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(SlabInit.purple_stained_glass_slab.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(SlabInit.blue_stained_glass_slab.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(SlabInit.brown_stained_glass_slab.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(SlabInit.green_stained_glass_slab.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(SlabInit.red_stained_glass_slab.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(SlabInit.black_stained_glass_slab.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(SlabInit.oak_trapdoor_slab.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(SlabInit.spruce_trapdoor_slab.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(SlabInit.birch_trapdoor_slab.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(SlabInit.jungle_trapdoor_slab.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(SlabInit.acacia_trapdoor_slab.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(SlabInit.dark_oak_trapdoor_slab.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(SlabInit.iron_bars_slab.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(SlabInit.vine_slab.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(SlabInit.GLOW_LICHEN_SLAB.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(SlabInit.slime_block_slab.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(SlabInit.iron_trapdoor_slab.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(SlabInit.sea_lantern_slab.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(SlabInit.honey_block_slab.get(), RenderType.translucent());

		ItemBlockRenderTypes.setRenderLayer(FenceInit.grass_block_fence.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(FenceInit.oak_leaves_fence.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(FenceInit.spruce_leaves_fence.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(FenceInit.birch_leaves_fence.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(FenceInit.jungle_leaves_fence.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(FenceInit.acacia_leaves_fence.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(FenceInit.dark_oak_leaves_fence.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(FenceInit.AZALEA_LEAVES_FENCE.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(FenceInit.FLOWERING_AZALEA_LEAVES_FENCE.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(FenceInit.glass_fence.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(FenceInit.spawner_fence.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(FenceInit.ice_fence.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(FenceInit.soul_sand_fence.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(FenceInit.white_stained_glass_fence.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(FenceInit.orange_stained_glass_fence.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(FenceInit.magenta_stained_glass_fence.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(FenceInit.light_blue_stained_glass_fence.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(FenceInit.yellow_stained_glass_fence.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(FenceInit.lime_stained_glass_fence.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(FenceInit.pink_stained_glass_fence.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(FenceInit.gray_stained_glass_fence.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(FenceInit.light_gray_stained_glass_fence.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(FenceInit.cyan_stained_glass_fence.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(FenceInit.purple_stained_glass_fence.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(FenceInit.blue_stained_glass_fence.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(FenceInit.brown_stained_glass_fence.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(FenceInit.green_stained_glass_fence.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(FenceInit.red_stained_glass_fence.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(FenceInit.black_stained_glass_fence.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(FenceInit.oak_trapdoor_fence.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(FenceInit.spruce_trapdoor_fence.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(FenceInit.birch_trapdoor_fence.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(FenceInit.jungle_trapdoor_fence.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(FenceInit.acacia_trapdoor_fence.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(FenceInit.dark_oak_trapdoor_fence.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(FenceInit.iron_bars_fence.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(FenceInit.vine_fence.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(FenceInit.GLOW_LICHEN_FENCE.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(FenceInit.slime_block_fence.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(FenceInit.iron_trapdoor_fence.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(FenceInit.sea_lantern_fence.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(FenceInit.honey_block_fence.get(), RenderType.translucent());

		ItemBlockRenderTypes.setRenderLayer(WallInit.grass_block_wall.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(WallInit.oak_leaves_wall.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(WallInit.spruce_leaves_wall.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(WallInit.birch_leaves_wall.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(WallInit.jungle_leaves_wall.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(WallInit.acacia_leaves_wall.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(WallInit.dark_oak_leaves_wall.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(WallInit.AZALEA_LEAVES_WALL.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(WallInit.FLOWERING_AZALEA_LEAVES_WALL.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(WallInit.glass_wall.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(WallInit.spawner_wall.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(WallInit.ice_wall.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(WallInit.soul_sand_wall.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(WallInit.white_stained_glass_wall.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(WallInit.orange_stained_glass_wall.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(WallInit.magenta_stained_glass_wall.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(WallInit.light_blue_stained_glass_wall.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(WallInit.yellow_stained_glass_wall.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(WallInit.lime_stained_glass_wall.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(WallInit.pink_stained_glass_wall.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(WallInit.gray_stained_glass_wall.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(WallInit.light_gray_stained_glass_wall.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(WallInit.cyan_stained_glass_wall.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(WallInit.purple_stained_glass_wall.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(WallInit.blue_stained_glass_wall.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(WallInit.brown_stained_glass_wall.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(WallInit.green_stained_glass_wall.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(WallInit.red_stained_glass_wall.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(WallInit.black_stained_glass_wall.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(WallInit.oak_trapdoor_wall.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(WallInit.spruce_trapdoor_wall.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(WallInit.birch_trapdoor_wall.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(WallInit.jungle_trapdoor_wall.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(WallInit.acacia_trapdoor_wall.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(WallInit.dark_oak_trapdoor_wall.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(WallInit.iron_bars_wall.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(WallInit.vine_wall.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(WallInit.GLOW_LICHEN_WALL.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(WallInit.slime_block_wall.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(WallInit.iron_trapdoor_wall.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(WallInit.sea_lantern_wall.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(WallInit.honey_block_wall.get(), RenderType.translucent());

		ItemBlockRenderTypes.setRenderLayer(TrapdoorInit.grass_block_trapdoor.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(TrapdoorInit.oak_leaves_trapdoor.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(TrapdoorInit.spruce_leaves_trapdoor.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(TrapdoorInit.birch_leaves_trapdoor.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(TrapdoorInit.jungle_leaves_trapdoor.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(TrapdoorInit.acacia_leaves_trapdoor.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(TrapdoorInit.dark_oak_leaves_trapdoor.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(TrapdoorInit.AZALEA_LEAVES_TRAPDOOR.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(TrapdoorInit.FLOWERING_AZALEA_LEAVES_TRAPDOOR.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(TrapdoorInit.glass_trapdoor.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(TrapdoorInit.spawner_trapdoor.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(TrapdoorInit.ice_trapdoor.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(TrapdoorInit.soul_sand_trapdoor.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(TrapdoorInit.white_stained_glass_trapdoor.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(TrapdoorInit.orange_stained_glass_trapdoor.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(TrapdoorInit.magenta_stained_glass_trapdoor.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(TrapdoorInit.light_blue_stained_glass_trapdoor.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(TrapdoorInit.yellow_stained_glass_trapdoor.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(TrapdoorInit.lime_stained_glass_trapdoor.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(TrapdoorInit.pink_stained_glass_trapdoor.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(TrapdoorInit.gray_stained_glass_trapdoor.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(TrapdoorInit.light_gray_stained_glass_trapdoor.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(TrapdoorInit.cyan_stained_glass_trapdoor.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(TrapdoorInit.purple_stained_glass_trapdoor.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(TrapdoorInit.blue_stained_glass_trapdoor.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(TrapdoorInit.brown_stained_glass_trapdoor.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(TrapdoorInit.green_stained_glass_trapdoor.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(TrapdoorInit.red_stained_glass_trapdoor.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(TrapdoorInit.black_stained_glass_trapdoor.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(TrapdoorInit.iron_bars_trapdoor.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(TrapdoorInit.vine_trapdoor.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(TrapdoorInit.GLOW_LICHEN_TRAPDOOR.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(TrapdoorInit.slime_block_trapdoor.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(TrapdoorInit.sea_lantern_trapdoor.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(TrapdoorInit.honey_block_trapdoor.get(), RenderType.translucent());

		Minecraft.getInstance().getBlockColors().register((a, r, g, b) -> r != null && g != null ? BiomeColors.getAverageGrassColor(r, g) : GrassColor.get(0.5D, 1.0D), StairsInit.grass_block_stairs.get(), SlabInit.grass_block_slab.get(), FenceInit.grass_block_fence.get(), WallInit.grass_block_wall.get(), TrapdoorInit.grass_block_trapdoor.get());
		Minecraft.getInstance().getBlockColors().register((a, r, g, b) -> FoliageColor.getEvergreenColor(), StairsInit.spruce_leaves_stairs.get(), SlabInit.spruce_leaves_slab.get(), FenceInit.spruce_leaves_fence.get(), WallInit.spruce_leaves_wall.get(), TrapdoorInit.spruce_leaves_trapdoor.get());
		Minecraft.getInstance().getBlockColors().register((a, r, g, b) -> FoliageColor.getBirchColor(), StairsInit.birch_leaves_stairs.get(), SlabInit.birch_leaves_slab.get(), FenceInit.birch_leaves_fence.get(), WallInit.birch_leaves_wall.get(), TrapdoorInit.birch_leaves_trapdoor.get());
		Minecraft.getInstance().getBlockColors().register((a, r, g, b) -> r != null && g != null ? BiomeColors.getAverageFoliageColor(r, g) : FoliageColor.getDefaultColor(), StairsInit.oak_leaves_stairs.get(), SlabInit.oak_leaves_slab.get(), FenceInit.oak_leaves_fence.get(), WallInit.oak_leaves_wall.get(), TrapdoorInit.oak_leaves_trapdoor.get(),
				StairsInit.jungle_leaves_stairs.get(), SlabInit.jungle_leaves_slab.get(), FenceInit.jungle_leaves_fence.get(), WallInit.jungle_leaves_wall.get(), TrapdoorInit.jungle_leaves_trapdoor.get(),
				StairsInit.acacia_leaves_stairs.get(), SlabInit.acacia_leaves_slab.get(), FenceInit.acacia_leaves_fence.get(), WallInit.acacia_leaves_wall.get(), TrapdoorInit.acacia_leaves_trapdoor.get(),
				StairsInit.dark_oak_leaves_stairs.get(), SlabInit.dark_oak_leaves_slab.get(), FenceInit.dark_oak_leaves_fence.get(), WallInit.dark_oak_leaves_wall.get(), TrapdoorInit.dark_oak_leaves_trapdoor.get(),
				StairsInit.vine_stairs.get(), SlabInit.vine_slab.get(), FenceInit.vine_fence.get(), WallInit.vine_wall.get(), TrapdoorInit.vine_trapdoor.get());

		Minecraft.getInstance().getItemColors().register((blockItem, tintIndexIn) -> {
					BlockState blockstate = ((BlockItem) blockItem.getItem()).getBlock().defaultBlockState();
					return Minecraft.getInstance().getBlockColors().getColor(blockstate, null, null, tintIndexIn);
				}, StairsInit.grass_block_stairs.get(), SlabInit.grass_block_slab.get(), FenceInit.grass_block_fence.get(), WallInit.grass_block_wall.get(), TrapdoorInit.grass_block_trapdoor.get(),
				StairsInit.vine_stairs.get(), SlabInit.vine_slab.get(), FenceInit.vine_fence.get(), WallInit.vine_wall.get(), TrapdoorInit.vine_trapdoor.get(),
				StairsInit.oak_leaves_stairs.get(), SlabInit.oak_leaves_slab.get(), FenceInit.oak_leaves_fence.get(), WallInit.oak_leaves_wall.get(), TrapdoorInit.oak_leaves_trapdoor.get(),
				StairsInit.spruce_leaves_stairs.get(), SlabInit.spruce_leaves_slab.get(), FenceInit.spruce_leaves_fence.get(), WallInit.spruce_leaves_wall.get(), TrapdoorInit.spruce_leaves_trapdoor.get(),
				StairsInit.birch_leaves_stairs.get(), SlabInit.birch_leaves_slab.get(), FenceInit.birch_leaves_fence.get(), WallInit.birch_leaves_wall.get(), TrapdoorInit.birch_leaves_trapdoor.get(),
				StairsInit.jungle_leaves_stairs.get(), SlabInit.jungle_leaves_slab.get(), FenceInit.jungle_leaves_fence.get(), WallInit.jungle_leaves_wall.get(), TrapdoorInit.jungle_leaves_trapdoor.get(),
				StairsInit.acacia_leaves_stairs.get(), SlabInit.acacia_leaves_slab.get(), FenceInit.acacia_leaves_fence.get(), WallInit.acacia_leaves_wall.get(), TrapdoorInit.acacia_leaves_trapdoor.get(),
				StairsInit.dark_oak_leaves_stairs.get(), SlabInit.dark_oak_leaves_slab.get(), FenceInit.dark_oak_leaves_fence.get(), WallInit.dark_oak_leaves_wall.get(), TrapdoorInit.dark_oak_leaves_trapdoor.get());

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
