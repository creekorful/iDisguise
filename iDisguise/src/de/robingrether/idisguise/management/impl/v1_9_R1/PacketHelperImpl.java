package de.robingrether.idisguise.management.impl.v1_9_R1;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_9_R1.CraftOfflinePlayer;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_9_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_9_R1.util.CraftChatMessage;
import org.bukkit.entity.Player;

import com.mojang.authlib.GameProfile;

import de.robingrether.idisguise.disguise.AgeableDisguise;
import de.robingrether.idisguise.disguise.CreeperDisguise;
import de.robingrether.idisguise.disguise.Disguise;
import de.robingrether.idisguise.disguise.DisguiseType;
import de.robingrether.idisguise.disguise.EndermanDisguise;
import de.robingrether.idisguise.disguise.FallingBlockDisguise;
import de.robingrether.idisguise.disguise.GuardianDisguise;
import de.robingrether.idisguise.disguise.HorseDisguise;
import de.robingrether.idisguise.disguise.ItemDisguise;
import de.robingrether.idisguise.disguise.MinecartDisguise;
import de.robingrether.idisguise.disguise.MobDisguise;
import de.robingrether.idisguise.disguise.ObjectDisguise;
import de.robingrether.idisguise.disguise.OcelotDisguise;
import de.robingrether.idisguise.disguise.PigDisguise;
import de.robingrether.idisguise.disguise.PlayerDisguise;
import de.robingrether.idisguise.disguise.RabbitDisguise;
import de.robingrether.idisguise.disguise.SheepDisguise;
import de.robingrether.idisguise.disguise.SizedDisguise;
import de.robingrether.idisguise.disguise.SkeletonDisguise;
import de.robingrether.idisguise.disguise.VillagerDisguise;
import de.robingrether.idisguise.disguise.WolfDisguise;
import de.robingrether.idisguise.disguise.ZombieDisguise;
import de.robingrether.idisguise.management.DisguiseManager;
import de.robingrether.idisguise.management.PacketHelper;
import de.robingrether.idisguise.management.PlayerHelper;
import de.robingrether.idisguise.management.VersionHelper;
import net.minecraft.server.v1_9_R1.Block;
import net.minecraft.server.v1_9_R1.Entity;
import net.minecraft.server.v1_9_R1.EntityAgeable;
import net.minecraft.server.v1_9_R1.EntityBat;
import net.minecraft.server.v1_9_R1.EntityCreeper;
import net.minecraft.server.v1_9_R1.EntityEnderman;
import net.minecraft.server.v1_9_R1.EntityFallingBlock;
import net.minecraft.server.v1_9_R1.EntityGuardian;
import net.minecraft.server.v1_9_R1.EntityHorse;
import net.minecraft.server.v1_9_R1.EntityInsentient;
import net.minecraft.server.v1_9_R1.EntityItem;
import net.minecraft.server.v1_9_R1.EntityMinecartRideable;
import net.minecraft.server.v1_9_R1.EntityOcelot;
import net.minecraft.server.v1_9_R1.EntityPig;
import net.minecraft.server.v1_9_R1.EntityPlayer;
import net.minecraft.server.v1_9_R1.EntityRabbit;
import net.minecraft.server.v1_9_R1.EntitySheep;
import net.minecraft.server.v1_9_R1.EntitySkeleton;
import net.minecraft.server.v1_9_R1.EntitySlime;
import net.minecraft.server.v1_9_R1.EntityVillager;
import net.minecraft.server.v1_9_R1.EntityWolf;
import net.minecraft.server.v1_9_R1.EntityZombie;
import net.minecraft.server.v1_9_R1.EnumColor;
import net.minecraft.server.v1_9_R1.EnumHorseType;
import net.minecraft.server.v1_9_R1.Item;
import net.minecraft.server.v1_9_R1.ItemStack;
import net.minecraft.server.v1_9_R1.Packet;
import net.minecraft.server.v1_9_R1.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_9_R1.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_9_R1.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_9_R1.PacketPlayOutPlayerInfo.PlayerInfoData;
import net.minecraft.server.v1_9_R1.PacketPlayOutSpawnEntity;
import net.minecraft.server.v1_9_R1.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_9_R1.World;
import net.minecraft.server.v1_9_R1.WorldSettings.EnumGamemode;

public class PacketHelperImpl extends PacketHelper {
	
	private Field fieldUUID;
	
	public PacketHelperImpl() {
		try {
			fieldUUID = PacketPlayOutNamedEntitySpawn.class.getDeclaredField("b");
			fieldUUID.setAccessible(true);
		} catch(Exception e) {
		}
	}
	
