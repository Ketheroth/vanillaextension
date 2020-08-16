package com.nbrichau.vanillaextension;

import com.nbrichau.vanillaextension.init.BlockInit;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.FoliageColors;
import net.minecraft.world.GrassColors;
import net.minecraft.world.IBlockDisplayReader;
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
		RenderTypeLookup.setRenderLayer(BlockInit.grass_block_stairs, RenderType.getCutoutMipped());
		RenderTypeLookup.setRenderLayer(BlockInit.oak_leaves_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.spruce_leaves_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.birch_leaves_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.jungle_leaves_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.acacia_leaves_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.dark_oak_leaves_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.glass_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.spawner_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.ice_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.soul_sand_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.white_stained_glass_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.orange_stained_glass_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.magenta_stained_glass_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.light_blue_stained_glass_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.yellow_stained_glass_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.lime_stained_glass_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.pink_stained_glass_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.gray_stained_glass_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.light_gray_stained_glass_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.cyan_stained_glass_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.purple_stained_glass_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.blue_stained_glass_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.brown_stained_glass_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.green_stained_glass_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.red_stained_glass_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.black_stained_glass_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.oak_trapdoor_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.spruce_trapdoor_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.birch_trapdoor_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.jungle_trapdoor_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.acacia_trapdoor_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.dark_oak_trapdoor_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.iron_bars_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.vine_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.slime_block_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.iron_trapdoor_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.sea_lantern_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.honey_block_stairs, RenderType.getTranslucent());

		RenderTypeLookup.setRenderLayer(BlockInit.grass_block_slab, RenderType.getCutoutMipped());
		RenderTypeLookup.setRenderLayer(BlockInit.oak_leaves_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.spruce_leaves_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.birch_leaves_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.jungle_leaves_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.acacia_leaves_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.dark_oak_leaves_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.glass_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.spawner_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.ice_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.soul_sand_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.white_stained_glass_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.orange_stained_glass_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.magenta_stained_glass_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.light_blue_stained_glass_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.yellow_stained_glass_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.lime_stained_glass_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.pink_stained_glass_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.gray_stained_glass_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.light_gray_stained_glass_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.cyan_stained_glass_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.purple_stained_glass_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.blue_stained_glass_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.brown_stained_glass_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.green_stained_glass_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.red_stained_glass_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.black_stained_glass_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.oak_trapdoor_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.spruce_trapdoor_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.birch_trapdoor_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.jungle_trapdoor_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.acacia_trapdoor_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.dark_oak_trapdoor_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.iron_bars_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.vine_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.slime_block_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.iron_trapdoor_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.sea_lantern_slab, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.honey_block_slab, RenderType.getTranslucent());

		RenderTypeLookup.setRenderLayer(BlockInit.grass_block_fence, RenderType.getCutoutMipped());
		RenderTypeLookup.setRenderLayer(BlockInit.oak_leaves_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.spruce_leaves_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.birch_leaves_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.jungle_leaves_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.acacia_leaves_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.dark_oak_leaves_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.glass_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.spawner_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.ice_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.soul_sand_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.white_stained_glass_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.orange_stained_glass_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.magenta_stained_glass_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.light_blue_stained_glass_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.yellow_stained_glass_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.lime_stained_glass_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.pink_stained_glass_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.gray_stained_glass_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.light_gray_stained_glass_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.cyan_stained_glass_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.purple_stained_glass_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.blue_stained_glass_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.brown_stained_glass_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.green_stained_glass_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.red_stained_glass_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.black_stained_glass_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.oak_trapdoor_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.spruce_trapdoor_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.birch_trapdoor_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.jungle_trapdoor_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.acacia_trapdoor_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.dark_oak_trapdoor_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.iron_bars_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.vine_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.slime_block_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.iron_trapdoor_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.sea_lantern_fence, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.honey_block_fence, RenderType.getTranslucent());

		RenderTypeLookup.setRenderLayer(BlockInit.grass_block_wall, RenderType.getCutoutMipped());
		RenderTypeLookup.setRenderLayer(BlockInit.oak_leaves_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.spruce_leaves_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.birch_leaves_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.jungle_leaves_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.acacia_leaves_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.dark_oak_leaves_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.glass_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.spawner_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.ice_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.soul_sand_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.white_stained_glass_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.orange_stained_glass_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.magenta_stained_glass_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.light_blue_stained_glass_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.yellow_stained_glass_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.lime_stained_glass_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.pink_stained_glass_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.gray_stained_glass_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.light_gray_stained_glass_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.cyan_stained_glass_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.purple_stained_glass_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.blue_stained_glass_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.brown_stained_glass_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.green_stained_glass_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.red_stained_glass_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.black_stained_glass_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.oak_trapdoor_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.spruce_trapdoor_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.birch_trapdoor_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.jungle_trapdoor_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.acacia_trapdoor_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.dark_oak_trapdoor_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.iron_bars_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.vine_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.slime_block_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.iron_trapdoor_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.sea_lantern_wall, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.honey_block_wall, RenderType.getTranslucent());

		Minecraft.getInstance().getBlockColors().register((a, r, g, b) -> r != null && g != null ? BiomeColors.getGrassColor(r, g) : GrassColors.get(0.5D, 1.0D), BlockInit.grass_block_stairs, BlockInit.grass_block_slab, BlockInit.grass_block_fence, BlockInit.grass_block_wall);
		Minecraft.getInstance().getBlockColors().register((a, r, g, b) -> FoliageColors.getSpruce(), BlockInit.spruce_leaves_stairs, BlockInit.spruce_leaves_slab, BlockInit.spruce_leaves_fence, BlockInit.spruce_leaves_wall);
		Minecraft.getInstance().getBlockColors().register((a, r, g, b) -> FoliageColors.getBirch(), BlockInit.birch_leaves_stairs, BlockInit.birch_leaves_slab, BlockInit.birch_leaves_fence, BlockInit.birch_leaves_wall);
		Minecraft.getInstance().getBlockColors().register((a, r, g, b) -> r != null && g != null ? BiomeColors.getFoliageColor(r, g) : FoliageColors.getDefault(), BlockInit.oak_leaves_stairs, BlockInit.oak_leaves_slab, BlockInit.oak_leaves_fence, BlockInit.oak_leaves_wall,
			BlockInit.jungle_leaves_stairs,BlockInit.jungle_leaves_slab, BlockInit.jungle_leaves_fence, BlockInit.jungle_leaves_wall,
			BlockInit.acacia_leaves_stairs, BlockInit.acacia_leaves_slab, BlockInit.acacia_leaves_fence, BlockInit.acacia_leaves_wall,
			BlockInit.dark_oak_leaves_stairs,BlockInit.dark_oak_leaves_slab, BlockInit.dark_oak_leaves_fence, BlockInit.dark_oak_leaves_wall,
			BlockInit.vine_stairs, BlockInit.vine_slab, BlockInit.vine_fence, BlockInit.vine_wall);

		Minecraft.getInstance().getItemColors().register((blockItem, tintIndexIn) -> {
			BlockState blockstate = ((BlockItem)blockItem.getItem()).getBlock().getDefaultState();
			return Minecraft.getInstance().getBlockColors().getColor(blockstate, null, null, tintIndexIn);
		}, BlockInit.grass_block_stairs, BlockInit.grass_block_slab, BlockInit.grass_block_fence, BlockInit.grass_block_wall,
			BlockInit.vine_stairs, BlockInit.vine_slab, BlockInit.vine_fence, BlockInit.vine_wall,
			BlockInit.oak_leaves_stairs, BlockInit.oak_leaves_slab, BlockInit.oak_leaves_fence, BlockInit.oak_leaves_wall,
			BlockInit.spruce_leaves_stairs, BlockInit.spruce_leaves_slab, BlockInit.spruce_leaves_fence, BlockInit.spruce_leaves_wall,
			BlockInit.birch_leaves_stairs, BlockInit.birch_leaves_slab, BlockInit.birch_leaves_fence, BlockInit.birch_leaves_wall,
			BlockInit.jungle_leaves_stairs, BlockInit.jungle_leaves_slab, BlockInit.jungle_leaves_fence, BlockInit.jungle_leaves_wall,
			BlockInit.acacia_leaves_stairs, BlockInit.acacia_leaves_slab, BlockInit.acacia_leaves_fence, BlockInit.acacia_leaves_wall,
			BlockInit.dark_oak_leaves_stairs, BlockInit.dark_oak_leaves_slab, BlockInit.dark_oak_leaves_fence, BlockInit.dark_oak_leaves_wall);

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
