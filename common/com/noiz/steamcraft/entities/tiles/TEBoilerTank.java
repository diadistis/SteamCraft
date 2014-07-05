package com.noiz.steamcraft.entities.tiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;

public class TEBoilerTank extends TileEntity {

	private int blockCount = 1;
	private float waterAmount = 0;
	private float steamAmount = 0;
	private float pressure = 0;

	@Override
	public void readFromNBT(NBTTagCompound par1nbtTagCompound) {
		super.readFromNBT(par1nbtTagCompound);

		blockCount = par1nbtTagCompound.getInteger("BlockCount");
		waterAmount = par1nbtTagCompound.getFloat("Water");
		steamAmount = par1nbtTagCompound.getFloat("Steam");
		pressure = par1nbtTagCompound.getFloat("Pressure");
	}

	@Override
	public void writeToNBT(NBTTagCompound par1nbtTagCompound) {
		super.writeToNBT(par1nbtTagCompound);

		par1nbtTagCompound.setInteger("BlockCount", blockCount);
		par1nbtTagCompound.setFloat("Water", waterAmount);
		par1nbtTagCompound.setFloat("Steam", steamAmount);
		par1nbtTagCompound.setFloat("Pressure", pressure);
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound tagCompound = new NBTTagCompound();
		writeToNBT(tagCompound);
		return new Packet132TileEntityData(xCoord, yCoord, zCoord, 1,
				tagCompound);
	}
}
