package tterrag.supermassivetech.proxy;

import static tterrag.supermassivetech.SuperMassiveTech.*;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.obj.WavefrontObject;
import tterrag.supermassivetech.client.model.ModelBlackHoleStorage;
import tterrag.supermassivetech.client.render.DirectionalModelRenderer;
import tterrag.supermassivetech.client.render.RenderBeaconEffect;
import tterrag.supermassivetech.client.render.RenderBlackHole;
import tterrag.supermassivetech.client.render.RenderStarHarvester;
import tterrag.supermassivetech.client.render.SimpleModelRenderer;
import tterrag.supermassivetech.client.render.entity.RenderFormingStar;
import tterrag.supermassivetech.entity.EntityFormingStar;
import tterrag.supermassivetech.lib.Reference;
import tterrag.supermassivetech.tile.TileBlackHole;
import tterrag.supermassivetech.tile.TileBlackHoleHopper;
import tterrag.supermassivetech.tile.TileBlackHoleStorage;
import tterrag.supermassivetech.tile.TileStarHarvester;
import tterrag.supermassivetech.tile.TileWaypoint;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
    public static DirectionalModelRenderer storage;

    public static DirectionalModelRenderer hopper;

    public static RenderStarHarvester starHarvester;

    public static RenderBeaconEffect beacon;
    public static SimpleModelRenderer waypoint;

    public static RenderBlackHole blackHole;

    @Override
    public void preInit()
    {
        renderIDStorage = RenderingRegistry.getNextAvailableRenderId();
        renderIDHopper = RenderingRegistry.getNextAvailableRenderId();
        renderIDStarHarvester = RenderingRegistry.getNextAvailableRenderId();
        renderIDWaypoint = RenderingRegistry.getNextAvailableRenderId();
        renderIDBlackHole = RenderingRegistry.getNextAvailableRenderId();
    }

    @Override
    public void registerRenderers()
    {
        storage = new DirectionalModelRenderer(new ModelBlackHoleStorage(), new ResourceLocation(Reference.MOD_TEXTUREPATH,
                "textures/models/storage.png"));
        hopper = new DirectionalModelRenderer(new ResourceLocation(Reference.MOD_TEXTUREPATH, "models/newHopper.obj"),
                new ResourceLocation(Reference.MOD_TEXTUREPATH, "textures/models/hopper.png"));
        starHarvester = new RenderStarHarvester(new ResourceLocation(Reference.MOD_TEXTUREPATH, "models/starHarvesterMain.obj"),
                new ResourceLocation(Reference.MOD_TEXTUREPATH, "models/starHarvesterSphere.obj"), new ResourceLocation(
                        Reference.MOD_TEXTUREPATH, "models/ring.obj"));
        beacon = new RenderBeaconEffect();
        blackHole = new RenderBlackHole();

        // BHS
        ClientRegistry.bindTileEntitySpecialRenderer(TileBlackHoleStorage.class, storage);
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(blockRegistry.blackHoleStorage), storage);

        // BHH
        ClientRegistry.bindTileEntitySpecialRenderer(TileBlackHoleHopper.class, hopper);
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(blockRegistry.blackHoleHopper), hopper);

        // Star Harvester
        ClientRegistry.bindTileEntitySpecialRenderer(TileStarHarvester.class, starHarvester);
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(blockRegistry.starHarvester), starHarvester);

        // Waypoint
        ClientRegistry.bindTileEntitySpecialRenderer(TileWaypoint.class, beacon);
        waypoint = new SimpleModelRenderer(new WavefrontObject(new ResourceLocation(Reference.MOD_TEXTUREPATH, "models/waypoint.obj")),
                renderIDWaypoint);
        RenderingRegistry.registerBlockHandler(waypoint);

        // Black Hole
        ClientRegistry.bindTileEntitySpecialRenderer(TileBlackHole.class, blackHole);
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(blockRegistry.starHarvester), starHarvester);

        // Entities
        RenderingRegistry.registerEntityRenderingHandler(EntityFormingStar.class, new RenderFormingStar());
    }
}
