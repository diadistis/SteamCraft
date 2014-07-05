package com.noiz.steamcraft.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.noiz.steamcraft.SteamCraft;
import com.noiz.steamcraft.SteamCraftConstants;
import com.noiz.steamcraft.entities.tiles.TileEntityHeater;
import com.noiz.steamcraft.handlers.GuiHandlerServer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockSteelHeater extends BlockContainer implements ITileEntityProvider {

	@SideOnly(Side.CLIENT)
	public static Icon frontOffIcon;

	@SideOnly(Side.CLIENT)
	public static Icon frontOnIcon;
	
	@SideOnly(Side.CLIENT)
	public static Icon sideIcon;
	
	public BlockSteelHeater(Material material) {
		super(1234, material);
		setBlockBounds(1.0F/16.0F, 0.0F, 1.0F/16.0F, 1.0F - (1.0F/16.0F), 1.0F, 1.0F - (1.0F/16.0F));
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityHeater();
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
		return SteamCraft.blockHeaterRenderId;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int side, int metadata) 
	{
		// return side != (metadata & 7) ? sideIcon : frontOffIcon;
		
		if( side != (metadata & 7) )
			return sideIcon;
		if( (metadata & 8) != 0 )
			return frontOnIcon;
		return frontOffIcon;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister icon) 
	{
		frontOffIcon = icon.registerIcon(SteamCraftConstants.ModId.toLowerCase() + ":heater_off");
		frontOnIcon = icon.registerIcon(SteamCraftConstants.ModId.toLowerCase() + ":heater_on");
		sideIcon = icon.registerIcon(SteamCraftConstants.ModId.toLowerCase() + ":heater_sides");
	};

    public void onBlockPlacedBy(World world, int i, int j, int k, EntityLivingBase entityliving, ItemStack par6ItemStack)
    {
        int l = MathHelper.floor_double((double)(entityliving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

        switch (l) {
        case 0:
        	world.setBlockMetadataWithNotify(i, j, k, 2, 2);
			break;
		case 1:
        	world.setBlockMetadataWithNotify(i, j, k, 5, 2);
			break;
		default:
			world.setBlockMetadataWithNotify(i, j, k, l+1, 2);
			break;			
		}
    }
    
	@Override
	public boolean onBlockActivated(World world, int i, int j, int k,
			EntityPlayer entityplayer, int par6, float par7, float par8,
			float par9) {

		if (world.isRemote) //
			return true;

		if ((TileEntityHeater) world.getBlockTileEntity(i, j, k) != null)
			entityplayer.openGui(SteamCraft.instance, GuiHandlerServer.GUI_SteelHeaterID, world, i, j, k);
		return true;
	}
}
