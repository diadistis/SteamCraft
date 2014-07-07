package com.noiz.steamcraft.entities.tiles.multiblock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import TFC.Core.TFC_Time;

public abstract class TileEntityRectMultiblock extends TileEntity {

	public static final int MaxScanDistance = 30;
	public static final long ActivationTimeout = 150;

	private static int last_tool_activattion_blockID = -1;
	private static int[] last_tool_activation_coords = null;
	private static long activation_expires_at = 0;

	private static final Map<Integer, int[]> structureLimits = new HashMap<>();
	private static final Map<String, Structure> structures = new HashMap<>();

	public final Set<ForgeDirection> multiblockInternalDirections = new HashSet<>();
	private int master[] = { 0, 0, 0 };
	String structureID;

	public static void registerStructureLimits(int blockID, int maxX, int maxY, int maxZ) {
		structureLimits.put(blockID, new int[] { Math.min(MaxScanDistance, maxX), Math.min(MaxScanDistance, maxY), Math.min(MaxScanDistance, maxZ) });
	}

	public static void onToolActivationAt(World world, EntityPlayer player, int blockID, int x, int y, int z) {
		if (last_tool_activation_coords == null || TFC_Time.getTotalTicks() > activation_expires_at || blockID != last_tool_activattion_blockID) {
			last_tool_activattion_blockID = blockID;
			last_tool_activation_coords = new int[] { x, y, z };
			activation_expires_at = TFC_Time.getTotalTicks() + ActivationTimeout;
			player.addChatMessage(String.format("Marker set @ (%d,%d,%d)", x, y, z));
			return;
		}

		int[] min = { Math.min(last_tool_activation_coords[0], x), Math.min(last_tool_activation_coords[1], y), Math.min(last_tool_activation_coords[2], z) };
		int[] max = { Math.max(last_tool_activation_coords[0], x), Math.max(last_tool_activation_coords[1], y), Math.max(last_tool_activation_coords[2], z) };
		last_tool_activation_coords = null;

		Structure struct = new Structure(min, max);
		if (!struct.isValid(structureLimits.get(blockID))) {
			player.addChatMessage("Structure dimensions are not valid (too big?)");
			return;
		}

		List<TileEntityRectMultiblock> entities = new ArrayList<>();
		Set<String> colliding = new HashSet<>();
		TileEntityRectMultiblock newMaster = getStructureBlocksAndMaster(world, struct, blockID, entities, colliding);
		if (newMaster == null) {
			player.addChatMessage("Structure is not complete/coherent");
			return;
		}

		String id = UUID.randomUUID().toString().replace("-", "");
		structures.put(id, struct);

		for (String c : colliding)
			structures.remove(c);

		for (TileEntityRectMultiblock entity : entities)
			entity.setStructureMaster(newMaster, id, struct);

		newMaster.structureCreatedWithThisAsMaster();
		player.addChatMessage("Structure built!");
	}

	public void onDestroy(World world) {
		if (structureID == null)
			return;

		TileEntityRectMultiblock masterEntity = null;
		if (isMaster())
			masterEntity = this;
		else
			masterEntity = (TileEntityRectMultiblock) world.getBlockTileEntity(master[0], master[1], master[2]);

		if (masterEntity != null)
			masterEntity.onStructureDismantle();

		Structure struct = structures.remove(structureID);
		for (int i = struct.minCoords[0]; i <= struct.maxCoords[0]; ++i)
			for (int j = struct.minCoords[1]; j <= struct.maxCoords[1]; ++j)
				for (int k = struct.minCoords[2]; k <= struct.maxCoords[2]; ++k) {
					TileEntityRectMultiblock entity = (TileEntityRectMultiblock) world.getBlockTileEntity(i, j, k);
					entity.setStructureMaster(entity, null, null);
				}

		System.out.println("Structure destroyed");
	}

	private void setStructureMaster(TileEntityRectMultiblock master, String id, Structure structure) {
		if (isMaster() && master != this)
			mergeThisMasterToNextOne(master);

		structureID = id;
		this.master[0] = master.xCoord;
		this.master[1] = master.yCoord;
		this.master[2] = master.zCoord;

		multiblockInternalDirections.clear();
		if (structure != null)
			structure.fillTouchedSides(this);

		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}

	protected boolean isMaster() {
		return structureID == null || (master[0] == xCoord && master[1] == yCoord && master[2] == zCoord);
	}

	protected abstract void mergeThisMasterToNextOne(TileEntityRectMultiblock nextMaster);

	protected abstract void onStructureDismantle();

	protected abstract void structureCreatedWithThisAsMaster();

