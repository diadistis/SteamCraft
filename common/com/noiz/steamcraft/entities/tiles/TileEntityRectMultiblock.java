package com.noiz.steamcraft.entities.tiles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public abstract class TileEntityRectMultiblock extends TileEntity {
	private static final Comparator<TileEntityRectMultiblock> CoordComparator = new Comparator<TileEntityRectMultiblock>() {
		@Override
		public int compare(TileEntityRectMultiblock o1, TileEntityRectMultiblock o2) {
			if (o1.xCoord != o2.xCoord)
				return o1.xCoord - o2.xCoord;
			if (o1.yCoord != o2.yCoord)
				return o1.yCoord - o2.yCoord;
			if (o1.zCoord != o2.zCoord)
				return o1.zCoord - o2.zCoord;
			return 0;
		}
	};

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
		boolean standalone = true;
		try {
			T east = scanTowards(world, blockId, ForgeDirection.EAST);
			T west = scanTowards(world, blockId, ForgeDirection.WEST);
			T north = scanTowards(world, blockId, ForgeDirection.NORTH);
			T south = scanTowards(world, blockId, ForgeDirection.SOUTH);
			T up = scanTowards(world, blockId, ForgeDirection.UP);
			T down = scanTowards(world, blockId, ForgeDirection.DOWN);

			computeLength(0, east, west);
			computeLength(1, up, down);
			computeLength(2, north, south);

			System.out.println("scan-2");
			if (len[0] > maxWidth || len[1] > maxWidth || len[2] > maxHeight)
				return;

			System.out.println("scan-3");
			List<TileEntityRectMultiblock> members = new ArrayList<>(len[0] * len[1] * len[2]);
			addNonNulls(members, this, east, west, north, south, up, down);
			if (!gatherAndSortStructureMembers(world, blockId, east, west, north, south, up, down, members) || members.size() == 0)
				return;

			System.out.println("scan-4");
			TileEntityRectMultiblock masterNode = members.get(0);
			for (TileEntityRectMultiblock member : members)
				member.setSlaveOf(masterNode, len);

			standalone = false;
		} finally {
			if (standalone)
				setStandalone();
		}
	}

	private <T extends TileEntityRectMultiblock> boolean gatherAndSortStructureMembers(World world, int blockId, T east, T west, T north, T south, T up, T down, List<T> members) {
		boolean foundAll = true;

		final int x_begin = xCoord - (east == null ? 0 : east.len[0]);
		final int x_end = xCoord + (west == null ? 0 : west.len[0]);
		final int y_begin = yCoord - (down == null ? 0 : down.len[0]);
		final int y_end = yCoord + (up == null ? 0 : up.len[0]);
		final int z_begin = zCoord - (south == null ? 0 : south.len[0]);
		final int z_end = zCoord + (north == null ? 0 : north.len[0]);

		System.out.println("  Box: [" + x_begin + " -> " + x_end + "]x[" + y_begin + " -> " + y_end + "]x[" + z_begin + " -> " + z_end + "]");

		for (int x = x_begin; foundAll && x <= x_end; ++x)
			for (int y = y_begin; foundAll && y <= y_end; ++y)
				for (int z = z_begin; foundAll && z <= z_end; ++z)
					if (shouldAddEntityAtCoords(x, y, z)) {
						T block = getAt(world, blockId, x, y, z);
						if (block == null) {
							System.out.println(">> missing block @ " + x + "," + y + "," + z);
							foundAll = false;
							break;
						}
						members.add(block);
					}
		if (!foundAll)
			return false;

		Collections.sort(members, CoordComparator);
		return true;
	}

	private boolean shouldAddEntityAtCoords(int x, int y, int z) {
		int zeros = 0;
		int xDelta = Math.abs(x - xCoord);
		if (xDelta > 1)
			return true;
		if (xDelta == 0)
			zeros++;

		int yDelta = Math.abs(y - yCoord);
		if (yDelta > 1)
			return true;
		if (yDelta == 0)
			zeros++;

		int zDelta = Math.abs(z - zCoord);
		if (zDelta > 1)
			return true;
		if (zDelta == 0)
			zeros++;

		return zeros < 2;
	}

	private <T extends TileEntityRectMultiblock> T scanTowards(World world, int blockId, ForgeDirection dir) {
		return getAt(world, blockId, xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ);
	}

	@SuppressWarnings("unchecked")
	private <T extends TileEntityRectMultiblock> T getAt(World world, int blockId, int x, int y, int z) {
		int id = world.getBlockId(x, y, z);
		if (id != blockId)
			return null;
		return (T) world.getBlockTileEntity(x, y, z);
	}

	private <T extends TileEntityRectMultiblock> void computeLength(int dimension, T a, T b) {
		len[dimension] = 1;
		if (a != null)
			len[dimension] += a.len[dimension];
		if (b != null)
			len[dimension] += b.len[dimension];
	}

	private void setStandalone() {
		len[0] = len[1] = len[2] = 1;
		master[0] = xCoord;
		master[1] = yCoord;
		master[2] = zCoord;
		structureChanged();
		onInventoryChanged();
	}

	private void setSlaveOf(TileEntityRectMultiblock master, int[] length) {
		this.master[0] = master.xCoord;
		this.master[1] = master.yCoord;
		this.master[2] = master.zCoord;
		len[0] = length[0];
		len[1] = length[1];
		len[2] = length[2];
		structureChanged();
		onInventoryChanged();
	}

	@SafeVarargs
	private final <T> void addNonNulls(List<T> list, T... r) {
		for (T l : r)
			if (l != null)
				list.add(l);
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

	protected abstract void structureChanged();
}