	public Packet<?>[] getPackets(Player player) {
		Disguise disguise = DisguiseManager.instance.getDisguise(player);
		if(disguise == null) {
			return null;
		}
		EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
		DisguiseType type = disguise.getType();
		List<Packet<?>> packets = new ArrayList<Packet<?>>();
		if(disguise instanceof MobDisguise) {
			MobDisguise mobDisguise = (MobDisguise)disguise;
			EntityInsentient entity;
			try {
				entity = (EntityInsentient)type.getClass(VersionHelper.getNMSPackage()).getConstructor(World.class).newInstance(entityPlayer.getWorld());
			} catch(Exception e) {
				entity = null;
			}
			if(mobDisguise.getCustomName() != null && !mobDisguise.getCustomName().isEmpty()) {
				entity.setCustomName(mobDisguise.getCustomName());
				entity.setCustomNameVisible(true);
			}
			if(entity instanceof EntityAgeable) {
				if(mobDisguise instanceof AgeableDisguise && !((AgeableDisguise)disguise).isAdult()) {
					((EntityAgeable)entity).setAge(-24000);
				}
			}
			Location location = player.getLocation();
			entity.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
			entity.f(entityPlayer.getId());
			if(mobDisguise instanceof SheepDisguise) {
				if(entity instanceof EntitySheep) {
					((EntitySheep)entity).setColor(EnumColor.fromColorIndex(((SheepDisguise)mobDisguise).getColor().getData()));
				}
			} else if(mobDisguise instanceof WolfDisguise) {
				if(entity instanceof EntityWolf) {
					WolfDisguise wolfDisguise = (WolfDisguise)mobDisguise;
					EntityWolf wolf = (EntityWolf)entity;
					wolf.setCollarColor(EnumColor.fromColorIndex(wolfDisguise.getCollarColor().getData()));
					wolf.setTamed(wolfDisguise.isTamed());
					wolf.setAngry(wolfDisguise.isAngry());
				}
			} else if(mobDisguise instanceof CreeperDisguise) {
				if(entity instanceof EntityCreeper) {
					((EntityCreeper)entity).setPowered(((CreeperDisguise)mobDisguise).isPowered());
				}
			} else if(mobDisguise instanceof EndermanDisguise) {
				if(entity instanceof EntityEnderman) {
					EndermanDisguise endermanDisguise = (EndermanDisguise)mobDisguise;
					((EntityEnderman)entity).setCarried(Block.getById(endermanDisguise.getBlockInHand().getId()).fromLegacyData(endermanDisguise.getBlockInHandData()));
				}
			} else if(mobDisguise instanceof GuardianDisguise) {
				if(entity instanceof EntityGuardian) {
					((EntityGuardian)entity).setElder(((GuardianDisguise)mobDisguise).isElder());
				}
			} else if(mobDisguise instanceof HorseDisguise) {
				if(entity instanceof EntityHorse) {
					HorseDisguise horseDisguise = (HorseDisguise)mobDisguise;
					EntityHorse horse = (EntityHorse)entity;
					horse.setType(EnumHorseType.a(horseDisguise.getVariant().ordinal()));
					horse.setVariant(horseDisguise.getColor().ordinal() & 0xFF | horseDisguise.getStyle().ordinal() << 8);
					horse.inventoryChest.setItem(0, horseDisguise.isSaddled() ? new ItemStack(Item.getById(329), 1, 0) : null);
					horse.inventoryChest.setItem(1, CraftItemStack.asNMSCopy(horseDisguise.getArmor().getItem()));
					horse.setHasChest(horseDisguise.hasChest());
				}
			} else if(mobDisguise instanceof OcelotDisguise) {
				if(entity instanceof EntityOcelot) {
					((EntityOcelot)entity).setCatType(((OcelotDisguise)mobDisguise).getCatType().getId());
				}
			} else if(mobDisguise instanceof PigDisguise) {
				if(entity instanceof EntityPig) {
					((EntityPig)entity).setSaddle(((PigDisguise)mobDisguise).isSaddled());
				}
			} else if(mobDisguise instanceof RabbitDisguise) {
				if(entity instanceof EntityRabbit) {
					((EntityRabbit)entity).setRabbitType(((RabbitDisguise)mobDisguise).getRabbitType().getId());
				}
			} else if(mobDisguise instanceof SizedDisguise) {
				if(entity instanceof EntitySlime) {
					((EntitySlime)entity).setSize(((SizedDisguise)mobDisguise).getSize());
				}
			} else if(mobDisguise instanceof SkeletonDisguise) {
				if(entity instanceof EntitySkeleton) {
					((EntitySkeleton)entity).setSkeletonType(((SkeletonDisguise)mobDisguise).getSkeletonType().getId());
				}
			} else if(mobDisguise instanceof VillagerDisguise) {
				if(entity instanceof EntityVillager) {
					((EntityVillager)entity).setProfession(((VillagerDisguise)mobDisguise).getProfession().getId());
				}
			} else if(mobDisguise instanceof ZombieDisguise) {
				if(entity instanceof EntityZombie) {
					ZombieDisguise zombieDisguise = (ZombieDisguise)mobDisguise;
					EntityZombie zombie = (EntityZombie)entity;
					zombie.setBaby(!zombieDisguise.isAdult());
					zombie.setVillagerType(zombieDisguise.isVillager() ? 0 : -1);
				}
			}
			if(entity instanceof EntityBat) {
				((EntityBat)entity).setAsleep(false);
			}
			if(attributes[0]) {
				entity.setCustomName(player.getName());
				entity.setCustomNameVisible(true);
			}
			packets.add(new PacketPlayOutSpawnEntityLiving(entity));
		} else if(disguise instanceof PlayerDisguise) {
			packets.add(new PacketPlayOutNamedEntitySpawn(((CraftPlayer)player).getHandle()));
			try {
				fieldUUID.set(packets.get(0), PlayerHelper.instance.getUniqueId(((PlayerDisguise)disguise).getName()));
			} catch(Exception e) {
			}
		} else if(disguise instanceof ObjectDisguise) {
			ObjectDisguise objectDisguise = (ObjectDisguise)disguise;
			Entity entity;
			try {
				entity = (Entity)type.getClass(VersionHelper.getNMSPackage()).getConstructor(World.class).newInstance(entityPlayer.getWorld());
			} catch(Exception e) {
				entity = null;
			}
			Location location = player.getLocation();
			entity.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
			entity.f(entityPlayer.getId());
			if(entity instanceof EntityFallingBlock) {
				packets.add(new PacketPlayOutSpawnEntity(entity, objectDisguise.getTypeId(), objectDisguise instanceof FallingBlockDisguise ? ((FallingBlockDisguise)objectDisguise).getMaterial().getId() : 1));
			} else if(entity instanceof EntityItem) {
				if(objectDisguise instanceof ItemDisguise) {
					ItemDisguise itemDisguise = (ItemDisguise)objectDisguise;
					if(itemDisguise.getItemStack().getType().isBlock()) {
						((EntityItem)entity).setItemStack(new ItemStack(Block.getById(itemDisguise.getItemStack().getTypeId()), itemDisguise.getItemStack().getAmount(), itemDisguise.getItemStack().getDurability()));
					} else {
						((EntityItem)entity).setItemStack(new ItemStack(Item.getById(itemDisguise.getItemStack().getTypeId()), itemDisguise.getItemStack().getAmount(), itemDisguise.getItemStack().getDurability()));
					}
				}
				packets.add(new PacketPlayOutSpawnEntity(entity, objectDisguise.getTypeId()));
				packets.add(new PacketPlayOutEntityMetadata(entity.getId(), entity.getDataWatcher(), true));
			} else if(entity instanceof EntityMinecartRideable) {
				if(objectDisguise instanceof MinecartDisguise) {
					MinecartDisguise minecartDisguise = (MinecartDisguise)objectDisguise;
					((EntityMinecartRideable)entity).setDisplayBlock(Block.getById(minecartDisguise.getDisplayedBlock().getId()).fromLegacyData(minecartDisguise.getDisplayedBlockData()));
				}
				packets.add(new PacketPlayOutSpawnEntity(entity, objectDisguise.getTypeId()));
				packets.add(new PacketPlayOutEntityMetadata(entity.getId(), entity.getDataWatcher(), true));
			} else {
				packets.add(new PacketPlayOutSpawnEntity(entity, objectDisguise.getTypeId()));
			}
		}
		return packets.toArray(new Packet<?>[0]);
	}
	
