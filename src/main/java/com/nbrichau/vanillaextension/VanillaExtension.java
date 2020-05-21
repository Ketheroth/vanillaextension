package com.nbrichau.vanillaextension;

import com.nbrichau.vanillaextension.init.BlockInit;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ILightReader;
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

import javax.annotation.Nullable;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("vanillaextension")
public class VanillaExtension
{
	public static final Logger LOGGER = LogManager.getLogger();
	public static final String MODID = "vanillaextension";
	public static VanillaExtension instance;

	public VanillaExtension() {
		final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		// Register the setup method for modloading
		modEventBus.addListener(this::setup);
		// Register the doClientStuff method for modloading
		modEventBus.addListener(this::doClientStuff);
		instance = this;
		// Register ourselves for server and other game events we are interested in
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
		RenderTypeLookup.setRenderLayer(BlockInit.oak_leaves_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.spruce_leaves_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.birch_leaves_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.jungle_leaves_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.acacia_leaves_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.dark_oak_leaves_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.glass_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.spawner_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.ice_stairs, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.cactus_stairs, RenderType.getTranslucent());
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
