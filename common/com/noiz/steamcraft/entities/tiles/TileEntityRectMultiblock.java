package com.noiz.steamcraft.entities.tiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public abstract class TileEntityRectMultiblock extends TileEntity {
	/** X & Z-axis */
	protected final int maxWidth;
	/** Y-axis */
	protected final int maxHeight;

	protected int len[] = { 1, 1, 1 };
	protected int master[] = { 1, 1, 1 };

	public TileEntityRectMultiblock(int maxWidth, int maxHeight) {
		this.maxWidth = maxWidth;
		this.maxHeight = maxHeight;
	}

	public <T extends TileEntityRectMultiblock> void scanFor(World world, int blockId) {
		master[0] = xCoord;
		master[1] = yCoord;
		master[2] = zCoord;

		T east = scanTowards(world, blockId, ForgeDirection.EAST);
		T west = scanTowards(world, blockId, ForgeDirection.WEST);
		boolean xLinked = east != null || west != null;
		boolean xBoth = east != null && west != null;

		T north = scanTowards(world, blockId, ForgeDirection.NORTH);
		T south = scanTowards(world, blockId, ForgeDirection.SOUTH);
		boolean zLinked = north != null || south != null;
		boolean zBoth = north != null && south != null;

		T up = scanTowards(world, blockId, ForgeDirection.UP);
		T down = scanTowards(world, blockId, ForgeDirection.DOWN);

		if (!(xBoth && zBoth) && !(xLinked && zLinked))
			return;

		computeLength(0, east, west);
		computeLength(1, up, down);
		computeLength(2, north, south);
		
		if( len[0] > maxWidth || len[1] > maxWidth || len[2] > maxHeight )
			return;
	}

	@SuppressWarnings("unchecked")
	private <T extends TileEntityRectMultiblock> T scanTowards(World world, int blockId, ForgeDirection dir) {
		int id = world.getBlockId(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ);
		if (id != blockId)
			return null;
		return (T) world.getBlockTileEntity(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ);
	}

	private <T extends TileEntityRectMultiblock> void computeLength(int dimension, T a, T b) {
		len[dimension] = 1;
		if (a != null)
			len[dimension] += a.len[dimension];
		if (b != null)
			len[dimension] += b.len[dimension];
	}

	@Override
	public void readFromNBT(NBTTagCompound par1nbtTagCompound) {
		super.readFromNBT(par1nbtTagCompound);
	}

	@Override
	public void writeToNBT(NBTTagCompound par1nbtTagCompound) {
		super.writeToNBT(par1nbtTagCompound);
	}
}