	public PlayerInfoData getPlayerInfo(OfflinePlayer offlinePlayer, Object context, int ping, Object gamemode) {
		Disguise disguise = DisguiseManager.instance.getDisguise(offlinePlayer);
		if(disguise == null) {
			return ((PacketPlayOutPlayerInfo)context).new PlayerInfoData(offlinePlayer.isOnline() ? ((CraftPlayer)offlinePlayer).getProfile() : ((CraftOfflinePlayer)offlinePlayer).getProfile(), ping, (EnumGamemode)gamemode, CraftChatMessage.fromString(offlinePlayer.isOnline() ? offlinePlayer.getPlayer().getPlayerListName() : offlinePlayer.getName())[0]);
		} else if(disguise instanceof PlayerDisguise) {
			return ((PacketPlayOutPlayerInfo)context).new PlayerInfoData((GameProfile)PlayerHelper.instance.getGameProfile(((PlayerDisguise)disguise).getName()), ping, (EnumGamemode)gamemode, CraftChatMessage.fromString(attributes[1] ? ((PlayerDisguise)disguise).getName() : offlinePlayer.isOnline() ? offlinePlayer.getPlayer().getPlayerListName() : offlinePlayer.getName())[0]);
		} else if(!attributes[1]) {
			return ((PacketPlayOutPlayerInfo)context).new PlayerInfoData(offlinePlayer.isOnline() ? ((CraftPlayer)offlinePlayer).getProfile() : ((CraftOfflinePlayer)offlinePlayer).getProfile(), ping, (EnumGamemode)gamemode, CraftChatMessage.fromString(offlinePlayer.isOnline() ? offlinePlayer.getPlayer().getPlayerListName() : offlinePlayer.getName())[0]);
		}
		return null;
	}
	
}