package com.nbrichau.vanillaextension;

import com.nbrichau.vanillaextension.init.*;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.world.FoliageColors;
import net.minecraft.world.GrassColors;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("vanillaextension")
public class VanillaExtension
{
	public static final Logger LOGGER = LogManager.getLogger();
	public static final String MODID = "vanillaextension";
	public static VanillaExtension instance;

	public VanillaExtension() {
		final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		modEventBus.addListener(this::setup);
		modEventBus.addListener(this::doClientStuff);
		instance = this;
		MinecraftForge.EVENT_BUS.register(this);
		/*
		//other mod compatibility
		// Register the enqueueIMC method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
		// Register the processIMC method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
		*/
	}

	private void setup(final FMLCommonSetupEvent event) {
	}

	private void doClientStuff(final FMLClientSetupEvent event) {
		RenderTypeLookup.setRenderLayer(StairsInit.grass_block_stairs, RenderType.getCutoutMipped());
		RenderTypeLookup.setRenderLayer(StairsInit.oak_leaves_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(StairsInit.spruce_leaves_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(StairsInit.birch_leaves_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(StairsInit.jungle_leaves_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(StairsInit.acacia_leaves_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(StairsInit.dark_oak_leaves_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(StairsInit.glass_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(StairsInit.spawner_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(StairsInit.ice_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(StairsInit.soul_sand_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(StairsInit.white_stained_glass_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(StairsInit.orange_stained_glass_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(StairsInit.magenta_stained_glass_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(StairsInit.light_blue_stained_glass_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(StairsInit.yellow_stained_glass_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(StairsInit.lime_stained_glass_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(StairsInit.pink_stained_glass_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(StairsInit.gray_stained_glass_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(StairsInit.light_gray_stained_glass_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(StairsInit.cyan_stained_glass_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(StairsInit.purple_stained_glass_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(StairsInit.blue_stained_glass_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(StairsInit.brown_stained_glass_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(StairsInit.green_stained_glass_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(StairsInit.red_stained_glass_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(StairsInit.black_stained_glass_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(StairsInit.oak_trapdoor_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(StairsInit.spruce_trapdoor_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(StairsInit.birch_trapdoor_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(StairsInit.jungle_trapdoor_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(StairsInit.acacia_trapdoor_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(StairsInit.dark_oak_trapdoor_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(StairsInit.iron_bars_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(StairsInit.vine_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(StairsInit.slime_block_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(StairsInit.iron_trapdoor_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(StairsInit.sea_lantern_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(StairsInit.honey_block_stairs, RenderType.getTranslucent());

		RenderTypeLookup.setRenderLayer(SlabInit.grass_block_slab, RenderType.getCutoutMipped());
		RenderTypeLookup.setRenderLayer(SlabInit.oak_leaves_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(SlabInit.spruce_leaves_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(SlabInit.birch_leaves_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(SlabInit.jungle_leaves_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(SlabInit.acacia_leaves_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(SlabInit.dark_oak_leaves_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(SlabInit.glass_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(SlabInit.spawner_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(SlabInit.ice_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(SlabInit.soul_sand_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(SlabInit.white_stained_glass_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(SlabInit.orange_stained_glass_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(SlabInit.magenta_stained_glass_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(SlabInit.light_blue_stained_glass_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(SlabInit.yellow_stained_glass_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(SlabInit.lime_stained_glass_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(SlabInit.pink_stained_glass_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(SlabInit.gray_stained_glass_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(SlabInit.light_gray_stained_glass_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(SlabInit.cyan_stained_glass_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(SlabInit.purple_stained_glass_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(SlabInit.blue_stained_glass_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(SlabInit.brown_stained_glass_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(SlabInit.green_stained_glass_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(SlabInit.red_stained_glass_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(SlabInit.black_stained_glass_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(SlabInit.oak_trapdoor_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(SlabInit.spruce_trapdoor_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(SlabInit.birch_trapdoor_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(SlabInit.jungle_trapdoor_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(SlabInit.acacia_trapdoor_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(SlabInit.dark_oak_trapdoor_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(SlabInit.iron_bars_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(SlabInit.vine_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(SlabInit.slime_block_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(SlabInit.iron_trapdoor_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(SlabInit.sea_lantern_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(SlabInit.honey_block_slab, RenderType.getTranslucent());

		RenderTypeLookup.setRenderLayer(FenceInit.grass_block_fence, RenderType.getCutoutMipped());
		RenderTypeLookup.setRenderLayer(FenceInit.oak_leaves_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(FenceInit.spruce_leaves_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(FenceInit.birch_leaves_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(FenceInit.jungle_leaves_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(FenceInit.acacia_leaves_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(FenceInit.dark_oak_leaves_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(FenceInit.glass_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(FenceInit.spawner_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(FenceInit.ice_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(FenceInit.soul_sand_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(FenceInit.white_stained_glass_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(FenceInit.orange_stained_glass_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(FenceInit.magenta_stained_glass_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(FenceInit.light_blue_stained_glass_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(FenceInit.yellow_stained_glass_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(FenceInit.lime_stained_glass_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(FenceInit.pink_stained_glass_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(FenceInit.gray_stained_glass_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(FenceInit.light_gray_stained_glass_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(FenceInit.cyan_stained_glass_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(FenceInit.purple_stained_glass_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(FenceInit.blue_stained_glass_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(FenceInit.brown_stained_glass_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(FenceInit.green_stained_glass_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(FenceInit.red_stained_glass_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(FenceInit.black_stained_glass_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(FenceInit.oak_trapdoor_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(FenceInit.spruce_trapdoor_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(FenceInit.birch_trapdoor_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(FenceInit.jungle_trapdoor_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(FenceInit.acacia_trapdoor_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(FenceInit.dark_oak_trapdoor_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(FenceInit.iron_bars_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(FenceInit.vine_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(FenceInit.slime_block_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(FenceInit.iron_trapdoor_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(FenceInit.sea_lantern_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(FenceInit.honey_block_fence, RenderType.getTranslucent());

		RenderTypeLookup.setRenderLayer(WallInit.grass_block_wall, RenderType.getCutoutMipped());
		RenderTypeLookup.setRenderLayer(WallInit.oak_leaves_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(WallInit.spruce_leaves_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(WallInit.birch_leaves_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(WallInit.jungle_leaves_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(WallInit.acacia_leaves_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(WallInit.dark_oak_leaves_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(WallInit.glass_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(WallInit.spawner_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(WallInit.ice_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(WallInit.soul_sand_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(WallInit.white_stained_glass_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(WallInit.orange_stained_glass_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(WallInit.magenta_stained_glass_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(WallInit.light_blue_stained_glass_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(WallInit.yellow_stained_glass_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(WallInit.lime_stained_glass_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(WallInit.pink_stained_glass_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(WallInit.gray_stained_glass_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(WallInit.light_gray_stained_glass_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(WallInit.cyan_stained_glass_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(WallInit.purple_stained_glass_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(WallInit.blue_stained_glass_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(WallInit.brown_stained_glass_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(WallInit.green_stained_glass_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(WallInit.red_stained_glass_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(WallInit.black_stained_glass_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(WallInit.oak_trapdoor_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(WallInit.spruce_trapdoor_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(WallInit.birch_trapdoor_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(WallInit.jungle_trapdoor_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(WallInit.acacia_trapdoor_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(WallInit.dark_oak_trapdoor_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(WallInit.iron_bars_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(WallInit.vine_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(WallInit.slime_block_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(WallInit.iron_trapdoor_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(WallInit.sea_lantern_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(WallInit.honey_block_wall, RenderType.getTranslucent());

		RenderTypeLookup.setRenderLayer(TrapdoorInit.grass_block_trapdoor, RenderType.getCutoutMipped());
		RenderTypeLookup.setRenderLayer(TrapdoorInit.oak_leaves_trapdoor, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(TrapdoorInit.spruce_leaves_trapdoor, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(TrapdoorInit.birch_leaves_trapdoor, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(TrapdoorInit.jungle_leaves_trapdoor, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(TrapdoorInit.acacia_leaves_trapdoor, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(TrapdoorInit.dark_oak_leaves_trapdoor, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(TrapdoorInit.glass_trapdoor, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(TrapdoorInit.spawner_trapdoor, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(TrapdoorInit.ice_trapdoor, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(TrapdoorInit.soul_sand_trapdoor, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(TrapdoorInit.white_stained_glass_trapdoor, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(TrapdoorInit.orange_stained_glass_trapdoor, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(TrapdoorInit.magenta_stained_glass_trapdoor, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(TrapdoorInit.light_blue_stained_glass_trapdoor, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(TrapdoorInit.yellow_stained_glass_trapdoor, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(TrapdoorInit.lime_stained_glass_trapdoor, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(TrapdoorInit.pink_stained_glass_trapdoor, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(TrapdoorInit.gray_stained_glass_trapdoor, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(TrapdoorInit.light_gray_stained_glass_trapdoor, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(TrapdoorInit.cyan_stained_glass_trapdoor, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(TrapdoorInit.purple_stained_glass_trapdoor, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(TrapdoorInit.blue_stained_glass_trapdoor, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(TrapdoorInit.brown_stained_glass_trapdoor, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(TrapdoorInit.green_stained_glass_trapdoor, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(TrapdoorInit.red_stained_glass_trapdoor, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(TrapdoorInit.black_stained_glass_trapdoor, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(TrapdoorInit.iron_bars_trapdoor, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(TrapdoorInit.vine_trapdoor, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(TrapdoorInit.slime_block_trapdoor, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(TrapdoorInit.sea_lantern_trapdoor, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(TrapdoorInit.honey_block_trapdoor, RenderType.getTranslucent());

		Minecraft.getInstance().getBlockColors().register((a, r, g, b) -> r != null && g != null ? BiomeColors.getGrassColor(r, g) : GrassColors.get(0.5D, 1.0D), StairsInit.grass_block_stairs, SlabInit.grass_block_slab, FenceInit.grass_block_fence, WallInit.grass_block_wall);
		Minecraft.getInstance().getBlockColors().register((a, r, g, b) -> FoliageColors.getSpruce(), StairsInit.spruce_leaves_stairs, SlabInit.spruce_leaves_slab, FenceInit.spruce_leaves_fence, WallInit.spruce_leaves_wall);
		Minecraft.getInstance().getBlockColors().register((a, r, g, b) -> FoliageColors.getBirch(), StairsInit.birch_leaves_stairs, SlabInit.birch_leaves_slab, FenceInit.birch_leaves_fence, WallInit.birch_leaves_wall);
		Minecraft.getInstance().getBlockColors().register((a, r, g, b) -> r != null && g != null ? BiomeColors.getFoliageColor(r, g) : FoliageColors.getDefault(), StairsInit.oak_leaves_stairs, SlabInit.oak_leaves_slab, FenceInit.oak_leaves_fence, WallInit.oak_leaves_wall,
			StairsInit.jungle_leaves_stairs,SlabInit.jungle_leaves_slab, FenceInit.jungle_leaves_fence, WallInit.jungle_leaves_wall,
			StairsInit.acacia_leaves_stairs, SlabInit.acacia_leaves_slab, FenceInit.acacia_leaves_fence, WallInit.acacia_leaves_wall,
			StairsInit.dark_oak_leaves_stairs,SlabInit.dark_oak_leaves_slab, FenceInit.dark_oak_leaves_fence, WallInit.dark_oak_leaves_wall,
			StairsInit.vine_stairs, SlabInit.vine_slab, FenceInit.vine_fence, WallInit.vine_wall);

		Minecraft.getInstance().getItemColors().register((blockItem, tintIndexIn) -> {
			BlockState blockstate = ((BlockItem)blockItem.getItem()).getBlock().getDefaultState();
			return Minecraft.getInstance().getBlockColors().getColor(blockstate, null, null, tintIndexIn);
		}, StairsInit.grass_block_stairs, SlabInit.grass_block_slab, FenceInit.grass_block_fence, WallInit.grass_block_wall,
			StairsInit.vine_stairs, SlabInit.vine_slab, FenceInit.vine_fence, WallInit.vine_wall,
			StairsInit.oak_leaves_stairs, SlabInit.oak_leaves_slab, FenceInit.oak_leaves_fence, WallInit.oak_leaves_wall,
			StairsInit.spruce_leaves_stairs, SlabInit.spruce_leaves_slab, FenceInit.spruce_leaves_fence, WallInit.spruce_leaves_wall,
			StairsInit.birch_leaves_stairs, SlabInit.birch_leaves_slab, FenceInit.birch_leaves_fence, WallInit.birch_leaves_wall,
			StairsInit.jungle_leaves_stairs, SlabInit.jungle_leaves_slab, FenceInit.jungle_leaves_fence, WallInit.jungle_leaves_wall,
			StairsInit.acacia_leaves_stairs, SlabInit.acacia_leaves_slab, FenceInit.acacia_leaves_fence, WallInit.acacia_leaves_wall,
			StairsInit.dark_oak_leaves_stairs, SlabInit.dark_oak_leaves_slab, FenceInit.dark_oak_leaves_fence, WallInit.dark_oak_leaves_wall);

	}

	// You can use SubscribeEvent and let the Event Bus discover methods to call
	@SubscribeEvent
	public void onServerStarting(FMLServerStartingEvent event) {
	}

	public static class VanillaExtensionItemGroup extends ItemGroup {
		public static final VanillaExtensionItemGroup instance = new VanillaExtensionItemGroup(ItemGroup.GROUPS.length,"vanillaextensiontab");
		@Override
		public ItemStack createIcon() {
			return new ItemStack(Blocks.STRUCTURE_BLOCK);
		}
		private VanillaExtensionItemGroup(int index, String label){
			super(index,label);
		}
	}
	/*
	//other mods compatibility
	private void enqueueIMC(final InterModEnqueueEvent event)
	{
		// some example code to dispatch IMC to another mod
		InterModComms.sendTo("vanillaextension", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
	}
	private void processIMC(final InterModProcessEvent event)
	{
		// some example code to receive and process InterModComms from other mods
		LOGGER.info("Got IMC {}", event.getIMCStream().
				map(m->m.getMessageSupplier().get()).
				collect(Collectors.toList()));
	}
	*/
}
