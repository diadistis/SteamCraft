package com.noiz.steamcraft.entities.tiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

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

	@Override
	public void readFromNBT(NBTTagCompound par1nbtTagCompound) {
		super.readFromNBT(par1nbtTagCompound);

		len = par1nbtTagCompound.getIntArray("Length");
		master = par1nbtTagCompound.getIntArray("Master");
	}

	@Override
	public void writeToNBT(NBTTagCompound par1nbtTagCompound) {
		super.writeToNBT(par1nbtTagCompound);

		par1nbtTagCompound.setIntArray("Length", len);
		par1nbtTagCompound.setIntArray("Master", master);
	}
}
