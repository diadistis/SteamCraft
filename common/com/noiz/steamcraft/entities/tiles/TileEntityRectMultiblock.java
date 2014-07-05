package com.noiz.steamcraft.entities.tiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public abstract class TileEntityRectMultiblock extends TileEntity {

	/** X-axis */
	protected final int maxWidth;
	/** Y-axis */
	protected final int maxHeight;
	/** Z-axis */
	protected final int maxLength;

	public TileEntityRectMultiblock(int maxWidth, int maxHeight, int maxLength) {
		this.maxWidth = maxWidth;
		this.maxHeight = maxHeight;
		this.maxLength = maxLength;
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
