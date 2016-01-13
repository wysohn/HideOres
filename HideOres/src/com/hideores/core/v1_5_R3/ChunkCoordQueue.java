package com.hideores.core.v1_5_R3;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import org.bukkit.craftbukkit.v1_5_R3.entity.CraftPlayer;

import com.google.common.collect.Lists;
import com.hideores.core.Iinstance.IChunkCoordQueue;

import net.minecraft.server.v1_5_R3.Chunk;
import net.minecraft.server.v1_5_R3.ChunkCoordIntPair;
import net.minecraft.server.v1_5_R3.Packet;
import net.minecraft.server.v1_5_R3.Packet51MapChunk;
import net.minecraft.server.v1_5_R3.Packet56MapChunkBulk;
import net.minecraft.server.v1_5_R3.TileEntity;
import net.minecraft.server.v1_5_R3.WorldServer;

public class ChunkCoordQueue extends LinkedList<ChunkCoordIntPair> implements IChunkCoordQueue{
	private static final long serialVersionUID = -7564871408908791693L;

	CraftPlayer player;
	
	public ChunkCoordQueue(CraftPlayer player){
		this.player = player;
		
	}
	
    @Override
    public boolean isEmpty() {
            ArrayList arraylist = Lists.newArrayList();
            Iterator iterator1 = super.iterator();
            ArrayList arraylist1 = Lists.newArrayList();

            Chunk chunk;
            
            //Bukkit.getLogger().log(Level.INFO,"iterator has next : "+ iterator1.hasNext());

            while (iterator1.hasNext() && arraylist.size() < 10) {
                ChunkCoordIntPair chunkcoordintpair = (ChunkCoordIntPair) iterator1.next();

                if (chunkcoordintpair != null) {
                    if (player.getHandle().world.isLoaded(chunkcoordintpair.x << 4, 0, chunkcoordintpair.z << 4)) {
                        chunk = player.getHandle().world.getChunkAt(chunkcoordintpair.x, chunkcoordintpair.z);
                        if (chunk.done) {
                            arraylist.add(chunk);
                            arraylist1.addAll(((WorldServer) player.getHandle().world).getTileEntities(chunkcoordintpair.x * 16, 0, chunkcoordintpair.z * 16, chunkcoordintpair.x * 16 + 16, 256, chunkcoordintpair.z * 16 + 16));
                            iterator1.remove();
                        }
                    }
                } else {
                    iterator1.remove();
                }
            }

            if (!arraylist.isEmpty()) {
                if (arraylist.size() == 1) {
                	Packet51MapChunk pomcPacket = new Packet51MapChunk((Chunk) arraylist.get(0), true, '\uffff');
                	
                	//modify packet before send
                	MapChunkCalculation.calcAndChange(player.getWorld(), pomcPacket);
                	
                	player.getHandle().playerConnection.sendPacket(pomcPacket);
                } else {
                	Packet56MapChunkBulk pomcbPacket = new Packet56MapChunkBulk(arraylist);
                	
                	//modify packet before send
                	MapChunkCalculation.calcAndChange(player.getWorld(), pomcbPacket);
                	
                	player.getHandle().playerConnection.sendPacket(pomcbPacket);
                }

                Iterator iterator2 = arraylist1.iterator();

                while (iterator2.hasNext()) {
                    TileEntity tileentity = (TileEntity) iterator2.next();

                    if (tileentity != null) {
                        Packet packet = tileentity.getUpdatePacket();

                        if (packet != null) {
                            player.getHandle().playerConnection.sendPacket(packet);
                        }
                    }
                }

                iterator2 = arraylist.iterator();

                while (iterator2.hasNext()) {
                    chunk = (Chunk) iterator2.next();
                    player.getHandle().o().getTracker().a(player.getHandle(), chunk);
                }
            }
        return true;
    }
	
    @Override
    public Iterator<ChunkCoordIntPair> iterator() {
        return new FakeIterator();
    }
    private class FakeIterator implements ListIterator<ChunkCoordIntPair> {

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public ChunkCoordIntPair next() {
            return null;
        }

        @Override
        public boolean hasPrevious() {
            return false;
        }

        @Override
        public ChunkCoordIntPair previous() {
            return null;
        }

        @Override
        public int nextIndex() {
            return 0;
        }

        @Override
        public int previousIndex() {
            return 0;
        }

        @Override
        public void remove() {}

        @Override
        public void set(ChunkCoordIntPair e) {}

        @Override
        public void add(ChunkCoordIntPair e) {}
    }
}
