package com.nbrichau.vanillaextension;

import com.nbrichau.vanillaextension.init.BlockInit;
import net.minecraft.block.Blocks;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
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
		RenderTypeLookup.setRenderLayer(BlockInit.white_stained_glass_stairs, RenderType.getTranslucent());
		// TODO: 19/05/2020 change render transparent here for transparent or cropped block
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
