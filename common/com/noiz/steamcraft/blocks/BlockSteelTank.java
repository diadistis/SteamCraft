package com.noiz.steamcraft.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import TFC.TFCItems;

import com.noiz.steamcraft.SteamCraft;
import com.noiz.steamcraft.SteamCraftBlocks;
import com.noiz.steamcraft.SteamCraftConstants;
import com.noiz.steamcraft.entities.tiles.TileEntityTank;
import com.noiz.steamcraft.handlers.GuiHandlerServer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockSteelTank extends Block implements ITileEntityProvider {

	@SideOnly(Side.CLIENT)
	public static Icon icon;

	public BlockSteelTank(Material material) {
		super(1235, material);
		setBlockBounds(1.0F / 16.0F, 0.0F, 1.0F / 16.0F, 1.0F - (1.0F / 16.0F), 1.0F, 1.0F - (1.0F / 16.0F));
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityTank();
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public int getRenderType() {
		return SteamCraftBlocks.blockTankRenderId;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister icon) {
		BlockSteelTank.icon = icon.registerIcon(SteamCraftConstants.ModId.toLowerCase() + ":heater_sides");
	};

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int side, int metadata) {
		return icon;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {

		if (world.isRemote)
			return true;

		TileEntityTank tank = (TileEntityTank) world.getBlockTileEntity(x, y, z);
		if (tank == null)
			return true;

		if (!tank.isFull()) {
			ItemStack equipped = player.getCurrentEquippedItem();
			if (equipped != null && equipped.getItem().itemID == TFCItems.WoodenBucketWater.itemID) {
				tank.addBucket();
				if (equipped.stackSize == 1)
					equipped.itemID = TFCItems.WoodenBucketEmpty.itemID;
				else {
					equipped.stackSize--;
					ItemStack stack = new ItemStack(TFCItems.WoodenBucketEmpty);
					player.setItemInUse(stack, TFCItems.WoodenBucketEmpty.getMaxItemUseDuration(stack));
				}
				return true;
			}
		}

		player.openGui(SteamCraft.instance, GuiHandlerServer.GUI_TankID, world, x, y, z);
		return true;
	}
}