	@SuppressWarnings("unchecked")
	public <T extends TileEntityRectMultiblock> T master() {
		if (isMaster())
			return (T) this;
		return (T) worldObj.getBlockTileEntity(master[0], master[1], master[2]);
	}

	@Override
	public void readFromNBT(NBTTagCompound par1nbtTagCompound) {
		super.readFromNBT(par1nbtTagCompound);

		master = par1nbtTagCompound.getIntArray("Master");
		structureID = par1nbtTagCompound.getString("SID");
		if (structureID != null && structureID.length() == 0)
			structureID = null;

		multiblockInternalDirections.clear();
		if (structureID != null) {
			fromTouchedSidesMask(par1nbtTagCompound.getInteger("TouchedMask"));
			if (isMaster()) {
				int[] min = par1nbtTagCompound.getIntArray("S_Min");
				int[] max = par1nbtTagCompound.getIntArray("S_Max");
				Structure struct = new Structure(min, max);
				structures.put(structureID, struct);
			}
		}

		if (worldObj != null)
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}

	@Override
	public void writeToNBT(NBTTagCompound par1nbtTagCompound) {
		super.writeToNBT(par1nbtTagCompound);

		par1nbtTagCompound.setIntArray("Master", master);
		par1nbtTagCompound.setString("SID", structureID == null ? "" : structureID);

		if (structureID != null) {
			par1nbtTagCompound.setInteger("TouchedMask", getTouchedSidesMask());
			if (isMaster()) {
				Structure struct = structures.get(structureID);
				par1nbtTagCompound.setIntArray("S_Min", struct.minCoords);
				par1nbtTagCompound.setIntArray("S_Max", struct.maxCoords);
			}
		}
	}

	public int masterX() {
		return structureID == null ? xCoord : master[0];
	}

	public int masterY() {
		return structureID == null ? yCoord : master[1];
	}

	public int masterZ() {
		return structureID == null ? zCoord : master[2];
	}

	protected int structureBlockCount() {
		if (structureID == null)
			return 1;
		Structure struct = structures.get(structureID);
		if (struct == null)
			return 1;
		return struct.blockCount();
	}

	private static TileEntityRectMultiblock getStructureBlocksAndMaster(World world, Structure struct, int blockID, List<TileEntityRectMultiblock> entities, Set<String> structures) {
		int[] master_coords = struct.masterCoords();
		TileEntityRectMultiblock master = null;

		for (int i = struct.minCoords[0]; i <= struct.maxCoords[0]; ++i)
			for (int j = struct.minCoords[1]; j <= struct.maxCoords[1]; ++j)
				for (int k = struct.minCoords[2]; k <= struct.maxCoords[2]; ++k) {
					int id = world.getBlockId(i, j, k);
					if (id != blockID) {
						return null;
					}
					TileEntityRectMultiblock entity = (TileEntityRectMultiblock) world.getBlockTileEntity(i, j, k);
					entities.add(entity);

					if (entity.structureID != null)
						structures.add(entity.structureID);

					if (master_coords[0] == i && master_coords[1] == j && master_coords[2] == k)
						master = entity;
				}
		return master;
	}

	protected List<TileEntityRectMultiblock> structureMembers(int blockID) {
		List<TileEntityRectMultiblock> members = new ArrayList<>();
		if (structureID == null)
			return members;

		Structure struct = structures.get(structureID);
		if (struct == null)
			return members;

		for (int i = struct.minCoords[0]; i <= struct.maxCoords[0]; ++i)
			for (int j = struct.minCoords[1]; j <= struct.maxCoords[1]; ++j)
				for (int k = struct.minCoords[2]; k <= struct.maxCoords[2]; ++k) {
					int bid = worldObj.getBlockId(i, j, k);
					if (bid != blockID)
						continue;
					TileEntityRectMultiblock entity = (TileEntityRectMultiblock) worldObj.getBlockTileEntity(i, j, k);
					members.add(entity);
				}

		return members;
	}

	private int getTouchedSidesMask() {
		int mask = 0;
		for (ForgeDirection d : multiblockInternalDirections)
			mask |= (1 << d.ordinal());
		return mask;
	}

	private void fromTouchedSidesMask(int mask) {
		multiblockInternalDirections.clear();
		for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS)
			if ((mask & (1 << d.ordinal())) > 0)
				multiblockInternalDirections.add(d);
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound tagCompound = new NBTTagCompound();
		writeToNBT(tagCompound);
		return new Packet132TileEntityData(xCoord, yCoord, zCoord, 1, tagCompound);
	}

	@Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt) {
		readFromNBT(pkt.data);
	}
}
