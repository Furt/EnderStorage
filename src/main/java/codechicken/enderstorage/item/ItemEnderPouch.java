package codechicken.enderstorage.item;

import codechicken.enderstorage.api.Frequency;
import codechicken.enderstorage.handler.ConfigurationHandler;
import codechicken.enderstorage.manager.EnderStorageManager;
import codechicken.enderstorage.storage.EnderItemStorage;
import codechicken.enderstorage.tile.TileEnderChest;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class ItemEnderPouch extends Item {

    public ItemEnderPouch() {
        setMaxStackSize(1);
        setCreativeTab(CreativeTabs.TRANSPORTATION);
        setUnlocalizedName("enderpouch");
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean extended) {
        Frequency freq = Frequency.fromItemStack(stack);
        if (freq.owner != null) {
            list.add(freq.owner);
        }
        list.add(String.format("%s/%s/%s", freq.getLocalizedLeft(), freq.getLocalizedMiddle(), freq.getLocalizedRight()));

        //if (stack.hasTagCompound() && !stack.getTagCompound().getString("owner").equals("global")) {
        //    list.add(stack.getTagCompound().getString("owner"));
        //}
    }

    @Override
    public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        if (world.isRemote) {
            return EnumActionResult.PASS;
        }

        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileEnderChest && player.isSneaking()) {
            TileEnderChest chest = (TileEnderChest) tile;
            if (!stack.hasTagCompound()) {
                stack.setTagCompound(new NBTTagCompound());
            }
            NBTTagCompound frequencyTag = new NBTTagCompound();
            Frequency frequency = chest.frequency.copy();
            if (ConfigurationHandler.anarchyMode && !frequency.owner.equals(player.getDisplayNameString())) {
                frequency.setOwner(null);
            }

            frequency.writeNBT(frequencyTag);
            stack.getTagCompound().setTag("Frequency", frequencyTag);
            //if (!ConfigurationHandler.anarchyMode || chest.owner.equals(player.getDisplayNameString())) {
            //    stack.getTagCompound().setString("owner", chest.owner);
            //} else {
            //    stack.getTagCompound().setString("owner", "global");
            //}

            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.PASS;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {

        if (world.isRemote || player.isSneaking()) {
            return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
        }
        ((EnderItemStorage) EnderStorageManager.instance(world.isRemote).getStorage(Frequency.fromItemStack(stack), "item")).openSMPGui(player, stack.getUnlocalizedName() + ".name");
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
    }

    @Deprecated
    public String getOwner(ItemStack stack) {
        return EnderStorageManager.getOwner(stack);
    }

    /*@Override
    public int getRenderPasses(int metadata){
        return 4;
    }

    @Override
    public IIcon getIcon(ItemStack stack, int renderPass){
        return spriteSheet.getSprite(getIconIndex(stack, renderPass));
    }

    public int getIconIndex(ItemStack stack, int renderPass) {
        if (renderPass == 0) {
            int i = 0;
            if (((EnderItemStorage) EnderStorageManager.instance(true).getStorage(getOwner(stack), Frequency.fromItemStack(stack), "item")).openCount() > 0) {
                i |= 1;
            }
            if (!getOwner(stack).equals("global")) {
                i |= 2;
            }
            return i;
        }

        return renderPass * 16 + EnderStorageManager.getColourFromFreq(stack.getItemDamage() & 0xFFF, renderPass - 1);
    }

    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @Override
    public void registerIcons(IIconRegister register){
        spriteSheet = SpriteSheetManager.getSheet(new ResourceLocation("enderstorage", "textures/enderpouch.png"));
        spriteSheet.requestIndicies(0, 1, 2, 3);
        for(int i = 16; i < 64; i++)
            spriteSheet.requestIndicies(i);
        spriteSheet.registerIcons(register);
    }*/
}