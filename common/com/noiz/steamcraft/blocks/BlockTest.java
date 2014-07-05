package com.noiz.steamcraft.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

import com.noiz.steamcraft.SteamCraft;
import com.noiz.steamcraft.SteamCraftConstants;
import com.noiz.steamcraft.entities.tiles.TEBoiler;
import com.noiz.steamcraft.gui.GuiHandlerServer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockTest extends BlockContainer implements ITileEntityProvider {

	@SideOnly(Side.CLIENT)
	public static Icon frontIcon;
	
	@SideOnly(Side.CLIENT)
	public static Icon sideIcon;
	
	public BlockTest(Material material) {
		super(1234, material);
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TEBoiler();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int side, int metadata) 
	{
		if (side==2) {
			return frontIcon;
		} else {
			return sideIcon;
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister icon) 
	{
		frontIcon = icon.registerIcon(SteamCraftConstants.ModId.toLowerCase() + ":boiler_off");
		sideIcon = icon.registerIcon(SteamCraftConstants.ModId.toLowerCase() + ":boiler_sides");
	};

	@Override
	public boolean onBlockActivated(World world, int i, int j, int k,
			EntityPlayer entityplayer, int par6, float par7, float par8,
			float par9) {

		if (world.isRemote) //
			return true;

		if ((TEBoiler) world.getBlockTileEntity(i, j, k) != null) {
			// TEBoiler boiler = (TEBoiler) world.getBlockTileEntity(i, j, k);
			// ItemStack is = entityplayer.getCurrentEquippedItem();

			entityplayer.openGui(SteamCraft.instance, GuiHandlerServer.GUI_BoilerID, world, i, j, k);
		}
		return true;
	}
}
