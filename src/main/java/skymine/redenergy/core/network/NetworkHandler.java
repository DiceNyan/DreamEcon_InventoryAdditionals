package skymine.redenergy.core.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.FMLOutboundHandler.OutboundTarget;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * 
 * @author forge
 */
@Sharable
public class NetworkHandler extends MessageToMessageCodec {

   private EnumMap channels;
   private LinkedList packets = new LinkedList();
   private boolean isPostInitialised = false;


   public boolean registerPacket(Class clazz) {
      if(this.packets.size() > 256) {
         return false;
      } else if(this.packets.contains(clazz)) {
         return false;
      } else if(this.isPostInitialised) {
         return false;
      } else {
         this.packets.add(clazz);
         return true;
      }
   }

   public LinkedList getPackets(){
	   return packets;
   }
   protected void encode(ChannelHandlerContext ctx, Object obj, List out) throws Exception {//(ChannelHandlerContext ctx, Object msg, List out
	   AbstractPacket msg = (AbstractPacket) obj;
	   ByteBuf buffer = Unpooled.buffer();
      Class clazz = msg.getClass();
      if(!this.packets.contains(msg.getClass())) {
         throw new NullPointerException("No Packet Registered for: " + msg.getClass().getCanonicalName());
      } else {
         byte discriminator = (byte)this.packets.indexOf(clazz);
         buffer.writeByte(discriminator);
         msg.encodeInto(ctx, buffer);
         FMLProxyPacket proxyPacket = new FMLProxyPacket(buffer.copy(), (String)ctx.channel().attr(NetworkRegistry.FML_CHANNEL).get());
         out.add(proxyPacket);
      }
   }

   protected void decode(ChannelHandlerContext ctx, Object obj, List out) throws Exception {
	   FMLProxyPacket msg = (FMLProxyPacket) obj;
      ByteBuf payload = msg.payload();
      byte discriminator = payload.readByte();
      Class clazz = (Class)this.packets.get(discriminator);
      if(clazz == null) {
         throw new NullPointerException("No packet registered for discriminator: " + discriminator);
      } else {
         AbstractPacket pkt = (AbstractPacket)clazz.newInstance();
         pkt.decodeInto(ctx, payload.slice());
         switch(NetworkHandler.Handler.sides[FMLCommonHandler.instance().getEffectiveSide().ordinal()]) {
         case 1:
            EntityPlayer player1 = this.getClientPlayer();
            pkt.handleClientSide(player1);
            break;
         case 2:
            INetHandler netHandler = (INetHandler)ctx.channel().attr(NetworkRegistry.NET_HANDLER).get();
            EntityPlayerMP player = ((NetHandlerPlayServer)netHandler).playerEntity;
            pkt.handleServerSide(player);
         }

         out.add(pkt);
      }
   }

   public void initialise() {
      this.channels = NetworkRegistry.INSTANCE.newChannel("skymine", new ChannelHandler[]{this});
   }

   public void postInitialise() {
      if(!this.isPostInitialised) {
         this.isPostInitialised = true;
         Collections.sort(this.packets, new Comparator() {
            public int compare(Object o1, Object o2) {
            	Class clazz1 = (Class) o1;
            	Class clazz2 = (Class) o2;
               int com = String.CASE_INSENSITIVE_ORDER.compare(clazz1.getCanonicalName(), clazz2.getCanonicalName());
               if(com == 0) {
                  com = clazz1.getCanonicalName().compareTo(clazz2.getCanonicalName());
               }

               return com;
            }
         });
      }
   }

   @SideOnly(Side.CLIENT)
   private EntityPlayer getClientPlayer() {
      return Minecraft.getMinecraft().thePlayer;
   }

   public void sendToAll(AbstractPacket message) {
      ((FMLEmbeddedChannel)this.channels.get(Side.SERVER)).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(OutboundTarget.ALL);
      ((FMLEmbeddedChannel)this.channels.get(Side.SERVER)).writeAndFlush(message);
   }

   public void sendTo(AbstractPacket message, EntityPlayerMP player) {
      ((FMLEmbeddedChannel)this.channels.get(Side.SERVER)).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(OutboundTarget.PLAYER);
      ((FMLEmbeddedChannel)this.channels.get(Side.SERVER)).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
      ((FMLEmbeddedChannel)this.channels.get(Side.SERVER)).writeAndFlush(message);
   }

   public void sendToAllAround(AbstractPacket message, TargetPoint point) {
      ((FMLEmbeddedChannel)this.channels.get(Side.SERVER)).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(OutboundTarget.ALLAROUNDPOINT);
      ((FMLEmbeddedChannel)this.channels.get(Side.SERVER)).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(point);
      ((FMLEmbeddedChannel)this.channels.get(Side.SERVER)).writeAndFlush(message);
   }

   public void sendToDimension(AbstractPacket message, int dimensionId) {
      ((FMLEmbeddedChannel)this.channels.get(Side.SERVER)).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(OutboundTarget.DIMENSION);
      ((FMLEmbeddedChannel)this.channels.get(Side.SERVER)).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(Integer.valueOf(dimensionId));
      ((FMLEmbeddedChannel)this.channels.get(Side.SERVER)).writeAndFlush(message);
   }

   public void sendToServer(AbstractPacket message) {
      ((FMLEmbeddedChannel)this.channels.get(Side.CLIENT)).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(OutboundTarget.TOSERVER);
      ((FMLEmbeddedChannel)this.channels.get(Side.CLIENT)).writeAndFlush(message);
   }

   static class Handler {

      static final int[] sides = new int[Side.values().length];


      static {
         try {
            sides[Side.CLIENT.ordinal()] = 1;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            sides[Side.SERVER.ordinal()] = 2;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }
}