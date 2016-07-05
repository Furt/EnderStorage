package codechicken.enderstorage.proxy;

import codechicken.core.CCUpdateChecker;
import codechicken.enderstorage.client.EnderPouchModelLoader;
import codechicken.enderstorage.client.render.tile.RenderTileEnderChest;
import codechicken.enderstorage.client.render.tile.RenderTileEnderTank;
import codechicken.enderstorage.handler.ConfigurationHandler;
import codechicken.enderstorage.init.ModBlocks;
import codechicken.enderstorage.init.ModItems;
import codechicken.enderstorage.network.EnderStorageCPH;
import codechicken.enderstorage.tile.TileEnderChest;
import codechicken.enderstorage.tile.TileEnderTank;
import codechicken.lib.packet.PacketCustom;
import codechicken.lib.render.TextureUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraftforge.fml.client.registry.ClientRegistry;

/**
 * Created by covers1624 on 4/11/2016.
 */
public class ClientProxy extends CommonProxy {

    @Override
    public void preInit() {
        super.preInit();
        //ModelLoaderRegistry.registerLoader(new EnderPouchModelLoader());
        EnderPouchModelLoader loader = new EnderPouchModelLoader();
        ((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(loader);
        TextureUtils.addIconRegister(loader);
        ModBlocks.registerModels();
        ModItems.registerModels();
    }

    @Override
    public void init() {
        if (ConfigurationHandler.clientCheckUpdates) {
            CCUpdateChecker.updateCheck("EnderStorage");
        }
        super.init();
        PacketCustom.assignHandler(EnderStorageCPH.channel, new EnderStorageCPH());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEnderChest.class, new RenderTileEnderChest());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEnderTank.class, new RenderTileEnderTank());
        //ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(Blocks.ender_chest), 0, TileEnderChest.class);
        //ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(Blocks.ender_chest), 1, TileEnderTank.class);
    }
}
